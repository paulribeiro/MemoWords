package com.paulribe.memowords.common;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static java.util.EnumSet.allOf;

import android.app.Activity;
import android.content.Context;
import android.view.MenuItem;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.paulribe.memowords.R;
import com.paulribe.memowords.views.MainActivity;
import com.paulribe.memowords.views.SplashActivity;

import org.junit.Rule;

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
        onView(withId(R.id.action_log_user)).perform(click());
        onView(withText(R.string.logout)).perform(ViewActions.click());
    }
}
