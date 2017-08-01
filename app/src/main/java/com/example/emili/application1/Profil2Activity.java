package com.example.emili.application1;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.emili.application1.Adapter.MessageAdapter;
import com.example.emili.application1.Donnee.Message;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Profil2Activity extends AppCompatActivity {

    private static final String ANONYMOUS = "anonyme";
    private static final String TAG = "Profil2Activity";
    private static final int RC_PHOTO_PICKER = 2;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    public static final int RC_SIGN_IN = 1;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference messagePhoto;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button envoyer;
    private EditText message_edit;
    private ImageView image_button;
    String username = "anonyme";
    boolean cliquer = false;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private String nom;
    private ChildEventListener childEventListener;
    final List<Message> mes_messages = new ArrayList<Message>();


    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil2);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        messagePhoto = firebaseStorage.getReference().child("chat_photos");
        databaseReference = firebaseDatabase.getReference().child("messages");
        envoyer = (Button) findViewById(R.id.envoyer_message);
        message_edit = (EditText) findViewById(R.id.message_chat);
        image_button = (ImageView) findViewById(R.id.image_button);
        nom = username;
        recyclerView = (RecyclerView) findViewById(R.id.chat_groupe);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        firebaseAuth = FirebaseAuth.getInstance();

        messageAdapter = new MessageAdapter(Profil2Activity.this, new ArrayList<Message>());

        message_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.toString().trim().length() > 0){
                    envoyer.setEnabled(true);
                }
                else {
                    envoyer.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        message_edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)
        });

        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //le bouton a été cliqué
                cliquer = true;
                Message mon_message = new Message(nom, message_edit.getText().toString(), null);
                databaseReference.push().setValue(mon_message);
                Toast.makeText(getApplicationContext(), "Le message a été envoyé", Toast.LENGTH_LONG).show();
                message_edit.setText("");
            }
        });

        image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){

                    //il est connecté

                    Toast.makeText(Profil2Activity.this, "You're now signed in. Welcome to FriendlyChat.", Toast.LENGTH_SHORT).show();
                    onSignedInInitialize(user.getDisplayName());
                }else {
                    onSignedOutCleanup();

                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                            new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                    );

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                }

                if (authStateListener != null) {
                    firebaseAuth.removeAuthStateListener(authStateListener);
                }
                //messageAdapter.clear();
                       //detachDatabaseReadListener();
            }
        };

    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.v(TAG, "OnResume failed");
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    private void onSignedInInitialize(String username) {
         this.username = username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        this.username = ANONYMOUS;
        messageAdapter.clear();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
             @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                 Message message = dataSnapshot.getValue(Message.class);
                 mes_messages.add(message);

             }

              public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
              public void onChildRemoved(DataSnapshot dataSnapshot) {}
              public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
              public void onCancelled(DatabaseError databaseError) {}
            };
            databaseReference.addChildEventListener(childEventListener);
            messageAdapter.ajouterTous(mes_messages);
            recyclerView.setAdapter(messageAdapter);

            Log.v(TAG, "detache database");
        }
    }
    private void detachDatabaseReadListener() {
        if (childEventListener != null) {
                 databaseReference.removeEventListener(childEventListener);
                childEventListener = null;
        }
    }

    protected void onPause(){
        super.onPause();
        if(authStateListener != null){

            firebaseAuth.removeAuthStateListener(authStateListener);
        }

        detachDatabaseReadListener();
        messageAdapter.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
          super.onActivityResult(requestCode, resultCode, data);
           if (requestCode == RC_SIGN_IN) {
               if (resultCode == RESULT_OK) {
                   // Sign-in succeeded, set up the UI
                   Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
               } else if (resultCode == RESULT_CANCELED) {
                   // Sign in was canceled by the user, finish the activity
                    Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                   finish();
               }

           } else if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
               // Sign in was canceled by the user, finish the activity
               Uri selectImageUri = data.getData();
               //Obtenir la reference pour stocker le fichier dans chat_photo/nomDuFichier
               StorageReference photoRef = messagePhoto.child(selectImageUri.getLastPathSegment());
               photoRef.putFile(selectImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       Uri downloadUrl = taskSnapshot.getDownloadUrl();


                       message_edit.setText(downloadUrl.toString().substring(0, 10));

                       if(cliquer){

                           Message message = new Message(null, username, downloadUrl.toString());
                           databaseReference.push().setValue(message);
                           message_edit.setText("");
                           Toast.makeText(getApplicationContext(), "Le fichier a été envoyé", Toast.LENGTH_LONG).show();
                       }

                   }
               });
           }

    }

    //Action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_deconnexion:

                AuthUI.getInstance().signOut(this);
                return true;

            case R.id.action_profil:
                Intent intent = new Intent(Profil2Activity.this, UserActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
