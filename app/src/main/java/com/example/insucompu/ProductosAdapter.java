package com.example.insucompu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.Productosadapter> {

    List<ProductosList> lista;
    String tipoUsuario;
    DatabaseReference deleteReference = FirebaseDatabase.getInstance().getReference();
    private final Context mainContext;

    public ProductosAdapter(List<ProductosList> lista, Context context, String tipoUsuario) {
        this.lista = lista;
        this.mainContext = context;
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public ProductosAdapter.Productosadapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_model, parent, false);
        ProductosAdapter.Productosadapter holder = new ProductosAdapter.Productosadapter(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ProductosAdapter.Productosadapter holder, int position) {
        final ProductosList item = lista.get(position);
        holder.tvProductTitle.setText(item.getNombre());
        holder.tvProductDescription.setText(item.getDescripcion());
        holder.tvProductPrice.setText("$"+item.getPrecio());
        if (tipoUsuario.equals("Cliente")){
            holder.btnParent.setVisibility(View.GONE);
        }

        holder.btnDelete.setOnClickListener(view -> deleteItem(item.getId()));
        holder.btnEdit.setOnClickListener(view -> editarDatos(item.getId(),item.getNombre(),item.getDescripcion(),item.getPrecio(),item.getDisponibilidad()));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class Productosadapter extends RecyclerView.ViewHolder {
        TextView tvProductTitle, tvProductDescription, tvProductPrice;
        ImageButton btnDelete, btnEdit;
        LinearLayout btnParent;
        public Productosadapter(View itemView) {
            super(itemView);
            tvProductTitle=itemView.findViewById(R.id.tvProductTitle);
            tvProductDescription=itemView.findViewById(R.id.tvProductDescription);
            tvProductPrice=itemView.findViewById(R.id.tvProductPrice);
            btnEdit=itemView.findViewById(R.id.btnEdit);
            btnDelete=itemView.findViewById(R.id.btnDelete);
            btnParent=itemView.findViewById(R.id.btnParent);
        }
    }
    public void deleteItem(String productId){
        AlertDialog.Builder builder = new AlertDialog.Builder(mainContext);
        builder.setMessage(R.string.Delete)
                .setTitle(R.string.DeleteItem)
                .setPositiveButton(R.string.Delete, (dialog, id) -> deleteReference.child("Products/"+productId).setValue(null).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(mainContext, R.string.itemDeleted, Toast.LENGTH_SHORT).show();
                    }
                }))
                .setNegativeButton(R.string.Cancel, (dialog, id) -> {
                });
        builder.create();
        builder.show();
    }

    public void editarDatos(String productId,String productName,String productDescription,String productPrice, String productDisponibility){
        mainContext.startActivity(new Intent(mainContext,AddProduct.class).putExtra("productId",productId).
                putExtra("Origen","Edit")
                .putExtra("productName",productName)
                .putExtra("productDescription",productDescription)
                .putExtra("productPrice",productPrice)
                .putExtra("productDisponibility",productDisponibility));
    }

}
