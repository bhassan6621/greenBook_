package com.example.s1.login_;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.s1.login_.R.id.fab;

public class updateDelete extends AppCompatActivity {
    String pushid;
    DatabaseReference mRef;
    String Uid;
    String subject;
    FloatingActionButton delete;
    FloatingActionButton update;
    FloatingActionButton send;
    Button back;
    public final static String passingtitle = "TITLE";
    public final static String passingnote = "NOTE";
    public final static String passingSubject = "SUBJECT";
    public static int x = 0;
    String title, note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_update_delete);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        pushid = intent.getStringExtra(AllNotes.MESSAGE);
        subject = intent.getStringExtra(AllNotes.SUBJECT);
        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        mRef = FirebaseDatabase.getInstance().getReference().child(Uid).child(subject);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    if(d.getKey().equals(pushid)){
                        EditText title = (EditText) findViewById(R.id.title);
                        EditText note = (EditText) findViewById(R.id.notespart);
                        title.setText(d.child("title").getValue().toString());
                        note.setText(d.child("note").getValue().toString());
                        break;
                    }
                    else if(d.getKey().equals("Study Guide")) {
                        EditText title = (EditText) findViewById(R.id.title);
                        EditText note = (EditText) findViewById(R.id.notespart);
                        title.setText(d.getKey());
                        note.setText(d.child("Note").getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        update = (FloatingActionButton) findViewById(R.id.fabUpdate);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot d: dataSnapshot.getChildren()){
                            if(d.getKey().equals(pushid)){
                                EditText ETtitle = (EditText) findViewById(R.id.title);
                                EditText ETnote = (EditText) findViewById(R.id.notespart);
                                String title = ETtitle.getText().toString();
                                String note = ETnote.getText().toString();
                                NoteTemplate n = new NoteTemplate(title,note);
                                mRef.child(pushid).setValue(n);
                            }
                            else if(d.getKey().equals("Study Guide")){
                                EditText ETtitle = (EditText) findViewById(R.id.title);
                                EditText ETnote = (EditText) findViewById(R.id.notespart);
                                String title = ETtitle.getText().toString();
                                String note = ETnote.getText().toString();
                                NoteTemplate n = new NoteTemplate(title,note);
                                mRef.child(d.getKey()).child("Note").setValue(note);
                            }

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
            }
        });

        delete = (FloatingActionButton) findViewById(R.id.fabDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot d: dataSnapshot.getChildren()){
                            if(d.getKey().equals(pushid)){
                                EditText ETtitle = (EditText) findViewById(R.id.title);
                                EditText ETnote = (EditText) findViewById(R.id.notespart);
                                String title = ETtitle.getText().toString();
                                String note = ETnote.getText().toString();
                                NoteTemplate n = new NoteTemplate(title,note);
                                mRef.child(pushid).removeValue();
                            }
                            else if(d.getKey().equals("Study Guide")){
                                EditText ETtitle = (EditText) findViewById(R.id.title);
                                EditText ETnote = (EditText) findViewById(R.id.notespart);
                                String title = ETtitle.getText().toString();
                                String note = ETnote.getText().toString();
                                NoteTemplate n = new NoteTemplate(title,note);
                                mRef.child(d.getKey()).removeValue();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
            }
        });

        send = (FloatingActionButton) findViewById(R.id.sendNote);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot d: dataSnapshot.getChildren()){
                            if(d.getKey().equals(pushid)){
                                title = d.child("title").getValue().toString();
                                note = d.child("note").getValue().toString();
                                Intent i = new Intent(updateDelete.this, pop.class);
                                i.putExtra(passingtitle,title);
                                i.putExtra(passingnote,note);
                                i.putExtra(passingSubject,subject);
                                startActivity(i);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });

            }
        });

        back = (Button) findViewById(R.id.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
