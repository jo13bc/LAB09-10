package com.miker.login.estudiante;

import com.miker.login.curso.Curso;
import com.miker.login.curso.Instancia;

import org.json.JSONObject;

import java.util.ArrayList;

public class Estudiante extends Instancia {

    private int id;
    private String nombre;
    private String apell1;
    private String apell2;
    private int edad;
    private String user;
    private String password;
    private ArrayList<Curso> cursos;

    public Estudiante(int id, String nombre, String apell1, String apell2, int edad, String user, String password, ArrayList<Curso> cursos) {
        this.id = id;
        this.nombre = nombre;
        this.apell1 = apell1;
        this.apell2 = apell2;
        this.edad = edad;
        this.user = user;
        this.password = password;
        this.cursos = cursos;
    }

    public Estudiante(int id, String nombre, String apell1, String apell2, int edad, String user, String password) {
        this.id = id;
        this.nombre = nombre;
        this.apell1 = apell1;
        this.apell2 = apell2;
        this.edad = edad;
        this.user = user;
        this.password = password;
    }

    public Estudiante() {
        this(0,  null, null,null,0,null,null);
    }

    public Estudiante(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApell1() {
        return apell1;
    }

    public void setApell1(String apell1) {
        this.apell1 = apell1;
    }

    public String getApell2() {
        return apell2;
    }

    public void setApell2(String apell2) {
        this.apell2 = apell2;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getUser() {
        return user;
    }

    public ArrayList<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(ArrayList<Curso> cursos) {
        this.cursos = cursos;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public JSONObject getJSON() throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("nombre", nombre);
        return json;
    }
}
