package com.converter.aiofinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    TextView username,email, phone, address, service, password, repassword;
    Button btnsignin, btnregister;
    CircleImageView profile_img;
    Uri imageUri;
    String imageURI;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...Please wait");
        progressDialog.setCancelable(false);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        profile_img = findViewById(R.id.profile_img);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        service = findViewById(R.id.service);
        password = findViewById(R.id.passsignup);
        repassword = findViewById(R.id.repassword);
        btnsignin = findViewById(R.id.btnsignin);
        btnregister = findViewById(R.id.btnregister);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = username.getText().toString();
                String emailID = email.getText().toString();
                String phn = phone.getText().toString();
                String addrs = address.getText().toString();
                String serv = service.getText().toString();
                String pass = password.getText().toString();
                String confpass = repassword.getText().toString();

                if (name.isEmpty()) {
                    username.setError("Username is empty");
                    username.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                if (emailID.isEmpty()) {
                    username.setError("Email is empty");
                    username.requestFocus();
                    progressDialog.dismiss();
                    return;

                }
                if (!Patterns.EMAIL_ADDRESS.matcher(emailID).matches()) {
                    email.setError("Enter the valid email address");
                    email.requestFocus();
                    progressDialog.dismiss();
                    return;
                }

                if (phn.isEmpty()) {
                    phone.setError("Phone is empty");
                    phone.requestFocus();
                    progressDialog.dismiss();
                    return;
                }

                if (addrs.isEmpty()) {
                    address.setError("Address is empty");
                    address.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                if (serv.isEmpty()) {
                    service.setError("Service is empty");
                    service.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                if (pass.isEmpty()) {
                    password.setError("Enter the password");
                    password.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                if (pass.length() < 6) {
                    password.setError("Length of the password should be more than 6");
                    password.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                if (confpass.isEmpty()) {
                    repassword.setError("Re-Enter same Password");
                    repassword.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                if (!confpass.equals(pass)) {
                    repassword.setError("Password not matched ");
                    repassword.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                auth.createUserWithEmailAndPassword(emailID, pass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            DatabaseReference reference = database.getReference().child("Users").child(auth.getUid());
                            StorageReference reference1 = storage.getReference().child("Upload").child(auth.getUid());

                            if(imageUri != null){
                                reference1.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if(task.isComplete()){
                                            reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    imageURI= uri.toString();
                                                    Users users=new Users(auth.getUid(), name,emailID, phn,addrs,serv, imageURI);
                                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                progressDialog.dismiss();
                                                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                            } else{
                                                                Toast.makeText(RegisterActivity.this, "You are not Registered! Try again", Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                            }else{
                                String status = "Hay There I am Using All in One ChatApp";
                                imageURI= "https://firebasestorage.googleapis.com/v0/b/aiofinal.appspot.com/o/profile_img.png?alt=media&token=7e78a51b-818b-48b8-b872-bc94b320eb0c";
                                Users users=new Users(auth.getUid(), name, emailID, phn,addrs,serv, imageURI);
                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            progressDialog.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Registered Successfully! ", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                                        } else{
                                            Toast.makeText(RegisterActivity.this, "You are not Registered! Try again", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                            }


                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "You are not Registered! Try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);

            }
        });

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.dismiss();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                switch (view.getId()){
                    case R.id.btnsignin:
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if (data!=null){
                progressDialog.dismiss();
                imageUri=data.getData();
                profile_img.setImageURI(imageUri);
            }
        }
    }
}
