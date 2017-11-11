package com.i032114.seales_de_trancito;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    TextView textViewusuario;
   ImageView imageViewusu;
    Button buttonsalir;
    private ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final int CAMERA_REQUEST = 0;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    public void onStart(){
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewusuario=(TextView)findViewById(R.id.id_log_usuario);

        imageViewusu=(ImageView)findViewById(R.id.id_img_login);

        imageViewusu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(Intent.createChooser(intent,"seleccione una imagen para su perfil"),CAMERA_REQUEST);
                }
            }
        });

        buttonsalir=(Button)findViewById(R.id.id_btn_login);
        buttonsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(auth.getCurrentUser() !=null){
                    auth.signOut();
                }

            }
        });
         progressDialog= new ProgressDialog(this);
        auth=FirebaseAuth.getInstance();
        authStateListener=(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    storageReference= FirebaseStorage.getInstance().getReference();
                    databaseReference= FirebaseDatabase.getInstance().getReference().child("user");
                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            textViewusuario.setText(dataSnapshot.child("name").getValue().toString());
                            String imageUrl= dataSnapshot.child("image").getValue().toString();
                            if(imageUrl.equals("default") || TextUtils.isEmpty(imageUrl)){
                                Picasso.with(MainActivity.this).load(Uri.parse(dataSnapshot.child("image").getValue().toString())).into(imageViewusu);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else {
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }

            }
        });
    }

    protected void onActivityResult(int requesCode,int resultCode,Intent data){

        super.onActivityResult(requesCode,resultCode,data);










































    }




}
