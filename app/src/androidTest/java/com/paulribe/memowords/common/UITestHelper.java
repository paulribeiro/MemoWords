package com.paulribe.memowords.common;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paulribe.memowords.R;

public class UITestHelper {

    private static final int WAITING_TIME = 100;

    public static boolean waitForElementUntilDisplayed(ViewInteraction element, final long millis) {
        final long startTime = System.currentTimeMillis();
        final long endTime = startTime + millis;
        do {
            try {
                element.check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(WAITING_TIME);
                } catch (Exception e1) {
                    e.printStackTrace();
                }
            }
        } while (System.currentTimeMillis() < endTime);
        return false;
    }

    public static void signIn(String email, String password) {
        waitForElementUntilDisplayed(onView(withId(R.id.editTextEmailRegister)), 10000);
        fillLoginInfo(email, password);
        onView(withId(R.id.buttonLogin)).perform(click());
    }

    private static void fillLoginInfo(String email, String password) {
        onView(withId(R.id.editTextEmailRegister))
                .perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText(password), closeSoftKeyboard());
    }

    public static void logout() {
        onView(withId(R.id.learningFragmentButton)).perform(click());
        onView(withId(R.id.action_log_user)).perform(click());
        onView(withText(R.string.logout)).perform(ViewActions.click());
    }

    public static void cleanUserWordsList() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            DatabaseReference wordsForUserReferenceDB = FirebaseDatabase.getInstance().getReference(currentUser.getUid() + "/words");
            wordsForUserReferenceDB.removeValue();
        }
    }

    public static void addNewWord(String wordNative, String wordTranslated, String wordContext) {
        onView(withId(R.id.newWordFragment)).perform(click());

        onView(withId(R.id.popupAddTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.popupEditTitle)).check(matches(not(isDisplayed())));

        fillNewWordInfo(wordNative, wordTranslated, wordContext);
        onView(withId(R.id.popupButton)).perform(click());
    }

    public static void fillNewWordInfo(String wordNative, String wordTranslate, String wordContext) {
        onView(withId(R.id.inputWordNative)).perform(typeText(wordNative), closeSoftKeyboard());
        onView(withId(R.id.inputWordTranslation)).perform(typeText(wordTranslate), closeSoftKeyboard());
        onView(withId(R.id.inputWordContext)).perform(typeText(wordContext), closeSoftKeyboard());
    }

    public static void startLearningNewWords() {
        onView(withId(R.id.learningFragmentButton)).perform(click());
        onView(withId(R.id.button_begin_learning)).perform(click());
    }

    public static void checkLearningFragmentAfterAnswerIsDisplayed(String word, String wordTranslated, String context) {
        onView(withId(R.id.textview_word)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_translation)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_word)).check(matches(withText(word)));
        onView(withId(R.id.textview_translation)).check(matches(withText(wordTranslated)));
        onView(withId(R.id.textViewContext)).check(matches(withText(context)));
    }
}
