package com.converter.aiofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText userlogin, passlogin;
    Button btnlogin;
    TextView account, forgetpass;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...Please wait");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        userlogin = findViewById(R.id.userlogin);
        passlogin = findViewById(R.id.passlogin);
        btnlogin = findViewById(R.id.btnlogin);
        forgetpass = findViewById(R.id.forgetpass);
        account = findViewById(R.id.account);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailId = userlogin.getText().toString();
                String pass = passlogin.getText().toString();

                if (emailId.isEmpty()) {
                    userlogin.setError("Email is empty");
                    userlogin.requestFocus();
                    progressDialog.dismiss();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
                    userlogin.setError("Enter the valid email address");
                    userlogin.requestFocus();
                    progressDialog.dismiss();
                    return;
                } else if (pass.isEmpty()) {
                    passlogin.setError("Enter the password");
                    passlogin.requestFocus();
                    progressDialog.dismiss();
                    return;
                } else if (pass.length() < 6) {
                    passlogin.setError("Length of the password should be more than 6");
                    passlogin.requestFocus();
                    progressDialog.dismiss();
                    return;
                } else {
                    auth.signInWithEmailAndPassword(emailId, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this,NavigationMain.class);
                                LoginActivity.this.startActivity(intent);
                                Toast.makeText(LoginActivity.this, "Login Successfull ", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Login Error, try Again! ", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                switch (view.getId()) {
                    case R.id.account:
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        break;
                }
            }
        });
    }
}