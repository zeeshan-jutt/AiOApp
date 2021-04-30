package com.converter.aiofinal.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.converter.aiofinal.CustomerHome;
import com.converter.aiofinal.R;
import com.converter.aiofinal.ViewPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_list_item_1;

public class HomeFragment extends Fragment {

    ViewPager viewPager;
    ImageView imgDone;
    Spinner spinner;
    String[] SPINNERVALUES = {"SELECT ITEM ","SEE ALL","RIDES","MEDICAL","MECHANICS","WOOD WORK","ADMISSIONS"};
    String SpinnerValue;
    FirebaseDatabase database;
    DatabaseReference reference;
    spinnerModel spinnerModel;
    int maxId = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = v. findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity());
        viewPager.setAdapter(viewPagerAdapter);
        imgDone= v.findViewById(R.id.confirm);

        spinner =v.findViewById(R.id.spinner1);

        spinnerModel = new spinnerModel();
        reference = database.getInstance().getReference().child("Spinner");

        List<String> Categories = new ArrayList<>();
        Categories.add(0,"Select Category");
//        Categories.add("Select Category");
        Categories.add("SEE ALL");
        Categories.add("HOME FIXING");
        Categories.add("MACHENICS");
        Categories.add("WOOD WORK");
        Categories.add("TECHNOLOGY");
        Categories.add("RIDES");
        Categories.add("ADMISSIONS");
        Categories.add("COURSES");
        Categories.add("EARNING");
        ArrayAdapter<String> DataAdapter;
        DataAdapter = new ArrayAdapter(getActivity(), android.R.layout. simple_list_item_1, SPINNERVALUES);
        spinner.setAdapter(DataAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i).equals("Select Service")){
                    SpinnerValue = (String)spinner.getSelectedItem();
                }
            else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxId = (int) snapshot.getChildrenCount();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        imgDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerModel.setSpinner((spinner.getSelectedItem().toString()));
                Toast.makeText(getActivity(),"Confirmed Successfully", Toast.LENGTH_LONG).show();

                reference.child(String.valueOf(maxId+1)).setValue(spinnerModel);
            }
        });



        return v;
    }
}