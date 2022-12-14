package com.example.insucompu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Account extends AppCompatActivity {
    String uid="";
    TextView txtNombre, txtCorreo, txtRol, txtTelefono,txtEmpresa;
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    LinearLayout loadedScreen, loadScreen;
    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Intent i=getIntent();
        Bundle extras=i.getExtras();
        uid=extras.getString("UID");
        txtNombre=findViewById(R.id.txtNombre);
        txtCorreo=findViewById(R.id.txtMail);
        txtRol=findViewById(R.id.txtRol);
        txtTelefono=findViewById(R.id.txtTelefono);
        txtEmpresa=findViewById(R.id.txtEmpresa);
        loadScreen=findViewById(R.id.loadScreen);
        loadedScreen=findViewById(R.id.loadedScreen);
        btnBack=findViewById(R.id.btnBack);
        loadUserData(uid);

        btnBack.setOnClickListener(view -> finish());
    }

    public void loadUserData(String uid){
        reference.child("Users/"+uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtNombre.setText(snapshot.child("Nombre").getValue(String.class)+" "+snapshot.child("Apellido").getValue(String.class));
                txtCorreo.setText(snapshot.child("Mail").getValue(String.class));
                txtRol.setText(snapshot.child("Rol").getValue(String.class));
                txtTelefono.setText(snapshot.child("Telefono").getValue(String.class));
                txtEmpresa.setText(snapshot.child("Empresa").getValue(String.class));
                loadScreen.setVisibility(View.GONE);
                loadedScreen.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}