package com.akkanben.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    public static final String TASK_NAME_EXTRA_TAG = "taskName";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setupTaskButtons();
        setupSettingsFloatingActionButton();
        setupAddTaskButton();
        setupAllTasksButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String usernameString = preferences.getString(SettingsActivity.USERNAME_TAG, getString(R.string.my_tasks));
        if (usernameString.equals("") || usernameString.equals(getString(R.string.my_tasks)))
            ((TextView)findViewById(R.id.main_activity_my_tasks_text_view)).setText(getString(R.string.my_tasks));
        else
            ((TextView)findViewById(R.id.main_activity_my_tasks_text_view)).setText(getString(R.string.usernames_tasks, usernameString));
    }


    private void setupTaskButtons() {
        Button taskOneButton = findViewById(R.id.button_main_activity_task_one);
        Button taskTwoButton = findViewById(R.id.button_main_activity_task_two);
        Button taskThreeButton = findViewById(R.id.button_main_activity_task_three);
        taskOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTaskOneIntent = new Intent(MainActivity.this, TaskDetailActivity.class);
                Button clickedButton = (Button) view;
                String taskName = clickedButton.getText().toString();
                goToTaskOneIntent.putExtra(TASK_NAME_EXTRA_TAG, taskName);
                startActivity(goToTaskOneIntent);
            }
        });
        taskTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTaskTwoIntent = new Intent(MainActivity.this, TaskDetailActivity.class);
                Button clickedButton = (Button) view;
                String taskName = clickedButton.getText().toString();
                goToTaskTwoIntent.putExtra(TASK_NAME_EXTRA_TAG, taskName);
                startActivity(goToTaskTwoIntent);
            }
        });
        taskThreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTaskThreeIntent= new Intent(MainActivity.this, TaskDetailActivity.class);
                Button clickedButton = (Button) view;
                String taskName = clickedButton.getText().toString();
                goToTaskThreeIntent.putExtra(TASK_NAME_EXTRA_TAG, taskName);
                startActivity(goToTaskThreeIntent);
            }
        });
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
}