package com.miker.login;

public class Administrador extends Usuario {

    public Administrador(String nombre, String user, String password) {
        this.nombre = nombre;
        this.user = user;
        this.password = password;
    }

    public Administrador() {
        this(null, null, null);
    }

    @Override
    public boolean isSuperUser() {
        return true;
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "name='" + nombre + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
