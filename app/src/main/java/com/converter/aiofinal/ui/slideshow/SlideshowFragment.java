package com.converter.aiofinal.ui.slideshow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.converter.aiofinal.R;
import com.converter.aiofinal.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SlideshowFragment extends Fragment {

    public SlideshowFragment() {
    }

    CircleImageView profile_image;
    EditText profile_name, profile_email,profile_phn,profile_add,profile_serv;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selctedImageUri;
    String emailId;
    ProgressDialog progressDialog;
    TextView profile_save;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_slideshow, container, false);

        profile_image = view.findViewById(R.id.profile_image);
        profile_name =view. findViewById(R.id.profile_name);
        profile_email = view.findViewById(R.id.profile_email);
        profile_phn = view.findViewById(R.id.profile_phn);
        profile_add =view. findViewById(R.id.profile_add);
        profile_serv = view.findViewById(R.id.profile_serv);

        profile_save = view.findViewById(R.id.profile_save);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...Please wait");
        progressDialog.setCancelable(false);

        DatabaseReference reference=database.getReference().child("Users").child(auth.getUid());
        StorageReference storageReference=storage.getReference().child("Upload").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                emailId=snapshot.child("emailId").getValue().toString();//email is public now
                String name = snapshot.child("name").getValue().toString();
                String phn=snapshot.child("phone").getValue().toString();
                String addrs=snapshot.child("address").getValue().toString();
                String serv=snapshot.child("service").getValue().toString();
                String imageURI=snapshot.child("imageUri").getValue().toString();

//Setting Data After Retrieving
                profile_email.setText(emailId);
                profile_name.setText(name);
                profile_phn.setText(phn);
                profile_add.setText(addrs);
                profile_serv.setText(serv);
                Picasso.get().load(imageURI).into(profile_image);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profile_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailID = profile_email.getText().toString();
                String name = profile_name.getText().toString();
                String phn = profile_phn.getText().toString();
                String addrs = profile_add.getText().toString();
                String serv = profile_serv.getText().toString();

                if (selctedImageUri != null) {
                    storageReference.putFile(selctedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String finalImageUri = uri.toString();
                                    Users users=new Users(auth.getUid(), emailID, name, phn,addrs,serv, finalImageUri);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Toast.makeText( getActivity(), "Data Successfully Updated", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    });
                                }
                            });
                        }
                    });
                }
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String finalImageUri = uri.toString();
                        Users users=new Users(auth.getUid(), emailID, name, phn,addrs,serv, finalImageUri);
                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Data Successfully Updated", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                    }
                });
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10)
        {
            if(data!=null)
            {

                selctedImageUri =data.getData();
                profile_image.setImageURI(selctedImageUri);
                progressDialog.dismiss();
            }
        }
    }
}