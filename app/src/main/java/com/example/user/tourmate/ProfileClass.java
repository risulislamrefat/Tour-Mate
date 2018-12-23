package com.example.user.tourmate;

public class ProfileClass {
   private String profileImage;
   private String name;
   private String email;
   private String age;
   private String address;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ProfileClass() {
    }

    public ProfileClass(String profileImage, String name, String email, String age, String address) {
        this.profileImage = profileImage;
        this.name = name;
        this.email = email;
        this.age = age;
        this.address = address;
    }
}
