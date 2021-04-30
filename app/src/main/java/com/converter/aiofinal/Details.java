package com.converter.aiofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Details extends AppCompatActivity {
    CircleImageView detail_img;
    TextView det_name, det_phn,det_adres,det_srv,det_email;
    String ReciverImage, ReciverUID, ReciverName, ReciverPhone,ReciverAdress,ReciverService,ReciverEmail ;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        detail_img = findViewById(R.id.detail_img);
        det_name = findViewById(R.id.det_name);
        det_phn = findViewById(R.id.det_phn);
        det_adres = findViewById(R.id.det_adres);
        det_srv = findViewById(R.id.det_srv);
//        det_email = findViewById(R.id.det_email);

        progressDialog=new ProgressDialog(this );
        progressDialog.setMessage("Loading...Please wait");
        progressDialog.setCancelable(false);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        ReciverName=getIntent().getStringExtra("name");
        ReciverImage=getIntent().getStringExtra("ReciverImage");
        ReciverPhone=getIntent().getStringExtra("phone");
        ReciverAdress=getIntent().getStringExtra("adress");
        ReciverService=getIntent().getStringExtra("service");
//        ReciverEmail=getIntent().getStringExtra("emailId");

        Picasso.get().load(ReciverImage).into(detail_img);
        det_name.setText(""+ReciverName);
        det_phn.setText(""+ReciverPhone);
        det_adres.setText(""+ReciverAdress);
        det_srv.setText(""+ReciverService);
//        det_email.setText(""+ReciverEmail);


////
//        det_phn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Details.this, CallActivity.class);
//            }
//        });
//

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Details");

        det_phn= findViewById(R.id.det_phn);
        registerForContextMenu(det_phn);

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.see_profile:
                progressDialog.dismiss();
                Toast.makeText(Details.this, "Working on this", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.chat_him:
                progressDialog.dismiss();
                Intent intent = new Intent(Details.this, SmsActivity.class);
                startActivity(intent);
                Toast.makeText(Details.this, "Send Him Message ", Toast.LENGTH_LONG).show();
            case R.id.call:
                progressDialog.dismiss();
                Intent i = new Intent(Details.this, CallActivity.class);
                i.putExtra("phone",ReciverPhone );
                startActivity(i);

                Toast.makeText(Details.this, "Sim Credit May Apply ", Toast.LENGTH_LONG).show();
                return true;
            case R.id.location:
                progressDialog.dismiss();
                Intent myIntent = new Intent(Details.this,
                        LocationMap.class);
                startActivity(myIntent);
            default: return super.onContextItemSelected(item);
        }

    }
}