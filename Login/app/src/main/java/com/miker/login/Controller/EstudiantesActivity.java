package com.miker.login.Controller;

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
import com.miker.login.Model.RecyclerItemTouchHelper;
import com.miker.login.Logic.Estudiante;
import com.miker.login.R;
import com.miker.login.DAO.ServicioEstudiante;
import com.miker.login.Model.EstudiantesAdapter;

import java.util.ArrayList;
import java.util.List;

public class EstudiantesActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, EstudiantesAdapter.EstudianteAdapterListener {

    private RecyclerView recyclerView;
    private EstudiantesAdapter adapter;
    private List<Estudiante> cursoList;
    private CoordinatorLayout coordinatorLayout;
    private SearchView searchView;
    private FloatingActionButton btn_insert;
    private ProgressDialog progressDialog;
    private String message;
    private ServicioEstudiante servicio;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiantes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //toolbar fancy stuff
        getSupportActionBar().setTitle(getString(R.string.item_estudiantes));

        recyclerView = findViewById(R.id.recycler_view);
        cursoList = new ArrayList<>();
        coordinatorLayout = findViewById(R.id.main_content);

        // go to update or add career
        btn_insert = findViewById(R.id.btn_insert);
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert_estudiante();
            }
        });

        //delete swiping left and right
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        // white background notification bar
        whiteNotificationBar(recyclerView);
        try {
            servicio = ServicioEstudiante.getServicio(getApplicationContext());
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        try {
            Estudiante aux = (Estudiante) adapter.getSwipedItem(viewHolder.getAdapterPosition());
            if (direction == ItemTouchHelper.START) {
                if (viewHolder instanceof EstudiantesAdapter.MyViewHolder) {
                    // get the removed item name to display it in snack bar
                    servicio.delete(aux);
                    Toast.makeText(getApplicationContext(), aux.getNombre_Completo() + " eliminado correctamente", Toast.LENGTH_LONG).show();
                    list list = new list();
                    list.execute();
                }
            } else {
                //If is editing a row object
                //send data to Edit Activity
                Intent intent = new Intent(this, EstudianteActivity.class);
                intent.putExtra("object", aux);
                intent.putExtra("usuario", getIntent().getExtras().getSerializable("usuario"));
                adapter.notifyDataSetChanged(); //restart left swipe view
                startActivity(intent);
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemMove(int source, int target) {
        adapter.onItemMove(source, target);
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

    @Override
    public void onSelected(Estudiante estudiante) { //TODO get the select item of recycleView
        Toast.makeText(getApplicationContext(), estudiante.getNombre_Completo(), Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void checkIntentInformation() {
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                Estudiante aux;
                aux = (Estudiante) getIntent().getSerializableExtra("insert");
                if (aux == null) {
                    aux = (Estudiante) getIntent().getSerializableExtra("update");
                    if (aux != null) {
                        //found an item that can be updated
                        servicio.update(aux);
                        //check if exist
                        Toast.makeText(getApplicationContext(), aux.getNombre_Completo() + " actualizado correctamente", Toast.LENGTH_LONG);

                    }
                } else {
                    //found a new Curso Object
                    servicio.insert(aux);
                    Toast.makeText(getApplicationContext(), aux.getNombre_Completo() + " agregado correctamente", Toast.LENGTH_LONG);
                }
            }
        } catch (Exception ex) {
            message = ex.getMessage();
        }
        list list = new list();
        list.execute();
    }

    public class list extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(EstudiantesActivity.this);
            progressDialog.setMessage("¡Cargando la Lista!");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                cursoList = servicio.list();
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

    public void showCursos() {
        adapter = new EstudiantesAdapter(cursoList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        //refresh view
        adapter.notifyDataSetChanged();
    }

    private void insert_estudiante() {
        Intent intent = new Intent(this, EstudianteActivity.class);
        intent.putExtra("usuario", getIntent().getExtras().getSerializable("usuario"));
        startActivity(intent);
    }
}
