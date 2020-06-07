package com.miker.login.estudiante;

import com.miker.login.curso.Curso;

import java.io.Serializable;
import java.util.List;

public class Estudiante implements Serializable {
    private int id;
    private List<Curso> cursos;

    public Estudiante(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
