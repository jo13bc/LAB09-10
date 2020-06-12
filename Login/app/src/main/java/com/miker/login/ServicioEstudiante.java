package com.miker.login;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.miker.login.estudiante.Estudiante;

import static com.miker.login.Utils.estudianteToContentValues;

public class ServicioEstudiante extends SQLiteOpenHelper {

    public static abstract class estudianteEntry implements BaseColumns {
        public static final String TABLE_NAME = "ESTUDIANTE";
        public static final String ID = "id";
        public static final String NOMBRE = "nombre";
        public static final String APELLIDO1 = "apellido1";
        public static final String APELLIDO2 = "apellido2";
        public static final String EDAD = "edad";
        public static final String USER = "user";
        public static final String PASSWORD = "password";
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SIMA.db";

    public ServicioEstudiante(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + estudianteEntry.TABLE_NAME + " ("
                + estudianteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + estudianteEntry.ID + " INTEGER NOT NULL,"
                + estudianteEntry.NOMBRE + " TEXT NOT NULL,"
                + estudianteEntry.APELLIDO1 + " TEXT NOT NULL,"
                + estudianteEntry.APELLIDO2 + " TEXT NOT NULL,"
                + estudianteEntry.EDAD + " INTEGER NOT NULL,"
                + estudianteEntry.USER + " TEXT NOT NULL,"
                + estudianteEntry.PASSWORD + " TEXT NOT NULL,"
                + "UNIQUE (" + estudianteEntry.ID + "))");


        // Insertar datos ficticios para prueba inicial
        registroData(db);
    }

    private void registroData(SQLiteDatabase sqLiteDatabase) {
        mockEstudiante(sqLiteDatabase, new Estudiante(1, "Monserrath","Molina","Sanchez",22,"monse","monse"));
        mockEstudiante(sqLiteDatabase, new Estudiante(2, "José","Beita","Cascante",21,"jose","jose"));
    }

       public long mockEstudiante(SQLiteDatabase db, Estudiante estudiante) {
        return db.insert(
                estudianteEntry.TABLE_NAME,
                null,
                estudianteToContentValues(estudiante)
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones
    }

    public long insert(Estudiante estudiante) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                estudianteEntry.TABLE_NAME,
                null,
                estudianteToContentValues(estudiante)
        );

    }

    public Cursor list() {
        return getReadableDatabase()
                .query(
                        estudianteEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor query(Estudiante estudiate) {
        Cursor c = getReadableDatabase().query(
                estudianteEntry.TABLE_NAME,
                null,
                estudianteEntry.ID + " LIKE ?",
                new String[]{String.valueOf(estudiate.getId())},
                null,
                null,
                null);
        return c;
    }

    public int delete(Estudiante estudiante) {
        return getWritableDatabase().delete(
                estudianteEntry.TABLE_NAME,
                estudianteEntry.ID + " LIKE ?",
                new String[]{String.valueOf(estudiante.getId())});
    }

    public int update(Estudiante estudiante) {
        return getWritableDatabase().update(
                estudianteEntry.TABLE_NAME,
                estudianteToContentValues(estudiante),
                estudianteEntry.ID + " LIKE ?",
                new String[]{String.valueOf(estudiante.getId())}
        );
    }
}
