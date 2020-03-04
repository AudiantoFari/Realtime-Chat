package com.ian.realtimechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText txtUsername, txtPassword, txtEmail, txtConfirmPassword;
    Button btnRegister;
    FirebaseAuth mAuth;
    ProgressDialog pDialog;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("User");

    private static final String TAG = Login.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtEmail = findViewById(R.id.txtEmail);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        mAuth = mAuth.getInstance();

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        String confirm = txtConfirmPassword.getText().toString();
        String email = txtEmail.getText().toString();

        switch (v.getId()) {
            case R.id.btnRegister :
                if (email.isEmpty()) {
                    txtEmail.setError("Field ini tidak boleh kosong");
                    txtEmail.requestFocus();
                } else if (password.isEmpty()) {
                    txtPassword.setError("Field ini tidak boleh kosong");
                    txtPassword.requestFocus();
                } else if (confirm.isEmpty()) {
                    txtConfirmPassword.setError("Field ini tidak boleh kosong");
                    txtConfirmPassword.requestFocus();
                } else if (confirm != null && !confirm.equals(password)) {
                    txtConfirmPassword.setError("Password tidak sama");
                    txtConfirmPassword.requestFocus();
                } else {
                    register(email, username, confirm);
                }
        }
    }

    private void register(final String email, final String username, String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Registering...");
        pDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(username, email);

                            FirebaseUser userId = mAuth.getCurrentUser();
                            myRef.child(userId.getUid()).setValue(user);

                            Log.d(TAG, "Register Success");
                            Toast.makeText(Register.this, "Register Success", Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                        } else {
                            Log.e(TAG,"Register Failed", task.getException());
                            Toast.makeText(Register.this, "Register Failed", Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }
                    }
                });
    }
}
