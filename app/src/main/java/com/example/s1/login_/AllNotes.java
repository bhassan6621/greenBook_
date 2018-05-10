package com.example.s1.login_;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class AllNotes extends AppCompatActivity {
    DatabaseReference myRef;
    public String subject;
    private LinearLayout mLayout;
    private TextView textView;
    private String Uid;
    public static final String MESSAGE ="pushID";
    public static final String SUBJECT = "subject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_all_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLayout = (LinearLayout) findViewById(R.id.mLayout);

        Intent intent = getIntent();
        subject = intent.getStringExtra(notes2.MESSAGE);
        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef = FirebaseDatabase.getInstance().getReference().child(Uid).child(subject);
    }

    @Override
    protected void onStart(){
        super.onStart();
        mLayout.removeAllViews();
//populate screen with buttons of the titles of notes
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot d: dataSnapshot.getChildren()){
                if(!d.getKey().equals("Study Guide")){
                    final Button btn = createNewTextView(d.child("title").getValue().toString());
                    final String pushid = d.getKey();
                    mLayout.addView(btn);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(AllNotes.this, updateDelete.class);
                            intent.putExtra(MESSAGE, pushid);
                            intent.putExtra(SUBJECT, subject);
                            startActivity(intent);
                        }
                    });
                }
                else{
                    final Button btn = createNewTextView(d.getKey());
                    final String id = d.getKey();
                    mLayout.addView(btn);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(AllNotes.this, updateDelete.class);
                            intent.putExtra(SUBJECT, subject);
                            startActivity(intent);
                        }
                    });
                }

            }
        }
            @Override
            public void onCancelled(DatabaseError error) {

            }
            });

        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        Button back = new Button(this);
        back.setLayoutParams(lparams);
        back.setText("back");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLayout.addView(back);

        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams subparam = new LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
        layout.setLayoutParams(subparam);
        subparam.setMargins(3,10,0,15);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setTop(50);
        final EditText search_bar = new EditText(this);
        search_bar.setHint("  Enter Keyword");
        search_bar.setWidth(825);
        search_bar.setLayoutParams(subparam);
        Button search_btn = new Button(this);
        search_btn.setText("search");
        search_btn.setLayoutParams(subparam);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addListenerForSingleValueEvent((new ValueEventListener() {
                    String keyword = search_bar.getText().toString();
                    //ArrayList<String> searchWords = new ArrayList<String>();
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mLayout.removeAllViews();
                        final Button backbtn = addBackBtn();
                        mLayout.addView(backbtn);
                        backbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               finish();

                            }
                        });

                        for(DataSnapshot d: dataSnapshot.getChildren()){
                            if(!d.getKey().equals("Study Guide")){
                            if((d.child("title").getValue().toString().contains(keyword))||d.child("note").getValue().toString().contains(keyword)){
                                //searchWords.add(d.child("title").getValue().toString());
                               // mLayout.addView(createNewTextView(d.child("title").getValue().toString()));
                                final Button btn = createNewTextView(d.child("title").getValue().toString());
                                final String pushid = d.getKey();
                                mLayout.addView(btn);
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(AllNotes.this, updateDelete.class);
                                        intent.putExtra(MESSAGE, pushid);
                                        intent.putExtra(SUBJECT, subject);
                                        startActivity(intent);
                                    }
                                });
                            } }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }));
            }
        });

        layout.addView(search_bar);
        layout.addView(search_btn);
        mLayout.addView(layout);


        }
    public Button addBackBtn(){
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        Button backbtn = new Button(this);
        backbtn.setLayoutParams(lparams);
        backbtn.setText("back");
        return backbtn;
    }


    public Button createNewTextView(String d){
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
