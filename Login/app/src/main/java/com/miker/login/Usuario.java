package com.miker.login;

import java.io.Serializable;

public abstract class Usuario implements Serializable {
    protected String user;
    protected String password;
    protected String nombre;

    public String getUser(){
        return user;
    }

    public void setUser(String user){
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public abstract boolean isSuperUser();

    @Override
    public abstract String toString();
}
