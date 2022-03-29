package com.akkanben.taskmaster.activity;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.akkanben.taskmaster.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UsernameSaveTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void usernameSaveTest() {

        ViewInteraction floatingSettingsButton = onView(
                allOf(withId(R.id.main_activity_settings_floating_action_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        floatingSettingsButton.perform(click());

        ViewInteraction usernameEditText = onView(
                allOf(withId(R.id.edit_text_settings_activity_username),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        usernameEditText.perform(replaceText(""), closeSoftKeyboard());

        ViewInteraction saveButton = onView(
                allOf(withId(R.id.button_settings_activity_save), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        saveButton.perform(click());

        pressBack();

        ViewInteraction usernameTitleTextView = onView(
                allOf(withId(R.id.main_activity_my_tasks_text_view), withText("My Tasks"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        usernameTitleTextView.check(matches(withText("My Tasks")));

        floatingSettingsButton.perform(click());
        usernameEditText.perform(replaceText("Ben"), closeSoftKeyboard());
        saveButton.perform(click());

        pressBack();

        ViewInteraction usernameTitleTextView2 = onView(
                allOf(withId(R.id.main_activity_my_tasks_text_view), withText("Ben's Tasks"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        usernameTitleTextView2.check(matches(withText("Ben's Tasks")));
        floatingSettingsButton.perform(click());
        usernameEditText.perform(replaceText(""), closeSoftKeyboard());
        saveButton.perform(click());

        pressBack();

        usernameTitleTextView.check(matches(withText("My Tasks")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
