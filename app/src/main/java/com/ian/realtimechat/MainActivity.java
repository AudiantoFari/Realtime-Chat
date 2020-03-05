package com.ian.realtimechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity  {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    RecyclerView lstContact;

    private ArrayList<Chat> contact = new ArrayList<>();

    public static final String EXTRA_UID = "extra_uid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String uid = getIntent().getStringExtra(EXTRA_UID);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference mRef = mDatabase.child("User").child(uid);
        mAuth = FirebaseAuth.getInstance();

        lstContact = findViewById(R.id.lstContact);
        lstContact.setHasFixedSize(true);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                Log.d("LOGIN", uid + ": " + user.getEmail() + " / " + user.getUsername());
                String username = user.getUsername();
                String email = user.getEmail();
                System.out.println(email + " / " + username);

                getSupportActionBar().setTitle(username);

                for (DataSnapshot ns: dataSnapshot.getChildren()) {
                    Chat chat = ns.getValue(Chat.class);
                    contact.add(chat);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Refresh", "loadNote:onCancelled", databaseError.toException());
            }
        });

        showChatList();

    }

    private void showChatList() {
        lstContact.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        setMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    private void setMode(int itemId) {
        switch (itemId){
            case R.id.btnLogout:
                mAuth.signOut();
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
        }
    }
}
