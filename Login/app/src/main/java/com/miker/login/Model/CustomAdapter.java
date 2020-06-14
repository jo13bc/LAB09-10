package com.miker.login.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miker.login.R;
import com.miker.login.Logic.Curso;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<Curso> cursoList;
    LayoutInflater inflter;
    private CustomAdapterListener listener;

    public CustomAdapter(Context applicationContext, List<Curso> cursoList, CustomAdapterListener listener) {
        this.context = context;
        this.cursoList = cursoList;
        this.listener = listener;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return cursoList.size();
    }

    @Override
    public Curso getItem(int i) {
        return cursoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return cursoList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.card_curso, null);
        TextView descripcion = view.findViewById(R.id.descripcion);
        TextView creditos = view.findViewById(R.id.creditos);
        descripcion.setText(cursoList.get(i).getDescripcion());
        creditos.setText("Cantidad de Créditos: " + String.valueOf(cursoList.get(i).getCreditos()));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send selected contact in callback
                listener.onSelected(cursoList.get(i));
            }
        });
        return view;
    }

    public interface CustomAdapterListener {
        void onSelected(Curso curso);
    }
}
