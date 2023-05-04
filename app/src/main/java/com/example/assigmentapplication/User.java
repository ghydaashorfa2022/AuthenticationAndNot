package com.example.assigmentapplication;

public class User {
    String id = "";
    String idUserAuth;
    String UserName;
    String UserImage;
    String email;
    String password;
    String mobileuser;

    public User(String id, String idUserAuth , String userName, String email, String image, String mobile,String password){
   this.id=id;
   this.idUserAuth=idUserAuth;
   this.UserImage=image;
   this.UserName=userName;
   this.mobileuser=mobile;
   this.email=email;
   this.password=password;
    }


    public String getId() {
        return id;
    }
    public String getUserName() {
        return UserName;
    }
    public String getIdUserAuth() {
        return idUserAuth;
    }
    public String getUserImage() {
        return UserImage;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getMobileuser() {
        return mobileuser;
    }

}
