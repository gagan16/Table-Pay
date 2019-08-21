package com.gjs.opentable;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.gjs.opentable.Bean.PersonBean;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.password;

public class LoginActivity extends AppCompatActivity {
    EditText etxtemail,etxtpassword;
    Button btnlogin,btntoregister,btnresetpass;
    private FirebaseAuth auth;
    PersonBean personbean;
    ImageView ivIcon;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String text="There is an error";
    android.app.AlertDialog alert;
    RequestQueue requestQueue;
    StringRequest stringRequest;

    int i=0;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


  //      if (auth.getCurrentUser() != null) {
   //         startActivity(new Intent(LoginActivity.this, MainActivity.class));
   //        finish();
   //     }
        setContentView(R.layout.activity_login);
        ivIcon=(ImageView)findViewById(R.id.imageView2);
        ivIcon.setImageResource(R.drawable.unnamed);
        requestQueue = Volley.newRequestQueue(this);

        preferences = getSharedPreferences(Util.PREFS_NAME,MODE_PRIVATE);
        editor = preferences.edit();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                   // Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                  //  Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        Initialie();
    }

    private void Initialie() {
        auth = FirebaseAuth.getInstance();
        personbean=new PersonBean();

        etxtemail=(EditText)findViewById(R.id.editloginemail);
        etxtpassword=(EditText)findViewById(R.id.editloginpassword);
        btnlogin=(Button)findViewById(R.id.buttonlogin);
        btntoregister=(Button)findViewById(R.id.buttontoregister);
        btnresetpass=(Button)findViewById(R.id.btn_reset_password);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onlogin();
            }
        });
        btntoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                finish();
            }
        });
        btnresetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
                finish();
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }

    private void onlogin() {
        personbean.setPassword(etxtpassword.getText().toString().trim());

        personbean.setEmail(etxtemail.getText().toString().trim());

        if ((personbean.getEmail().isEmpty())&&(i==0)) {
            text = "Please enter emal id";
            i=1;
            showalertdialog();
        }
        if ((personbean.getPassword().isEmpty())&&(i==0)) {
            text = "Please enter password";
            i=1;
            showalertdialog();
        }
        if ((!isEmailValid(personbean.getEmail()))&&(i==0)) {i=1;
            //Toast.makeText(RegisterActivity.this,"12345",Toast.LENGTH_LONG).show();
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please check email id")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        if ((personbean.getPassword().length() < 6)&&(i==0)) {
            text = "password should be more than 6 letter";
            i=1;
            showalertdialog();
        }
        if(i==0){
            allow();
        }
    }
        private void allow() {
/*
            auth.signInWithEmailAndPassword(personbean.getEmail(), personbean.getPassword())
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            //  progressBar.setVisibility(View.GONE);
                            if (!task.isSuccessful()) {
                                // there was an error
                                if (personbean.getPassword().length() < 6) {
                                    etxtpassword.setError(getString(R.string.minimum_password));
                                } else {
                                    editor.putString(Util.KEY_EMAIL, personbean.getEmail());

                                    editor.commit();
                                    // Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            } else {

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                finish();
                            }
                        }
                    });*/
            stringRequest = new StringRequest(Request.Method.POST,"http://gjsblog.esy.es/login.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int success = jsonObject.getInt("success");
                                String message = jsonObject.getString("message");
                                if(success == 1) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();

                                }


                                editor.putString(Util.KEY_EMAIL,personbean.getEmail());


                                editor.commit();
                                //   Toast.makeText(SignUpActivity.this,message,Toast.LENGTH_LONG).show();

                            }catch (Exception e){
                                Toast.makeText(LoginActivity.this,"Some Error",Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(LoginActivity.this,"Some Error",Toast.LENGTH_LONG).show();
                        }
                    }
            )

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<>();

                    map.put("email",personbean.getEmail());

                    map.put("password",personbean.getPassword());

                    return map;
                }
            }
            ;

            // Now the request shall be executed
            requestQueue.add(stringRequest);



        }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }
    public void showalertdialog(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(text)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        // alert.dismiss();
                        if (alert != null && alert.isShowing()) {
                            alert.dismiss();
                        }
                    }
                });

        alert = builder.create();
        alert.show();

    }




    @Override
    protected void onDestroy() {
        try {
            if (alert != null && alert.isShowing()) {
                alert.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
