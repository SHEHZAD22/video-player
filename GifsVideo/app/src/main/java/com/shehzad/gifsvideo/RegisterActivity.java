package com.shehzad.gifsvideo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.shehzad.gifsvideo.preferences.ManagePreferences;


public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout textUsername,textPassword,textEmail;
    private Button register;
    private TextView login;

    final String PREF = "loginpreference";

    SharedPreferences sharedPreferences;
    private ManagePreferences managePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        managePreferences = new ManagePreferences(this);
        if (managePreferences.isLoggedIn()) {
            startLoginActivity();
            finish();
            return;
        }

        setContentView(R.layout.register_page);

        textUsername = findViewById(R.id.textUsernameInput);
        textPassword = findViewById(R.id.textPasswordInput);
        textEmail = findViewById(R.id.textEmailInput);
        register = findViewById(R.id.signupButton);
        login = findViewById(R.id.signinButton);

        register.setOnClickListener(view -> setRegistration());

        textUsername.getEditText().addTextChangedListener(createTextWatcher(textUsername));
        textPassword.getEditText().addTextChangedListener(createTextWatcher(textPassword));
        textEmail.getEditText().addTextChangedListener(createTextWatcher(textEmail));

        login.setOnClickListener(view -> {
            startLoginActivity();
            finish();
        });

    }

//    private void setRegistration() {
//        sharedPreferences = getSharedPreferences(PREF, MODE_PRIVATE);
//        String username = textUsername.getText().toString();
//        String password = textPassword.getText().toString();
//        String email = textEmail.getText().toString();
//
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("username", username);
//        editor.putString("password", password);
//        editor.putString("email", email);
//        editor.commit();
//
//        startLoginActivity();
//        finish();
//    }

    //Validation and error messages
    private void setRegistration() {
        sharedPreferences = getSharedPreferences(PREF,MODE_PRIVATE);
        String username = textUsername.getEditText().getText().toString();
        String password = textPassword.getEditText().getText().toString();
        String email = textEmail.getEditText().getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("email", email);
        editor.commit();

        if (username.isEmpty()) textUsername.setError("Username must not be empty");
        else if (password.isEmpty()) textPassword.setError("Password must not be empty");
        else if (email.isEmpty()) textEmail.setError("Email must not be empty");
        else performRegistration();
    }

    private void performRegistration() {
        textPassword.setEnabled(false);
        textUsername.setEnabled(false);
        register.setVisibility(View.INVISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            startLoginActivity();
            finish();
        }, 2000);

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

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
