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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignIn extends AppCompatActivity {
    ImageButton btnBack;
    Button btnSignin;
    EditText etNombre, etApellido, etTelefono, etCorreo, etContra, etConfContra, etEmpresa;
    FirebaseAuth auth;
    DatabaseReference reference=FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        btnBack=findViewById(R.id.btnBack);
        btnSignin=findViewById(R.id.btnSignIn);
        etNombre=findViewById(R.id.etName);
        etApellido=findViewById(R.id.etSecondName);
        etTelefono=findViewById(R.id.etPhone);
        etCorreo=findViewById(R.id.etMail);
        etContra=findViewById(R.id.etPassword);
        etConfContra=findViewById(R.id.etPasswordConfirm);
        etEmpresa=findViewById(R.id.etBusiness);
        auth=FirebaseAuth.getInstance();
        btnBack.setOnClickListener(view -> finish());
        btnSignin.setOnClickListener(view -> validateData());

    }

    public void validateData(){
        String name=etNombre.getText().toString();
        String secondName=etApellido.getText().toString();
        String phone=etTelefono.getText().toString();
        String mail=etCorreo.getText().toString();
        String password=etContra.getText().toString();
        String confirmPassword=etConfContra.getText().toString();
        String business=etEmpresa.getText().toString();
        if(name.isEmpty() ||secondName.isEmpty() ||phone.isEmpty()
                ||mail.isEmpty() ||password.isEmpty() ||confirmPassword.isEmpty() ||business.isEmpty() ){
            Toast.makeText(this, "Por favor llene todos los datos", Toast.LENGTH_SHORT).show();
        }else{
            if(!password.equals(confirmPassword)){
                Toast.makeText(this, "Ambas contraseñas deben coincidir", Toast.LENGTH_SHORT).show();
            }else{
                auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String uid=auth.getCurrentUser().getUid();
                        saveUserData(name, secondName,phone,mail,business,uid);
                    }else{

                    }
                });
            }
        }
    }

    public void saveUserData(String name, String secondName, String phone, String mail, String business, String uid){
        HashMap<String, Object> data =new HashMap<>();
        data.put("Nombre",name);
        data.put("Apellido",secondName);
        data.put("Telefono",phone);
        data.put("Mail",mail);
        data.put("Empresa",business);
        data.put("UID",uid);
        data.put("Rol","Cliente");
        reference.child("Users/"+uid).setValue(data).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(SignIn.this, R.string.signinSucces, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignIn.this, Catalogo.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP).putExtra("UID",uid));
                finish();
            }else{

            }
        });
    }
}