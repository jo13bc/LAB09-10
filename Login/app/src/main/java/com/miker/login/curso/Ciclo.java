package com.miker.login.curso;

import java.sql.Date;
import org.json.JSONObject;

public class Ciclo extends Instancia {

    private int id;
    private int anno;
    private int nume;
    private Date fech_inic;
    private Date fech_fina;

    public Ciclo(int id, int anno, int nume, Date fech_inic, Date fech_fina) {
        this.id = id;
        this.anno = anno;
        this.nume = nume;
        this.fech_inic = fech_inic;
        this.fech_fina = fech_fina;
    }

    public Ciclo() {
        this(-1, -1, -1, null, null);
    }

    public Ciclo(int id) {
        this();
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public int getNume() {
        return nume;
    }

    public void setNume(int nume) {
        this.nume = nume;
    }

    public Date getFech_inic() {
        return fech_inic;
    }

    public void setFech_inic(Date fech_inic) {
        this.fech_inic = fech_inic;
    }

    public Date getFech_fina() {
        return fech_fina;
    }

    public void setFech_fina(Date fech_fina) {
        this.fech_fina = fech_fina;
    }

    @Override
    public String toString() {
        return "Ciclo{" + "id=" + id + '}';
    }

    @Override
    public JSONObject getJSON() throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("anno", anno);
        json.put("numero", nume);
        json.put("fecha_inicial", fech_inic);
        json.put("fecha_final", fech_fina);
        return json;
    }
}
