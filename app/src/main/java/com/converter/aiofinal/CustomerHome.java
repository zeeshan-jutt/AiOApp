package com.converter.aiofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.converter.aiofinal.ui.home.spinnerModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;
import java.util.List;

public class CustomerHome extends AppCompatActivity {
    TextView register, btnNext;
    FloatingActionButton floatingActionButton;
    ProgressDialog progressDialog;
    spinnerModel spinnerModel;
    Spinner spinner;
    String[] SPINNERVALUES = {"SELECT ITEM ","SEE ALL","Home Fixing","ELECTRICIANS","DOCTORS"};
    String SpinnerValue;
    Intent intent;
    DatabaseReference reference;
    ValueEventListener Listener;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDataList;

    String url1 = "data:image/jpeg;base64,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";
    String url2 = "https://www.dumpsters.com/images/blog/carpentry_1530018525/carpentry-1200x600.jpg";
    String url3 = "data:image/jpeg;base64,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";
    String url4 = "https://i.pinimg.com/originals/3f/af/92/1Furfhe8BFbkEhXn1xcYPr8jYAACNpfV7p.jpg";
    String url5 = "https://thejandpgroup.co.uk/wp-content/uploads/2020/07/fix-sink-2020-scaled.jpg";
    String url6 = "https://miro.medium.com/max/4320/1*JktzC9GrA_l4yz0cCy8a5Q.jpeg";
    String url7 = "https://www.doctor2u.my/wp-content/uploads/2018/01/D2U_FBCanvas_Live_Chat-1.jpg";
//    String url8 = "";
//    String url9 = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...Please wait");
        progressDialog.setCancelable(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.hometoolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Home");
        btnNext = findViewById(R.id.btnNext);
        spinner =findViewById(R.id.spinner);
        reference = FirebaseDatabase.getInstance().getReference("Spinner");

        spinnerDataList = new ArrayList<>();
      adapter = new ArrayAdapter<String>(CustomerHome.this, android.R.layout.simple_dropdown_item_1line,SPINNERVALUES);
      spinner.setAdapter(adapter);
//        retrieveData();


//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CustomerHome.this, android.R.layout.simple_list_item_1, SPINNERVALUES);
//        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SpinnerValue = (String)spinner.getSelectedItem();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                switch(SpinnerValue){
                    case "Select Category ":
                        Toast.makeText(CustomerHome.this,"please Select Category", Toast.LENGTH_SHORT).show();
                        break;

                    case "SEE ALL":
                        intent = new Intent(CustomerHome.this, AllMembers.class);
                        startActivity(intent);
                        progressDialog.dismiss();
                        break;

//                    case "ELECTRICIANS":
//                        Toast.makeText(CustomerHome.this,"please wait", Toast.LENGTH_SHORT).show();
////                        intent = new Intent(MainActivity.this, PHP_Activity.class);
////                        startActivity(intent);
//                        break;
//
//                    case "DOCTORS":
//                        Toast.makeText(CustomerHome.this,"Working on it", Toast.LENGTH_SHORT).show();
////                        intent = new Intent(MainActivity.this, BLOGGER_Activity.class);
////                        startActivity(intent);
//                        break;


                }
            }
        });

        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.dismiss();
                startActivity(new Intent(CustomerHome.this, RegisterActivity.class));
            }
        });

        floatingActionButton = findViewById(R.id.fab1);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.dismiss();
                Intent intent = new Intent(CustomerHome.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        // we are creating array list for storing our image urls.
        ArrayList<SliderItem> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        SliderView sliderView = findViewById(R.id.imageSlider);

        // adding the urls inside array list
        sliderDataArrayList.add(new SliderItem(url1));
        sliderDataArrayList.add(new SliderItem(url2));
        sliderDataArrayList.add(new SliderItem(url3));
        sliderDataArrayList.add(new SliderItem(url4));
        sliderDataArrayList.add(new SliderItem(url5));
        sliderDataArrayList.add(new SliderItem(url6));
        sliderDataArrayList.add(new SliderItem(url7));

        // passing this array list inside our adapter class.
        SliderAdapter sliderAdapter= new SliderAdapter(this, sliderDataArrayList);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(sliderAdapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();
    }

//    private void retrieveData(){
//        Listener = reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                List<DataSnapshot> list = (List<DataSnapshot>) snapshot.getChildren();
//                for(DataSnapshot item: snapshot.getChildren()){
////                    spinnerModel = item.toString();
//                    String val = item.getValue().toString();
//                    val = val.replaceAll("[\\\\p{Ps}\\\\p{Pe}]", "");
//                    String[] parts = val.split("=");
//                    spinnerDataList.add(parts[1]);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cust_menu, menu);
//        MenuItem menuItem = menu.findItem(R.menu.cust_menu);
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setQueryHint("Search here");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                progressDialog.dismiss();
                Toast.makeText(CustomerHome.this, "Searching...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.Login:
                progressDialog.dismiss();
                Intent i = new Intent(CustomerHome.this, LoginActivity.class);
                startActivity(i);
                return true;
            case R.id.More:
                progressDialog.dismiss();
                Toast.makeText(CustomerHome.this, "Loading More...", Toast.LENGTH_SHORT).show();
                return true;
            default:   return super.onOptionsItemSelected(item);
        }
    }
}
