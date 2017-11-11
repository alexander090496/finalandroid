package com.i032114.seales_de_trancito;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.i032114.seales_de_trancito.Objetos.FirbasesReferences;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText editTextreguser;
    private EditText editTextregemail;
    private EditText editTextregpassword;
    private FirebaseAuth mauth;
    private ProgressDialog progressDialog;
    private   Button buttonregister;

    protected void onStart(){
        super.onStart();
        mauth.addAuthStateListener(authStateListener);
    }
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mauth = FirebaseAuth.getInstance();

        editTextreguser=(EditText)findViewById(R.id.id_reg_user_name);
        editTextregemail=(EditText)findViewById(R.id.id_reg_mail_user);
        editTextregpassword=(EditText)findViewById(R.id.id_reg_user_pass);
        buttonregister=(Button)findViewById(R.id.id_btn_reg_usu);
        buttonregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startRegister();
            }
        });

        progressDialog = new ProgressDialog(this);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){

                    Intent intent = new Intent(RegisterUserActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        };

    }

    private void startRegister() {

        final String name = editTextreguser.getText().toString().trim();
        final String email = editTextregemail.getText().toString().trim();
        final String password = editTextregpassword.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            progressDialog.setMessage("Registrando usuario");
            progressDialog.show();
            mauth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if(task.isSuccessful()){
                                mauth.signInWithEmailAndPassword(email,password);
                                //String user_id=mauth.getCurrentUser().getUid();
                                // Toast.makeText(RegisterUserActivity.this,user_id,Toast.LENGTH_SHORT).show();
                              /*
                             DatabaseReference DATABASE = FirebaseDatabase.getInstance().getReference().child("users");
                              DatabaseReference currentUserDB= DATABASE.child(mauth.getCurrentUser().getUid());
                              currentUserDB.child("name").setValue(name);
                              currentUserDB.child("image").setValue("default");
                              */

                                Toast.makeText( RegisterUserActivity.this,"Usuario Creado",Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText( RegisterUserActivity.this,"Error al registrar el usuario ",Toast.LENGTH_SHORT).show();


                        }
                    });


        }


    }


}
