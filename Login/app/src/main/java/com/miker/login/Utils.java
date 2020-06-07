package com.miker.login;

import android.content.ContentValues;

import com.miker.login.curso.Curso;
import com.miker.login.estudiante.Estudiante;

public class Utils {

    public static ContentValues cursoToContentValues(Curso curso) {
        ContentValues values = new ContentValues();
        values.put(ServicioCurso.cursoEntry.ID, curso.getId());
        values.put(ServicioCurso.cursoEntry.DESCRIPCION, curso.getDescripcion());
        values.put(ServicioCurso.cursoEntry.CREDITOS, curso.getCreditos());
        return values;
    }

    public static ContentValues curso_X_EstudianteToContentValues(Estudiante estudiante, Curso curso) {
        ContentValues values = new ContentValues();
        values.put(ServicioCurso_X_Estudiante.curso_x_estudianteEntry.ID_ESTUDIANTE, estudiante.getId());
        values.put(ServicioCurso_X_Estudiante.curso_x_estudianteEntry.ID_CURSO, curso.getId());
        return values;
    }
}
