package com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db;

/**
 * Created by nitin on 9/19/14.
 */
public class UserModel {
    @Override
    public String toString() {
        return "UserModel{" +
                "userName='" + userName + '\'' +
                ", age=" + age +
                ", userId=" + userId +
                ", sex='" + sex + '\'' +
                ", score=" + score +
                '}';
    }

    public String userName=null;
    public  int age=0;
    public int userId=-1;
    public String sex=null;

    public ScoresModel score=null;

    public ScoresModel getScore() {
        return score;
    }

    public void setScore(ScoresModel score) {
        this.score = score;

    }

    public UserModel(){

        score=new ScoresModel();
    }

    public UserModel(String sex, int age, String userName) {
        this.sex = sex;
        this.age = age;
        this.userName = userName;
        score= new ScoresModel();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public int getUserId() {
        return userId;
    }

    public int getAge() {
        return age;
    }

    public String getUserName() {
        return userName;
    }
    public UserModel generateTestUser()
    {
        UserModel user= new UserModel();
        user.setAge(24);
        user.setSex("M");
        user.setUserName("NitinA");
        return user;
    }
}
