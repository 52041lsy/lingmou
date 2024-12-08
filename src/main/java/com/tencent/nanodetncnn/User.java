package com.tencent.nanodetncnn;
import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private String userid;
    private String password;
    private String phonenumber;
    private int type;

    public User(){}
    public User(String userid, String password, String phonenumber,int type) {
        this.userid = userid;
        this.password = password;
        this.phonenumber = phonenumber;
        this.type=type;
    }

    public String getId() {
        return userid;
    }

    public void setId(String id) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getnumber() {
        return phonenumber;
    }

    public void setUsernumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "User{" +
                "userid='" + userid + '\'' +
                ", password='" + password + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userid, user.userid) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid, password, phonenumber);
    }
}
