package com.akkanben.taskmaster.activity.authentication;

import static com.akkanben.taskmaster.utility.AnimationUtility.setupAnimatedBackground;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.akkanben.taskmaster.R;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class SignUpActivity extends AppCompatActivity {
    public static final String SIGN_UP_EMAIL_TAG = "sign_up_email_tag";
    public static final String TAG = "sign_up_activity_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ConstraintLayout constraintLayout = findViewById(R.id.sign_up_activity_layout);
        setupAnimatedBackground(constraintLayout);
        Button submitButton = findViewById(R.id.button_sign_up_activity_submit);
        submitButton.setOnClickListener(view -> {
            String emailEditText = ((EditText)findViewById(R.id.edit_text_sign_up_activity_email)).getText().toString();
            String passwordEditText = ((EditText)findViewById(R.id.edit_text_sign_up_activity_password)).getText().toString();
            String confirmPasswordEditText = ((EditText)findViewById(R.id.edit_text_sign_up_activity_confirm_password)).getText().toString();
            String nicknameEditText = ((EditText)findViewById(R.id.edit_text_sign_up_activitiy_nickname)).getText().toString();
            Amplify.Auth.signUp(
                    emailEditText,
                    passwordEditText,
                    AuthSignUpOptions.builder()
                    .userAttribute(AuthUserAttributeKey.email(), emailEditText)
                    .userAttribute(AuthUserAttributeKey.nickname(), nicknameEditText)
                    .build(),
                    success -> {
                        Log.i(TAG, "Signup success: " + success.toString());
                        Intent goToVerifyIntent = new Intent(SignUpActivity.this, VerifyActivity.class);
                        goToVerifyIntent.putExtra(SIGN_UP_EMAIL_TAG, emailEditText);
                        startActivity(goToVerifyIntent);
                    },
                    failure -> {
                        Log.i(TAG, "Signup failure: " + failure.toString());
                    }
            );
        });
    }
}