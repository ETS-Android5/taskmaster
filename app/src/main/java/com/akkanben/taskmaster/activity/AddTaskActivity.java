package com.akkanben.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.akkanben.taskmaster.R;
import com.akkanben.taskmaster.database.TaskmasterDatabase;
import com.akkanben.taskmaster.model.Task;
import com.akkanben.taskmaster.model.TaskStatus;
import com.google.android.material.snackbar.Snackbar;

public class AddTaskActivity extends AppCompatActivity {

    TaskmasterDatabase taskmasterDatabase;

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
        Spinner taskStatusSpinner = findViewById(R.id.spinner_add_task_status);
        taskStatusSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskStatus.values()
        ));

        Button addTaskButton = findViewById(R.id.button_add_task_add_task);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task newTask = new Task(
                        ((EditText)findViewById(R.id.edit_text_add_task_task_title)).getText().toString(),
                        ((EditText)findViewById(R.id.text_edit_add_task_task_description)).getText().toString(),
                        TaskStatus.fromString(taskStatusSpinner.getSelectedItem().toString())
                );
                taskmasterDatabase.taskDao().insertTask(newTask);
                Snackbar.make(findViewById(R.id.view_add_task), "Task Saved", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
