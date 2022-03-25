package com.akkanben.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.akkanben.taskmaster.R;
import com.akkanben.taskmaster.adapter.TaskListRecyclerViewAdapter;
import com.akkanben.taskmaster.database.TaskmasterDatabase;
import com.akkanben.taskmaster.model.Task;
import com.akkanben.taskmaster.model.TaskStatus;
import com.google.android.material.snackbar.Snackbar;

public class AddTaskActivity extends AppCompatActivity {

    private final int FADE_IN_SPEED = 500;
    private final int FADE_OUT_SPEED = 1200;
    TaskmasterDatabase taskmasterDatabase;
    TaskListRecyclerViewAdapter taskListRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskmasterDatabase = Room.databaseBuilder(
               getApplicationContext(),
               TaskmasterDatabase.class,
               "akkanben_taskmaster")
               .allowMainThreadQueries()
               .build();

        Button addTaskButton = findViewById(R.id.button_add_task_add_task);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task newTask = new Task(
                        ((EditText)findViewById(R.id.edit_text_add_task_task_title)).getText().toString(),
                        ((EditText)findViewById(R.id.text_edit_add_task_task_description)).getText().toString(),
                        TaskStatus.IN_PROGRESS
                );
                taskmasterDatabase.taskDao().insertTask(newTask);
                taskListRecyclerViewAdapter.notifyItemInserted(taskmasterDatabase.taskDao().getTaskCount());
                // TODO make snackbar
            }
        });
    }
}
