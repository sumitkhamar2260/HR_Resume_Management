package com.example.hrresumemanagement;

public class companyPOJO {
    public String companyname,sector,email,city,description,companyID;
    public companyPOJO(){}
    public companyPOJO(String companyID,String companyname,String sector,String email,String city,String description){
        this.companyname=companyname;
        this.sector=sector;
        this.email=email;
        this.city=city;
        this.description=description;
        this.companyID=companyID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
