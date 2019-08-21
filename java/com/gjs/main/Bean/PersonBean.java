package com.gjs.opentable.Bean;

/**
 * Created by Gagan on 12/17/2016.
 */

public class PersonBean {
    String Name,Email,Password,Dob,Mobile;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }
    public String validatePerson(){
        if(Name.isEmpty()){
            //    Toast.makeText(RegisterActivity.this,name,Toast.LENGTH_LONG).show();
            return "Please enter name";

        }
        if(Email.isEmpty()){

            return "Please enter email id ";
        }
        if(Mobile.isEmpty()){

            return "Please enter mobile";
        }

        if(Dob.isEmpty()){
            return "Please enter dob";
        }
        if(Password.isEmpty()){

            return "Please enter password";
        }
        if(Mobile.length()<10){
            return " Please check the mobile number";
        }
        if(Password.length()<6){
            return "password should be more than 6 letter";
        }
        return "abc";
    }
}
