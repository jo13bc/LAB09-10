package com.miker.login.estudiante;

import com.miker.login.Usuario;
import com.miker.login.curso.Curso;
import com.miker.login.curso.Instancia;

import org.json.JSONObject;

import java.util.ArrayList;

public class Estudiante extends Usuario {

    private int id;
    private String apell1;
    private String apell2;
    private int edad;
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
        this(id, nombre, apell1, apell2, edad, user, password, new ArrayList<>());
    }

    public Estudiante() {
        this(0,  null, null,null,0,null,null);
    }

    public Estudiante(String user, String password) {
        this();
        this.user = user;
        this.password = password;
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

    public ArrayList<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(ArrayList<Curso> cursos) {
        this.cursos = cursos;
    }

    public String getNombre_Completo() {
        return nombre + " " + apell1 + " " + apell2;
    }

    @Override
    public boolean isSuperUser() {
        return false;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apell1='" + apell1 + '\'' +
                ", apell2='" + apell2 + '\'' +
                ", edad=" + edad +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", cursos=" + cursos +
                '}';
    }
}
