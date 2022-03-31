package com.akkanben.taskmaster.activity;

import static com.akkanben.taskmaster.utility.AnimationUtility.setupAnimatedBackground;

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
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStatus;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AddTaskActivity extends AppCompatActivity {

    public static final String TAG = "add_task_tag";
    CompletableFuture<List<Team>> teamsFuture = null;
    Spinner taskStatusSpinner = null;
    Spinner taskTeamSpinner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ConstraintLayout constraintLayout = findViewById(R.id.view_add_task);
        setupAnimatedBackground(constraintLayout);
        teamsFuture = new CompletableFuture<>();
        List<Team> teamList = new ArrayList<>();
        List<String> teamListAsString = new ArrayList<>();

        taskStatusSpinner = findViewById(R.id.spinner_add_task_status);
        ArrayList<String> statusList = EnumUtility.getTaskStatusList();
        taskStatusSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                statusList
        ));

        taskTeamSpinner = findViewById(R.id.spinner_add_task_team);
        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Read products successfully");
                    for (Team team : success.getData()) {
                        teamList.add(team);
                    }
                    teamsFuture.complete(teamList);
                    runOnUiThread(() -> {
                        for (Team team : teamList)
                            teamListAsString.add(team.getName());
                        taskTeamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                teamListAsString
                        ));
                    });
                },
                failure -> Log.i(TAG, "Failed to read products")
        );

        Button addTaskButton = findViewById(R.id.button_add_task_add_task);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText titleEditText = findViewById(R.id.edit_text_add_task_task_title);
                EditText descriptionEditText = findViewById(R.id.text_edit_add_task_task_description);
                TaskStatus newStatus = EnumUtility.taskStatusFromString(taskStatusSpinner.getSelectedItem().toString());
                String teamString = taskTeamSpinner.getSelectedItem().toString();
                Team team = teamList.stream().filter(e -> e.getName().equals(teamString)).collect(Collectors.toList()).get(0);
                Task newTask = Task.builder()
                        .title(titleEditText.getText().toString())
                        .body(descriptionEditText.getText().toString())
                        .status(newStatus)
                        .team(team)
                        .build();
                Amplify.API.mutate(
                        ModelMutation.create(newTask),
                        success -> Log.i(TAG, "AddTaskActivity.onCreate(): added a task"),
                        failure -> Log.i(TAG, "AddTaskActivity.onCreate(): failed to add a task")
                );
                ((EditText)findViewById(R.id.edit_text_add_task_task_title)).setText("");
                ((EditText)findViewById(R.id.text_edit_add_task_task_description)).setText("");
                taskStatusSpinner.setSelection(0);
                taskTeamSpinner.setSelection(0);
                titleEditText.requestFocus();
                Snackbar.make(findViewById(R.id.view_add_task), "Task Saved", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
