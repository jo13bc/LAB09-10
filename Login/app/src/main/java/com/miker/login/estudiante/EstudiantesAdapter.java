package com.miker.login.estudiante;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.miker.login.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Luis Carrillo Rodriguez on 18/4/2018.
 */
public class EstudiantesAdapter extends RecyclerView.Adapter<EstudiantesAdapter.MyViewHolder> implements Filterable {

    private List<Estudiante> aplicationList;
    private List<Estudiante> aplicationListFiltered;
    private EstudiantesAdapterListener listener;
    private Estudiante object;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre, apell1, apell2, edad, user,id, pass;
        public RelativeLayout viewForeground, viewBackgroundDelete, viewBackgroundEdit;

        public MyViewHolder(View view) {
            super(view);
            nombre = (TextView) view.findViewById(R.id.nombre);
            apell1 = (TextView) view.findViewById(R.id.apell1);
            apell2 = (TextView) view.findViewById(R.id.apell2);
            edad = (TextView) view.findViewById(R.id.edad);
            user = (TextView) view.findViewById(R.id.user);
            pass = (TextView) view.findViewById(R.id.pass);
            viewBackgroundDelete = view.findViewById(R.id.view_background_delete);
            viewBackgroundEdit = view.findViewById(R.id.view_background_edit);
            viewForeground = view.findViewById(R.id.view_foreground);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onSelected(aplicationListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public EstudiantesAdapter(List<Estudiante> aplicationList, EstudiantesAdapterListener listener) {
        this.aplicationList = aplicationList;
        this.listener = listener;
        this.aplicationListFiltered = aplicationList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.estudiante_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Estudiante aplication = aplicationListFiltered.get(position);
        holder.id.setText(aplication.getId());
        holder.nombre.setText(aplication.getNombre());
        holder.apell1.setText(aplication.getApellido1());
        holder.apell2.setText(aplication.getApellido1());
        holder.edad.setText(aplication.getEdad());
        holder.user.setText(aplication.getUser());
        holder.pass.setText(aplication.getPassword());
    }

    @Override
    public int getItemCount() {
        return aplicationListFiltered.size();
    }

    public void removeItem(int position) {
        object = aplicationListFiltered.remove(position);
        Iterator<Estudiante> iter = aplicationList.iterator();
        while (iter.hasNext()) {
            Estudiante aux = iter.next();
            if (object.equals(aux))
                iter.remove();
        }
        // notify item removed
        notifyItemRemoved(position);
    }

    public void restoreItem(int position) {

        if (aplicationListFiltered.size() == aplicationList.size()) {
            aplicationListFiltered.add(position, object);
        } else {
            aplicationListFiltered.add(position, object);
            aplicationList.add(object);
        }
        notifyDataSetChanged();
        // notify item added by position
        notifyItemInserted(position);
    }

    public Estudiante getSwipedItem(int index) {
        if (this.aplicationList.size() == this.aplicationListFiltered.size()) { //not filtered yet
            return aplicationList.get(index);
        } else {
            return aplicationListFiltered.get(index);
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (aplicationList.size() == aplicationListFiltered.size()) { // without filter
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(aplicationList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(aplicationList, i, i - 1);
                }
            }
        } else {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(aplicationListFiltered, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(aplicationListFiltered, i, i - 1);
                }
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    aplicationListFiltered = aplicationList;
                } else {
                    List<Estudiante> filteredList = new ArrayList<>();
                    for (Estudiante row : aplicationList) {
                        // filter use two parameters
                        if (row.getNombre().toLowerCase().contains(charString.toLowerCase()) || row.getApellido1().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    aplicationListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = aplicationListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                aplicationListFiltered = (ArrayList<Estudiante>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface EstudiantesAdapterListener {
        void onSelected(Estudiante aplication);
    }
}

