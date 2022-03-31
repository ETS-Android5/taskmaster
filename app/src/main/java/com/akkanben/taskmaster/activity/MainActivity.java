package com.akkanben.taskmaster.activity;

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
import com.akkanben.taskmaster.adapter.TaskListRecyclerViewAdapter;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStatus;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = "main_activity_tag";
    public static final String TASK_NAME_EXTRA_TAG = "taskName";
    public static final String TASK_STATUS_EXTRA_TAG = "taskStatus";
    public static final String TASK_DESCRIPTION_EXTRA_TAG = "taskDescription";
    List<Task> taskList = new ArrayList<>();
    SharedPreferences preferences;
    TaskListRecyclerViewAdapter taskListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        ConstraintLayout constraintLayout = findViewById(R.id.main_activity_layout);

// Builder Pattern for hardcoded teams
//        Team workTeam =  Team.builder()
//                .name("Work")
//                .build();
//        Amplify.API.mutate(
//                ModelMutation.create(workTeam),
//                success -> Log.i(TAG, "Added"),
//                failure -> Log.i(TAG, "Failed")
//        );

        setupAnimatedBackground(constraintLayout);
        setupSettingsFloatingActionButton();
        setupAddTaskButton();
        setupAllTasksButton();
        setupTaskListRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String usernameString = preferences.getString(SettingsActivity.USERNAME_TAG, getString(R.string.my_tasks));
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

    private void setupAllTasksButton() {
        Button allTasksButton = findViewById(R.id.button_main_all_tasks);
        allTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAllTasksIntent = new Intent(MainActivity.this, AllTasksActivity.class);
                startActivity(goToAllTasksIntent);
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