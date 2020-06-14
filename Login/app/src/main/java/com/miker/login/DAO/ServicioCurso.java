package com.miker.login.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.BaseColumns;

import androidx.annotation.RequiresApi;

import com.miker.login.Logic.Curso;

import java.util.ArrayList;
import java.util.List;

import static com.miker.login.Logic.Utils.cursoToContentValues;
import static com.miker.login.Logic.Utils.tableExists;

public class ServicioCurso extends Servicio {
    public Context context;
    private static ServicioCurso servicio = new ServicioCurso();

    private ServicioCurso() {
        //Constructor privado
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ServicioCurso getServicio(Context context) {
        servicio.context = context;
        servicio.createTable();
        return servicio;
    }

    public static abstract class cursoEntry implements BaseColumns {
        public static final String TABLE_NAME = "CURSO";
        public static final String DESCRIPCION = "descripcion";
        public static final String CREDITOS = "creditos";
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createTable() {
        boolean[] exists = new boolean[1];
        try {
            connection(context, (SQLiteDatabase sqLiteDatabase) -> {
                exists[0] = tableExists(sqLiteDatabase, cursoEntry.TABLE_NAME);
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + cursoEntry.TABLE_NAME + " ("
                        + cursoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                        + cursoEntry.DESCRIPCION + " TEXT NOT NULL,"
                        + cursoEntry.CREDITOS + " INTEGER NOT NULL,"
                        + "UNIQUE (" + cursoEntry._ID + "))");
            });
            // Insertar datos ficticios para prueba inicial
            if (!exists[0]) registroData();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registroData() {
        insert(new Curso(1, "Programación I", 3));
        insert(new Curso(2, "Programación II", 3));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long insert(Curso curso) {
        long[] result = new long[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = sqLiteDatabase.insert(
                    cursoEntry.TABLE_NAME,
                    null,
                    cursoToContentValues(curso)
            );
        });
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int update(Curso curso) {
        int[] result = new int[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = sqLiteDatabase.update(
                    cursoEntry.TABLE_NAME,
                    cursoToContentValues(curso),
                    cursoEntry._ID + " LIKE ?",
                    new String[]{String.valueOf(curso.getId())}
            );
        });
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int delete(Curso curso) {
        int[] result = new int[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = sqLiteDatabase.delete(
                    cursoEntry.TABLE_NAME,
                    cursoEntry._ID + " LIKE ?",
                    new String[]{String.valueOf(curso.getId())}
            );
        });
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Curso> list() {
        List<Curso>[] result = new List[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = list_to_cursor(
                    sqLiteDatabase.query(
                            cursoEntry.TABLE_NAME, null, null, null, null, null, null
                    )
            );
        });
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Curso query(Curso curso) {
        Curso[] result = new Curso[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = list_to_cursor(sqLiteDatabase.query(
                    cursoEntry.TABLE_NAME,
                    null,
                    cursoEntry._ID + " LIKE ?",
                    new String[]{String.valueOf(curso.getId())},
                    null,
                    null,
                    null)).get(0);
        });
        return result[0];
    }

    public List<Curso> list_to_cursor(Cursor cursor) {
        List<Curso> cursoList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Curso curso = new Curso();
                    curso.setId(cursor.getInt(cursor.getColumnIndex(ServicioCurso.cursoEntry._ID)));
                    curso.setDescripcion(cursor.getString(cursor.getColumnIndex(ServicioCurso.cursoEntry.DESCRIPCION)));
                    curso.setCreditos(cursor.getInt(cursor.getColumnIndex(ServicioCurso.cursoEntry.CREDITOS)));
                    //
                    cursoList.add(curso);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return cursoList;
    }

}
