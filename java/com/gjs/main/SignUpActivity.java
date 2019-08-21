package com.gjs.opentable;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//import com.firebase.client.Firebase;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword,inputdob,inputmobile,inputname;
    private Button btnlogin, btnSignUp;
    PersonBean personbean;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    ImageView ivIcon;
    int day, month, year;
    android.app.AlertDialog alert;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    // Firebase firebase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ivIcon=(ImageView)findViewById(R.id.imageView);
        ivIcon.setImageResource(R.drawable.unnamed);
        requestQueue = Volley.newRequestQueue(this);
      //  Firebase.setAndroidContext(this);
        String a= FirebaseInstanceId.getInstance().getToken();
        preferences = getSharedPreferences(Util.PREFS_NAME,MODE_PRIVATE);
        editor = preferences.edit();
        initialize();
    }

    private void storeinfo() {
       // Toast.makeText(SignUpActivity.this, personbean.getEmail()+personbean.getPassword(), Toast.LENGTH_SHORT).show();

        auth.createUserWithEmailAndPassword(personbean.getEmail(),personbean.getPassword())
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        // progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
       stringRequest = new StringRequest(Request.Method.POST,"http://gjsblog.esy.es/register.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int success = jsonObject.getInt("success");
                            String message = jsonObject.getString("message");
                            editor.putString(Util.KEY_NAME,personbean.getName());
                            editor.putString(Util.KEY_EMAIL,personbean.getEmail());
                            editor.putString(Util.KEY_PHONE, personbean.getMobile());

                            editor.commit();
                            //   Toast.makeText(SignUpActivity.this,message,Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                        }catch (Exception e){
                            Toast.makeText(SignUpActivity.this,"Some Error",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SignUpActivity.this,"Some Error",Toast.LENGTH_LONG).show();
                    }
                }
        )

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("name",personbean.getName());
                map.put("mobile",personbean.getMobile());
                map.put("email",personbean.getEmail());
                map.put("dob",personbean.getDob());
                map.put("password",personbean.getPassword());

                return map;
            }
        }
        ;

        // Now the request shall be executed
        requestQueue.add(stringRequest);
    }



    private void initialize() {
        auth = FirebaseAuth.getInstance();

        personbean=new PersonBean();
        inputname=(EditText)findViewById(R.id.editname);
        inputEmail=(EditText)findViewById(R.id.editemail);
        inputPassword=(EditText)findViewById(R.id.editPassword);
        inputdob=(EditText)findViewById(R.id.editdob);
        inputmobile=(EditText)findViewById(R.id.editmobile);
        btnSignUp = (Button) findViewById(R.id.buttonRegister);
        btnlogin=(Button)findViewById(R.id.buttonlogin);
        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        inputdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SignUpActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                inputdob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, year, month, day);
                datePickerDialog.show();

            }
        });



        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnregister();

            }
        });


    }
    public void btnregister() {

        personbean.setName(inputname.getText().toString().trim());
        personbean.setEmail(inputEmail.getText().toString().trim());
        personbean.setDob(inputdob.getText().toString().trim());
        personbean.setMobile(inputmobile.getText().toString().trim());
        personbean.setPassword(inputPassword.getText().toString().trim());

        if (personbean.validatePerson().equals("abc") ) {
            if (isEmailValid(personbean.getEmail())) {

                if (Util.isNetworkConnected(this)) {
                    //   Toast.makeText(RegisterActivity.this," 1 ",Toast.LENGTH_LONG).show();

                    storeinfo();

                } else {
                    Toast.makeText(this, "Please check your connectivity", Toast.LENGTH_LONG).show();
                }


            } else if (!isEmailValid(personbean.getEmail())) {
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
        }else {
            showalertdialog();

        }

    }

        boolean isEmailValid(CharSequence email) {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches();
        }
        public void showalertdialog(){
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setMessage(personbean.validatePerson())
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

