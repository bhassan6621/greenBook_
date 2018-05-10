package com.example.s1.login_;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by bibi on 3/7/2018.
 */

public class pop extends Activity {
    FloatingActionButton send;
    EditText ETsendToUser;
    String sendToUser;
    private DatabaseReference mRef;
    String passingTitle, passingNote,passingSubject;
    DatabaseReference userRef;
    public String UserUid;


    @Override
    protected void onCreate(Bundle savedInstanceState){
     super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        ETsendToUser = (EditText) findViewById(R.id.sendToUser);


        Intent i = getIntent();
        passingTitle = i.getStringExtra(updateDelete.passingtitle);
        passingNote = i.getStringExtra(updateDelete.passingnote);
        passingSubject = i.getStringExtra(updateDelete.passingSubject);

        mRef = FirebaseDatabase.getInstance().getReference();

        send = (FloatingActionButton) findViewById(R.id.fab_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToUser = ETsendToUser.getText().toString();
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot d: dataSnapshot.getChildren()) {
                            if (d.child("email").getValue().toString().equals(sendToUser)){
                                UserUid = d.getKey();
                            }

                        }
                        userRef = FirebaseDatabase.getInstance().getReference(UserUid).child(passingSubject);
                        NoteTemplate n = new NoteTemplate(passingTitle,passingNote);
                        userRef.push().setValue(n);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                });
            }
        });


    }
}
