package com.akkanben.taskmaster.activity;

import static com.akkanben.taskmaster.activity.SettingsActivity.USERNAME_TAG;
import static com.akkanben.taskmaster.utility.AnimationUtility.setupAnimatedBackground;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.akkanben.taskmaster.R;
import com.akkanben.taskmaster.activity.authentication.LogInActivity;
import com.akkanben.taskmaster.adapter.TaskListRecyclerViewAdapter;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "main_activity_tag";
    public static final String TASK_NAME_EXTRA_TAG = "taskName";
    public static final String TASK_STATUS_EXTRA_TAG = "taskStatus";
    public static final String TASK_DESCRIPTION_EXTRA_TAG = "taskDescription";
    public static final String TASK_ATTACHMENT_EXTRA_TAG = "taskAttachment";

    List<Task> taskList = new ArrayList<>();
    SharedPreferences preferences;
    TaskListRecyclerViewAdapter taskListAdapter;
    String usernameString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        ConstraintLayout constraintLayout = findViewById(R.id.main_activity_layout);
        setupAnimatedBackground(constraintLayout);
        setupSettingsFloatingActionButton();
        setupAddTaskButton();
        setupTaskListRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String usernameStringInPreferences = preferences.getString(SettingsActivity.USERNAME_TAG, getString(R.string.my_tasks));
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        if (authUser == null) {
            Intent goToLogInIntent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(goToLogInIntent);
        } else {
            Amplify.Auth.fetchUserAttributes(
                    success -> {
                        for (AuthUserAttribute userAttribute : success) {
                            if (userAttribute.getKey().getKeyString().equals("nickname")) {
                                if (!preferences.getString(USERNAME_TAG, getString(R.string.my_tasks)).toString().equals(userAttribute.getValue())) {
                                    runOnUiThread(() -> {
                                        SharedPreferences.Editor preferencesEditor = preferences.edit();
                                        usernameString = userAttribute.getValue();
                                        preferencesEditor.putString(USERNAME_TAG, usernameString);
                                        preferencesEditor.apply();
                                    });
                                }
                            }
                        }
                    },
                    failure -> Log.i(TAG, "Failed to get user attributes: " + failure.toString())
            );
        }
        usernameString = preferences.getString(SettingsActivity.USERNAME_TAG, getString(R.string.my_tasks));
        if (usernameString.equals("") || usernameString.equals(getString(R.string.my_tasks)))
            ((TextView)findViewById(R.id.main_activity_my_tasks_text_view)).setText(getString(R.string.my_tasks));
        else
            ((TextView)findViewById(R.id.main_activity_my_tasks_text_view)).setText(getString(R.string.usernames_tasks, usernameString));
        setupTaskListFromDatabase();
        taskListAdapter.updateListData(taskList);
    }

    private void setupTaskListFromDatabase() {
        String currentTeam = preferences.getString(SettingsActivity.TEAM_TAG, "All");
        taskList.clear();
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(TAG, "Read products successfully");
                    for (Task task : success.getData()) {
                        if (currentTeam.equals("All") || task.getTeam().getName().equals(currentTeam))
                            taskList.add(task);
                    }
                    runOnUiThread(() -> taskListAdapter.notifyDataSetChanged());
        },
                failure -> Log.i(TAG, "Failed to read products")
        );
    }

    private void setupSettingsFloatingActionButton() {
        FloatingActionButton settingsFloatingActionButton = findViewById(R.id.main_activity_settings_floating_action_button);
        settingsFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToSettingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(goToSettingsIntent);
            }
        });
    }

    private void setupAddTaskButton() {
        Button addTaskButton = findViewById(R.id.button_main_add_task);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAddTaskIntent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(goToAddTaskIntent);
            }
        });
    }

    private void setupTaskListRecyclerView() {
        RecyclerView taskListRecyclerView = findViewById(R.id.recycler_view_main_activity_task_list);
        RecyclerView.LayoutManager taskListLayoutManager = new LinearLayoutManager(this);
        taskListRecyclerView.setLayoutManager(taskListLayoutManager);
        taskListAdapter = new TaskListRecyclerViewAdapter(taskList, this);
        taskListRecyclerView.setAdapter(taskListAdapter);
    }
}