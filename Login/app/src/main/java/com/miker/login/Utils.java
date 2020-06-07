package com.miker.login;

import android.content.ContentValues;

import com.miker.login.curso.Curso;

public class Utils {

    public static ContentValues cursoToContentValues(Curso curso) {
        ContentValues values = new ContentValues();
        values.put(ServicioCurso.cursoEntry.ID, curso.getId());
        values.put(ServicioCurso.cursoEntry.DESCRIPCION, curso.getDescripcion());
        values.put(ServicioCurso.cursoEntry.CREDITOS, curso.getCreditos());
        return values;
    }
}
