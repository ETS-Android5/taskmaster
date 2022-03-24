package com.akkanben.taskmaster.activity;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
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
public class TaskDetailTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void taskDetailTest() {
        ViewInteraction materialButton = onView(
allOf(withId(R.id.button_task_list_fragment_task_list_item), withText("Lab: 28 - RecyclerView"),
childAtPosition(
allOf(withId(R.id.frameLayout),
childAtPosition(
withId(R.id.recycler_view_main_activity_task_list),
0)),
0),
isDisplayed()));
        materialButton.perform(click());
        
        ViewInteraction textView = onView(
allOf(withId(R.id.task_detail_activity_task_title_text_view), withText("Lab: 28 - RecyclerView"),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        textView.check(matches(withText("Lab: 28 - RecyclerView")));
        
        ViewInteraction textView2 = onView(
allOf(withId(R.id.text_view_task_detail_status), withText("IN_PROGRESS"),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        textView2.check(matches(withText("IN_PROGRESS")));
        
        ViewInteraction textView3 = onView(
allOf(withId(R.id.text_view_task_detail_description), withText("It's a lab. Fun."),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        textView3.check(matches(withText("It's a lab. Fun.")));
        
        pressBack();
        
        ViewInteraction materialButton2 = onView(
allOf(withId(R.id.button_task_list_fragment_task_list_item), withText("Code Challenge: Class 28"),
childAtPosition(
allOf(withId(R.id.frameLayout),
childAtPosition(
withId(R.id.recycler_view_main_activity_task_list),
1)),
0),
isDisplayed()));
        materialButton2.perform(click());
        
        ViewInteraction textView4 = onView(
allOf(withId(R.id.task_detail_activity_task_title_text_view), withText("Code Challenge: Class 28"),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        textView4.check(matches(withText("Code Challenge: Class 28")));
        
        ViewInteraction textView5 = onView(
allOf(withId(R.id.text_view_task_detail_status), withText("COMPLETE"),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        textView5.check(matches(withText("COMPLETE")));
        
        ViewInteraction textView6 = onView(
allOf(withId(R.id.text_view_task_detail_description), withText("Quick! Sort!"),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()));
        textView6.check(matches(withText("Quick! Sort!")));
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
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
