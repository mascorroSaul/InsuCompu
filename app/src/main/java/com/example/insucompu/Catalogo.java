package com.example.insucompu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Catalogo extends AppCompatActivity {
    ImageButton btnAccount, btnLogout;
    FirebaseAuth auth;
    String uid="";
    String rol="";
    //CARGAR DATPS DE FIREBASE
    DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
    RecyclerView recyclerView;
    List<ProductosList> list;
    ProductosAdapter adapter;
    DatabaseReference userReference=FirebaseDatabase.getInstance().getReference();
    FloatingActionButton btnAddProduct;
    SearchView buscar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);
        btnLogout=findViewById(R.id.btnLogout);
        btnAccount=findViewById(R.id.btnAccount);
        auth=FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.recyclerCatalogo);
        btnAddProduct=findViewById(R.id.btnAddProduct);
        buscar=findViewById(R.id.etBuscar);
        Intent i=getIntent();
        Bundle extras=i.getExtras();
        uid=extras.getString("UID");
        btnLogout.setOnClickListener(view -> cerrarSesion());
        getUserRol();
        btnAccount.setOnClickListener(view -> startActivity(new Intent(Catalogo.this, Account.class).putExtra("UID",uid)));
        btnAddProduct.setOnClickListener(view -> startActivity(new Intent(Catalogo.this, AddProduct.class).putExtra("Origen","New")));

        buscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                loadData(s);
                return false;
            }
        });
    }

    public void cerrarSesion(){
        auth.signOut();
        startActivity(new Intent(Catalogo.this, Login.class));
        finish();
    }

    public void getUserRol(){
        userReference.child("Users/"+uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rol=snapshot.child("Rol").getValue(String.class);
                loadData("");
                if(rol.equals("Cliente")){
                    btnAddProduct.setVisibility(View.GONE);
                }else{
                    btnAddProduct.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void loadData(String filtro){
        list=new ArrayList<>();
        adapter=new ProductosAdapter(list,this,rol);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        reference.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             list.removeAll(list);
                for (DataSnapshot snapshot:
                     dataSnapshot.getChildren()) {
                    ProductosList listap=snapshot.getValue(ProductosList.class);
                    if(listap.getNombre().toLowerCase().contains(filtro)){
                        list.add(listap);
                    }

                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}