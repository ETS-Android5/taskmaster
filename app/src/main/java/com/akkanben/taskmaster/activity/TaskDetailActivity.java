package com.akkanben.taskmaster.activity;

import static com.akkanben.taskmaster.R.color.purple_200;
import static com.akkanben.taskmaster.R.color.purple_500;
import static com.akkanben.taskmaster.R.color.purple_700;
import static com.akkanben.taskmaster.R.color.teal_200;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.akkanben.taskmaster.R;
import com.akkanben.taskmaster.utility.EnumUtility;
import com.amplifyframework.datastore.generated.model.TaskStatus;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Intent callingIntent = getIntent();
        String taskNameString = null;
        String taskStatusString = null;
        String taskDescriptionString = null;
        TaskStatus status = null;
        if (callingIntent != null) {
            taskNameString = callingIntent.getStringExtra(MainActivity.TASK_NAME_EXTRA_TAG);
            taskStatusString = callingIntent.getStringExtra(MainActivity.TASK_STATUS_EXTRA_TAG);
            status = TaskStatus.valueOf(taskStatusString);
            taskDescriptionString = callingIntent.getStringExtra(MainActivity.TASK_DESCRIPTION_EXTRA_TAG);
            setupStatusBackgroundColor(status);
        }
        TextView taskNameTextView = findViewById(R.id.task_detail_activity_task_title_text_view);
        TextView taskStatusTextView = findViewById(R.id.text_view_task_detail_status);
        TextView taskDescriptionTextView = findViewById(R.id.text_view_task_detail_description);
        if (taskNameString != null)
            taskNameTextView.setText(taskNameString);
        else
            taskNameTextView.setText(R.string.no_task_name);
        if (taskStatusString != null)
            taskStatusTextView.setText(EnumUtility.taskStatusToString(status));
        else
            taskNameTextView.setText(R.string.no_task_name);
        if (taskDescriptionString != null)
            taskDescriptionTextView.setText(taskDescriptionString);
        else
            taskDescriptionTextView.setText(R.string.no_task_name);
    }

    private void setupStatusBackgroundColor(TaskStatus status) {
        ConstraintLayout constraintLayout = findViewById(R.id.task_detail_layout);
        switch(status) {
            case COMPLETE:
                constraintLayout.setBackgroundResource(R.drawable.activity_detail_background_complete);
                break;
            case ASSIGNED:
                constraintLayout.setBackgroundResource(R.drawable.activity_detail_background_assigned);
                break;
            case IN_PROGRESS:
                constraintLayout.setBackgroundResource(R.drawable.activity_detail_background_in_progress);
                break;
            default:
                constraintLayout.setBackgroundResource(R.drawable.activity_detail_background_new);
                break;
        }
    }
}