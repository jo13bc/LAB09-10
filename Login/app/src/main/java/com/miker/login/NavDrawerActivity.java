package com.miker.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.miker.login.curso.CursosActivity;
import com.miker.login.curso_x_estudiante.MatriculaActivity;
import com.miker.login.estudiante.EstudiantesActivity;
import com.miker.login.oferta.curso.OfertaActivity;

public class NavDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences mPrefs;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPrefs = this.getSharedPreferences(getString(R.string.preference_user_key), Context.MODE_PRIVATE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        Menu menu = navigationView.getMenu();

        LinearLayout layout = (LinearLayout) navigationView.getHeaderView(0);

        if (usuario.isSuperUser()) {
            menu.findItem(R.id.nav_matricula).setVisible(false);
        } else {
            menu.findItem(R.id.nav_cursos).setVisible(false);
            menu.findItem(R.id.nav_estudiantes).setVisible(false);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent = null;

        if (usuario == null) {
            usuario = new Administrador();
        }
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            finish();
            intent = new Intent(NavDrawerActivity.this, LoginActivity.class);
        } else {
            if (usuario.isSuperUser()) {
                if (id == R.id.nav_cursos) {
                    intent = new Intent(NavDrawerActivity.this, CursosActivity.class);
                } else if (id == R.id.nav_estudiantes) {
                    intent = new Intent(NavDrawerActivity.this, EstudiantesActivity.class);
                }
            } else {
                if (id == R.id.nav_matricula) {
                    intent = new Intent(NavDrawerActivity.this, OfertaActivity.class);
                    // Handle the camera action
                } else if (id == R.id.nav_historial) {
                    intent = new Intent(NavDrawerActivity.this, MatriculaActivity.class);
                }
            }
            intent.putExtra("usuario", usuario);
        }
        startActivityForResult(intent, 0);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
