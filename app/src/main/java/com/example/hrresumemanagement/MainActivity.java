package com.example.hrresumemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextView signup;
    TextInputEditText email,password;
    MaterialButton login;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        /*if(firebaseUser!=null){
            Intent i = new Intent(MainActivity.this, JobOpenings.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signup=findViewById(R.id.signup);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        firebaseAuth=FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignUp.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }
    public void loginUser(){
        String Email=email.getText().toString().trim();
        String Password=password.getText().toString().trim();
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
        if(Password.isEmpty()){
            password.setError("Please Enter Password");
            password.requestFocus();
            return;
        }
        login.setText("");
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    checkemailverification();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    login.setText("Login");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
    public void checkemailverification(){
        FirebaseUser firebaseUser=firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag=firebaseUser.isEmailVerified();
        if(emailflag){
            startActivity(new Intent(MainActivity.this,JobOpenings.class));
            login.setText("Login");
            progressBar.setVisibility(View.GONE);
        }
        else{
            Toast.makeText(this, "Please Verify Your Email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            login.setText("Login");
            progressBar.setVisibility(View.GONE);
        }
    }
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
