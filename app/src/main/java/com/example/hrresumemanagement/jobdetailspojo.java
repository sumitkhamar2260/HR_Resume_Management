package com.example.hrresumemanagement;

import android.icu.text.CaseMap;

public class jobdetailspojo {
        private String JobTitle,Location,Salary,Experience;
        private int temperature;
        private int humidity;
        private int pressure;

        public jobdetailspojo() {

        }
        public void setJobTitle(String JobTitle) {
            this.JobTitle = JobTitle;
        }
        public void Location (String Location) {
            this.Location = Location;
        }
        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }
        public void setPressure (int pressure) {
            this.pressure = pressure;
        }
        public String getJobTitle() {
            return JobTitle;
        }
        public String getLocation() {
            return Location;
        }
        public String getSalary() {
            return Salary;
        }
        public String getExperience() {
            return Experience;
        }

}
