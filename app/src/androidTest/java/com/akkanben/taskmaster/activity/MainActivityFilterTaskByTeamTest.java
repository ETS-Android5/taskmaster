package com.akkanben.taskmaster.activity;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.akkanben.taskmaster.R;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityFilterTaskByTeamTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void prep() {
        clearDatabase();
        mActivityTestRule.launchActivity(null);
    }

    public void clearDatabase() {
        List<Task> taskList = new ArrayList<>();
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i("test", "Read products successfully");
                    for (Task task : success.getData()) {
                        Amplify.API.mutate(
                                ModelMutation.delete(task),
                                deletionSuccess -> Log.i("test", "Test.clearDatabase(): deleted a task"),
                                deletionFailure -> Log.i("test", "Test.clearDatabase(): failed to delete a task")
                        );
                    }
                },
                failure -> Log.i("test", "Failed to read products")
        );
    }

    @Test
    public void mainActivityFilterTaskByTeamTest() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.button_main_add_task), withText("add task"),
                        childAtPosition(
                                allOf(withId(R.id.main_activity_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.edit_text_add_task_task_title),
                        childAtPosition(
                                allOf(withId(R.id.view_add_task),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.edit_text_add_task_task_title),
                        childAtPosition(
                                allOf(withId(R.id.view_add_task),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("Study Task 1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.text_edit_add_task_task_description),
                        childAtPosition(
                                allOf(withId(R.id.view_add_task),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.button_add_task_add_task), withText("add task"),
                        childAtPosition(
                                allOf(withId(R.id.view_add_task),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                6),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.edit_text_add_task_task_title),
                        childAtPosition(
                                allOf(withId(R.id.view_add_task),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("Work Task 1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.text_edit_add_task_task_description),
                        childAtPosition(
                                allOf(withId(R.id.view_add_task),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spinner_add_task_team),
                        childAtPosition(
                                allOf(withId(R.id.view_add_task),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                8),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction materialTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        materialTextView.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.button_add_task_add_task), withText("add task"),
                        childAtPosition(
                                allOf(withId(R.id.view_add_task),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                6),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.edit_text_add_task_task_title),
                        childAtPosition(
                                allOf(withId(R.id.view_add_task),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText8.perform(click());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.edit_text_add_task_task_title),
                        childAtPosition(
                                allOf(withId(R.id.view_add_task),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText11.perform(click());

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.edit_text_add_task_task_title),
                        childAtPosition(
                                allOf(withId(R.id.view_add_task),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText12.perform(replaceText("Home Task 1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText13 = onView(
                allOf(withId(R.id.text_edit_add_task_task_description),
                        childAtPosition(
                                allOf(withId(R.id.view_add_task),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatEditText13.perform(click());

        ViewInteraction appCompatEditText14 = onView(
                allOf(withId(R.id.text_edit_add_task_task_description),
                        childAtPosition(
                                allOf(withId(R.id.view_add_task),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatEditText14.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatSpinner3 = onView(
                allOf(withId(R.id.spinner_add_task_team),
                        childAtPosition(
                                allOf(withId(R.id.view_add_task),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                8),
                        isDisplayed()));
        appCompatSpinner3.perform(click());

        DataInteraction materialTextView3 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(2);
        materialTextView3.perform(click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.button_add_task_add_task), withText("add task"),
                        childAtPosition(
                                allOf(withId(R.id.view_add_task),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                6),
                        isDisplayed()));
        materialButton6.perform(click());

        pressBack();

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.main_activity_settings_floating_action_button), withContentDescription("Settings"),
                        childAtPosition(
                                allOf(withId(R.id.main_activity_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatSpinner6 = onView(
                allOf(withId(R.id.spinner_settings_team),
                        childAtPosition(
                                allOf(withId(R.id.settings_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatSpinner6.perform(click());

        DataInteraction materialTextView6 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        materialTextView6.perform(click());

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.button_settings_activity_save), withText("Save"),
                        childAtPosition(
                                allOf(withId(R.id.settings_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton8.perform(click());

        pressBack();

        ViewInteraction materialButton9 = onView(
                allOf(withId(R.id.button_task_list_fragment_task_list_item), withText("Study Task 1"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout),
                                        childAtPosition(
                                                withId(R.id.recycler_view_main_activity_task_list),
                                                0)),
                                0),
                        isDisplayed()));
        materialButton9.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.task_detail_activity_task_title_text_view), withText("Study Task 1"),
                        withParent(allOf(withId(R.id.task_detail_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView.check(matches(withText("Study Task 1")));

        pressBack();

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.main_activity_settings_floating_action_button), withContentDescription("Settings"),
                        childAtPosition(
                                allOf(withId(R.id.main_activity_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatSpinner7 = onView(
                allOf(withId(R.id.spinner_settings_team),
                        childAtPosition(
                                allOf(withId(R.id.settings_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatSpinner7.perform(click());

        DataInteraction materialTextView7 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(2);
        materialTextView7.perform(click());

        ViewInteraction materialButton11 = onView(
                allOf(withId(R.id.button_settings_activity_save), withText("Save"),
                        childAtPosition(
                                allOf(withId(R.id.settings_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton11.perform(click());

        pressBack();

        ViewInteraction materialButton12 = onView(
                allOf(withId(R.id.button_task_list_fragment_task_list_item), withText("Work Task 1"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout),
                                        childAtPosition(
                                                withId(R.id.recycler_view_main_activity_task_list),
                                                0)),
                                0),
                        isDisplayed()));
        materialButton12.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.task_detail_activity_task_title_text_view), withText("Work Task 1"),
                        withParent(allOf(withId(R.id.task_detail_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView3.check(matches(withText("Work Task 1")));

        pressBack();

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.main_activity_settings_floating_action_button), withContentDescription("Settings"),
                        childAtPosition(
                                allOf(withId(R.id.main_activity_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        floatingActionButton3.perform(click());

        ViewInteraction appCompatSpinner8 = onView(
                allOf(withId(R.id.spinner_settings_team),
                        childAtPosition(
                                allOf(withId(R.id.settings_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatSpinner8.perform(click());

        DataInteraction materialTextView8 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(3);
        materialTextView8.perform(click());

        ViewInteraction materialButton14 = onView(
                allOf(withId(R.id.button_settings_activity_save), withText("Save"),
                        childAtPosition(
                                allOf(withId(R.id.settings_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton14.perform(click());

        pressBack();

        ViewInteraction materialButton15 = onView(
                allOf(withId(R.id.button_task_list_fragment_task_list_item), withText("Home Task 1"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout),
                                        childAtPosition(
                                                withId(R.id.recycler_view_main_activity_task_list),
                                                0)),
                                0),
                        isDisplayed()));
        materialButton15.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.task_detail_activity_task_title_text_view), withText("Home Task 1"),
                        withParent(allOf(withId(R.id.task_detail_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView5.check(matches(withText("Home Task 1")));

        pressBack();

        ViewInteraction floatingActionButton4 = onView(
                allOf(withId(R.id.main_activity_settings_floating_action_button), withContentDescription("Settings"),
                        childAtPosition(
                                allOf(withId(R.id.main_activity_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        floatingActionButton4.perform(click());

        ViewInteraction appCompatSpinner9 = onView(
                allOf(withId(R.id.spinner_settings_team),
                        childAtPosition(
                                allOf(withId(R.id.settings_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatSpinner9.perform(click());

        DataInteraction materialTextView9 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(0);
        materialTextView9.perform(click());

        ViewInteraction materialButton13 = onView(
                allOf(withId(R.id.button_settings_activity_save), withText("Save"),
                        childAtPosition(
                                allOf(withId(R.id.settings_layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton13.perform(click());
        clearDatabase();
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
