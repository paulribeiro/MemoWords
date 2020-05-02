package com.paulribe.memowords;

import android.os.Bundle;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.paulribe.memowords.enumeration.LanguageEnum;
import com.paulribe.memowords.enumeration.OrderByEnum;
import com.paulribe.memowords.model.Word;
import com.paulribe.memowords.model.mContext;
import com.paulribe.memowords.restclient.FirebaseDataHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    final Fragment learningFragment = new LearningFragment();
    final Fragment newWordFragment = new NewWordFragment();
    final Fragment listFragment = new ListFragment();
    final Fragment fragmentRevisionFinished = new RevisionFinishedFragment();
    final Fragment fragmentNoMoreWords = new NoWordsFragment();

    final FragmentManager fm = getSupportFragmentManager();
    final List<Fragment> active = new ArrayList<>();

    private BottomNavigationView bottomMenu;
    private androidx.appcompat.widget.Toolbar toolbar;
    private TextView searchBar;
    private FrameLayout searchBarLayout;

    private View notificationBadge;
    boolean newLanguageLoaded;
    private String searchedString = "";
    private boolean isFavoriteSelected = false;
    private Button deleteSearchWordButton;
    private OrderByEnum orderByEnum = OrderByEnum.LAST_TRY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        searchBar = findViewById(R.id.search_word);
        searchBarLayout = findViewById(R.id.search_word_toolbar);
        searchBarLayout.setVisibility(View.GONE);
        deleteSearchWordButton = findViewById(R.id.delete_search_word);
        createBottomMenu();
        createOptionMenuSelectLanguage();
        displayBadgeNumberCardsToRevise();
    }

    private void createOptionMenuSelectLanguage() {
        if(toolbar.getMenu() != null) {
            toolbar.getMenu().clear();
        }
        toolbar.inflateMenu(R.menu.menu_main);
        switch(mContext.getCurrentLanguage()) {
            case GERMAN:
                toolbar.getMenu().getItem(0).setIcon(R.drawable.flag_germany_24dp);
                break;
            case ENGLISH:
                toolbar.getMenu().getItem(0).setIcon(R.drawable.flag_england_24dp);
                break;
            case PORTUGUESE:
                toolbar.getMenu().getItem(0).setIcon(R.drawable.flag_portugal_24dp);
                break;
        }

        MenuItem menuItemGerman = toolbar.getMenu().getItem(0).getSubMenu().getItem(0);
        MenuItem menuItemPortuguese = toolbar.getMenu().getItem(0).getSubMenu().getItem(1);
        MenuItem menuItemEnglish = toolbar.getMenu().getItem(0).getSubMenu().getItem(2);
        MenuItem.OnMenuItemClickListener menuItemClickListener = menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_german:
                    switchWordsLanguage(LanguageEnum.GERMAN);
                    toolbar.getMenu().getItem(0).setIcon(R.drawable.flag_germany_24dp);
                    return true;
                case R.id.action_portuguese:
                    switchWordsLanguage(LanguageEnum.PORTUGUESE);
                    toolbar.getMenu().getItem(0).setIcon(R.drawable.flag_portugal_24dp);
                    return true;
                case R.id.action_english:
                    switchWordsLanguage(LanguageEnum.ENGLISH);
                    toolbar.getMenu().getItem(0).setIcon(R.drawable.flag_england_24dp);
                    return true;
                default:
                    return true;
            }
        };

        menuItemGerman.setOnMenuItemClickListener(menuItemClickListener);
        menuItemPortuguese.setOnMenuItemClickListener(menuItemClickListener);
        menuItemEnglish.setOnMenuItemClickListener(menuItemClickListener);
    }

    private void createOptionMenuOrderBy() {
        if(toolbar.getMenu() != null){
            toolbar.getMenu().clear();
        }
        toolbar.inflateMenu(R.menu.menu_order_by);
        MenuItem menuItemOrderByLastTry = toolbar.getMenu().getItem(1).getSubMenu().getItem(0);
        MenuItem menuItemOrderByKnowledgeLevel = toolbar.getMenu().getItem(1).getSubMenu().getItem(1);
        MenuItem menuItemOrderByAZ = toolbar.getMenu().getItem(1).getSubMenu().getItem(2);
        MenuItem menuItemOrderByZA = toolbar.getMenu().getItem(1).getSubMenu().getItem(3);

        MenuItem.OnMenuItemClickListener menuItemClickListener = menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_last_tried:
                    orderByEnum = OrderByEnum.LAST_TRY;
                    break;
                case R.id.action_knowledge_level:
                    orderByEnum = OrderByEnum.KNOWLEDGE_LEVEL;
                    break;
                case R.id.action_AZ:
                    orderByEnum = OrderByEnum.AZ;
                    break;
                case R.id.action_ZA:
                    orderByEnum = OrderByEnum.ZA;
                    break;
                default:
                    break;
            }
            ((ListFragment)listFragment).updateRecyclerView(orderByEnum, searchedString, isFavoriteSelected);
            return true;
        };

        menuItemOrderByLastTry.setOnMenuItemClickListener(menuItemClickListener);
        menuItemOrderByKnowledgeLevel.setOnMenuItemClickListener(menuItemClickListener);
        menuItemOrderByAZ.setOnMenuItemClickListener(menuItemClickListener);
        menuItemOrderByZA.setOnMenuItemClickListener(menuItemClickListener);

        MenuItem menuItemFavorite = toolbar.getMenu().getItem(0);
        menuItemFavorite.setOnMenuItemClickListener(menuItem -> {
            isFavoriteSelected = !isFavoriteSelected;
            if(isFavoriteSelected) {
                menuItemFavorite.setIcon(R.drawable.star_filled);
            } else {
                menuItemFavorite.setIcon(R.drawable.star_empty_white);
            }
            ((ListFragment)listFragment).updateRecyclerView(orderByEnum, searchedString, isFavoriteSelected);
            return true;
        });

        View crossIcon = findViewById(R.id.delete_search_word);
        crossIcon.setVisibility(View.GONE);
        searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s != null && !s.toString().equals("")) {
                    crossIcon.setVisibility(View.VISIBLE);
                } else {
                    crossIcon.setVisibility(View.GONE);
                }
                searchedString = s.toString();
                ((ListFragment)listFragment).updateRecyclerView(orderByEnum, searchedString, isFavoriteSelected);
            }
        });

        deleteSearchWordButton.setOnClickListener(view -> {
            searchedString = "";
            searchBar.setText("");
            ((ListFragment)listFragment).updateRecyclerView(orderByEnum, searchedString, isFavoriteSelected);
        });

    }

    private void switchWordsLanguage(LanguageEnum language) {
        if(!mContext.getCurrentLanguage().equals(language)) {
            newLanguageLoaded = true;
            mContext.setCurrentLanguage(language);
            FirebaseDataHelper firebaseDataHelper = mContext.getFirebaseDataHelper();
            firebaseDataHelper.setReferenceWords(language);
            firebaseDataHelper.readWords(new FirebaseDataHelper.DataStatus() {

                @Override
                public void dataIsLoaded(List<Word> w, List<String> keys) {
                    mContext.setWords(w);
                }

                @Override
                public void dataIsInserted() {}

                @Override
                public void dataIsUpdated(List<Word> w) {
                    mContext.setWords(w);
                    if(newLanguageLoaded){
                        newLanguageLoaded = false;
                        ((LearningFragment)learningFragment).updateWordList();
                    }
                }

                @Override
                public void dataIsDeleted() {}
            });
        }
    }

    private void createBottomMenu() {

        fm.beginTransaction().add(R.id.content, listFragment, "3").hide(listFragment).commit();
        fm.beginTransaction().add(R.id.content, newWordFragment, "2").hide(newWordFragment).commit();
        fm.beginTransaction().add(R.id.content, fragmentRevisionFinished, "4").hide(fragmentRevisionFinished).commit();


        if(CollectionUtils.isEmpty(mContext.getWordsToDisplay())) {
            fm.beginTransaction().add(R.id.content, learningFragment, "1").hide(learningFragment).commit();
            fm.beginTransaction().add(R.id.content, fragmentNoMoreWords, "5").commit();
            active.add(fragmentNoMoreWords);
        } else {
            fm.beginTransaction().add(R.id.content, learningFragment, "1").commit();
            fm.beginTransaction().add(R.id.content, fragmentNoMoreWords, "5").hide(fragmentNoMoreWords).commit();
            active.add(learningFragment);
        }

        bottomMenu = findViewById(R.id.bottom_nav);
        Menu menu = bottomMenu.getMenu();

        menu.getItem(0).setOnMenuItemClickListener(menuItem -> {
            if(CollectionUtils.isEmpty(mContext.getWordsToDisplay()) && mContext.getIsRevisionFinished()) {
                fm.beginTransaction().hide(active.get(0)).show(fragmentNoMoreWords).commit();
                active.add(0, fragmentNoMoreWords);
            } else if (CollectionUtils.isEmpty(mContext.getWordsToDisplay()) && !mContext.getIsRevisionFinished()) {
                fm.beginTransaction().hide(active.get(0)).show(fragmentRevisionFinished).commit();
                active.add(0, fragmentRevisionFinished);
            } else {
                fm.beginTransaction().hide(active.get(0)).show(learningFragment).commit();
                ((LearningFragment) learningFragment).displayNextWord();
                active.add(0, learningFragment);
            }
            createOptionMenuSelectLanguage();
            searchBarLayout.setVisibility(View.GONE);
            return true;
        });

        bottomMenu.getMenu().getItem(1).setOnMenuItemClickListener(menuItem -> {
            fm.beginTransaction().hide(active.get(0)).show(newWordFragment).commit();
            active.add(0, newWordFragment);
            createOptionMenuSelectLanguage();
            searchBarLayout.setVisibility(View.GONE);
            return true;
        });

        bottomMenu.getMenu().getItem(2).setOnMenuItemClickListener(menuItem -> {
            fm.beginTransaction().hide(active.get(0)).show(listFragment).commit();
            active.add(0, listFragment);
            createOptionMenuOrderBy();
            searchBarLayout.setVisibility(View.VISIBLE);
            return true;
        });
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

    private void deleteBadge() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomMenu.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(0);
        itemView.removeView(notificationBadge);
    }

    public void displayNoMoreWords() {
        fm.beginTransaction().hide(active.get(0)).show(fragmentNoMoreWords).commit();
        active.add(0, fragmentNoMoreWords);
    }

    public void displayRevisionFinished() {
        mContext.setIsRevisionFinished(true);
        deleteBadge();
        fm.beginTransaction().hide(active.get(0)).show(fragmentRevisionFinished).commit();
        active.add(0, fragmentRevisionFinished);
    }

    public void displayLearningFragment(){
        if(!active.get(0).equals(learningFragment)){
            fm.beginTransaction().hide(active.get(0)).show(learningFragment).commit();
            active.add(0, learningFragment);
        }
    }

    public void comeBackLearningFragment() {
        mContext.calculateWordsToLearn();
        if(CollectionUtils.isEmpty(mContext.getWordsToDisplay())){
            displayNoMoreWords();
        } else {
            ((LearningFragment) learningFragment).displayNextWord();
            fm.beginTransaction().hide(active.get(0)).show(learningFragment).commit();
            active.add(0, learningFragment);
        }
    }

    private void displayBadgeNumberCardsToRevise() {
        if(!mContext.getIsRevisionFinished()) {
            int nbWordToRevise = mContext.getWordsToDisplay().size();
            if(nbWordToRevise != 0) {
                setBadgeText(Integer.toString(nbWordToRevise));
            }
        }
    }
}
