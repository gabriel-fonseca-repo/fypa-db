package com.fypa.projdb.beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class DatabaseBean {

    public DatabaseBean() {
        System.out.println("Teste");
    }

    public void botao() {
        System.out.println("Teste botão");
    }

    public String getHelloWorld() {
        return "Hello, world!";
    }

}

