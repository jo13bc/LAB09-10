package com.miker.login.curso_x_estudiante;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miker.login.R;
import com.miker.login.curso.Curso;
import com.miker.login.estudiante.Estudiante;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<Curso> cursoList;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, List<Curso> cursoList) {
        this.context = context;
        this.cursoList = cursoList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return cursoList.size();
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
        descripcion.setText(cursoList.get(i).getDescripcion());
        creditos.setText("Cantidad de Créditos: " + String.valueOf(cursoList.get(i).getCreditos()));
        return view;
    }
}
