package com.shehzad.gifsvideo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shehzad.gifsvideo.preferences.ManagePreferences;


public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout textConfirmPassword, textPassword, textEmail;
    private Button register;
    private TextView login;
    private ManagePreferences managePreferences;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

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

        textConfirmPassword = findViewById(R.id.textConfirmPasswordInput);
        textPassword = findViewById(R.id.textPasswordInput);
        textEmail = findViewById(R.id.textEmailInput);
        register = findViewById(R.id.signupButton);
        login = findViewById(R.id.signinButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Registration");
        progressDialog.setCanceledOnTouchOutside(false);

        register.setOnClickListener(view -> setRegistration1());

        textConfirmPassword.getEditText().addTextChangedListener(createTextWatcher(textConfirmPassword));
        textPassword.getEditText().addTextChangedListener(createTextWatcher(textPassword));
        textEmail.getEditText().addTextChangedListener(createTextWatcher(textEmail));

        login.setOnClickListener(view -> {
            Intent intent = new Intent(this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

    }

    //signup validation and error messages
    private void setRegistration1() {
        String confirmPassword = textConfirmPassword.getEditText().getText().toString();
        String password = textPassword.getEditText().getText().toString();
        String email = textEmail.getEditText().getText().toString();

        if (email.isEmpty()) textEmail.setError("Email must not be empty");
        else if (!email.matches(emailPattern)) textEmail.setError("Enter Correct Email");
        else if (password.isEmpty()) textPassword.setError("Password must not be empty");
        else if (password.length() < 6) textPassword.setError("Enter atleast 8 characters");
        else if (!password.equals(confirmPassword))
            textConfirmPassword.setError("Password not matched");
        else {
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        startLoginActivity();
                        finish();
                        Toast.makeText(RegisterActivity.this, "Registered Succesfully", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
