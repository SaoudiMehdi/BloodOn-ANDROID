package com.example.bloodon;

public class FindDonnators {


    public String email, profileimage,fullname,status;

    public FindDonnators()
    {

    }

    public FindDonnators(String email, String profileimage, String fullname) {
        this.email = email;
        this.profileimage = profileimage;
        this.fullname = fullname;
        //this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
