package com.fypa.projdb.beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named("databaseBean")
@RequestScoped
public class DatabaseBean {

    public String getHelloWorld() {
        return "Hello, world!";
    }

}
