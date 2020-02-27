package com.example.hrresumemanagement;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class RecievedApplications extends AppCompatActivity {
    Intent intent;
    String JobId;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    Map<String,String> fetch;
    ArrayList<String> ids,name,experiance,skill,testScore;
    ArrayList<String> resumeScore;
    HashMap<Double,String> for_sort;
    RecyclerView sorted_resumes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieved_applications);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Received Applications");
        intent = getIntent();
        JobId = intent.getStringExtra("Job ID");
        fetch = new HashMap<>();
        for_sort = new HashMap<Double, String>();
        ids = new ArrayList<>();
        name = new ArrayList<>();
        experiance = new ArrayList<>();
        testScore = new ArrayList<>();
        skill = new ArrayList<>();
        resumeScore = new ArrayList<>();
        sorted_resumes = findViewById(R.id.sorted_resume);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Companies")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Job Openings")
                .child(JobId)
                .child("Applicants").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //fetch.clear();
                        for_sort.clear();
                        name.clear();
                        experiance.clear();
                        resumeScore.clear();
                        testScore.clear();
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            fetch = (Map)dataSnapshot1.getValue();
                            for_sort.put(Double.parseDouble(fetch.get("Resume Score")),String.valueOf(fetch.get("uid")));
                            testScore.add(fetch.get("Score"));
                        }
                        sort(for_sort);
                        fetch_data(ids);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    public void sort(HashMap<Double,String> for_sort){
        Map<Double, String> map = new TreeMap<Double, String>(for_sort);
        Set set = map.entrySet();
        Iterator iterator2 = set.iterator();
        while(iterator2.hasNext()) {
            Map.Entry m = (Map.Entry)iterator2.next();
            ids.add(String.valueOf(m.getValue()));
            resumeScore.add(String.valueOf(m.getKey()));
        }
        Collections.reverse(ids);
        Collections.reverse(resumeScore);
        System.out.println("ID:"+ ids.size()+" Resume:"+resumeScore.size());
    }
    public void fetch_data(final ArrayList<String> ids){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        for(int i=0;i<ids.size();i++){
            final int finalI = i;
            ref.child("users").child(ids.get(i).trim()).child("Personal details").child("Name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    name.add(String.valueOf(dataSnapshot.getValue()));
                    ref.child("users").child(ids.get(finalI)).child("Experience").child("Total Experience").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            experiance.add(String.valueOf(dataSnapshot.getValue()));
                            if(experiance.size()==resumeScore.size()) {
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                sorted_resumes.setLayoutManager(layoutManager);
                                DisplaySortedResumes dsr = new DisplaySortedResumes(getApplicationContext(), name, experiance, resumeScore, testScore,ids);
                                sorted_resumes.setAdapter(dsr);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
