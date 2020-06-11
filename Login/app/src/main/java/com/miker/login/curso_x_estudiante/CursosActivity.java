package com.miker.login.curso_x_estudiante;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import com.miker.login.Model;
import com.miker.login.NavDrawerActivity;
import com.miker.login.R;
import com.miker.login.ServicioCurso;
import com.miker.login.curso.Curso;

import java.util.ArrayList;
import java.util.List;

public class CursosActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, CursosAdapter.CursoAdapterListener {

    private RecyclerView recyclerView;
    private CursosAdapter adapter;
    private List<Curso> cursoList;
    private Curso deleteCurso;
    private CoordinatorLayout coordinatorLayout;
    private SearchView searchView;
    private FloatingActionButton btn_insert;
    private Model model;
    private ProgressDialog progressDialog;
    private ServicioCurso servicio;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        servicio = new ServicioCurso(getApplicationContext());

        //toolbar fancy stuff
        getSupportActionBar().setTitle(getString(R.string.item_cursos));

        recyclerView = findViewById(R.id.recycler_view);
        cursoList = new ArrayList<>();
        model = new Model();
        coordinatorLayout = findViewById(R.id.main_content);

        // go to update or add career
        btn_insert = findViewById(R.id.btn_insert);
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert_curso();
            }
        });

        //delete swiping left and right
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        // Receive the Carrera sent by AddUpdCarreraActivity
        checkIntentInformation checkIntentInformation = new checkIntentInformation();
        checkIntentInformation.execute();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (direction == ItemTouchHelper.START) {
            if (viewHolder instanceof CursosAdapter.MyViewHolder) {
                // get the removed item name to display it in snack bar
                deleteCurso = cursoList.get(viewHolder.getAdapterPosition());
                delete delete = new delete();
                delete.execute();
            }
        } else {
            //If is editing a row object
            Curso aux = adapter.getSwipedItem(viewHolder.getAdapterPosition());
            //send data to Edit Activity
            Intent intent = new Intent(this, MatriculaActivity.class);
            intent.putExtra("object", aux);
            adapter.notifyDataSetChanged(); //restart left swipe view
            startActivity(intent);
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
    public void onSelected(Curso curso) { //TODO get the select item of recycleView
        Toast.makeText(getApplicationContext(), "Selected: " + curso.getDescripcion(), Toast.LENGTH_LONG).show();
    }

    public class checkIntentInformation extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(CursosActivity.this);
            progressDialog.setMessage("¡Buscando Registros!");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            message = "";
            String result = "";
            try {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    Curso aux;
                    aux = (Curso) getIntent().getSerializableExtra("insert");
                    if (aux == null) {
                        aux = (Curso) getIntent().getSerializableExtra("update");
                        if (aux != null) {
                            //found an item that can be updated
                            servicio.update(aux);
                            //check if exist
                            message = aux.getDescripcion() + " actualizado correctamente";

                        }
                    } else {
                        //found a new Curso Object
                        servicio.insert(aux);
                        message = aux.getDescripcion() + " agregado correctamente";
                    }
                }
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
                list task = new list();
                task.execute();
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }

    }

    public class list extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(CursosActivity.this);
            progressDialog.setMessage("¡Cargando la Lista!");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            message = "";
            String result = "";
            try {
                Cursor cursor = servicio.list();
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        cursoList = new ArrayList<>();
                        do {
                            Curso curso = new Curso();
                            curso.setId(cursor.getInt(cursor.getColumnIndex(ServicioCurso.cursoEntry.ID)));
                            curso.setDescripcion(cursor.getString(cursor.getColumnIndex(ServicioCurso.cursoEntry.DESCRIPCION)));
                            curso.setCreditos(cursor.getInt(cursor.getColumnIndex(ServicioCurso.cursoEntry.CREDITOS)));
                            //
                            cursoList.add(curso);
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
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
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }

    }

    public void showCursos() {
        adapter = new CursosAdapter(cursoList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        //refresh view
        adapter.notifyDataSetChanged();
    }

    public class delete extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(CursosActivity.this);
            progressDialog.setMessage("¡Eliminando " + deleteCurso.getDescripcion() + "!");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            message = "";
            try {
                servicio.delete(deleteCurso);
                message = deleteCurso.getDescripcion() + " eliminado correctamente";
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            // dismiss the progress dialog after receiving data from API
            progressDialog.dismiss();
            //Json
            try {
                list task = new list();
                task.execute();
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }

    }

    private void insert_curso() {
        Intent intent = new Intent(this, MatriculaActivity.class);
        startActivity(intent);
    }
}