package com.miker.login.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.BaseColumns;

import androidx.annotation.RequiresApi;

import com.miker.login.Logic.Estudiante;

import java.util.ArrayList;
import java.util.List;

import static com.miker.login.Logic.Utils.estudianteToContentValues;
import static com.miker.login.Logic.Utils.tableExists;

public class ServicioEstudiante extends Servicio {
    public Context context;
    private static ServicioEstudiante servicio = new ServicioEstudiante();

    private ServicioEstudiante() {
        //Constructor privado
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ServicioEstudiante getServicio(Context context) throws Exception {
        servicio.context = context;
        servicio.createTable();
        return servicio;
    }

    public static abstract class estudianteEntry implements BaseColumns {
        public static final String TABLE_NAME = "ESTUDIANTE";
        public static final String NOMBRE = "nombre";
        public static final String APELLIDO1 = "apellido1";
        public static final String APELLIDO2 = "apellido2";
        public static final String EDAD = "edad";
        public static final String USER = "user";
        public static final String PASSWORD = "password";
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createTable() throws Exception {
        boolean[] exists = new boolean[1];
        try {
            connection(context, (SQLiteDatabase sqLiteDatabase) -> {
                exists[0] = tableExists(sqLiteDatabase, estudianteEntry.TABLE_NAME);
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + estudianteEntry.TABLE_NAME + " ("
                        + estudianteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                        + estudianteEntry.NOMBRE + " TEXT NOT NULL,"
                        + estudianteEntry.APELLIDO1 + " TEXT NOT NULL,"
                        + estudianteEntry.APELLIDO2 + " TEXT NOT NULL,"
                        + estudianteEntry.EDAD + " INTEGER NOT NULL,"
                        + estudianteEntry.USER + " TEXT NOT NULL,"
                        + estudianteEntry.PASSWORD + " TEXT NOT NULL,"
                        + "UNIQUE (" + estudianteEntry._ID + "))");
            });
            // Insertar datos ficticios para prueba inicial
            if (!exists[0]) registroData();
        } catch (Exception ex) {
            throw ex;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registroData() {
        insert(new Estudiante(1, "Monserrath", "Molina", "Sanchez", 22, "monse", "monse"));
        insert(new Estudiante(2, "José", "Beita", "Cascante", 21, "jose", "jose"));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long insert(Estudiante estudiante) {
        long[] result = new long[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = sqLiteDatabase.insert(
                    estudianteEntry.TABLE_NAME,
                    null,
                    estudianteToContentValues(estudiante)
            );
        });
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int update(Estudiante estudiante) {
        int[] result = new int[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = sqLiteDatabase.update(
                    estudianteEntry.TABLE_NAME,
                    estudianteToContentValues(estudiante),
                    estudianteEntry._ID + " LIKE ?",
                    new String[]{String.valueOf(estudiante.getId())}
            );
        });
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int delete(Estudiante estudiante) {
        int[] result = new int[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = sqLiteDatabase.delete(
                    estudianteEntry.TABLE_NAME,
                    estudianteEntry._ID + " LIKE ?",
                    new String[]{String.valueOf(estudiante.getId())}
            );
        });
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Estudiante> list() {
        List<Estudiante>[] result = new List[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = list_to_cursor(
                    sqLiteDatabase.query(
                            estudianteEntry.TABLE_NAME, null, null, null, null, null, null
                    )
            );
        });
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Estudiante query(Estudiante estudiante) {
        Estudiante[] result = new Estudiante[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = list_to_cursor(sqLiteDatabase.query(
                    estudianteEntry.TABLE_NAME,
                    null,
                    estudianteEntry._ID + " LIKE ?",
                    new String[]{String.valueOf(estudiante.getId())},
                    null,
                    null,
                    null)).get(0);
        });
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Estudiante login(Estudiante estudiante) {
        Estudiante[] result = new Estudiante[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            List<Estudiante> list = list_to_cursor(sqLiteDatabase.query(
                    estudianteEntry.TABLE_NAME,
                    null,
                    estudianteEntry.USER + " LIKE ? AND " + estudianteEntry.PASSWORD + " LIKE ?",
                    new String[]{estudiante.getUser(), estudiante.getPassword()},
                    null,
                    null,
                    null));
            result[0] = (list.isEmpty()) ? null : list.get(0);
        });
        return result[0];
    }

    public List<Estudiante> list_to_cursor(Cursor cursor) {
        List<Estudiante> estudianteList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Estudiante estudiante = new Estudiante();
                    estudiante.setId(cursor.getInt(cursor.getColumnIndex(estudianteEntry._ID)));
                    estudiante.setNombre(cursor.getString(cursor.getColumnIndex(estudianteEntry.NOMBRE)));
                    estudiante.setApell1(cursor.getString(cursor.getColumnIndex(estudianteEntry.APELLIDO1)));
                    estudiante.setApell2(cursor.getString(cursor.getColumnIndex(estudianteEntry.APELLIDO2)));
                    estudiante.setEdad(cursor.getInt(cursor.getColumnIndex(estudianteEntry.EDAD)));
                    estudiante.setUser(cursor.getString(cursor.getColumnIndex(estudianteEntry.USER)));
                    estudiante.setPassword(cursor.getString(cursor.getColumnIndex(estudianteEntry.PASSWORD)));
                    //
                    estudianteList.add(estudiante);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return estudianteList;
    }
}
