package com.example.user.tourmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private Button signUp;
    private EditText emailEt,passwordEt, confirmPasswordEt;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        emailEt = findViewById(R.id.emailInput);
        passwordEt = findViewById(R.id.passwordInput);
        confirmPasswordEt = findViewById(R.id.confirmPasswordInput);
        signUp = findViewById(R.id.signUpBtn);


        mAuth = FirebaseAuth.getInstance();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();
                String confirmPassword = confirmPasswordEt.getText().toString();

                if(email.length()>0 && password.length()>0) {
                    if (password.equals(confirmPassword) ) {
                        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                        progressDialog.setMessage("Please wait...");
                        progressDialog.show();
                        progressDialog.setCanceledOnTouchOutside(false);
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();

                                            Log.d("create", "signInWithEmail:success");
                                            Toast.makeText(SignUpActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                            startActivity(intent);

                                        }
                                        else {

                                            Log.w("create", "signInWithEmail:failure", task.getException());
                                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                                    Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });

                    }
                    else {
                        Toast.makeText(SignUpActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                    }

                }
                else {

                        Toast.makeText(SignUpActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
}
