package com.gjs.opentable;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText etxtresertemail;
    Button btnresetpassword;
    private FirebaseAuth auth;
    ImageView ivIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toast.makeText(ResetPasswordActivity.this,"  3 ",Toast.LENGTH_LONG).show();
        ivIcon=(ImageView)findViewById(R.id.imageView3);
        ivIcon.setImageResource(R.drawable.unnamed);
        auth = FirebaseAuth.getInstance();
        initialize();

    }


    private void initialize() {
        etxtresertemail=(EditText)findViewById(R.id.editresetemail);
        btnresetpassword=(Button)findViewById(R.id.buttonresetpassword);
        btnresetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetpassword();
            }
        });
    }

    private void resetpassword() {
       // String email=etxtresertemail.getText().toString().trim();
        auth.sendPasswordResetEmail(etxtresertemail.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }

                       // progressBar.setVisibility(View.GONE);
                    }
                });
    }
    }
