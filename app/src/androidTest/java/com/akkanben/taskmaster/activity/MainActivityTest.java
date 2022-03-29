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
import androidx.test.espresso.internal.data.model.ActionData;
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
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {

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
        ViewInteraction textView = onView(
                allOf(withText("TaskMaster"),
                        withParent(allOf(withId(androidx.appcompat.R.id.action_bar),
                                withParent(withId(androidx.appcompat.R.id.action_bar_container)))),
                        isDisplayed()));
        textView.check(matches(withText("TaskMaster")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.main_activity_my_tasks_text_view), withText("My Tasks"),
                        withParent(allOf(withId(R.id.main_activity_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView2.check(matches(withText("My Tasks")));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.main_activity_settings_floating_action_button),
                        withParent(allOf(withId(R.id.main_activity_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.button_main_add_task), withText("ADD TASK"),
                        withParent(allOf(withId(R.id.main_activity_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.button_main_all_tasks), withText("ALL TASKS"),
                        withParent(allOf(withId(R.id.main_activity_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));
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
