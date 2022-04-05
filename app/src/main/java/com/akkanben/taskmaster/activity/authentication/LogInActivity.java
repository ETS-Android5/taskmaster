package com.akkanben.taskmaster.activity.authentication;

import static com.akkanben.taskmaster.utility.AnimationUtility.setupAnimatedBackground;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.akkanben.taskmaster.R;
import com.akkanben.taskmaster.activity.MainActivity;
import com.amplifyframework.core.Amplify;
import com.google.android.material.snackbar.Snackbar;

public class LogInActivity extends AppCompatActivity {
    public static final String VERIFY_EMAIL_EXTRA_TAG = "verify_email_tag";
    public static final String TAG = "log_in_activity_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ConstraintLayout constraintLayout = findViewById(R.id.log_in_activity_layout);
        setupAnimatedBackground(constraintLayout);
        setupSignUpButton();
        setupLogInButton();
    }

    private void setupSignUpButton() {
        Button signUpButton = findViewById(R.id.button_log_in_activity_sign_up);
        signUpButton.setOnClickListener(view -> {
            Intent goToSignUpActivityIntent = new Intent(LogInActivity.this, SignUpActivity.class);
            startActivity(goToSignUpActivityIntent);
        });
    }

    private void setupLogInButton() {
        Button logInButton = findViewById(R.id.button_log_in_activity_submit);
        logInButton.setOnClickListener(view -> {
            EditText email = findViewById(R.id.edit_text_log_in_activity_email);
            EditText password = findViewById(R.id.edit_text_log_in_activity_password);
            Amplify.Auth.signIn(
                    email.getText().toString(),
                    password.getText().toString(),
                    success -> {
                       Log.i(TAG, "Login Succeeded: " + success.toString());
                       Intent goToMainActivityIntent = new Intent(LogInActivity.this, MainActivity.class);
                       startActivity(goToMainActivityIntent);
                    },
                    failure -> {
                        Log.i(TAG, "Login Failed: " + failure.toString());
                        runOnUiThread(() -> Toast.makeText(LogInActivity.this, "Log In Error", Toast.LENGTH_SHORT).show());
                    }
            );
        });
    }
}
