package com.ian.realtimechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    Button btnLogout;
    TextView tvUsername;

    public static final String EXTRA_UID = "extra_uid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String uid = getIntent().getStringExtra(EXTRA_UID);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference mRef = mDatabase.child("User").child(uid);
        mAuth = FirebaseAuth.getInstance();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                Log.d("LOGIN", uid + ": " + user.getEmail() + " / " + user.getUsername());
                String username = user.getUsername();
                String email = user.getEmail();
                System.out.println(email + " / " + username);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mRef.addListenerForSingleValueEvent(eventListener);

        tvUsername = findViewById(R.id.txtUsername);
        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                mAuth.signOut();
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
        }
    }
}
