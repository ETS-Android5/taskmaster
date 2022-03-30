package com.akkanben.taskmaster.activity;


import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
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
import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;
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
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TaskDetailTest {

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
    public void taskDetailTest() {


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
        appCompatEditText2.perform(replaceText("This is only a Test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.text_edit_add_task_task_description),
                        childAtPosition(
                                allOf(withId(R.id.view_add_task),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("A nice description of the test."), closeSoftKeyboard());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spinner_add_task_status), withContentDescription("Task Status"),
                        childAtPosition(
                                allOf(withId(R.id.view_add_task),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                7),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction materialTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(2);
        materialTextView.perform(click());

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

        pressBack();

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.button_task_list_fragment_task_list_item), withText("This is only a Test"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout),
                                        childAtPosition(
                                                withId(R.id.recycler_view_main_activity_task_list),
                                                0)),
                                0),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.task_detail_activity_task_title_text_view), withText("This is only a Test"),
                        withParent(allOf(withId(R.id.task_detail_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView.check(matches(withText("This is only a Test")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.text_view_task_detail_status), withText("In Progress"),
                        withParent(allOf(withId(R.id.task_detail_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView2.check(matches(withText("In Progress")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.text_view_task_detail_description), withText("A nice description of the test."),
                        withParent(allOf(withId(R.id.task_detail_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView3.check(matches(withText("A nice description of the test.")));
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
