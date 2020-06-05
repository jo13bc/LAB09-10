package com.miker.login.carrera;

import com.miker.login.curso.Instancia;

import org.json.JSONObject;

public class Carrera extends Instancia {

    private int id;
    private String codigo;
    private String nombre;
    private String titulo;

    public Carrera(int id, String codigo, String nombre, String titulo) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.titulo = titulo;
    }

    public Carrera() {
        this(-1, null, null, null);
    }

    public Carrera(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        return "Carrera:\n\tId: " + id + "\n\tCódigo: " + codigo + "\n\tNombre: " + nombre + "\n\tTitulo: " + titulo;
    }

    @Override
    public JSONObject getJSON() throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("codigo", codigo);
        json.put("nombre", nombre);
        json.put("titulo", titulo);
        return json;
    }
}
