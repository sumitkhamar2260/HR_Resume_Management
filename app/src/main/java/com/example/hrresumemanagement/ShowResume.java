package com.example.hrresumemanagement;

import android.app.ProgressDialog;
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
    String name,uid,mobile,email,city,state;
    ArrayList<String> c_name,c_location,j_title,s_date,e_date,degree,fieldOfStudy,skills;
    DatabaseReference databaseReference;
    TextView name_t,email_t,mobile_t,city_state,skill,education,experience;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_resume);
       /* ProgressBar progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.VISIBLE);
       */
        progressDialog = new ProgressDialog(ShowResume.this);
        progressDialog.setMessage("Loading");
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
        skills = new ArrayList<>();
        education = findViewById(R.id.education);
        experience = findViewById(R.id.experience);
        degree = new ArrayList<>();
        fieldOfStudy = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog.show();
        retrievePersonalDetails();
        retrieveEducationalDetails();
        retrieveExperienceDetails();
        retrieveSkillDetails();
        progressDialog.dismiss();
       // progressBar.setVisibility(View.GONE);
    }
    public void retrievePersonalDetails(){
        databaseReference.child("users").child(uid).child("Personal details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,String> map = (Map)dataSnapshot.getValue();
                mobile = map.get("Mobile No");
                email = map.get("Email");
                city = map.get("City");
                state = map.get("State");
                name_t.setText(name);
                email_t.setText(email);
                mobile_t.setText(mobile);
                city_state.setText(city+","+state);
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
                for(DataSnapshot details : dataSnapshot.getChildren()) {
                    //for (DataSnapshot details1 : details.getChildren()) {
                        Map<String, String> map = (Map) details.getValue();
                        degree.add( map.get("Degree"));
                        fieldOfStudy.add( map.get("Field Of Study"));

                    //}
                }
                for (int i = 0;i<degree.size();i++){
                    education.append(degree.get(i)+" in " +fieldOfStudy.get(i)+"\n");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void retrieveExperienceDetails(){
        databaseReference.child("users").child(uid).child("Experience").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot details : dataSnapshot.getChildren()){
                    Map<String,String> map = (Map)details.getValue();
                    c_name.add(map.get("Company Name"));
                    c_location.add(map.get("Company Location"));
                    j_title.add(map.get("Job Title"));
                    s_date.add(map.get("Start Date"));
                    e_date.add(map.get("End Date"));
                    System.out.println(c_name);
                }
                for(int i =0;i<c_name.size();i++){
                    experience.append(j_title.get(i)+"\n"+c_name.get(i)+"\n"+s_date.get(i)+" - "+e_date.get(i)+"\n");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void retrieveSkillDetails(){
        databaseReference.child("users").child(uid).child("Skills").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,String> map = (Map)dataSnapshot.getValue();
                for(int i = 1;i<dataSnapshot.getChildrenCount();i++){
                    skill.append(map.get("Skill "+i)+"\n");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
