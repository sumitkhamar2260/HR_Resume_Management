package com.example.hrresumemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ShowResume extends AppCompatActivity {
    Intent intent;
    String name,uid,mobile,email,city,state,degree,fieldOfStudy;
    ArrayList<String> c_name,c_location,j_title,s_date,e_date;
    DatabaseReference databaseReference;
    TextView name_t,email_t,mobile_t,city_state,skill,education;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_resume);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intent = getIntent();
        uid = intent.getStringExtra("uid");
        name = intent.getStringExtra("name");
        c_name = new ArrayList<>();
        c_location = new ArrayList<>();
        j_title = new ArrayList<>();
        s_date = new ArrayList<>();
        e_date = new ArrayList<>();
        name_t = findViewById(R.id.name);
        email_t = findViewById(R.id.email);
        mobile_t = findViewById(R.id.mobile);
        city_state = findViewById(R.id.city_state);
        skill = findViewById(R.id.skill);
        education = findViewById(R.id.education);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        retrievePersonalDetails();
    }
    public void retrievePersonalDetails(){
        databaseReference.child("users").child(uid).child("Personal details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot details : dataSnapshot.getChildren()){
                    Map<String,String> map = (Map)details.getValue();
                    mobile = map.get("Mobile No");
                    email = map.get("Email");
                    city = map.get("City");
                    state = map.get("State");
                    name_t.setText(name);
                    email_t.setText(email);
                    city_state.setText(city+","+state);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void retrieveEducationalDetails(){
        databaseReference.child("users").child(uid).child("Education").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*for(DataSnapshot details : dataSnapshot.getChildren()){
                    for(DataSnapshot details1 : da)
                    Map<String,String> map = (Map)details.getValue();
                    degree = map.get("Degree");
                    fieldOfStudy = map.get("Field Of Study");
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void retrieveExperienceDetails(){
        databaseReference.child("users").child(uid).child("Experience").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot details : dataSnapshot.getChildren()){
                    Map<String,String> map = (Map)details.getValue();
                    c_name.add(map.get("Company Name"));
                    c_location.add(map.get("Company Location"));
                    j_title.add(map.get("Job Title"));
                    s_date.add(map.get("Start Date"));
                    e_date.add(map.get("End Date"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
