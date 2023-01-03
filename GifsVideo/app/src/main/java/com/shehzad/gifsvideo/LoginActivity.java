package com.shehzad.gifsvideo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.shehzad.gifsvideo.preferences.ManagePreferences;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout textUsername;
    private TextInputLayout textPassword;
    private Button loginButton;
    private TextView register;
    private ProgressBar progressBar;
    private ManagePreferences preferences;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new ManagePreferences(this);
        if (preferences.isLoggedIn()) {
            startMainactivity();
            finish();
            return;
        }
        setContentView(R.layout.activity_login);

        textUsername = findViewById(R.id.user);
        textPassword = findViewById(R.id.pwd);
        loginButton = findViewById(R.id.sign_in_button);
        progressBar = findViewById(R.id.progressBar);
        register = findViewById(R.id.sign_up_button);

        loginButton.setOnClickListener(view -> LoginActivity.this.onLoginClicked());

        textUsername.getEditText().addTextChangedListener(createTextWatcher(textUsername));
        textPassword.getEditText().addTextChangedListener(createTextWatcher(textPassword));

        register.setOnClickListener(view -> {
                Intent registerIntent = new Intent(this,RegisterActivity.class);
                startActivity(registerIntent);
                finish();
        });

    }

    //Error message cleanup
    private TextWatcher createTextWatcher(TextInputLayout textPassword) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                textPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    //Validation and error messages
    private void onLoginClicked() {
        String username = textUsername.getEditText().getText().toString();
        String password = textPassword.getEditText().getText().toString();

        sharedPreferences = getSharedPreferences("loginpreference",MODE_PRIVATE);
        String prefName = sharedPreferences.getString("username",username);
        String prefPwd = sharedPreferences.getString("password",password);
        Log.d("username", "onLoginClicked: " + prefName);
        Log.d("password", "onLoginClicked: " + prefPwd);

        if (username.isEmpty()) textUsername.setError("Username must not be empty");
        else if (password.isEmpty()) textPassword.setError("Password must not be empty");
        else if ((!password.equals(prefPwd)) && (!username.equals(prefName))) showErrorDialog();
        else performLogin();
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Login Failed")
                .setMessage(R.string.loginFailedMessage)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }


    private void performLogin() {
        preferences.setLoggedIn(true);

        textPassword.setEnabled(false);
        textUsername.setEnabled(false);
        loginButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            startMainactivity();
            finish();
        }, 2000);

    }

    private void startMainactivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}