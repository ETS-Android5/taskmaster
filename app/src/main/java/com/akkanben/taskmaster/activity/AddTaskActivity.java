package com.akkanben.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.akkanben.taskmaster.R;

public class AddTaskActivity extends AppCompatActivity {

    private final int FADE_IN_SPEED = 500;
    private final int FADE_OUT_SPEED = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button addTaskButton = findViewById(R.id.button_add_task_add_task);

        addTaskButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TextView countText = findViewById(R.id.text_total_tasks_counter);
                Integer count = Integer.parseInt(String.valueOf(countText.getText()));
                count++;
                countText.setText(count.toString());
                TextView submittedText = findViewById(R.id.add_task_submitted_text);
                submittedText.setAlpha(0f);
                submittedText.setVisibility(View.VISIBLE);
                submittedText.animate().alpha(1f).setDuration(FADE_IN_SPEED).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        submittedText.animate().alpha(0f).setDuration(FADE_OUT_SPEED).setListener(null);
                    }
                });
            }
        });
    }
}
