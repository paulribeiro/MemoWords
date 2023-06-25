package com.paulribe.memowords;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paulribe.memowords.authentication.login.LoginActivity;
import com.paulribe.memowords.common.enumeration.LanguageEnum;
import com.paulribe.memowords.common.model.Word;
import com.paulribe.memowords.learning.LearningFragment;
import com.paulribe.memowords.learning.NoWordsFragment;
import com.paulribe.memowords.learning.RevisionFinishedFragment;
import com.paulribe.memowords.list.ListFragment;
import com.paulribe.memowords.newword.NewWordFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<Fragment> activeFragment;
    LearningFragment learningFragment;
    NewWordFragment newWordFragment;
    ListFragment listFragment;
    RevisionFinishedFragment fragmentRevisionFinished;
    NoWordsFragment fragmentNoMoreWords;

    final FragmentManager fm = getSupportFragmentManager();

    private BottomNavigationView bottomMenu;
    private androidx.appcompat.widget.Toolbar toolbar;
    private View notificationBadge;
    private FirebaseAuth firebaseAuth;
    private LoadingDialog loadingDialog;
    private BaseViewModel baseViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            return;
        }
        initDataBinding();
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
    }

    private void createOptionMenuSelectLanguage() {
        if(toolbar.getMenu() != null) {
            toolbar.getMenu().clear();
        }
        toolbar.inflateMenu(R.menu.menu_main);
        switch(baseViewModel.getCurrentLanguage().getValue()) {
            case GERMAN:
                toolbar.getMenu().getItem(1).setIcon(R.drawable.flag_germany_24dp);
                break;
            case ENGLISH:
                toolbar.getMenu().getItem(1).setIcon(R.drawable.flag_england_24dp);
                break;
            case PORTUGUESE:
                toolbar.getMenu().getItem(1).setIcon(R.drawable.flag_portugal_24dp);
                break;
        }

        MenuItem menuItemGerman = toolbar.getMenu().getItem(1).getSubMenu().getItem(0);
        MenuItem menuItemPortuguese = toolbar.getMenu().getItem(1).getSubMenu().getItem(1);
        MenuItem menuItemEnglish = toolbar.getMenu().getItem(1).getSubMenu().getItem(2);
        MenuItem.OnMenuItemClickListener menuItemClickListener = menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_german:
                    switchWordsLanguage(LanguageEnum.GERMAN);
                    toolbar.getMenu().getItem(1).setIcon(R.drawable.flag_germany_24dp);
                    break;
                case R.id.action_portuguese:
                    switchWordsLanguage(LanguageEnum.PORTUGUESE);
                    toolbar.getMenu().getItem(1).setIcon(R.drawable.flag_portugal_24dp);
                    break;
                case R.id.action_english:
                    switchWordsLanguage(LanguageEnum.ENGLISH);
                    toolbar.getMenu().getItem(1).setIcon(R.drawable.flag_england_24dp);
                    break;
                default:
                    break;
            }
            return true;
        };

        menuItemGerman.setOnMenuItemClickListener(menuItemClickListener);
        menuItemPortuguese.setOnMenuItemClickListener(menuItemClickListener);
        menuItemEnglish.setOnMenuItemClickListener(menuItemClickListener);

        MenuItem menuItemLogout = toolbar.getMenu().getItem(0);
        menuItemLogout.setTitle(baseViewModel.getCurrentUser().getEmail());
        menuItemLogout.getSubMenu().getItem(0).setOnMenuItemClickListener(menuItem -> {
            firebaseAuth.signOut();
            baseViewModel.setCurrentUser(null);
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            return true;
        });

    }

    private void switchWordsLanguage(LanguageEnum language) {
        if(!language.equals(baseViewModel.getCurrentLanguage().getValue())) {
            startLoader();
            deleteBadge();
            baseViewModel.updateLanguage(language);
        }
    }

    private void createBottomMenu() {
        bottomMenu = findViewById(R.id.bottom_nav);
        bottomMenu.setOnNavigationItemSelectedListener(
                menuItem -> {
                    int id = menuItem.getItemId();
                    switch (id) {
                        case R.id.learningFragmentButton:
                            if (learningFragment.getLearningViewModel() != null && learningFragment.getLearningViewModel().getLearningFragmentStateEnum() != null) {
                                switch (learningFragment.getLearningViewModel().getLearningFragmentStateEnum().getValue()) {
                                    case NO_MORE_WORDS:
                                        displayNoMoreWords(true);
                                        break;
                                    case REVISION_FINISHED:
                                        displayRevisionFinished(true);
                                        break;
                                    case LEARNING_FRAGMENT:
                                        displayLearningFragment(true);
                                        break;
                                }
                            } else {
                                displayLearningFragment(true);
                            }
                            createOptionMenuSelectLanguage();
                            toolbar.setVisibility(View.VISIBLE);
                            return true;
                        case R.id.newWordFragment:
                            displayNewWordFragment(null, Boolean.FALSE);
                            return true;
                        case R.id.SecondFragment:
                            displayListFragment();
                            return true;
                        default:
                            return true;
                    }
                });
    }

    private void displayListFragment() {
        displayFragment(listFragment);
        toolbar.setVisibility(View.GONE);
    }

    private void defineFragments() {
        if(activeFragment == null) {
            activeFragment = new ArrayList<>();
        } else {
            emptyActiveFragment();
        }
        learningFragment = new LearningFragment();
        newWordFragment = new NewWordFragment();
        listFragment = new ListFragment();
        fragmentRevisionFinished = new RevisionFinishedFragment();
        fragmentNoMoreWords = new NoWordsFragment();

        fm.beginTransaction().add(R.id.content, learningFragment, "1").commit();
        fm.beginTransaction().add(R.id.content, newWordFragment, "2").hide(newWordFragment).commit();
        fm.beginTransaction().add(R.id.content, listFragment, "3").hide(listFragment).commit();
        fm.beginTransaction().add(R.id.content, fragmentRevisionFinished, "4").hide(fragmentRevisionFinished).commit();
        fm.beginTransaction().add(R.id.content, fragmentNoMoreWords, "5").hide(fragmentNoMoreWords).commit();
        activeFragment.add(learningFragment);
    }

    public void displayNewWordFragment(Word word, Boolean isEditWordMode) {
        displayFragment(newWordFragment);
        createOptionMenuSelectLanguage();
        if(isEditWordMode) {
            newWordFragment.switchToEditWordMode(word);
        } else {
            if(word != null) {
                newWordFragment.updateInputFields(word);
            }
        }
        toolbar.setVisibility(View.GONE);
    }

    public void setBadgeText(String badgeText) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomMenu.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(0);
        if(notificationBadge != null) {
            itemView.removeView(notificationBadge);
        }
        notificationBadge = LayoutInflater.from(this).inflate(R.layout.notification_badge, menuView, false);
        TextView textView = notificationBadge.findViewById(R.id.notifications_badgeTextView);
        textView.setText(badgeText);
        itemView.addView(notificationBadge);
    }

    public void deleteBadge() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomMenu.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(0);
        itemView.removeView(notificationBadge);
    }

    public void displayNoMoreWords(boolean forced) {
        if(!activeFragment.get(0).equals(fragmentNoMoreWords) && isTriggeredFromLearningFragmentOrForced(forced)) {
            displayFragment(fragmentNoMoreWords);
        }
    }

    public void displayRevisionFinished(boolean forced) {
        if(!activeFragment.get(0).equals(fragmentRevisionFinished) && isTriggeredFromLearningFragmentOrForced(forced)) {
            deleteBadge();
            displayFragment(fragmentRevisionFinished);
        }
    }

    public void displayLearningFragment(boolean forced){
        if(!activeFragment.get(0).equals(learningFragment) && isTriggeredFromLearningFragmentOrForced(forced)) {
            displayFragment(learningFragment);
        }
    }

    private boolean isTriggeredFromLearningFragmentOrForced(boolean forced) {
        Fragment currentFragment = activeFragment.get(0);
        return Arrays.asList(learningFragment, fragmentNoMoreWords, fragmentRevisionFinished).contains(currentFragment) || forced;
    }

    public void comeBackLearningFragmentLearnNewWords() {
        learningFragment.getLearningViewModel().getIsRevisionFinished().setValue(true);
        learningFragment.getLearningViewModel().prepareWordsToLearn();
        learningFragment.getLearningViewModel().updateLearningState();
    }

    private void initDataBinding() {
        baseViewModel = new ViewModelProvider(this).get(BaseViewModel.class);
        baseViewModel.init();
        if(Boolean.FALSE.equals(baseViewModel.getNormalState().getValue())) {
            finish();
            startActivity(new Intent(getApplicationContext(), SplashActivity.class));
            return;
        }
        setUpChangeValueListener();
        baseViewModel.loadUserConfig();
    }

    private void setUpChangeValueListener() {
        baseViewModel.getNormalState().observe(this, this::onNormalStateChange);
        baseViewModel.getCurrentLanguage().observe(this, this::onCurrentLanguageChange);
    }

    private void onNormalStateChange(Boolean normalState) {
        if(!normalState) {
            finish();
            startActivity(new Intent(getApplicationContext(), SplashActivity.class));
        }
    }

    private void onCurrentLanguageChange(LanguageEnum languageEnum) {
        defineFragments();
        createBottomMenu();
        createOptionMenuSelectLanguage();
        stopLoader();
    }

    public void startLoader() {
        if(loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.startLoadingDialog(this.getResources().getString(R.string.loading_data));
    }

    public void stopLoader() {
        if(loadingDialog != null) {
            loadingDialog.dismissDialog();
        }
    }

    public void changeBottomMenuItemSelected(int itemMenuSelected) {
        bottomMenu.setSelectedItemId(itemMenuSelected);
    }

    private void displayFragment(Fragment fragment) {
        emptyActiveFragment();
        fm.beginTransaction().show(fragment).commit();
        activeFragment.add(0, fragment);
    }

    private void emptyActiveFragment() {
        while (!CollectionUtils.isEmpty(activeFragment)) {
            fm.beginTransaction().hide(activeFragment.get(0)).commit();
            activeFragment.remove(0);
        }
    }
}
