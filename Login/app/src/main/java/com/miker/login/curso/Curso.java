package com.miker.login.curso;

import com.miker.login.carrera.Carrera;

import org.json.JSONObject;

/**
 * Created by Luis Carrillo Rodriguez on 18/4/2018.
 */
public class Curso extends Instancia {
    private int id;
    private String descripcion;
    private int creditos;

    public Curso(int id, String descripcion, int creditos) {
        this.id = id;
        this.descripcion = descripcion;
        this.creditos = creditos;
    }

    public Curso() {
        this(-1, null, -1);
    }

    public Curso(int id) {
        this();
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    @Override
    public String toString() {
        return "Curso:\n\tId: " + id + "\n\tNombre: " + descripcion + "\n\tCreditos: " + creditos;
    }
}
