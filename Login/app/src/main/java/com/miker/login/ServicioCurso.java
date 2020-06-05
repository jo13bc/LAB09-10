package com.miker.login;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.miker.login.curso.Curso;
import com.miker.login.curso.CursosActivity;
import com.miker.login.curso.CursosAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.miker.login.Model.LIST_CURSO_URL;

public class ServicioCurso {
    public final static String LIST_CURSO_URL = "http://10.0.2.2:8080/SIMA/curso?opcion=list";
    public final static String INSERT_CURSO_URL = "http://10.0.2.2:8080/SIMA/curso?opcion=insert";
    public final static String UPDATE_CURSO_URL = "http://10.0.2.2:8080/SIMA/curso?opcion=update";
    public final static String DELETE_CURSO_URL = "http://10.0.2.2:8080/SIMA/curso?opcion=delete";
    private final static Gson gson = new Gson();
    private final static ServicioCurso servicioCurso = new ServicioCurso();

    public static ServicioCurso getServicioCurso() {
        return servicioCurso;
    }

    public static String insert(Curso object) throws Exception {
        return object.getJSON().toString();
    }

    public static String update(Curso object) {
        return gson.toJson(object);
    }

    public static String delete(Curso object) {
        return gson.toJson(object);
    }

    public static Curso query(String json_format) throws Exception {
        return gson.fromJson(json_format, Curso.class);
    }

    public static List<Curso> list(String json_format) throws Exception {
        List<Curso> list = new ArrayList<>();
        try {
            JSONArray JSONList = new JSONArray(json_format);
            for (int i = 0; i < JSONList.length(); i++) {
                JSONObject dataDetail = JSONList.getJSONObject(i);
                list.add(
                        gson.fromJson(dataDetail.toString(), Curso.class)
                );
            }
        } catch (JSONException e) {
            throw e;
        }
        return list;
    }
}
