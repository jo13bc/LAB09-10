package com.miker.login.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.BaseColumns;

import androidx.annotation.RequiresApi;

import com.miker.login.Logic.Curso;
import com.miker.login.Logic.Estudiante;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.miker.login.Logic.Utils.matriculaToContentValues;
import static com.miker.login.Logic.Utils.tableExists;

public class ServicioMatricula extends Servicio {
    public Context context;
    private static ServicioMatricula servicio = new ServicioMatricula();

    private ServicioMatricula() {
        //Constructor privado
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ServicioMatricula getServicio(Context context) throws Exception {
        servicio.context = context;
        servicio.createTable();
        return servicio;
    }

    public static abstract class matriculaEntry implements BaseColumns {
        public static final String TABLE_NAME = "MATRICULA";
        public static final String ID_ESTUDIANTE = "id_estudiante";
        public static final String ID_CURSO = "id_curso";
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createTable() throws Exception {
        boolean[] exists = new boolean[1];
        try {
            connection(context, (SQLiteDatabase sqLiteDatabase) -> {
                exists[0] = tableExists(sqLiteDatabase, matriculaEntry.TABLE_NAME);
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + matriculaEntry.TABLE_NAME + " ("
                        + matriculaEntry.ID_ESTUDIANTE + " INTEGER NOT NULL,"
                        + matriculaEntry.ID_CURSO + " TEXT NOT NULL,"
                        + "PRIMARY KEY (" + matriculaEntry.ID_ESTUDIANTE + "," + matriculaEntry.ID_CURSO + "),"
                        + "UNIQUE (" + matriculaEntry.ID_ESTUDIANTE + "," + matriculaEntry.ID_CURSO + "))");
            });
            // Insertar datos ficticios para prueba inicial
            if (!exists[0]) registroData();
        } catch (Exception ex) {
            throw ex;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registroData() {
        insert(new Estudiante(1), new Curso(1));
        insert(new Estudiante(1), new Curso(2));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long insert(Estudiante estudiante, Curso curso) {
        long[] result = new long[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = sqLiteDatabase.insert(
                    matriculaEntry.TABLE_NAME,
                    null,
                    matriculaToContentValues(estudiante, curso)
            );
        });
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int delete(Estudiante estudiante, Curso curso) {
        int[] result = new int[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = sqLiteDatabase.delete(
                    matriculaEntry.TABLE_NAME,
                    matriculaEntry.ID_ESTUDIANTE + " LIKE ? AND " + matriculaEntry.ID_CURSO + " LIKE ?",
                    new String[]{String.valueOf(estudiante.getId()), String.valueOf(curso.getId())}
            );
        });
        return result[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Curso> list(Estudiante estudiante) {
        List<Curso>[] result = new List[1];
        connection(context, (SQLiteDatabase sqLiteDatabase) -> {
            result[0] = list_to_cursor(sqLiteDatabase.query(
                    matriculaEntry.TABLE_NAME,
                    null,
                    matriculaEntry.ID_ESTUDIANTE + " LIKE ?",
                    new String[]{String.valueOf(estudiante.getId())},
                    null,
                    null,
                    null));
        });
        ServicioCurso servicioCurso = ServicioCurso.getServicio(context);
        return result[0].stream().map(x -> servicioCurso.query(x)).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Curso> list_to_cursor(Cursor cursor) {
        List<Curso> cursoList = new ArrayList<>();
        try {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        Curso curso = new Curso();
                        curso.setId(cursor.getInt(cursor.getColumnIndex(matriculaEntry.ID_CURSO)));
                        //
                        cursoList.add(curso);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return cursoList;
    }
}
