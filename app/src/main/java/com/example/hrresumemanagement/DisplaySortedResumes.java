package com.example.hrresumemanagement;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DisplaySortedResumes extends RecyclerView.Adapter<DisplaySortedResumes.MyViewHolder> {
    ArrayList<String> name;
    ArrayList<String> experiance;
    ArrayList<String> resumeScore;
    ArrayList<String> testScore;
    ArrayList<String> uid;
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView applicant_name,experiance,skill,resumeScore,testScore;
        CardView video_card;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            applicant_name = (TextView)itemView.findViewById(R.id.applicant_name);
            experiance = (TextView)itemView.findViewById(R.id.experience);
            resumeScore = (TextView)itemView.findViewById(R.id.resumeScore);
            testScore = (TextView)itemView.findViewById(R.id.testScore);
            skill = (TextView)itemView.findViewById(R.id.keySkill);
            video_card = (CardView)itemView.findViewById(R.id.resume_card);
        }
    }
    public DisplaySortedResumes(Context context,ArrayList<String> name, ArrayList<String> experiance, ArrayList<String> resumeScore,ArrayList<String> testScore,ArrayList<String> uid){
        this.name = name;
        this.experiance = experiance;
        this.resumeScore = resumeScore;
        this.context = context;
        this.testScore = testScore;
        this.uid = uid;
    }
    public DisplaySortedResumes.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);
        return new DisplaySortedResumes.MyViewHolder(itemView);
    }
    public void onBindViewHolder(@NonNull DisplaySortedResumes.MyViewHolder holder, final int position) {
        holder.applicant_name.setText(name.get(position));
        holder.experiance.setText(experiance.get(position));
        holder.skill.setText("Android");
        holder.resumeScore.setText(resumeScore.get(position).substring(0,5)+"/100");
        holder.testScore.setText(testScore.get(position)+"/30");
        holder.video_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ShowResume.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name",name.get(position));
                intent.putExtra("uid",uid.get(position));
                context.startActivity(intent);
            }
        });
    }
    public int getItemCount() {
        return resumeScore.size();
    }
}
