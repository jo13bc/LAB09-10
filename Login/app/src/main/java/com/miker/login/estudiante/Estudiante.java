package com.miker.login.estudiante;

import com.miker.login.curso.Curso;

import java.io.Serializable;
import java.util.List;

public class Estudiante implements Serializable {
    private int id;
    private String nombre;
    private String apellido2;
    private String apellido1;
    private int edad;
    private String user;
    private String password;
    private List<Curso> cursos;


    public Estudiante(int id,  String nombre, String apellido2, String apellido1, int edad, String user, String password, List<Curso> cursos) {
        this.id = id;
        this.nombre = nombre;
        this.apellido2 = apellido2;
        this.apellido1 = apellido1;
        this.edad = edad;
        this.user = user;
        this.password = password;
        this.cursos = cursos;
    }

    public Estudiante(int id, String nombre, String apellido2, String apellido1, int edad, String user, String password) {
        this.id = id;
        this.nombre = nombre;
        this.apellido2 = apellido2;
        this.apellido1 = apellido1;
        this.edad = edad;
        this.user = user;
        this.password = password;
    }
    public Estudiante() {
        this.id = 0;
        this.nombre = null;
        this.apellido2 = null;
        this.apellido1 = null;
        this.edad = 0;
        this.user = null;
        this.password = null;
        this.cursos = null;
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

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
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

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "id=" + id +
                '}';
    }
}
