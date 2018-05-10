package com.example.s1.login_;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.s1.login_.R.id.fab;
import static com.example.s1.login_.R.id.start;

public class notes2 extends AppCompatActivity {
    private EditText EditText1;
    private String note;
    private DatabaseReference mRef;
    private DatabaseReference SGRef;
    private FirebaseAuth auth;
    private String Uid;
    private String subject;
    private EditText editText;
    private String title;
    private Button button;
    private FloatingActionButton fab;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    public static final String MESSAGE = "subject";
    private String StudyGuide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText1 = (EditText) findViewById(R.id.EditText1);
        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent intent = getIntent();
        subject = intent.getStringExtra(choosingSubjects.MESSAGE);
        StudyGuide = intent.getStringExtra(choosingSubjects.STUDYGUIDE);



        mRef = FirebaseDatabase.getInstance().getReference(Uid).child(subject);
        editText = (EditText) findViewById(R.id.editText);
        title = editText.getText().toString();
        button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save();
            }
        });

        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notes2.this, AllNotes.class);
                intent.putExtra(MESSAGE, subject);
                startActivity(intent);
            }
        });

    }

    public void Save() {

        title = editText.getText().toString();
        note = EditText1.getText().toString();
        NoteTemplate n = new NoteTemplate(title, note);
        /*String id = mRef.push().toString();
        mRef.child(id).child("title").setValue(title);
        mRef.child(id).child("note").setValue(note); */
        mRef.push().setValue(n);

       if (note.contains("/*") && note.contains("*/")) {
           int start1 = note.indexOf("/*");
           int end1 = note.indexOf("*/");
           final String SGnote = note.substring(start1,end1);

           mRef.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   for(DataSnapshot d : dataSnapshot.getChildren()) {
                         if(d.getKey().equals("Study Guide")){
                             String oldn = d.child("Note").getValue().toString();
                             mRef.child(d.getKey()).child("Note").setValue(oldn + "\n" + SGnote);
                         }
                   }
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }

           });
       }
        }

    }




