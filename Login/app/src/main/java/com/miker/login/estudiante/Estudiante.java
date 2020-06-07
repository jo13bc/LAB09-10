package com.miker.login.estudiante;

public class Estudiante {
    private int id;

    public Estudiante(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "id=" + id +
                '}';
    }
}
