package com.miker.login;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.miker.login.curso.Curso;
import com.miker.login.estudiante.Estudiante;

import static com.miker.login.Utils.cursoToContentValues;
import static com.miker.login.Utils.curso_X_EstudianteToContentValues;

public class ServicioCurso_X_Estudiante extends SQLiteOpenHelper {

    public static abstract class curso_x_estudianteEntry implements BaseColumns {
        public static final String TABLE_NAME = "CURSOXESTUDIANTE";
        public static final String ID_ESTUDIANTE = "id_estudiante";
        public static final String ID_CURSO = "id_curso";
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SIMA.db";

    public ServicioCurso_X_Estudiante(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + curso_x_estudianteEntry.TABLE_NAME + " ("
                + curso_x_estudianteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + curso_x_estudianteEntry.ID_ESTUDIANTE + " INTEGER NOT NULL,"
                + curso_x_estudianteEntry.ID_CURSO + " TEXT NOT NULL,"
                + "UNIQUE (" + curso_x_estudianteEntry.ID_ESTUDIANTE + "," + curso_x_estudianteEntry.ID_CURSO + "))");


        // Insertar datos ficticios para prueba inicial
        registroData(db);
    }

    private void registroData(SQLiteDatabase sqLiteDatabase) {
        mockCurso(sqLiteDatabase, new Curso(1, "Programación I", 3));
        mockCurso(sqLiteDatabase, new Curso(2, "Programación II", 3));
    }

    public long mockCurso(SQLiteDatabase db, Curso curso) {
        return db.insert(
                curso_x_estudianteEntry.TABLE_NAME,
                null,
                cursoToContentValues(curso)
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones
    }

    public long insert(Estudiante estudiante, Curso curso) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(
                curso_x_estudianteEntry.TABLE_NAME,
                null,
                curso_X_EstudianteToContentValues(estudiante, curso)
        );

    }

    public Cursor query(Curso curso) {
        Cursor c = getReadableDatabase().query(
                curso_x_estudianteEntry.TABLE_NAME,
                null,
                curso_x_estudianteEntry.ID_ESTUDIANTE + " LIKE ?",
                new String[]{String.valueOf(curso.getId())},
                null,
                null,
                null);
        return c;
    }

    public int delete(Curso curso, Estudiante estudiante) {
        return getWritableDatabase().delete(
                curso_x_estudianteEntry.TABLE_NAME,
                curso_x_estudianteEntry.ID_ESTUDIANTE + " LIKE ? AND " + curso_x_estudianteEntry.ID_CURSO + " LIKE ?",
                new String[]{String.valueOf(estudiante.getId()), String.valueOf(curso.getId())});
    }
}
