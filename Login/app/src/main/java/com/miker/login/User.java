package com.miker.login;

import com.miker.login.carrera.Carrera;
import com.miker.login.curso.Curso;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by HsingPC on 20/4/2018.
 */

public class User implements Serializable {

    private String name;
    private int privilege;
    private ArrayList<Curso> selectedCursos;
    private ArrayList<Carrera> selectedCarrera;
    private String email;
    private String password;

    public User(String name, int privilege, ArrayList<Curso> selectedCursos,ArrayList<Carrera> selectedCarrera, String email, String password) {
        this.name = name;
        this.privilege = privilege;
        this.selectedCursos = selectedCursos;
        this.selectedCarrera = selectedCarrera;
        this.email = email;
        this.password = password;
    }

    public User() {
        this.name = "";
        this.privilege = 0;
        this.selectedCursos = new ArrayList<>();
        this.selectedCarrera = new ArrayList<>();
        this.email = "@";
        this.password = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public ArrayList<Curso> getSelectedCursos() {
        return selectedCursos;
    }

    public void setSelectedCursos(ArrayList<Curso> selectedProducts) {
        this.selectedCursos = selectedProducts;
    }

    public void addCurso(Curso p){
        this.selectedCursos.add(p);
    }


    public ArrayList<Carrera> getSelectedCarreras() {
        return selectedCarrera;
    }

    public void setSelectedCarreras(ArrayList<Carrera> selectedCarrera) {
        this.selectedCarrera = selectedCarrera;
    }

    public void addCarrera(Carrera p){
        this.selectedCarrera.add(p);
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", privilege=" + privilege +
                ", selectedCursos=" + selectedCursos +
                ", selectedCarrea=" + selectedCarrera +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
