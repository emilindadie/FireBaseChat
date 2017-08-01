package com.example.emili.application1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.emili.application1.Donnee.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ConnexionActivity extends AppCompatActivity {


    EditText email, password;
    SessionManager session;
    Button connexion;
    boolean succes;
    ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);


        email = (EditText) findViewById(R.id.email_connexion);
        password = (EditText) findViewById(R.id.password_connexion);
        connexion = (Button)findViewById(R.id.connexion);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        session = new SessionManager(getApplicationContext());
        auth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.INVISIBLE);


        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().length() > 0 && password.getText().toString().length() > 0){

                    String monEmail = email.getText().toString();
                    String monPassword = password.getText().toString();
                    connexion(monEmail, monPassword);
                }

                else {

                    Toast.makeText(ConnexionActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_inscription, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_inscription:

                Intent intent = new Intent(ConnexionActivity.this, MainActivity.class);
                startActivity(intent);

                // Comportement du bouton "A Propos"
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void connexion(String email, String password){

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(ConnexionActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        onVisibilityChange();
                        if (!task.isSuccessful()) {
                            // there was an error
                            onVisibilityChange();
                            Toast.makeText(ConnexionActivity.this, "Adresse ou mot de passe inconnu", Toast.LENGTH_LONG).show();

                        } else {
                            Intent intent = new Intent(ConnexionActivity.this, UserActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private void onVisibilityChange(){

        Thread getUiThread = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progressBar.getVisibility() ==  View.VISIBLE) {

                            progressBar.setVisibility(View.INVISIBLE);

                        }
                        else {
                            progressBar.setVisibility(View.VISIBLE);
                            email.setVisibility(View.INVISIBLE);
                            password.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });

        getUiThread.start();
    }
}
