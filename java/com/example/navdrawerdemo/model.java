package com.example.navdrawerdemo;

public class model {

    String name,course,date,email,purl,diagnosis,med,dep;
    model()
    {

    }
    public model(String name, String course, String email, String purl, String med, String dep, String date) {
        this.name = name;
        this.course = course;
        this.date = date;
        this.email = email;
        this.purl = purl;
        this.med = med;
        this.dep = dep;
    }

    public model(String diagnosis) {
        this.diagnosis = diagnosis;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getCourse() {

        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getPurl() {

        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getMed() {
        return med;
    }

    public void setMed(String med) {
        this.med = med;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }
}



