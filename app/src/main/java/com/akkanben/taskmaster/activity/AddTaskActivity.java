package com.akkanben.taskmaster.activity;

import static com.akkanben.taskmaster.activity.SettingsActivity.USERNAME_TAG;
import static com.akkanben.taskmaster.utility.AnimationUtility.setupAnimatedBackground;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.akkanben.taskmaster.R;
import com.akkanben.taskmaster.activity.authentication.LogInActivity;
import com.akkanben.taskmaster.utility.EnumUtility;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStatus;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AddTaskActivity extends AppCompatActivity {
    public static final String TAG = "add_task_tag";
    CompletableFuture<List<Team>> teamsFuture = null;
    Spinner taskStatusSpinner = null;
    Spinner taskTeamSpinner = null;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ByteArrayOutputStream byteArrayOutputStream;
    String pickedFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ConstraintLayout constraintLayout = findViewById(R.id.view_add_task);
        byteArrayOutputStream = new ByteArrayOutputStream();
        setupAnimatedBackground(constraintLayout);
        setupFloatingAddFileButton();
        setupCallingIntent();
        activityResultLauncher = getActivityResultLauncher();
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
                        .attachment(pickedFileName)
                        .build();
                Amplify.API.mutate(
                        ModelMutation.create(newTask),
                        success -> {
                            Log.i(TAG, "AddTaskActivity.onCreate(): added a task");
                            InputStream pickedFileInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                            uploadInputStreamToS3(pickedFileInputStream, pickedFileName);
                        },
                        failure -> Log.i(TAG, "AddTaskActivity.onCreate(): failed to add a task")
                );
                ((EditText) findViewById(R.id.edit_text_add_task_task_title)).setText("");
                ((EditText) findViewById(R.id.text_edit_add_task_task_description)).setText("");
                taskStatusSpinner.setSelection(0);
                taskTeamSpinner.setSelection(0);
                titleEditText.requestFocus();
                Snackbar.make(findViewById(R.id.view_add_task), "Task Saved", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (byteArrayOutputStream != null) {
            InputStream pickedFileInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ImageView taskPreviewImageView = findViewById(R.id.image_view_add_task_activity_preview);
            taskPreviewImageView.setImageBitmap(BitmapFactory.decodeStream(pickedFileInputStream));
            taskPreviewImageView.setVisibility(View.VISIBLE);
        }
    }

    private void setupCallingIntent() {
        Intent callingIntent = getIntent();
        if (callingIntent != null && callingIntent.getType() != null && callingIntent.getType().startsWith("image")) {
            Uri fileUri = callingIntent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (fileUri != null) {
                InputStream pickedFileInputStream = null;
                try {
                    pickedFileInputStream = getContentResolver().openInputStream(fileUri);
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    int length;
                    while ((length = pickedFileInputStream.read(byteBuffer)) != -1)
                        byteArrayOutputStream.write(byteBuffer, 0, length);
                    byteArrayOutputStream.flush();
                } catch (IOException ioe) {
                    Log.i(TAG, "Could not save calling intent image to byte buffer" + ioe);
                }
                pickedFileName = getFileNameFromUri(fileUri);
                InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                ImageView taskPreviewImageView = findViewById(R.id.image_view_add_task_activity_preview);
                taskPreviewImageView.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                taskPreviewImageView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void uploadInputStreamToS3(InputStream inputStream, String fileName) {
        Amplify.Storage.uploadInputStream(
                fileName,
                inputStream,
                success -> {
                    Log.i(TAG, "Successfully uploaded. Key: " + success.getKey());
                },
                failure -> {
                    Log.i(TAG, "Could not upload " + fileName + " :" + failure.getMessage());
                }
        );
    }

    private void setupFloatingAddFileButton() {
        FloatingActionButton addFileFloatingButton = findViewById(R.id.floating_action_button_add_task_activity_add_file);
        addFileFloatingButton.setOnClickListener(view -> {
            Intent filePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
            filePickingIntent.setType("*/*");
            filePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpg", "image/png", "image/jpeg"});
            activityResultLauncher.launch(filePickingIntent);
        });
    }

    // Some code for creating the byteArrayOutputStream from https://stackoverflow.com/questions/5923817/how-to-clone-an-inputstream
    private ActivityResultLauncher<Intent> getActivityResultLauncher() {
        ActivityResultLauncher<Intent> pickingIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Uri pickedFileUri = result.getData().getData();
                            try {
                                InputStream pickedFileInputStream = getContentResolver().openInputStream(pickedFileUri);
                                byteArrayOutputStream = new ByteArrayOutputStream();
                                byte[] byteBuffer = new byte[1024];
                                int length;
                                while ((length = pickedFileInputStream.read(byteBuffer)) != -1)
                                    byteArrayOutputStream.write(byteBuffer, 0, length);
                                byteArrayOutputStream.flush();
                                pickedFileName = getFileNameFromUri(pickedFileUri);
                                Log.i(TAG, "Successfully took input stream from file. Filename: " + pickedFileName + " Uri: " + pickedFileUri);
                            } catch (IOException ioe) {
                                Log.e(TAG, "Could not pick file: " + ioe.getMessage(), ioe);
                            }
                        } else
                            Log.e(TAG, "Activity result error.");
                    }
                }
        );
        return pickingIntent;
    }

    // Taken from https://stackoverflow.com/a/25005243/16889809
    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
