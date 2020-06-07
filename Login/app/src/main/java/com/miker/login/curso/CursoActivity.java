package com.miker.login.curso;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miker.login.R;
import com.miker.login.carrera.Carrera;

public class CursoActivity extends AppCompatActivity {

    private FloatingActionButton btn_insert_update;
    private TextView descripcion;
    private TextView creditos;
    private Curso curso = new Curso();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Components Matching
        matching();
        //Prepare view
        prepare_datas();
    }

    private void matching() {
        // view
        setContentView(R.layout.activity_curso);
        //textview
        descripcion = findViewById(R.id.nombre);
        creditos = findViewById(R.id.creditos);
        //button
        btn_insert_update = findViewById(R.id.btn_insert_update);
        btn_insert_update.show();
    }

    private void load_curso(Curso curso) {
        this.curso.setId(curso.getId());
        descripcion.setText(curso.getDescripcion());
        creditos.setText(String.valueOf(curso.getCreditos()));
    }

    private void clean() {
        descripcion.setText("");
        creditos.setText("");
        curso = new Curso();
    }

    private void prepare_datas() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            load_curso((Curso) getIntent().getSerializableExtra("object"));
            btn_insert_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    insert_update("update");
                }
            });
        } else {
            clean();
            btn_insert_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    insert_update("insert");
                }
            });
        }
    }

    private void insert_update(String type) {
        if (validateForm()) {
            Intent intent = new Intent(getBaseContext(), CursosActivity.class);
            intent.putExtra(type, curso);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public boolean validateForm() {
        int error = 0;
        if (TextUtils.isEmpty(this.descripcion.getText())) {
            descripcion.setError("Nombre requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.creditos.getText())) {
            creditos.setError("Cantidad de creditos requerida");
            error++;
        }
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "Algunos errores", Toast.LENGTH_LONG).show();
            return false;
        } else {
            curso.setDescripcion(descripcion.getText().toString());
            curso.setCreditos(Integer.parseInt(creditos.getText().toString()));
        }
        return true;
    }
}
