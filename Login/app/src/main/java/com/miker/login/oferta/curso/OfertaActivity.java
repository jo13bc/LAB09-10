package com.miker.login.oferta.curso;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miker.login.Helper.RecyclerItemTouchHelper;
import com.miker.login.NavDrawerActivity;
import com.miker.login.R;
import com.miker.login.ServicioCurso;
import com.miker.login.ServicioMatricula;
import com.miker.login.Usuario;
import com.miker.login.curso.Curso;
import com.miker.login.curso_x_estudiante.CustomAdapter;
import com.miker.login.curso_x_estudiante.MatriculaActivity;
import com.miker.login.estudiante.Estudiante;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OfertaActivity extends AppCompatActivity implements OfertaAdapter.OfertaAdapterListener {

    private RecyclerView recyclerView;
    private OfertaAdapter adapter;
    private List<Oferta> cursoList;
    private RelativeLayout coordinatorLayout;
    private SearchView searchView;
    private FloatingActionButton btn_insert;
    private ProgressDialog progressDialog;
    private String message;
    private ServicioCurso servicio;
    private ArrayList<Curso> cursoListSelected;
    private Usuario usuario;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferta);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //toolbar fancy stuff
        getSupportActionBar().setTitle(getString(R.string.item_oferta));

        recyclerView = findViewById(R.id.recycler_view);
        cursoList = new ArrayList<>();
        coordinatorLayout = findViewById(R.id.main_content);

        // go to update or add career
        btn_insert = findViewById(R.id.btn_insert);
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmation();
            }
        });
        // white background notification bar
        whiteNotificationBar(recyclerView);
        try {
            servicio = ServicioCurso.getServicio(getApplicationContext());
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        cursoListSelected = new ArrayList<>();
    }

    private void confirmation() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_matricula);
        ListView lv = (ListView) dialog.findViewById(R.id.list);
        Button btn_accept = (Button) dialog.findViewById(R.id.btn_accept);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        lv.setAdapter(new CustomAdapter(getApplicationContext(), this.cursoListSelected, null));
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                insert();
            }
        });
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setTitle("Confirmación de Acción");
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        try {
            // Receive the Carrera sent by AddUpdCarreraActivity
            checkIntentInformation();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds cursoList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView   !IMPORTANT
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change, every type on input
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        Intent a = new Intent(this, NavDrawerActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        a.putExtra("usuario", getIntent().getExtras().getSerializable("usuario"));
        startActivity(a);
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSelected(Oferta oferta, TextView nombre, TextView creditos, RelativeLayout viewForeground) {
        if (!checkCurso(cursoListSelected, oferta.getCurso())) {
            oferta.setSelected(true);
            cursoListSelected.add(oferta.getCurso());
        } else {
            oferta.setSelected(false);
            cursoListSelected.remove(oferta.getCurso());
        }
        if (oferta.isSelected()) {
            nombre.setTextColor(Color.WHITE);
            creditos.setTextColor(Color.WHITE);
            viewForeground.setBackgroundColor(Color.GRAY);
        } else {
            nombre.setTextColor(Color.BLACK);
            creditos.setTextColor(Color.BLACK);
            viewForeground.setBackgroundColor(Color.WHITE);
        }
        Toast.makeText(getApplicationContext(), oferta.getCurso().getDescripcion() + ((oferta.isSelected()) ? " seleccionado." : " deseleccionado."), Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void checkIntentInformation() {
        usuario = (Usuario) getIntent().getExtras().getSerializable("usuario");
        list list = new list();
        list.execute();
    }

    public class list extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(OfertaActivity.this);
            progressDialog.setMessage("¡Cargando la Lista!");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                List<Curso> list = ServicioMatricula.getServicio(getApplicationContext()).list((Estudiante) usuario);
                cursoList = servicio.list().stream().map(x -> new Oferta(x, checkCurso(list, x))).collect(Collectors.toList());
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            // dismiss the progress dialog after receiving data from API
            progressDialog.dismiss();
            //Json
            try {
                showCursos();
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean checkCurso(List<Curso> list, Curso curso) {
        return list.stream().filter(x -> curso.getId() == x.getId()).count() > 0;
    }

    public void showCursos() {
        adapter = new OfertaAdapter(cursoList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        //refresh view
        adapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void insert() {
        try {
            for (Curso curso : cursoListSelected) {
                ServicioMatricula.getServicio(getApplicationContext()).insert((Estudiante)usuario, curso);
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
