package com.akkanben.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.akkanben.taskmaster.R;
import com.akkanben.taskmaster.utility.EnumUtility;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskStatus;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TaskDetailActivity extends AppCompatActivity {

    public static final String TAG = "task_detail_activity_tag";
    private final MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        setupTaskDetailsViaIntent();
        setupTextToSpeechButton();
    }

    private void setupTaskDetailsViaIntent() {
        Intent callingIntent = getIntent();
        String taskNameString = null;
        String taskStatusString = null;
        String taskDescriptionString = null;
        String taskAttachementKeyString = null;
        String taskLocationString = null;
        TaskStatus status = null;
        if (callingIntent != null) {
            taskNameString = callingIntent.getStringExtra(MainActivity.TASK_NAME_EXTRA_TAG);
            taskStatusString = callingIntent.getStringExtra(MainActivity.TASK_STATUS_EXTRA_TAG);
            status = TaskStatus.valueOf(taskStatusString);
            taskDescriptionString = callingIntent.getStringExtra(MainActivity.TASK_DESCRIPTION_EXTRA_TAG);
            taskAttachementKeyString = callingIntent.getStringExtra(MainActivity.TASK_ATTACHMENT_EXTRA_TAG);
            taskLocationString = callingIntent.getStringExtra(MainActivity.TASK_LOCATION_EXTRA_TAG);
            setupStatusBackgroundColor(status);
        }

        TextView taskNameTextView = findViewById(R.id.text_view_task_detail_activity_task_title);
        if (taskNameString != null)
            taskNameTextView.setText(taskNameString);
        else
            taskNameTextView.setText(R.string.no_task_name);


        TextView taskStatusTextView = findViewById(R.id.text_view_task_detail_status);
        if (taskStatusString != null)
            taskStatusTextView.setText(EnumUtility.taskStatusToString(status));
        else
            taskNameTextView.setText(R.string.no_task_status);


        TextView taskDescriptionTextView = findViewById(R.id.text_view_task_detail_description);
        if (taskDescriptionString != null)
            taskDescriptionTextView.setText(taskDescriptionString);
        else
            taskDescriptionTextView.setText(R.string.no_task_name);

        TextView taskLocationTextView = findViewById(R.id.text_view_task_detail_activity_location);
        if (taskLocationString != null)
            taskLocationTextView.setText(taskLocationString);
        else
            taskLocationTextView.setText(R.string.no_location);

        if (taskAttachementKeyString != null && !taskAttachementKeyString.isEmpty()) {
            Amplify.Storage.downloadFile(
                    taskAttachementKeyString,
                    new File(getApplication().getFilesDir(), taskAttachementKeyString),
                    success -> {
                        ImageView taskImageView = findViewById(R.id.image_view_task_detail_attachment);
                        taskImageView.setImageBitmap(BitmapFactory.decodeFile(success.getFile().getPath()));
                    },
                    failure -> {
                        Log.e(TAG, "Unable to get image with S3 key because " + failure.getMessage());
                    }
            );
        }
    }

    private void setupTextToSpeechButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.floating_action_button_task_detail_activity_tts);
        String textToSpeech = ((TextView)findViewById(R.id.text_view_task_detail_activity_task_title)).getText().toString()
                + ", "
                + ((TextView)findViewById(R.id.text_view_task_detail_description)).getText().toString();
        floatingActionButton.setOnClickListener(v -> {
                Amplify.Predictions.convertTextToSpeech(
                        textToSpeech,
                        success -> playTextToSpeechAudio(success.getAudioData()),
                        failure -> Log.e(TAG, "Failed to convert text to speech")
                );
        });
    }

    private void playTextToSpeechAudio(InputStream inputStream) {
        File audioMP3File = new File(getCacheDir(), "tts.mp3");

        try (OutputStream out = new FileOutputStream(audioMP3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mediaPlayer.reset();
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setDataSource(new FileInputStream(audioMP3File).getFD());
            mediaPlayer.prepareAsync();
        } catch (IOException ioe) {
            Log.e(TAG, "Error writing text-to-speech file", ioe);
        }
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