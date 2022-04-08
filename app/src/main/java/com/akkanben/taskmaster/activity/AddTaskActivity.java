package com.akkanben.taskmaster.activity;

import static com.akkanben.taskmaster.activity.SettingsActivity.USERNAME_TAG;
import static com.akkanben.taskmaster.utility.AnimationUtility.setupAnimatedBackground;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    FusedLocationProviderClient locationProviderClient = null;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ConstraintLayout constraintLayout = findViewById(R.id.view_add_task);
        byteArrayOutputStream = new ByteArrayOutputStream();
        setupAnimatedBackground(constraintLayout);
        setupFloatingAddFileButton();
        setupCallingIntent();
        setupLocation();
        activityResultLauncher = getActivityResultLauncher();
        teamsFuture = new CompletableFuture<>();
        List<Team> teamList = new ArrayList<>();
        setupSpinners(teamList);
        setupSaveButton(teamList);
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

    private void setupSpinners(List<Team> teamList) {
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
    }

    private void setupSaveButton(List<Team> teamList) {
        Button addTaskButton = findViewById(R.id.button_add_task_add_task);
        addTaskButton.setOnClickListener(view -> {
            EditText titleEditText = findViewById(R.id.edit_text_add_task_task_title);
            String title = titleEditText.getText().toString();
            String description = ((EditText)findViewById(R.id.text_edit_add_task_task_description)).getText().toString();
            TaskStatus newStatus = EnumUtility.taskStatusFromString(taskStatusSpinner.getSelectedItem().toString());
            String teamString = taskTeamSpinner.getSelectedItem().toString();
            Team team = teamList.stream().filter(e -> e.getName().equals(teamString)).collect(Collectors.toList()).get(0);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "No permissions for fine or coarse location");
                return;
            }
            locationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                Log.i(TAG, "LAT: " + location.getLatitude());
                Log.i(TAG, "LON: " + location.getLongitude());
                String lat = Double.toString(location.getLatitude());
                String lon = Double.toString(location.getLongitude());
                String address = "";
                String taskLocation = "";
                try {
                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    taskLocation = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "LOCATION: " + address);
                saveProductToCloud(title, description, newStatus, team, pickedFileName, lat, lon, taskLocation);
            });
            clearInputs(titleEditText);
            Snackbar.make(findViewById(R.id.view_add_task), "Task Saved", Snackbar.LENGTH_SHORT).show();
                });
    }

    private void saveProductToCloud(String title, String body, TaskStatus status, Team team, String attachmentFileNameString, String latitude, String longitude, String location) {
        Task newTask = Task.builder()
                .title(title)
                .body(body)
                .status(status)
                .team(team)
                .attachment(attachmentFileNameString)
                .latitude(latitude)
                .longitude(longitude)
                .location(location)
                .build();
        Amplify.API.mutate(
                ModelMutation.create(newTask),
                success -> {
                    Log.i(TAG, "AddTaskActivity.onCreate(): added a task");
                    InputStream pickedFileInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                    uploadInputStreamToS3(pickedFileInputStream, pickedFileName);
                    byteArrayOutputStream = null;
                },
                failure -> Log.i(TAG, "AddTaskActivity.onCreate(): failed to add a task")
        );
    }

    private void clearInputs(EditText newFocus) {
        ((EditText) findViewById(R.id.edit_text_add_task_task_title)).setText("");
        ((EditText) findViewById(R.id.text_edit_add_task_task_description)).setText("");
        ((ImageView) findViewById(R.id.image_view_add_task_activity_preview)).setVisibility(View.INVISIBLE);
        taskStatusSpinner.setSelection(0);
        taskTeamSpinner.setSelection(0);
        newFocus.requestFocus();
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

    private void setupLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "This app does not have permission to access either fine or coarse location");
            return;
        }
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        locationProviderClient.flushLocations();

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
