package com.example.emili.application1;


import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.emili.application1.Donnee.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Handler handler;
    EditText nom, prenom, email,motDePasse1, motDePasse2;
    Button inscription;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    String emailSaisi, motDePasseSaisi, prenomSaisi,  nomSaisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.handler = new Handler();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        email = (EditText) findViewById(R.id.email);
        motDePasse1 = (EditText) findViewById(R.id.motDePasse1);
        motDePasse2 = (EditText) findViewById(R.id.motDePasse2);
        inscription = (Button) findViewById(R.id.inscription);



        inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nom.getText().toString().length() != 0 | prenom.getText().toString().length() != 0 |
                        email.getText().toString().length() != 0 | motDePasse1.getText().toString().length() != 0
                        |  motDePasse2.getText().toString().length() != 0){

                    if(motDePasse1.getText().toString().equals(motDePasse2.getText().toString())){
                        emailSaisi = email.getText().toString();
                        motDePasseSaisi = motDePasse1.getText().toString();
                        prenomSaisi = prenom.getText().toString();
                        nomSaisi = nom.getText().toString();
                        createAccount(emailSaisi, motDePasseSaisi);
                    }
                    else {

                        Toast.makeText(getApplicationContext(), "Les deux mot de passe ne correspondes pas", Toast.LENGTH_SHORT).show();

                    }

                }
                else {

                    Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    //Action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_connexion, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_connexion:

                Intent intent = new Intent(MainActivity.this, ConnexionActivity.class);
                startActivity(intent);

                // Comportement du bouton "A Propos"
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createAccount(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            onAuthSuccess(task.getResult().getUser());

                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }
                });
    }

    private void onAuthSuccess(final FirebaseUser user) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                writeNewUser(user.getUid(), nomSaisi, prenomSaisi, user.getEmail());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Votre compte a été créer", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, ConnexionActivity.class);
                        startActivity(intent);

                    }
                });
            }
        });
        thread.start();

    }

    private void writeNewUser(String userId, String nom, String prenom, String email) {
        User user = new User(nom, prenom, email);
        databaseReference.child("users").child(userId).setValue(user);
    }


}
