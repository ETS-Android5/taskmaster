package com.akkanben.taskmaster.activity;

import static com.akkanben.taskmaster.utility.AnimationUtility.setupAnimatedBackground;
import static com.amplifyframework.datastore.generated.model.TaskStatus.NEW;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.akkanben.taskmaster.R;
import com.akkanben.taskmaster.utility.EnumUtility;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStatus;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class AddTaskActivity extends AppCompatActivity {

    public static final String TAG = "add_task_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ConstraintLayout constraintLayout = findViewById(R.id.view_add_task);
        setupAnimatedBackground(constraintLayout);

        //TODO: change to dynamo DB

        Spinner taskStatusSpinner = findViewById(R.id.spinner_add_task_status);
        ArrayList<String> statusList = EnumUtility.getTaskStatusList();
        taskStatusSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                statusList
        ));

        Button addTaskButton = findViewById(R.id.button_add_task_add_task);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText titleEditText = findViewById(R.id.edit_text_add_task_task_title);
                EditText descriptionEditText = findViewById(R.id.text_edit_add_task_task_description);
                TaskStatus newStatus = EnumUtility.taskStatusFromString(taskStatusSpinner.getSelectedItem().toString());
                Task newTask = Task.builder()
                        .title(titleEditText.getText().toString())
                        .body(descriptionEditText.getText().toString())
                        .status(newStatus)
                        .build();
                Amplify.API.mutate(
                        ModelMutation.create(newTask),
                        success -> Log.i(TAG, "AddTaskActivity.onCreate(): added a task"),
                        failure -> Log.i(TAG, "AddTaskActivity.onCreate(): failed to add a task")
                );
                ((EditText)findViewById(R.id.edit_text_add_task_task_title)).setText("");
                ((EditText)findViewById(R.id.text_edit_add_task_task_description)).setText("");
                taskStatusSpinner.setSelection(0);
                titleEditText.requestFocus();
                Snackbar.make(findViewById(R.id.view_add_task), "Task Saved", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
