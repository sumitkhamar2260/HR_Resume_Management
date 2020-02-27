package com.example.hrresumemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class JobOpenings extends AppCompatActivity {
    ArrayList<String> jobtitle,location,applications,jobID;
    RecyclerView rv;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase fd;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_openings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton new_recruitment = findViewById(R.id.new_recruitment);
        new_recruitment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
                startActivity(new Intent(JobOpenings.this,job_details.class));
            }
        });
        rv=findViewById(R.id.rv);
        jobtitle=new ArrayList<>();
        location=new ArrayList<>();
        applications=new ArrayList<>();
        jobID = new ArrayList<>();
        firebaseAuth=FirebaseAuth.getInstance();
        fd=FirebaseDatabase.getInstance();
        ref=fd.getReference("Companies").child(firebaseAuth.getCurrentUser().getUid()).child("Job Openings");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jobtitle.clear();
                location.clear();
                applications.clear();
                for (DataSnapshot jobdetails:dataSnapshot.getChildren()){
                    Map<String,String> ob=(Map) jobdetails.getValue();
                    jobtitle.add( String.valueOf(ob.get("JobTitle")));
                    System.out.println("Job title is "+ob.get("JobTitle"));
                    location.add( String.valueOf(ob.get("Location")));
                    applications.add(String.valueOf(jobdetails.child("Applicants").getChildrenCount()));
                    jobID.add(String.valueOf(ob.get("JobId")));
                }
                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                rv.setLayoutManager(layoutManager);
                jobdetailrow ob=new jobdetailrow(getApplicationContext(),jobtitle,location,applications,jobID);
                rv.setAdapter(ob);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError);

            }
        });
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);
        jobdetailrow ob=new jobdetailrow(getApplicationContext(),jobtitle,location,applications,jobID);
        rv.setAdapter(ob);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
}
