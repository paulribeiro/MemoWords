package com.paulribe.memowords;

import android.view.View;

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

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoginUITest {

    private static final String EMAIL = "test1@gmail.com";
    private static final String PASSWORD = "aaaa0000";
    private static final int WAITING_TIME = 100;
    private String userIdForTest;

    @Rule
    public ActivityScenarioRule<SplashActivity> activityRule
            = new ActivityScenarioRule<>(SplashActivity.class);

    private View decorView;

    @Before
    public void setUp() {
        activityRule.getScenario().onActivity(activity -> decorView = activity.getWindow().getDecorView());
    }

    @After
    public void cleanUp() throws InterruptedException {
        deleteUserAfterTest();
    }

    @Test
    public void LoginRegistrationTest() {

        waitForElementUntilDisplayed(onView(withId(R.id.editTextEmailRegister)), 10000);

        fillLoginInfo(EMAIL, PASSWORD);
        onView(withId(R.id.buttonLogin)).perform(click());

        ViewInteraction verifToast = onView(withText(R.string.login_failed)).inRoot(withDecorView(not(decorView)));
        waitForElementUntilDisplayed(verifToast, 3000);

        onView(withId(R.id.textViewSignUp)).perform(click());
        fillRegisteringInfo(EMAIL, PASSWORD, R.string.french);
        onView(withId(R.id.buttonSignup)).perform(click());

        waitForElementUntilDisplayed(onView(withId(R.id.noMoreWordsFragment)), 3000);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.newWordFragment)).perform(click());

        onView(withId(R.id.popupAddTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.popupEditTitle)).check(matches(not(isDisplayed())));

        fillNewWordInfo("TestWord1", "TestWord1Translate", "TestWord1Context");

        onView(withId(R.id.learningFragmentButton)).perform(click());
        onView(withId(R.id.button_begin_learning)).perform(click());

        onView(withId(R.id.learningFragmentConstraintLayout)).check(matches(isDisplayed()));

        onView(withId(R.id.textview_word)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_translation)).check(matches(not(isDisplayed())));

        onView(withId(R.id.showTranslationButton)).perform(click());

        onView(withId(R.id.textview_word)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_translation)).check(matches(isDisplayed()));

        onView(withId(R.id.textview_word)).check(matches(withText("TestWord1")));
        onView(withId(R.id.textview_translation)).check(matches(withText("TestWord1Translate")));
        onView(withId(R.id.textViewContext)).check(matches(withText("TestWord1Context")));

    }

    private void fillNewWordInfo(String wordNative, String wordTranslate, String wordContext) {
        onView(withId(R.id.inputWordNative)).perform(typeText(wordNative), closeSoftKeyboard());
        onView(withId(R.id.inputWordTranslation)).perform(typeText(wordTranslate), closeSoftKeyboard());
        onView(withId(R.id.inputWordContext)).perform(typeText(wordContext), closeSoftKeyboard());
        onView(withId(R.id.popupButton)).perform(click());
    }

    private void fillLoginInfo(String email, String password) {
        onView(withId(R.id.editTextEmailRegister))
                .perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText(password), closeSoftKeyboard());
    }

    private void fillRegisteringInfo(String email, String password, int language) {
        onView(withId(R.id.editTextEmailRegister))
                .perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.flags_spinner))
                .perform(click());
        onView(withText(language)).perform(click());
    }

    private static boolean waitForElementUntilDisplayed(ViewInteraction element, final long millis) {
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

    private void deleteUserAfterTest() throws InterruptedException {
        Thread.sleep(3000);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            userIdForTest = currentUser.getUid();
            currentUser.delete();
        }
    }

    private void cleanDataAfterTest() {
        DatabaseReference userReferenceDB = FirebaseDatabase.getInstance().getReference(userIdForTest);
        userReferenceDB.removeValue();
    }

}
