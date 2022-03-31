package com.akkanben.taskmaster.activity;

import static com.akkanben.taskmaster.utility.AnimationUtility.setupAnimatedBackground;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.akkanben.taskmaster.R;
import com.akkanben.taskmaster.utility.EnumUtility;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences preferences;
    public static final String USERNAME_TAG = "username";
    public static final String TEAM_TAG = "team";
    public static final String TAG = "settings_activity";
    Spinner taskTeamSpinner = null;
    CompletableFuture<List<Team>> teamsFuture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ConstraintLayout constraintLayout = findViewById(R.id.settings_layout);
        setupAnimatedBackground(constraintLayout);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String usernameString = preferences.getString(USERNAME_TAG, "");
        String teamString = preferences.getString(TEAM_TAG, "");


        teamsFuture = new CompletableFuture<>();
        List<Team> teamList = new ArrayList<>();
        List<String> teamListAsString = new ArrayList<>();
        taskTeamSpinner = findViewById(R.id.spinner_settings_team);
        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Read products successfully");
                    for (Team team : success.getData()) {
                        teamList.add(team);
                    }
                    teamsFuture.complete(teamList);
                    runOnUiThread(() -> {
                        teamListAsString.add("All");
                        for (Team team : teamList)
                            teamListAsString.add(team.getName());
                        taskTeamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                teamListAsString
                        ));
                        if (!teamString.isEmpty()) {
                            taskTeamSpinner.setSelection(teamListAsString.indexOf(teamString));
                        }
                    });
                },
                failure -> Log.i(TAG, "Failed to read products")
        );

        if (!usernameString.isEmpty()) {
            EditText usernameEditText = findViewById(R.id.edit_text_settings_activity_username);
            usernameEditText.setText(usernameString);
        }

        Button saveButton = findViewById(R.id.button_settings_activity_save);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SharedPreferences.Editor preferencesEditor = preferences.edit();
                EditText usernameEditText = findViewById(R.id.edit_text_settings_activity_username);
                String usernameString = usernameEditText.getText().toString();
                String teamString = taskTeamSpinner.getSelectedItem().toString();
                preferencesEditor.putString(USERNAME_TAG, usernameString);
                preferencesEditor.putString(TEAM_TAG, teamString);
                preferencesEditor.apply();
            }
        });
    }
}