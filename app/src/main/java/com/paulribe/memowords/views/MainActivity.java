package com.paulribe.memowords.views;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paulribe.memowords.R;
import com.paulribe.memowords.enumeration.LanguageEnum;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.viewmodels.BaseViewModel;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
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

    private BaseViewModel baseViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        //displayBadgeNumberCardsToRevise();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
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
            fm.beginTransaction().hide(activeFragment.get(0)).commit();
            deleteBadge();
            baseViewModel.updateLanguage(language);
        }
    }

    private void createBottomMenu() {

        bottomMenu = findViewById(R.id.bottom_nav);
        Menu menu = bottomMenu.getMenu();

        menu.getItem(0).setOnMenuItemClickListener(menuItem -> {
            if(learningFragment.getLearningViewModel() != null && learningFragment.getLearningViewModel().getLearningFragmentStateEnum() != null) {
                switch(learningFragment.getLearningViewModel().getLearningFragmentStateEnum().getValue()) {
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
        });

        bottomMenu.getMenu().getItem(1).setOnMenuItemClickListener(menuItem -> {
            displayNewWordFragment(null, Boolean.FALSE);
            return true;
        });

        bottomMenu.getMenu().getItem(2).setOnMenuItemClickListener(menuItem -> {
            displayListFragment();
            return true;
        });
    }

    private void displayListFragment() {
        fm.beginTransaction().hide(activeFragment.get(0)).show(listFragment).commit();
        activeFragment.add(0, listFragment);
        toolbar.setVisibility(View.GONE);
    }

    private void defineFragments() {
        activeFragment = new ArrayList<>();
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
        fm.beginTransaction().hide(activeFragment.get(0)).show(newWordFragment).commit();
        activeFragment.add(0, newWordFragment);
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
            fm.beginTransaction().hide(activeFragment.get(0)).show(fragmentNoMoreWords).commit();
            activeFragment.add(0, fragmentNoMoreWords);
        }
    }

    public void displayRevisionFinished(boolean forced) {
        if(!activeFragment.get(0).equals(fragmentRevisionFinished) && isTriggeredFromLearningFragmentOrForced(forced)) {
            deleteBadge();
            fm.beginTransaction().hide(activeFragment.get(0)).show(fragmentRevisionFinished).commit();
            activeFragment.add(0, fragmentRevisionFinished);
        }
    }

    public void displayLearningFragment(boolean forced){
        if(!activeFragment.get(0).equals(learningFragment) && isTriggeredFromLearningFragmentOrForced(forced)) {
            fm.beginTransaction().hide(activeFragment.get(0)).show(learningFragment).commit();
            activeFragment.add(0, learningFragment);
        }
    }

    private boolean isTriggeredFromLearningFragmentOrForced(boolean forced) {
        Fragment currentFragment = activeFragment.get(0);
        return Arrays.asList(learningFragment, fragmentNoMoreWords, fragmentRevisionFinished).contains(currentFragment) || forced;
    }

    public void comeBackLearningFragment() {
        learningFragment.getLearningViewModel().getIsRevisionFinished().setValue(true);
        learningFragment.getLearningViewModel().calculateWordsToLearn();
        learningFragment.getLearningViewModel().updateLearningState();
        //fm.beginTransaction().hide(activeFragment.get(0)).show(learningFragment).commit();
        //activeFragment.add(0, learningFragment);
    }

    private void initDataBinding() {
        baseViewModel = new ViewModelProvider(this).get(BaseViewModel.class);
        baseViewModel.init();
        setUpChangeValueListener();
        baseViewModel.loadUserConfig();
    }

    private void setUpChangeValueListener() {
        baseViewModel.getCurrentLanguage().observe(this, this::onCurrentLanguageChange);
    }

    private void onCurrentLanguageChange(LanguageEnum languageEnum) {
        defineFragments();
        createBottomMenu();
        createOptionMenuSelectLanguage();
    }
}
