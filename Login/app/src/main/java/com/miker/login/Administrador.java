package com.miker.login;

import com.miker.login.carrera.Carrera;
import com.miker.login.curso.Curso;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by HsingPC on 20/4/2018.
 */

public class Administrador implements Serializable {

    private String name;
    private String user;
    private String password;

    public Administrador(String name, String user, String password) {
        this.name = name;
        this.user = user;
        this.password = password;
    }

    public Administrador() {
        this.name = "";
        this.user = "";
        this.password = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String email) {
        this.user = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "name='" + name + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
