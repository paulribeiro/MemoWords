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
import static com.paulribe.memowords.common.UITestHelper.addNewWord;
import static com.paulribe.memowords.common.UITestHelper.checkLearningFragmentAfterAnswerIsDisplayed;
import static com.paulribe.memowords.common.UITestHelper.cleanUserWordsList;
import static com.paulribe.memowords.common.UITestHelper.logout;
import static com.paulribe.memowords.common.UITestHelper.signIn;
import static com.paulribe.memowords.common.UITestHelper.startLearningNewWords;
import static com.paulribe.memowords.common.UITestHelper.waitForElementUntilDisplayed;
import static org.hamcrest.core.StringContains.containsString;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.paulribe.memowords.common.RecyclerViewAction;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Instrumented test to verify that the creation of words functionality is working.
 */
@RunWith(AndroidJUnit4.class)
public class SearchWordsUITest {

    private static final String EXISTING_EMAIL = "test@gmail.com";
    private static final String PASSWORD = "aaaa0000";

    public static final String WORD_1 = "TestWord1";
    public static final String WORD_TRANSLATED_1 = "TestWord1Translate";
    public static final String WORD_CONTEXT_1 = "TestWord1Context";
    public static final String WORD_2 = "TestWord2";
    public static final String WORD_TRANSLATED_2 = "TestWord2Translate";
    public static final String WORD_CONTEXT_2 = "TestWord2Context";
    public static final String WORD_3 = "TestWord3";
    public static final String WORD_TRANSLATED_3 = "TestWord3Translate";
    public static final String WORD_CONTEXT_3 = "TestWord3Context";

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

    @Test
    public void ManageWordsListTest() throws InterruptedException {

        signIn(EXISTING_EMAIL, PASSWORD);

        waitForElementUntilDisplayed(onView(withId(R.id.noMoreWordsFragment)), 3000);

        addNewWord(WORD_1, WORD_TRANSLATED_1, WORD_CONTEXT_1);
        addNewWord(WORD_2, WORD_TRANSLATED_2, WORD_CONTEXT_2);
        addNewWord(WORD_3, WORD_TRANSLATED_3, WORD_CONTEXT_3);

        goToWordListFragment();

        assertRecyclerViewWordList(Arrays.asList(WORD_1, WORD_2, WORD_3));

        goToWordListFragment();
        markWordAsFavoriteAtPosition(1);

        assertWordListAfterClickOnFavorite(Collections.singletonList(WORD_2));
        assertWordListAfterClickOnFavorite(Arrays.asList(WORD_1, WORD_2, WORD_3));

        assertWordListAfterClickOnFilteringAction(R.string.z_a, Arrays.asList(WORD_3, WORD_2, WORD_1));
        assertWordListAfterClickOnFilteringAction(R.string.a_z, Arrays.asList(WORD_1, WORD_2, WORD_3));

        startLearningNewWords();
        onView(withId(R.id.showTranslationButton)).perform(click());
        checkLearningFragmentAfterAnswerIsDisplayed(WORD_3, WORD_TRANSLATED_3, WORD_CONTEXT_3);
        onView(withId(R.id.button2)).perform(click());

        onView(withId(R.id.showTranslationButton)).perform(click());
        checkLearningFragmentAfterAnswerIsDisplayed(WORD_2, WORD_TRANSLATED_2, WORD_CONTEXT_2);
        onView(withId(R.id.button2)).perform(click());

        goToWordListFragment();

        assertWordListAfterClickOnFilteringAction(R.string.last_tried, Arrays.asList(WORD_2, WORD_3, WORD_1));
        assertWordListAfterClickOnFilteringAction(R.string.knowledge_level, Arrays.asList(WORD_1, WORD_2, WORD_3));
        assertWordListAfterClickOnFilteringAction(R.string.knowledge_level_desc, Arrays.asList(WORD_2, WORD_3, WORD_1));
    }

    private static void assertWordListAfterClickOnFavorite(List<String> filteredOrderList) {
        onView(withId(R.id.action_show_favorite)).perform(click());
        assertRecyclerViewWordList(filteredOrderList);
    }

    private static void assertWordListAfterClickOnFilteringAction(int filteringAction, List<String> filteredOrderList) throws InterruptedException {
        onView(withId(R.id.action_order_by)).perform(click());
        onView(withText(filteringAction)).perform(ViewActions.click());
        Thread.sleep(3000);
        assertRecyclerViewWordList(filteredOrderList);
    }

    private static void assertRecyclerViewWordList(List<String> filteredOrderList) {
        int rankInRecyclerView = 0;
        for(String word: filteredOrderList) {
            recyclerViewContainsStringAtPosition(word, rankInRecyclerView);
            rankInRecyclerView++;
        }
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

    private static void markWordAsFavoriteAtPosition(int position) {
        onView(withId(R.id.recycler_view_words))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position, RecyclerViewAction.clickChildViewWithId(R.id.favorite)));
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
