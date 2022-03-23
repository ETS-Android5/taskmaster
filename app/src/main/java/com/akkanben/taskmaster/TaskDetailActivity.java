package com.akkanben.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent callingIntent = getIntent();
        String taskNameString = null;
        if (callingIntent != null)
            taskNameString = callingIntent.getStringExtra(MainActivity.TASK_NAME_EXTRA_TAG);
        TextView taskNameTextView = findViewById(R.id.task_detail_activity_task_title_text_view);
        if (taskNameString != null)
            taskNameTextView.setText(taskNameString);
        else
            taskNameTextView.setText(R.string.no_task_name);
    }
}