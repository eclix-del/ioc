package com.l.testIoc.ioc;


import com.l.testIoc.annotation.A;
import com.l.testIoc.annotation.C;
import com.l.testIoc.annotation.R;

@C
public class Book {

    public Book(){
        System.out.println("book init");
    }

    @A
    private User user;

    @R
    private Money money;

    public void speak(){
        System.out.println("this is book");
        user.speak();
        money.speak();
    }

    public void userSpeak(){

    }

}
