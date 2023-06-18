package com.paulribe.memowords;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.paulribe.memowords.common.RecyclerViewAction.atPosition;
import static com.paulribe.memowords.common.UITestHelper.cleanUserWordsList;
import static com.paulribe.memowords.common.UITestHelper.logout;
import static com.paulribe.memowords.common.UITestHelper.signIn;
import static com.paulribe.memowords.common.UITestHelper.waitForElementUntilDisplayed;
import static org.hamcrest.core.StringContains.containsString;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.paulribe.memowords.common.RecyclerViewAction;
import com.paulribe.memowords.views.SplashActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test to verify that the creation of words functionality is working.
 */
@RunWith(AndroidJUnit4.class)
public class SearchWordsUITest {

    private static final String EXISTING_EMAIL = "test@gmail.com";
    private static final String PASSWORD = "aaaa0000";

    @Rule
    public ActivityScenarioRule<SplashActivity> activityRule
            = new ActivityScenarioRule<>(SplashActivity.class);

    @Before
    public void setUp() {

    }

    @After
    public void cleanUp() {
        cleanUserWordsList();
        logout();
    }

    @Test
    public void SearchNewWordTest() {

        signIn(EXISTING_EMAIL, PASSWORD);
        waitForElementUntilDisplayed(onView(withId(R.id.noMoreWordsFragment)), 3000);

        goToWordListFragment();

        checkInitialStateOnListFragment();
        searchForWordWithTranslationService("table", R.id.buttonCallPons);
        recyclerViewContainsStringAtPosition("table", 2);
        addWordSearchedAtPositionToList(2);

        goToWordListFragment();

        recyclerViewContainsStringAtPosition("table", 0);

        swapLanguage();

        searchForWordWithTranslationService("hund", R.id.buttonCallMyMemory);
        recyclerViewContainsStringAtPosition("hund", 0);
        addWordSearchedAtPositionToList(0);

        goToWordListFragment();
        assertWordsCreatedInList();
    }

    private static void goToWordListFragment() {
        onView(withId(R.id.SecondFragment)).perform(click());
    }

    private static void checkInitialStateOnListFragment() {
        onView(withId(R.id.search_word_toolbar)).check(matches(isDisplayed()));
        checkSourceTargetLanguage("French", "German");
        onView(withId(R.id.recycler_view_words)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view_words))
                .check(matches(atPosition(0, hasDescendant(withText("No Result")))));
    }

    private static void swapLanguage() {
        onView(withId(R.id.swapLanguageButton)).perform(click());
        checkSourceTargetLanguage("German", "French");
    }

    private static void searchForWordWithTranslationService(String hund, int buttonCallTranslationService) {
        replaceSearchedWord(hund);
        onView(withId(buttonCallTranslationService)).perform(click());
    }

    private static void addWordSearchedAtPositionToList(int position) {
        onView(withId(R.id.recycler_view_words))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position, RecyclerViewAction.clickChildViewWithId(R.id.addSearchedWordButton)));
        onView(withId(R.id.popupButton)).perform(click());
    }

    private static void assertWordsCreatedInList() {
        replaceSearchedWord("");
        recyclerViewContainsStringAtPosition("table", 0);
        recyclerViewContainsStringAtPosition("hund", 1);
    }

    private static void replaceSearchedWord(String table) {
        onView(withId(R.id.search_word)).perform(replaceText(table), closeSoftKeyboard());
    }

    private static void checkSourceTargetLanguage(String sourceLanguage, String targetLanguage) {
        onView(withId(R.id.sourceLanguageTextView)).check(matches(withText(sourceLanguage)));
        onView(withId(R.id.targetLanguageTextView)).check(matches(withText(targetLanguage)));
    }

    private static void recyclerViewContainsStringAtPosition(String word, int position) {
        onView(withId(R.id.recycler_view_words))
                .check(matches(atPosition(position, hasDescendant(withText(containsString(word))))));
    }



}
