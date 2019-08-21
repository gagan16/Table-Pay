package com.gjs.opentable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.gjs.opentable.Adapter.BillAdapter;
import com.gjs.opentable.Bean.TableBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QrActivity extends AppCompatActivity {

    IntentIntegrator integrator;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    ListView listviewtable;
    private ArrayList<TableBean> tablelist;
    private BillAdapter adapter;
    TextView txttotalamount,txttotalquantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        listviewtable=(ListView)findViewById(R.id.billlistview);
        requestQueue = Volley.newRequestQueue(this);
        txttotalamount=(TextView)findViewById(R.id.textViewtotalamount);
       txttotalquantity=(TextView)findViewById(R.id.textViewtotalquantity);

        integrator= new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {

            if (result.getContents() == null) {
                Toast.makeText(QrActivity.this,"can not recognize qr code",Toast.LENGTH_LONG).show();
                finish();
                //not able to cconnect
            } else {
              //  Toast.makeText(QrActivity.this,result.getContents(),Toast.LENGTH_LONG).show();
                Util.tablename=result.getContents().toString().trim();

                seeBill();


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void seeBill() {
     //   Toast.makeText(QrActivity.this, "1", Toast.LENGTH_LONG).show();

        stringRequest = new StringRequest(Request.Method.POST, "http://gjsblog.esy.es/table.php",

                // success
                new Response
                        .Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject jsonObj = new JSONObject(s);
                            Gson gson = new Gson();
                          //  Toast.makeText(QrActivity.this, "1", Toast.LENGTH_LONG).show();
                            JSONArray jsonArray = jsonObj.getJSONArray(Util.tablename);


                          Type listType = new TypeToken<List<TableBean>>() {
                          }.getType();
                            tablelist = (ArrayList<TableBean>) gson.fromJson(jsonArray.toString(), listType);

                           adapter =new BillAdapter(QrActivity.this,R.layout.listviewtable,tablelist);
                     //       Toast.makeText(QrActivity.this, "1 ", Toast.LENGTH_LONG).show();

                           listviewtable.setAdapter(adapter);
                            txttotalquantity.setText(String.valueOf(Util.quantity));
                            txttotalamount.setText(String.valueOf(Util.total));
                        } catch (JSONException e) {
                          //  e.printStackTrace();
                            Toast.makeText(QrActivity.this, "Some JSON Parsing Error", Toast.LENGTH_LONG).show();

                        }
                    }

                },

                // failure
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // Toast.makeText(AllPersonsActivity.this,"Some Volley Error", Toast.LENGTH_LONG).show();
                        Log.i("AllPersonsActivity", volleyError.toString());
                        Log.i("AllPersonsActivity",volleyError.getMessage());
                    }
                })

        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

              map.put("name",Util.tablename);

                return map;
            }

        };



        requestQueue.add(stringRequest);

    }
}
