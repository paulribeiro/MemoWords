package com.paulribe.memowords;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.paulribe.memowords.common.UITestHelper.logout;
import static com.paulribe.memowords.common.UITestHelper.signIn;
import static com.paulribe.memowords.common.UITestHelper.waitForElementUntilDisplayed;
import static org.hamcrest.Matchers.not;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
public class CreateWordsUITest {

    private static final String EXISTING_EMAIL = "test@gmail.com";
    private static final String PASSWORD = "aaaa0000";
    public static final String WORD_1 = "TestWord1";
    public static final String WORD_TRANSLATED_1 = "TestWord1Translate";
    public static final String WORD_CONTEXT_1 = "TestWord1Context";
    public static final String WORD_UPDATED_1 = "TestWordUpdated";
    public static final String WORD_TRANSLATED_UPDATED_1 = "TestWordUpdatedTranslate";
    public static final String WORD_CONTEXT_UPDATED_1 = "TestWordUpdatedContext";

    @Rule
    public ActivityScenarioRule<SplashActivity> activityRule
            = new ActivityScenarioRule<>(SplashActivity.class);

    @Before
    public void setUp() {

    }

    @After
    public void cleanUp() throws InterruptedException {
        cleanUserWordsList();
        logout();
    }

    @Test
    public void CreateUpdateDeleteWordsTest() throws InterruptedException {

        signIn(EXISTING_EMAIL, PASSWORD);

        waitForElementUntilDisplayed(onView(withId(R.id.noMoreWordsFragment)), 3000);

        addNewWord(WORD_1, WORD_TRANSLATED_1, WORD_CONTEXT_1);

        startLearningNewWords();

        checkLearningFragmentBeforeAnswerIsDisplayed();
        onView(withId(R.id.showTranslationButton)).perform(click());
        checkLearningFragmentAfterAnswerIsDisplayed(WORD_1, WORD_TRANSLATED_1, WORD_CONTEXT_1);

        // Update
        onView(withId(R.id.editWordButton)).perform(click());
        onView(withId(R.id.popupAddTitle)).check(matches(not(isDisplayed())));
        onView(withId(R.id.popupEditTitle)).check(matches(isDisplayed()));

        updateWordInfo(WORD_UPDATED_1, WORD_TRANSLATED_UPDATED_1, WORD_CONTEXT_UPDATED_1);
        onView(withId(R.id.learningFragmentButton)).perform(click());
        //TODO: answer button already clicked
        // onView(withId(R.id.popupButton)).perform(click());

        checkLearningFragmentBeforeAnswerIsDisplayed();
        checkLearningFragmentAfterAnswerIsDisplayed(WORD_UPDATED_1, WORD_TRANSLATED_UPDATED_1, WORD_CONTEXT_UPDATED_1);

        //delete
        onView(withId(R.id.editWordButton)).perform(click());
        onView(withId(R.id.popupAddTitle)).check(matches(not(isDisplayed())));
        onView(withId(R.id.popupEditTitle)).check(matches(isDisplayed()));

        onView(withId(R.id.popupDeleteButton)).perform(click());
    }

    private void startLearningNewWords() {
        onView(withId(R.id.learningFragmentButton)).perform(click());
        onView(withId(R.id.button_begin_learning)).perform(click());
    }

    private void checkLearningFragmentAfterAnswerIsDisplayed(String word, String wordTranslated, String context) {
        onView(withId(R.id.textview_word)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_translation)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_word)).check(matches(withText(word)));
        onView(withId(R.id.textview_translation)).check(matches(withText(wordTranslated)));
        onView(withId(R.id.textViewContext)).check(matches(withText(context)));
    }

    private void checkLearningFragmentBeforeAnswerIsDisplayed() {
        onView(withId(R.id.learningFragmentConstraintLayout)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_word)).check(matches(isDisplayed()));
        //TODO: onView(withId(R.id.textview_translation)).check(matches(not(isDisplayed())));
    }

    private void addNewWord(String wordNative, String wordTranslated, String wordContext) {
        onView(withId(R.id.newWordFragment)).perform(click());

        onView(withId(R.id.popupAddTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.popupEditTitle)).check(matches(not(isDisplayed())));

        fillNewWordInfo(wordNative, wordTranslated, wordContext);
        onView(withId(R.id.popupButton)).perform(click());
    }

    private void fillNewWordInfo(String wordNative, String wordTranslate, String wordContext) {
        onView(withId(R.id.inputWordNative)).perform(typeText(wordNative), closeSoftKeyboard());
        onView(withId(R.id.inputWordTranslation)).perform(typeText(wordTranslate), closeSoftKeyboard());
        onView(withId(R.id.inputWordContext)).perform(typeText(wordContext), closeSoftKeyboard());
    }

    private void updateWordInfo(String wordNative, String wordTranslate, String wordContext) {
        onView(withId(R.id.inputWordNative)).perform(replaceText(wordNative), closeSoftKeyboard());
        onView(withId(R.id.inputWordTranslation)).perform(replaceText(wordTranslate), closeSoftKeyboard());
        onView(withId(R.id.inputWordContext)).perform(replaceText(wordContext), closeSoftKeyboard());
    }

    private void cleanUserWordsList() throws InterruptedException {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            DatabaseReference wordsForUserReferenceDB = FirebaseDatabase.getInstance().getReference(currentUser.getUid() + "/words");
            wordsForUserReferenceDB.removeValue();
        }
    }
}
