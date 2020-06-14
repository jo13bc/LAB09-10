package com.miker.login;

import com.miker.login.curso.Curso;

import java.io.Serializable;
import java.util.ArrayList;

public class Administrador extends Usuario {
    private String name;

    public Administrador(String name, String user, String password) {
        this.name = name;
        this.user = user;
        this.password = password;
    }

    public Administrador() {
        this(null, null, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isSuperUser() {
        return true;
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
