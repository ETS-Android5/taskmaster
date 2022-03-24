package com.akkanben.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.akkanben.taskmaster.R;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent callingIntent = getIntent();
        String taskNameString = null;
        String taskStatusString = null;
        String taskDescriptionString = null;
        if (callingIntent != null) {
            taskNameString = callingIntent.getStringExtra(MainActivity.TASK_NAME_EXTRA_TAG);
            taskStatusString = callingIntent.getStringExtra(MainActivity.TASK_STATUS_EXTRA_TAG);
            taskDescriptionString = callingIntent.getStringExtra(MainActivity.TASK_DESCRIPTION_EXTRA_TAG);
        }
        TextView taskNameTextView = findViewById(R.id.task_detail_activity_task_title_text_view);
        TextView taskStatusTextView = findViewById(R.id.text_view_task_detail_status);
        TextView taskDescriptionTextView = findViewById(R.id.text_view_task_detail_description);
        if (taskNameString != null)
            taskNameTextView.setText(taskNameString);
        else
            taskNameTextView.setText(R.string.no_task_name);
        if (taskStatusString != null)
            taskStatusTextView.setText(taskStatusString);
        else
            taskNameTextView.setText(R.string.no_task_name);
        if (taskDescriptionString != null)
            taskDescriptionTextView.setText(taskDescriptionString);
        else
            taskDescriptionTextView.setText(R.string.no_task_name);
    }
}