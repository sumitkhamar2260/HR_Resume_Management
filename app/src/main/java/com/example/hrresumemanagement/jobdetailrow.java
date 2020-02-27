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

public class jobdetailrow extends RecyclerView.Adapter<jobdetailrow.MyViewHolder>  {
    ArrayList<String> jobtitle;
    ArrayList<String> location;
    ArrayList<String> applications;
    ArrayList<String> jobID;
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView jobtit,loc,apps;
        CardView video_card;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            jobtit = (TextView)itemView.findViewById(R.id.job_title);
            loc = (TextView)itemView.findViewById(R.id.location);
            apps = (TextView)itemView.findViewById(R.id.applications);
            video_card = (CardView)itemView.findViewById(R.id.video_card);
        }
    }
    public jobdetailrow(Context context,ArrayList<String> jobtitle, ArrayList<String> location, ArrayList<String> applications,ArrayList<String> jobID){
        this.jobtitle = jobtitle;
        this.location = location;
        this.applications = applications;
        this.context = context;
        this.jobID = jobID;
    }
    public jobdetailrow.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_row,parent,false);
        return new MyViewHolder(itemView);
    }
    public void onBindViewHolder(@NonNull jobdetailrow.MyViewHolder holder, final int position) {
        String jobtit=jobtitle.get(position);
        String loc=location.get(position);
        String apps=applications.get(position);
        holder.jobtit.setText(jobtit);
        holder.loc.setText(loc);
        holder.apps.setText(apps);
        holder.video_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,RecievedApplications.class);
                intent.putExtra("Job ID",jobID.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
    public int getItemCount() {
        return jobtitle.size();
    }
}
