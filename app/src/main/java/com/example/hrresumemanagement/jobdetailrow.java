package com.example.hrresumemanagement;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class jobdetailrow extends RecyclerView.Adapter<jobdetailrow.MyViewHolder>  {
    ArrayList<String> jobtitle;
    ArrayList<String> location;
    ArrayList<String> applications;
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
    public jobdetailrow(Context context,ArrayList<String> jobtitle, ArrayList<String> location, ArrayList<String> applications){
        this.jobtitle = jobtitle;
        this.location = location;
        this.applications = applications;
        this.context = context;
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
    }
    public int getItemCount() {
        return jobtitle.size();
    }
}
