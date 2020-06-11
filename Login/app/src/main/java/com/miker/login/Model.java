package com.miker.login;

import android.database.Cursor;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.miker.login.carrera.Carrera;
import com.miker.login.curso.Ciclo;
import com.miker.login.curso.Curso;
import com.miker.login.estudiante.Estudiante;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.sql.Date;

/**
 * Created by HsingPC on 20/4/2018.
 */

public class Model extends AppCompatActivity implements  Serializable {
    private String apiUrl = "http://10.0.2.2:56884/BackEnd/ServeletUser";
    public final static String LIST_CURSO_URL = "http://10.0.2.2:8080/SIMA/curso?opcion=list";
    private Administrador loggedAdministrador;
    private Estudiante loggedEstudiante;
    private ArrayList<Curso> cursos;
    private ArrayList<Carrera> carreras;
    private int[] covers;
    private final static Gson gson = new Gson();
    Administrador usuarioAdmin; //Admin creado por default
    boolean esAdmin = false;

    public Model() {
        initUser();
    }

    public Administrador getLoggedAdministrador() {
        return loggedAdministrador;
    }

    public void setLoggedAdministrador(Administrador loggedAdministrador) {
        this.loggedAdministrador = loggedAdministrador;
    }

    public ArrayList<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(ArrayList<Curso> cursos) {
        this.cursos = cursos;
    }

    public int[] getCovers() {
        return covers;
    }

    public void setCovers(int[] covers) {
        this.covers = covers;
    }

    public ArrayList<Carrera> getCarreras() {
        return carreras;
    }

    public void setCarreras(ArrayList<Carrera> carreras) {
        this.carreras = carreras;
    }

    public Estudiante getLoggedEstudiante() {
        return loggedEstudiante;
    }

    public void setLoggedEstudiante(Estudiante loggedEstudiante) {
        this.loggedEstudiante = loggedEstudiante;
    }

    @Override
    public String toString() {
        return "Model{" +
                ", loggedAdministrador=" + loggedAdministrador +
                ", cursos=" + cursos +
                ", carreras=" + carreras +
                '}';
    }

    public void initUser() {
        usuarioAdmin = new Administrador("admin", "admin", "admin");
    }

    public String run(String apiUrl) throws Exception{
        String current = "";
        try {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(apiUrl);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    current += (char) data;
                    data = isw.read();
                    System.out.print(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

        } catch (Exception e) {
            throw e;
        }
        // return the data to onPostExecute method
        return current;
    }

    public void getCursosFromJSON(String json_format) throws Exception {
        try {
            JSONArray list = new JSONArray(json_format);
            this.cursos = new ArrayList<>();
            for (int i = 0; i < list.length(); i++) {
                JSONObject dataDetail = list.getJSONObject(i);
                this.cursos.add(
                        gson.fromJson(dataDetail.toString(), Curso.class)
                );
            }
        } catch (JSONException e) {
            throw e;
        }
    }
    public Estudiante loginEstudiante( ServicioEstudiante servicio, String user, String password) throws Exception {
        ArrayList<Estudiante> listEstudiante = new ArrayList<>();
        Estudiante estudiante = null;
        listEstudiante = getEstudiantes(servicio);

        estudiante = comprobarUsuario(listEstudiante, user, password);
        loggedEstudiante = estudiante;
        return estudiante;
    }

    public Administrador loginAdmin(String user, String password) throws Exception {
        Administrador administrador = null;

        if(user.equals(usuarioAdmin.getUser()) && password.equals(usuarioAdmin.getPassword())){
            loggedAdministrador = usuarioAdmin;
            administrador = usuarioAdmin;
            esAdmin = true;
        }
        return administrador;
    }


    public Estudiante comprobarUsuario(ArrayList<Estudiante> estudiantes, String user , String pass) {
        Estudiante usuario = null;
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getUser().equals(user)
                    && estudiante.getPassword().equals(pass)) {
                usuario = estudiante;
            }
        }
        return usuario;
    }

    public ArrayList<Estudiante> getEstudiantes(ServicioEstudiante servicio) {
        String message = "";
        String result = "";
        Estudiante est = new Estudiante();
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        try {
            Cursor cursor = servicio.list();
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        est.setId(cursor.getInt(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.ID)));
                        est.setNombre(cursor.getString(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.NOMBRE)));
                        est.setApellido1(cursor.getString(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.APELLIDO1)));
                        est.setApellido2(cursor.getString(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.APELLIDO2)));
                        est.setEdad(cursor.getInt(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.EDAD)));
                        est.setUser(cursor.getString(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.USER)));
                        est.setPassword(cursor.getString(cursor.getColumnIndex(ServicioEstudiante.estudianteEntry.PASSWORD)));
                        estudiantes.add(est);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        } catch (Exception ex) {
            message = ex.getMessage();
        }
        return estudiantes;
    }

    public ArrayList<Curso> getCursos(ServicioCurso servicio) {
        String message = "";
        String result = "";
        Curso curso = new Curso();
        ArrayList<Curso> cursos = new ArrayList<>();
        try {
            Cursor cursor = servicio.list();
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        curso.setId(cursor.getInt(cursor.getColumnIndex(ServicioCurso.cursoEntry.ID)));
                        curso.setDescripcion(cursor.getString(cursor.getColumnIndex(ServicioCurso.cursoEntry.DESCRIPCION)));
                        curso.setCreditos(cursor.getInt(cursor.getColumnIndex(ServicioCurso.cursoEntry.CREDITOS)));
                        cursos.add(curso);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        } catch (Exception ex) {
            message = ex.getMessage();
        }
        return cursos;
    }
}
