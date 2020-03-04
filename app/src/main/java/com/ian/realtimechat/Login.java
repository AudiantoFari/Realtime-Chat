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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    Button btnLogin;
    EditText txtEmail, txtPassword;
    TextView tvRegister;
    ProgressDialog pDialog;

    private static final String TAG = Login.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        tvRegister = findViewById(R.id.tvRegister);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.getCurrentUser();
    }

    @Override
    public void onClick(View v) {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        switch (v.getId()) {
            case R.id.btnLogin :
                login(email, password);
                break;
            case R.id.tvRegister :
                Intent intent = new Intent(this, Register.class);
                startActivity(intent);
                break;
        }
    }

    private void login(String email, String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in...");
        pDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if  (task.isSuccessful()) {
                            Log.d(TAG, "SignIn With Email Success");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            pDialog.dismiss();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.putExtra(MainActivity.EXTRA_UID, user.getUid());
                            startActivity(intent);
                        } else {
                            Log.e(TAG, "SignIn With Email Failed");
                            Toast.makeText(getApplicationContext(), "Gagal Login", Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }
                    }
                });
    }
}
