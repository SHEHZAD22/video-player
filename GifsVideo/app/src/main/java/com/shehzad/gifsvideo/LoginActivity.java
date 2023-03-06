package com.shehzad.gifsvideo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shehzad.gifsvideo.preferences.ManagePreferences;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout textEmail;
    private TextInputLayout textPassword;
    private Button loginButton;
    private TextView signup;
    private ManagePreferences preferences;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    ProgressDialog progressDialog;

    private final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

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

        textEmail = findViewById(R.id.textEmailInput);
        textPassword = findViewById(R.id.pwd);
        loginButton = findViewById(R.id.sign_in_button);
        signup = findViewById(R.id.sign_up_button);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Registration");
        progressDialog.setCanceledOnTouchOutside(false);


        loginButton.setOnClickListener(view -> LoginActivity.this.onLoginClicked());

        textEmail.getEditText().addTextChangedListener(createTextWatcher(textEmail));
        textPassword.getEditText().addTextChangedListener(createTextWatcher(textPassword));

        signup.setOnClickListener(view -> {
            Intent intent = new Intent(this,RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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

    //login Validation and error messages
    private void onLoginClicked() {
        String email = textEmail.getEditText().getText().toString();
        String password = textPassword.getEditText().getText().toString();

        if (email.isEmpty()) textEmail.setError("Email must not be empty");
        else if (!email.matches(emailPattern)) textEmail.setError("Enter Correct Email");
        else if (password.isEmpty()) textPassword.setError("Password must not be empty");
        else {
            preferences.setLoggedIn(true);
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        startMainactivity();
                        finish();
                        Toast.makeText(LoginActivity.this, "Login Succesfully", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void startMainactivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}