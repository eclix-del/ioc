package com.l.testIoc.ioc;


import com.l.testIoc.annotation.C;

@C
public class User {

    public void speak(){
        System.out.println("this is user");
    }

    public void speak2(){
        System.out.println("this is user speak");
    }

}
