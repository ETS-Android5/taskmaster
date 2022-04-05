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
import com.amplifyframework.core.Amplify;

public class VerifyActivity extends AppCompatActivity {

    public static final String TAG = "verify_activity_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        ConstraintLayout constraintLayout = findViewById(R.id.verify_activity_layout);
        setupAnimatedBackground(constraintLayout);
        Intent callingIntent = getIntent();
        String email = callingIntent.getStringExtra(SignUpActivity.SIGN_UP_EMAIL_TAG);
        Button submitButton = findViewById(R.id.button_verify_activity_submit);
        submitButton.setOnClickListener(view -> {
           String verificationCode = ((EditText)findViewById(R.id.edit_text_verify_activity_verfication_number)).getText().toString();
           Amplify.Auth.confirmSignUp(
                   email,
                   verificationCode,
                   success -> {
                       Log.i(TAG, "Verification Succeeded: " + success.toString());
                       Intent goToLogInIntent = new Intent(VerifyActivity.this, LogInActivity.class);
                       startActivity(goToLogInIntent);
                   },
                   failure -> {
                       Log.i(TAG, "Vefication Failed: " + failure.toString());
                       runOnUiThread(() -> Toast.makeText(VerifyActivity.this, "Verification Failed", Toast.LENGTH_SHORT));
                   }
           );
        });


    }
}