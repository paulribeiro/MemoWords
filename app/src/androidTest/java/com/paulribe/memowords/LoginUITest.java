package com.paulribe.memowords;

import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paulribe.memowords.views.MainActivity;
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
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.paulribe.memowords.common.UITestHelper.signIn;
import static com.paulribe.memowords.common.UITestHelper.waitForElementUntilDisplayed;
import static org.hamcrest.Matchers.not;

/**
 * Instrumented test to verify that the login functionality is working.
 */
@RunWith(AndroidJUnit4.class)
public class LoginUITest {

    private static final String UNREGISTERED_EMAIL = "test1@gmail.com";
    private static final String PASSWORD = "aaaa0000";

    private View decorView;

    @Rule
    public ActivityScenarioRule<SplashActivity> activityRule
            = new ActivityScenarioRule<>(SplashActivity.class);

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

        signIn(UNREGISTERED_EMAIL, PASSWORD);
        verifyLoginFailedToastDisplayed();
        signUp(UNREGISTERED_EMAIL, PASSWORD, R.string.french);

        waitForElementUntilDisplayed(onView(withId(R.id.noMoreWordsFragment)), 3000);
    }

    private void verifyLoginFailedToastDisplayed() {
        ViewInteraction toastCheck = onView(withText(R.string.login_failed)).inRoot(withDecorView(not(decorView)));
        waitForElementUntilDisplayed(toastCheck, 3000);
    }

    private void signUp(String email, String password, int nativeLanguageId) {
        onView(withId(R.id.textViewSignUp)).perform(click());
        fillRegisteringInfo(email, password, nativeLanguageId);
        onView(withId(R.id.buttonSignup)).perform(click());
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

    private void deleteUserAfterTest() throws InterruptedException {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            cleanDataAfterTest(currentUser.getUid());
            currentUser.delete();
        }
    }

    private void cleanDataAfterTest(String userId) {
        DatabaseReference userReferenceDB = FirebaseDatabase.getInstance().getReference(userId);
        userReferenceDB.removeValue();
    }

}
