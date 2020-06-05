package com.miker.login;

import com.google.gson.Gson;
import com.miker.login.carrera.Carrera;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServicioCarrera {
    public final static String LIST_CARRERA_URL = "http://10.0.2.2:8080/SIMA/carrera?opcion=list";
    public final static String INSERT_CARRERA_URL = "http://10.0.2.2:8080/SIMA/carrera?opcion=insert";
    public final static String UPDATE_CARRERA_URL = "http://10.0.2.2:8080/SIMA/carrera?opcion=update";
    public final static String DELETE_CARRERA_URL = "http://10.0.2.2:8080/SIMA/carrera?opcion=delete";
    private final static Gson gson = new Gson();
    private final static ServicioCarrera servicioCarrera = new ServicioCarrera();

    public static ServicioCarrera getServicioCarrera() {
        return servicioCarrera;
    }

    public static String insert(Carrera object) {
        return gson.toJson(object);
    }

    public static String update(Carrera object) {
        return gson.toJson(object);
    }

    public static String delete(Carrera object) {
        return gson.toJson(object);
    }

    public static Carrera query(String json_format) throws Exception {
        return gson.fromJson(json_format, Carrera.class);
    }

    public static List<Carrera> list(String json_format) throws Exception {
        List<Carrera> list = new ArrayList<>();
        try {
            JSONArray JSONList = new JSONArray(json_format);
            for (int i = 0; i < JSONList.length(); i++) {
                JSONObject dataDetail = JSONList.getJSONObject(i);
                list.add(
                        gson.fromJson(dataDetail.toString(), Carrera.class)
                );
            }
        } catch (JSONException e) {
            throw e;
        }
        return list;
    }
}
