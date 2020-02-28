package com.example.hrresumemanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class ShowResume extends AppCompatActivity {
    Intent intent;
    String name,uid,mobile,email,city,state,degree,fieldOfStudy;
    ArrayList<String> c_name,c_location,j_title,s_date,e_date;
    DatabaseReference databaseReference;
    TextView name_t,email_t,mobile_t,city_state,skill,education;
    Button dpdf,sendemail;
    FirebaseStorage storage;
    StorageReference storageRef,islandRef;
    ProgressDialog pDialog;
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
        sendemail=findViewById(R.id.sendemail);
        name_t = findViewById(R.id.name);
        email_t = findViewById(R.id.email);
        mobile_t = findViewById(R.id.mobile);
        city_state = findViewById(R.id.city_state);
        skill = findViewById(R.id.skill);
        education = findViewById(R.id.education);
        dpdf=findViewById(R.id.download);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        retrievePersonalDetails();
        dpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              downloadFile();
            }
        });
        sendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendhireemail();
            }
        });
    }

    private void sendhireemail() {

        Intent it = new Intent(Intent.ACTION_SEND);
        it.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        it.putExtra(Intent.EXTRA_SUBJECT, "calling for interview");
        it.setType("message/rfc822");
        startActivity(Intent.createChooser(it,"Choose Mail App"));
    }

    private void downloadFile() {
        pDialog = new ProgressDialog(ShowResume.this);
        pDialog.setMessage("loading..");
        pDialog.show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://hr-resume-management.appspot.com/Uploads");
        StorageReference  islandRef = storageRef.child(uid);

        File rootPath = new File(Environment.getExternalStorageDirectory(),"documents");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath, String.format("%s.pdf", name));

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                  pDialog.dismiss();
                Toast.makeText(ShowResume.this, String.format(";local tem file created  created %s",localFile.toString()) , Toast.LENGTH_SHORT).show();
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                pDialog.dismiss();
                Log.e("firebase ",";local tem file not created  created " +exception.toString());
            }
        });
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
                mobile_t.setText(mobile);
                name_t.setText(name);
                email_t.setText(email);
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
