package com.example.s1.login_;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class choosingSubjects extends AppCompatActivity {
    private LinearLayout mLayout;
    private EditText mEditText;
    private Button mButton;
    private DatabaseReference mRef;
    private DatabaseReference SGRef;
    private DatabaseReference mRootRef;
    private String email;
    private TextView textView;
    public String subject;
    private FirebaseAuth auth;
    private String Uid;
    private DatabaseReference mSubject;
    public static final String MESSAGE = "subject";
    public static final String STUDYGUIDE = "Study";
    private String getSubject;
    private ArrayList <Button> listSubjects;
    private DataSnapshot emailSnapshot;
    private FirebaseUser user;

    public choosingSubjects(){

    }
    public choosingSubjects(String subject){
        subject = mEditText.getText().toString();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_choosing_subjects);

        listSubjects = new ArrayList<Button>();
        textView = (TextView) findViewById(R.id.textView);
        mLayout = (LinearLayout) findViewById(R.id.mLayout);
        mEditText = (EditText) findViewById(R.id.editText);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(onClick());


        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mSubject = mRootRef.child(Uid);
        mRef = FirebaseDatabase.getInstance().getReference(Uid);


        mSubject.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(!(snapshot.getKey().equals("email"))){
                        final Button btn = createNewTextView(snapshot.getKey());
                        mLayout.addView(btn);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(choosingSubjects.this, notes2.class);
                                intent.putExtra(MESSAGE, btn.getText().toString());
                                intent.putExtra(STUDYGUIDE, "Study Guide");
                                startActivity(intent);
                            }
                        });
                    }}
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
    @Override
    protected void onStart(){
        super.onStart();

       mButton.setOnClickListener(onClick());


    }


  //creates new button with newly created button
    private View.OnClickListener onClick() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                subject = mEditText.getText().toString();
                Button btn = (createNewTextView(subject));
                mLayout.addView(btn);
                mRef.child(subject).setValue(subject);
                SGRef = FirebaseDatabase.getInstance().getReference(Uid).child(subject);
                //NoteTemplate SG = new NoteTemplate("Study Guide", "");
                SGRef.child("Study Guide").child("Note").setValue("");
                btn.setOnClickListener(addbtnOnClick());
            }
    };
    }

    private View.OnClickListener addbtnOnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(choosingSubjects.this, notes2.class);
                i.putExtra(MESSAGE, subject);
                startActivity(i);
    }};}

    private Button createNewTextView(String d) {
        Button button = new Button(this);
        button.setText(d);
        LinearLayout.LayoutParams lparam = (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        lparam.setMargins(15,15,15,15);
        button.setLayoutParams(lparam);
        button.setWidth(LinearLayoutCompat.LayoutParams.MATCH_PARENT);
        button.setBackgroundColor(Color.parseColor("#ffffff"));
        //button.setBackground(getDrawable(R.drawable.borderc));
        //GradientDrawable gd = new GradientDrawable();
        //gd.setCornerRadius(15.0f);
        return button;
    }

}
