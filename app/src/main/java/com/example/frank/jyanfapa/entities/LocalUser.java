package com.example.frank.jyanfapa.entities;


/**
 * Created by Frank on 2018/2/3.
 */

public class LocalUser {
    private int number;
    private String username;
    private  String password;
    private  String nickname;
    private  String realname;
    private  String email;
    private  String address;
    private int sex;
    private String birthday;
    private  int identity;

    public int getNumber()
    {
        return number;
    }
    public String getUsername()
    {
        return username;
    }
    public String getPassword()
    {
        return password;
    }
    public String getNickname()
    {
        return nickname;
    }
    public String getRealname()
    {
        return realname;
    }
    public String getEmail()
    {
        return email;
    }
    public String getAddress()
    {
        return address;
    }
    public int getSex(){return sex;}
    public int getIdentity(){return identity;}
    public String getBirthday(){return birthday;}

}
