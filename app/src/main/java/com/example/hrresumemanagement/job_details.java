package com.example.hrresumemanagement;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class job_details extends AppCompatActivity {
    TextInputLayout jobtitlelay, experiencelay, skillslay, locationlay, salarylay;
    TextInputEditText jobtitle, experience, location, salary;
    AutoCompleteTextView skill;
    MaterialButton maddbtn,savejobdetailbtn;
    MaterialCardView mcard;
    TextView tv;
    int count = 0;
    LinearLayout parentLinearLayout;
    ArrayAdapter<String> skilladapter;
    Map<Object,String> skillmap=new HashMap<>();
    String job_title,experince_string,location_string,salary_string;
    ArrayList<String> skills;
    int i=1;
    final String jobid=String.valueOf((new Date().getTime()));
    FirebaseAuth firebaseAuth;
    DatabaseReference ref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        firebaseAuth=FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference("Companies").child(firebaseAuth.getCurrentUser().getUid());

        jobtitle = findViewById(R.id.jobtitle);
        experience = findViewById(R.id.experience);
        skill = findViewById(R.id.autoskill);
        location = findViewById(R.id.location);
        salary = findViewById(R.id.salary);
        jobtitlelay = findViewById(R.id.jobtitlelay);
        experiencelay = findViewById(R.id.experiencelay);
        skillslay = findViewById(R.id.skilllay);
        locationlay = findViewById(R.id.Locationlay);
        salarylay = findViewById(R.id.salarylay);
        maddbtn = findViewById(R.id.addbtn);
        parentLinearLayout = findViewById(R.id.parent_linear_layout2);
        savejobdetailbtn=findViewById(R.id.savejobdetailbtn);
        Resources res = getResources();

        skills = new ArrayList<>(Arrays.asList(res.getStringArray(R.array.skillset)));
        skilladapter =
                new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, skills);
        skill.setThreshold(1);
        skill.setAdapter(skilladapter);
        maddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddskill();
            }
        });
        savejobdetailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                job_title=jobtitle.getText().toString().trim();
                experince_string=experience.getText().toString().trim();
                location_string=location.getText().toString().trim();
                salary_string=salary.getText().toString().trim();

                if(job_title.isEmpty()){
                    jobtitlelay.setError("What is job title?");
                }
                else if(experince_string.isEmpty()){
                    experiencelay.setError("Specify experience requirements");
                }
                else if(location_string.isEmpty()){
                    locationlay.setError("Specify Location of Recruitment");
                }
                else if(salary_string.isEmpty()){
                    salarylay.setError("Enter salary range");
                }
                else if(skills.isEmpty()){
                    skill.setError("Please Enter Skill");
                }
                else{
                    Map<Object,String > jobdetail=new HashMap<>();
                    jobdetail.put("JobTitle",job_title);
                    jobdetail.put("Experience",experince_string);
                    jobdetail.put("Location",location_string);
                    jobdetail.put("Salary",salary_string);
                    jobdetail.put("JobId","JobId"+jobid);
                    jobdetail.put("Companyid",firebaseAuth.getCurrentUser().getUid());
                    ref.child("Job Openings").child("JobId"+jobid).setValue(jobdetail);
                    if(skillmap.size()>0) {
                        ref = FirebaseDatabase.getInstance().getReference().child("Companies").child(firebaseAuth.getCurrentUser().getUid()).child("Job Openings").child("JobId" + jobid);
                        ref.child("Skills").setValue(skillmap);
                    }
                    startActivity(new Intent(job_details.this,JobOpenings.class));
                }
            }
        });
    }

    public void onAddskill() {
        skillslay.setError("");
        String skill1=skill.getText().toString().trim();
        if(skills.contains(skill1)) {
            for (String ele : skills) {
                if (ele.equals(skill.getText().toString().trim())) {
                    tv = new TextView(this);
                    mcard = new MaterialCardView(this);
                    mcard.setId(++count);
                    tv.setId(++count);
                    tv.setText(skill.getText());
                    mcard.addView(tv);
                    mcard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    parentLinearLayout.addView(mcard);
                }
            }
            skillmap.put("skill"+i,skill1);
            skills.remove(skill1);
            skilladapter =
                    new ArrayAdapter<>(this, android.R.layout.select_dialog_item, skills);
            skill.setAdapter(skilladapter);
            i++;
        }
        else{
            skillslay.setError("Select from the suggested skills");
        }



        /*for (String ele : skills) {
            if (ele.equals(skill.getText().toString().trim())) {
                tv = new TextView(this);
                mcard = new MaterialCardView(this);
                mcard.setId(++count);
                tv.setId(++count);
                tv.setText(skill.getText());
                mcard.addView(tv);
                mcard.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                parentLinearLayout.addView(mcard);
            }
        }*/
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(job_details.this,JobOpenings.class));
    }
}

