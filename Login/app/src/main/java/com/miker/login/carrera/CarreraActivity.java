package com.miker.login.carrera;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miker.login.R;


public class CarreraActivity extends AppCompatActivity {

    private FloatingActionButton btn_insert_update;
    private TextView codigo;
    private TextView nombre;
    private TextView titulo;
     private Carrera carrera = new Carrera();

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
        setContentView(R.layout.activity_carrera);
        //textview
        codigo = findViewById(R.id.codigo);
        nombre = findViewById(R.id.nombre);
        titulo = findViewById(R.id.titulo);
        //button
        btn_insert_update = findViewById(R.id.btn_insert_update);
        btn_insert_update.show();
    }

    private void load_carrera(Carrera carrera) {
        this.carrera.setId(carrera.getId());
        codigo.setText(carrera.getCodigo());
        nombre.setText(carrera.getNombre());
        titulo.setText(carrera.getTitulo());
    }

    private void clean() {
        codigo.setText("");
        nombre.setText("");
        titulo.setText("");
    }

    private void prepare_datas() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            load_carrera((Carrera) getIntent().getSerializableExtra("object"));
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
            Intent intent = new Intent(getBaseContext(), CarrerasActivity.class);
            intent.putExtra(type, carrera);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public boolean validateForm() {
        int error = 0;
        if (TextUtils.isEmpty(this.codigo.getText())) {
            codigo.setError("Código requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.nombre.getText())) {
            nombre.setError("Nombre requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.titulo.getText())) {
            titulo.setError("Titulo requerido");
            error++;
        }
               if (error > 0) {
            Toast.makeText(getApplicationContext(), "Algunos errores", Toast.LENGTH_LONG).show();
            return false;
        } else {
            carrera.setCodigo(codigo.getText().toString());
                   carrera.setNombre(nombre.getText().toString());
                   carrera.setTitulo(titulo.getText().toString());
        }
        return true;
    }
}
