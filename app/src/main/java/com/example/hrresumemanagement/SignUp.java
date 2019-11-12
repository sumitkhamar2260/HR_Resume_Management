package com.example.hrresumemanagement;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    TextInputEditText email,companyname,password,description;
    AutoCompleteTextView sector, city;
    TextView login;
    MaterialButton signup;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = findViewById(R.id.email);
        sector = findViewById(R.id.sector);
        city = findViewById(R.id.city);
        login=findViewById(R.id.login);
        signup=findViewById(R.id.signup);
        companyname=findViewById(R.id.companyName);
        password=findViewById(R.id.password);
        description=findViewById(R.id.description);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        Resources res = getResources();
        String[] industries = res.getStringArray(R.array.sectors);
        String[] cities=res.getStringArray(R.array.cities);
        ArrayAdapter<String> sectorAdapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, industries);
        sector.setThreshold(1);
        sector.setAdapter(sectorAdapter);
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, cities);
        city.setThreshold(1);
        city.setAdapter(cityAdapter);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this,MainActivity.class));
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerCompany();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUp.this,MainActivity.class));
    }
    public void registerCompany(){
        final String companyName=companyname.getText().toString().trim();
        String pass=password.getText().toString().trim();
        final String Sector=sector.getText().toString().trim();
        final String Email=email.getText().toString().trim();
        final String City=city.getText().toString().trim();
        final String Description=description.getText().toString().trim();
        if(companyName.isEmpty()){
            companyname.setError("Please Enter Your Company Name");
            companyname.requestFocus();
            return;
        }
        if(Sector.isEmpty()){
            sector.setError("In Which Sector Your Company Belongs ?");
            sector.requestFocus();
            return;
        }
        if(Email.isEmpty()){
            email.setError("Please Enter Email Address");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("Please Enter Valid Email Address");
            email.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            password.setError("Please Enter Your Password");
            password.requestFocus();
            return;
        }
        if(City.isEmpty()){
            city.setError("Please Enter City");
            city.requestFocus();
            return;
        }
        if(Description.isEmpty()){
            description.setError("What your comnay do ?");
            description.requestFocus();
            return;
        }
        signup.setText("");
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(Email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Verification Email is sent to: "+Email, Toast.LENGTH_LONG).show();
                        }
                    });
                    String companyid= java.util.UUID.randomUUID().toString().replaceAll("-","");
                    companyid=companyName.substring(0,Math.min(companyName.length(),3)) + companyid.substring(0,Math.min(companyid.length(),8));
                    companyPOJO companyPOJO=new companyPOJO(
                            companyid,
                            companyName,
                            Sector,
                            Email,
                            City,
                            Description
                    );
                    firebaseDatabase.getReference("Companies").
                            child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                            setValue(companyPOJO);
                    startActivity(new Intent(SignUp.this,MainActivity.class));
                    progressBar.setVisibility(View.GONE);
                    signup.setText("Sign Up");
                }else{
                    Toast.makeText(SignUp.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    signup.setText("Sign Up");
                }
            }
        });
    }
}
