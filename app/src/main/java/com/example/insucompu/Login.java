package com.example.insucompu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    Button btnLogin, btnSignin;
    EditText etMail, etPassword;
    FirebaseAuth mauth;
    TextView btnTerminos;
    ImageButton fbBtn,instaBtn, whatsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //OBJETOS DEL LAYOUT
        btnLogin=findViewById(R.id.btnLogin);
        btnSignin=findViewById(R.id.btnSignIn);
        etMail=findViewById(R.id.etMail);
        etPassword=findViewById(R.id.etPassword);
        btnTerminos=findViewById(R.id.btnTerminos);
        fbBtn=findViewById(R.id.facebookbtn);
        instaBtn=findViewById(R.id.instagrambtn);
        whatsBtn=findViewById(R.id.whatsappbtn);
        //REFERENCIA AL SERVICIO AUTH DE FIREBASE
        mauth=FirebaseAuth.getInstance();
        //EVENTOS ON CLICK
        btnLogin.setOnClickListener(view -> validateData());
        btnSignin.setOnClickListener(view -> startActivity(new Intent(Login.this, SignIn.class)));
        fbBtn.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/saul.mascorroluevano"))));
        instaBtn.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/mascorro.saul/"))));
        whatsBtn.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://api.whatsapp.com/send?phone="+"+524496066262"))));
        btnTerminos.setOnClickListener(view -> startActivity(new Intent(Login.this, TerminosCondiciones.class)));
    }
    public void validateData(){
        String mail=etMail.getText().toString();
        String password=etPassword.getText().toString();
        if (mail.isEmpty() || password.isEmpty() || !mail.contains("@")){
            Toast.makeText(this, "Por favor ingrese los datos correctos", Toast.LENGTH_SHORT).show();
        }else{
            loginWithFirebase(mail,password);
        }
    }
    public void loginWithFirebase(String mail, String password){
        mauth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                String uid=mauth.getCurrentUser().getUid();
                startActivity(new Intent(Login.this, Catalogo.class).putExtra("UID",uid));
                finish();
            }else{

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user= mauth.getCurrentUser();
        if (user != null){
            String uid=mauth.getCurrentUser().getUid();
            startActivity(new Intent(Login.this, Catalogo.class).putExtra("UID",uid));
            finish();
        }
    }
}