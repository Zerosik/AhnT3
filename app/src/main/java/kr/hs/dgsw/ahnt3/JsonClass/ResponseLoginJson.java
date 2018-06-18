package kr.hs.dgsw.ahnt3.JsonClass;

import java.io.Serializable;

public class ResponseLoginJson implements Serializable {
    int status;
    String message;
    LoginData data;

    public String getToken(){ return data.getToken(); }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LoginData getData() {
        return data;
    }



}

class LoginData
{
    String token;
    User user;
    public String getToken() {
        return token;
    }
    public User getUser() {
        return user;
    }
}
class MyClass
{
    int grade;
    private int class_room;
    private int class_number;

    public int getGrade() {
        return grade;
    }
    public int getClass_room() {
        return class_room;
    }
    public int getClass_number() {
        return class_number;
    }
}

class User
{
    String email;
    String name;
    String gender;
    String mobile;
    int auth;
    MyClass my_class;

    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public String getGender() {
        return gender;
    }
    public String getMobile() {
        return mobile;
    }
    public int getAuth() {
        return auth;
    }
    public MyClass getMy_class() {
        return my_class;
    }
}

