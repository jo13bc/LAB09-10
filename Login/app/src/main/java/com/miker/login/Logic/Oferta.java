package com.miker.login.Logic;

import com.miker.login.Logic.Curso;
import com.miker.login.Logic.Instancia;

/**
 * Created by Luis Carrillo Rodriguez on 18/4/2018.
 */
public class Oferta extends Instancia {
    private Curso curso;
    private boolean selected;

    public Oferta(Curso curso, boolean selected) {
        this.curso = curso;
        this.selected = selected;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "Oferta{" +
                "curso=" + curso +
                ", selected=" + selected +
                '}';
    }
}
