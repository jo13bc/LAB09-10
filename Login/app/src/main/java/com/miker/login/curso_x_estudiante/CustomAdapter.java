package com.miker.login.curso_x_estudiante;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miker.login.R;
import com.miker.login.estudiante.Estudiante;

public class CustomAdapter extends BaseAdapter {
    Context context;
    Estudiante estudiante;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, Estudiante estudiante) {
        this.context = context;
        this.estudiante = estudiante;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return estudiante.getCursos().size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.cardview_curso, null);
        TextView descripcion = view.findViewById(R.id.descripcion);
        TextView creditos = view.findViewById(R.id.creditos);
        descripcion.setText(estudiante.getCursos().get(i).getDescripcion());
        creditos.setText("Cantidad de Créditos: " + String.valueOf(estudiante.getCursos().get(i).getCreditos()));
        return view;
    }
}
