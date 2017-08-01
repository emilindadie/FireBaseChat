package com.example.emili.application1;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class UserActivity extends AppCompatActivity {

    //Instance de la base de donnée
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;

    DatabaseReference nomUser, prenomUser;


    Handler handler;

    //instance de l'authentification
    FirebaseAuth firebaseAuth;
    TextView nom, prenom , email;
    FirebaseUser user;
    Button bouton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        nomUser = databaseReference.child("users").child(user.getUid()).child("nom");
        prenomUser = databaseReference.child("users").child(user.getUid()).child("prenom");

        nom = (TextView) findViewById(R.id.nom_profil);
        prenom = (TextView) findViewById(R.id.prenom_profil);
        email = (TextView) findViewById(R.id.email_profil);
        handler = new Handler();

        bouton = (Button) findViewById(R.id.modif_image);
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, Profil2Activity.class);
                startActivity(intent);
            }
        });


        if(user != null) {
            getUserInformation();
        }
    }


    private void getUserInformation() {

        String monEmail = user.getEmail();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                nomUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String name = dataSnapshot.getValue(String.class);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                nom.setText(name);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                prenomUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String monPrenom = dataSnapshot.getValue(String.class);


                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                prenom.setText(monPrenom);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

        });
        thread.start();
        email.setText(monEmail);
    }



    //Action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_profil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_deconnexion:

                AuthUI.getInstance().signOut(this);
                return true;

            case R.id.action_profil:
                Intent intent = new Intent(UserActivity.this, Profil2Activity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
