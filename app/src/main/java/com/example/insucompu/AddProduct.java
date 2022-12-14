package com.example.insucompu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class AddProduct extends AppCompatActivity {
    EditText etProdName,etProdPrice,etProdDescription,etProdDisponibility;
    Button btnSaveProduct;
    ImageButton backButton;
    String id="";
    DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        btnSaveProduct=findViewById(R.id.btnSaveProduct);
        etProdName=findViewById(R.id.etProductName);
        backButton=findViewById(R.id.btnClose);
        etProdPrice=findViewById(R.id.etProdutPrice);
        etProdDescription=findViewById(R.id.etProductDescription);
        etProdDisponibility=findViewById(R.id.etProductDisponibility);
        Calendar c=Calendar.getInstance();
        int min=c.get(Calendar.MINUTE);
        int hora=c.get(Calendar.HOUR_OF_DAY);
        int dia=c.get(Calendar.DAY_OF_MONTH);
        int mes=c.get(Calendar.MONTH);
        int anio=c.get(Calendar.YEAR);
        id=dia+mes+anio+"-"+hora+"-"+min;
        btnSaveProduct.setOnClickListener(view -> validateData());
        //REVISAR SI ES UN NUEVO REGISTRO O SE VA A EDITAR
        Intent i=getIntent();
        Bundle extras=i.getExtras();
        if(extras.getString("Origen").equals("Edit")){
            id=extras.getString("productId");
            etProdName.setText(extras.getString("productName"));
            etProdDescription.setText(extras.getString("productDescription"));
            etProdPrice.setText(extras.getString("productPrice"));
            etProdDisponibility.setText(extras.getString("productDisponibility"));
        }

        backButton.setOnClickListener(view -> finish());
    }

    public void validateData(){
        String prodName=etProdName.getText().toString();
        String prodDescription=etProdDescription.getText().toString();
        String prodPrice=etProdPrice.getText().toString();
        String prodDisponibility=etProdDisponibility.getText().toString();
        if(prodName.isEmpty()||prodDescription.isEmpty()||prodPrice.isEmpty()||prodDisponibility.isEmpty()){
            Toast.makeText(this, "Por favor llene todos los datos", Toast.LENGTH_SHORT).show();
        }else{
            saveData(prodName,prodDescription,prodPrice,prodDisponibility);
        }
    }

    private void saveData(String prodName, String prodDescription, String prodPrice, String prodDisponibility) {
        HashMap<String, Object> data=new HashMap<>();
        data.put("Nombre", prodName);
        data.put("Descripcion", prodDescription);
        data.put("Precio", prodPrice);
        data.put("Disponibilidad", prodDisponibility);
        data.put("Id", id);
        reference.child("Products/"+id).setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(this, R.string.productSuccess, Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "Error: "+task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}