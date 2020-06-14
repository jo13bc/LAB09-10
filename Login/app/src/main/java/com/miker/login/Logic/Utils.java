package com.miker.login.Logic;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.miker.login.DAO.ServicioCurso;
import com.miker.login.DAO.ServicioEstudiante;
import com.miker.login.DAO.ServicioMatricula;

public class Utils {

    public static ContentValues cursoToContentValues(Curso curso) {
        ContentValues values = new ContentValues();
        values.put(ServicioCurso.cursoEntry.DESCRIPCION, curso.getDescripcion());
        values.put(ServicioCurso.cursoEntry.CREDITOS, curso.getCreditos());
        return values;
    }

    public static ContentValues matriculaToContentValues(Estudiante estudiante, Curso curso) {
        ContentValues values = new ContentValues();
        values.put(ServicioMatricula.matriculaEntry.ID_ESTUDIANTE, estudiante.getId());
        values.put(ServicioMatricula.matriculaEntry.ID_CURSO, curso.getId());
        return values;
    }

    public static ContentValues estudianteToContentValues(Estudiante estudiante) {
        ContentValues values = new ContentValues();
        values.put(ServicioEstudiante.estudianteEntry.NOMBRE, estudiante.getNombre());
        values.put(ServicioEstudiante.estudianteEntry.APELLIDO1, estudiante.getApell1());
        values.put(ServicioEstudiante.estudianteEntry.APELLIDO2, estudiante.getApell2());
        values.put(ServicioEstudiante.estudianteEntry.EDAD, estudiante.getEdad());
        values.put(ServicioEstudiante.estudianteEntry.USER, estudiante.getUser());
        values.put(ServicioEstudiante.estudianteEntry.PASSWORD, estudiante.getPassword());
        return values;
    }

    public static boolean tableExists(SQLiteDatabase db, String tableName) {
        if (tableName == null || db == null || !db.isOpen()) {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }
}
