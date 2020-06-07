package com.miker.login;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.miker.login.curso.Curso;

import static com.miker.login.Utils.cursoToContentValues;

public class ServicioCurso extends SQLiteOpenHelper {

    public static abstract class cursoEntry implements BaseColumns {
        public static final String TABLE_NAME = "CURSO";
        public static final String ID = "id";
        public static final String DESCRIPCION = "descripcion";
        public static final String CREDITOS = "creditos";
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SIMA.db";

    public ServicioCurso(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + cursoEntry.TABLE_NAME + " ("
                + cursoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + cursoEntry.ID + " INTEGER NOT NULL,"
                + cursoEntry.DESCRIPCION + " TEXT NOT NULL,"
                + cursoEntry.CREDITOS + " INTEGER NOT NULL,"
                + "UNIQUE (" + cursoEntry.ID + "))");


        // Insertar datos ficticios para prueba inicial
        registroData(db);
    }

    private void registroData(SQLiteDatabase sqLiteDatabase) {
        mockCurso(sqLiteDatabase, new Curso(1, "Programación I", 3));
        mockCurso(sqLiteDatabase, new Curso(2, "Programación II", 3));
    }

    public long mockCurso(SQLiteDatabase db, Curso curso) {
        return db.insert(
                cursoEntry.TABLE_NAME,
                null,
                cursoToContentValues(curso)
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones
    }

    public long insert(Curso curso) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                cursoEntry.TABLE_NAME,
                null,
                cursoToContentValues(curso)
        );

    }

    public Cursor list() {
        return getReadableDatabase()
                .query(
                        cursoEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor query(Curso curso) {
        Cursor c = getReadableDatabase().query(
                cursoEntry.TABLE_NAME,
                null,
                cursoEntry.ID + " LIKE ?",
                new String[]{String.valueOf(curso.getId())},
                null,
                null,
                null);
        return c;
    }

    public int delete(Curso curso) {
        return getWritableDatabase().delete(
                cursoEntry.TABLE_NAME,
                cursoEntry.ID + " LIKE ?",
                new String[]{String.valueOf(curso.getId())});
    }

    public int update(Curso curso) {
        return getWritableDatabase().update(
                cursoEntry.TABLE_NAME,
                cursoToContentValues(curso),
                cursoEntry.ID + " LIKE ?",
                new String[]{String.valueOf(curso.getId())}
        );
    }
}
