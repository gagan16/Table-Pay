package com.gjs.opentable.HomeFragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gjs.opentable.R;
import com.gjs.opentable.Util;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakeFragment extends Fragment {

    Spinner table_for;
    EditText date, time;
    int day, month, year, hour, minute;
    Button btmake;

    RequestQueue requestQueue;
    StringRequest stringRequest;

    public MakeFragment() {
        // Required empty public constructor

    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_make, container, false);

        table_for = (Spinner) view.findViewById(R.id.spinner);
        date = (EditText) view.findViewById(R.id.editTextdate);
        time = (EditText) view.findViewById(R.id.editTexttime);
        btmake=(Button)view.findViewById(R.id.buttonmakereservation);
       // date.setShowSoftInputOnFocus(false);
        init();
    btmake.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          requestQueue = Volley.newRequestQueue(getActivity());
            btnfunction();

    }
});
        return view;
    }

    private void btnfunction() {


        stringRequest = new StringRequest(Request.Method.POST, "http://gjsblog.esy.es/Reservation.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                        //    Toast.makeText(getActivity(), Util.placeid+"     "+date.getText().toString().trim()+"      "+table_for.getSelectedItem().toString()+"  "+time.getText().toString().trim(),Toast.LENGTH_LONG).show();
                            JSONObject jsonObject = new JSONObject(s);
                            int success = jsonObject.getInt("success");
                            String message = jsonObject.getString("message");

                           Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                            Toast.makeText(getActivity(),"Some Exception",Toast.LENGTH_LONG).show();
                        }finally {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(getActivity(),"VolleyError "+volleyError, Toast.LENGTH_LONG).show();
                        Log.i("Volley", volleyError.toString());
                        Log.i("Volley",volleyError.getMessage());
                    }
                }
        )

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();

                map.put("resid",Util.placeid.toString().trim());
                map.put("date",date.getText().toString().trim());
                map.put("people",table_for.getSelectedItem().toString());
                map.put("time",time.getText().toString().trim());
                return map;
            }
        };
        // Now the request shall be executed
        requestQueue.add(stringRequest);

    }

    private void init() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Table_for, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        table_for.setAdapter(adapter);

        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);

        date.setText(day + "-" + month + "-" + year);
        time.setText(hour + ":" + minute);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, year, month, day);
                datePickerDialog.show();
                //showDialog(DIALOG_ID);

            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                time.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });
    }
}
