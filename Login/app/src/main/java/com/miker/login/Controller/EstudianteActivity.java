package com.miker.login.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miker.login.Logic.Estudiante;
import com.miker.login.R;


public class EstudianteActivity extends AppCompatActivity {

    private FloatingActionButton btn_insert_update;
    private TextView edad;
    private TextView nombre;
    private TextView apell1;
    private TextView apell2;
    private TextView user;
    private TextView pass;
    private Estudiante estudiante = new Estudiante();

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
        setContentView(R.layout.activity_estudiante);
        //textview
        nombre = findViewById(R.id.nombre);
        apell1 = findViewById(R.id.apell1);
        apell2 = findViewById(R.id.apell2);
        edad = findViewById(R.id.edad);
        user = findViewById(R.id.user);
        pass = findViewById(R.id.pass);
        //button
        btn_insert_update = findViewById(R.id.btn_insert_update);
        btn_insert_update.show();
    }

    private void load_estudiantes(Estudiante estudiante) {
        this.estudiante.setId(estudiante.getId());
        nombre.setText(estudiante.getNombre());
        apell1.setText(estudiante.getApell1());
        apell2.setText(estudiante.getApell2());
        edad.setText(String.valueOf(estudiante.getEdad()));
        user.setText(estudiante.getUser());
        pass.setText(estudiante.getPassword());
    }

    private void clean() {
        nombre.setText("");

    }

    private void prepare_datas() {
        Bundle extras = getIntent().getExtras();
        String tipo;
        if (extras.getSerializable("object") != null) {
            load_estudiantes((Estudiante) getIntent().getSerializableExtra("object"));
            tipo = "update";
        } else {
            clean();
            tipo = "insert";
        }
        btn_insert_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert_update(tipo);
            }
        });
    }

    private void insert_update(String type) {
        if (validateForm()) {
            Intent intent = new Intent(getBaseContext(), EstudiantesActivity.class);
            intent.putExtra(type, estudiante);
            intent.putExtra("usuario", getIntent().getExtras().getSerializable("usuario"));
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public boolean validateForm() {
        int error = 0;
        if (TextUtils.isEmpty(this.nombre.getText())) {
            nombre.setError("Nombre requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.apell1.getText())) {
            apell1.setError("Apellido 1 requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.apell2.getText())) {
            apell2.setError("Apellido 2 requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.edad.getText())) {
            edad.setError("Edad requerida");
            error++;
        }
        try {
            int edad = Integer.valueOf(this.edad.getText().toString());
            if(edad < 0 || edad > 99){
                throw new Exception();
            }
        } catch (Exception ex) {
            edad.setError("La edad debe ser un número comprendido entre 0 y 99");
            error++;
        }
        if (TextUtils.isEmpty(this.user.getText())) {
            user.setError("Usuario requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.pass.getText())) {
            pass.setError("Contraseña requerida");
            error++;
        }
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "Algunos errores", Toast.LENGTH_LONG).show();
            return false;
        } else {
            estudiante.setNombre(nombre.getText().toString());
            estudiante.setApell1(apell1.getText().toString());
            estudiante.setApell2(apell2.getText().toString());
            estudiante.setEdad(Integer.valueOf(edad.getText().toString()));
            estudiante.setUser(user.getText().toString());
            estudiante.setPassword(pass.getText().toString());
        }
        return true;
    }
}
