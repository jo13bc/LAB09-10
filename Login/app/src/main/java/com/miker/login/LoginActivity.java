package com.miker.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.miker.login.estudiante.Estudiante;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText user;
    private EditText password;
    private Estudiante estudiante = null;
    private RadioButton esAdmin;
    private Administrador administrador = null;
    private Model model;
    private ServicioEstudiante servicio;
    private List<Estudiante> cursoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // initiate a button
        ImageButton loginButton =  (ImageButton ) findViewById(R.id.btn);
        user = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.password);
        esAdmin = (RadioButton) findViewById(R.id.radioButton);
        servicio = new ServicioEstudiante(getApplicationContext());
        // perform click event on the button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if(esAdmin.isChecked()) {
                        administrador =  model.loginAdmin(user.getText().toString(),password.getText().toString());

                    }else{
                        estudiante = model.loginEstudiante(servicio, user.getText().toString(), password.getText().toString());
                    }
                   if (estudiante != null || administrador != null) {
                        openActivity(estudiante);
                   } else {
                       Toast.makeText(getApplicationContext(), "¡Datos no encontrados!", Toast.LENGTH_LONG).show();  // display a toast message
                       esAdmin.setChecked(false);
                   }
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        model = (Model) getIntent().getSerializableExtra("model");
        if(model == null){
            model = new Model();
        }
    }

    private void openActivity(Estudiante estudiante){
        Intent intent;
        String message;
        if(estudiante != null){
            intent = new Intent(getApplicationContext(), NavDrawerActivity.class);
            message = "Estudiante";
        }else {
            intent = new Intent(getApplicationContext(), NavDrawerActivity.class);
            message = "Administrador";
        }
        intent.putExtra("model", model);
        startActivityForResult(intent, 0);
        Toast.makeText(getApplicationContext(), "¡Bienvenido " + message + "!", Toast.LENGTH_LONG).show();  // display a toast message
    }
}
