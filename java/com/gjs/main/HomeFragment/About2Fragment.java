package com.gjs.opentable.HomeFragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.gjs.opentable.Adapter.ReviewAdapter;
import com.gjs.opentable.Bean.ReviewBean;
import com.gjs.opentable.R;
import com.gjs.opentable.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class About2Fragment extends Fragment implements OnMapReadyCallback{

    RequestQueue requestQueue;
    StringRequest stringRequest;
    TextView txtaddress, txtphone, txtwebsite, txttiming;
    String url;
    private ArrayList<ReviewBean> reviewslist;
    ReviewAdapter adapter;
    ListView  listreview;
    GoogleMap mMap;
    SupportMapFragment mapFragment;

    public About2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        txtaddress = (TextView) view.findViewById(R.id.textViewresAddress);
        txtphone = (TextView) view.findViewById(R.id.textViewresPhone);
        txtwebsite = (TextView) view.findViewById(R.id.textViewresWebsite);
        txttiming = (TextView) view.findViewById(R.id.textViewresTiming);
        requestQueue = Volley.newRequestQueue(getActivity());
        listreview = (ListView) view.findViewById(R.id.reviewlistview);
       // Toast.makeText(getActivity(), " About", Toast.LENGTH_LONG).show();
        url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + Util.placeid + "&key=" + Util.API_KEY;

           mapFragment = ((SupportMapFragment) getChildFragmentManager()
                   .findFragmentById(R.id.map));
          mapFragment.getMapAsync(this);

         getrestaurantinfo();
        return view;
    }

    private void getrestaurantinfo() {

        stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url
                ,
                new com.android.volley.Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        // pd.dismiss();
                        try {
                            JSONObject jsonObj = new JSONObject(s);

                            JSONObject jsonObject = jsonObj.getJSONObject("result");
                            // name = jsonObject.getString("name");
                         //   Toast.makeText(getActivity(),"1" , Toast.LENGTH_LONG).show();


                            txtaddress.setText(Util.resaddress);
                            txtphone.setText(jsonObject.getString("formatted_phone_number"));

                           try{ txtwebsite.setText(jsonObject.getString("website"));}
                           catch(Exception e){
                               txtwebsite.setText("https://www."+Util.resname+".com");

                            }
                            try{
                                JSONObject opening_hours=jsonObject.getJSONObject("opening_hours");
                            txttiming.setText(opening_hours.getString("weekday_text"));}
                            catch(Exception e){
                            txttiming.setText("11:00 AM – 11:00 PM");
                            }
                          //  String weekday = "11:00 AM – 11:00 PM";

                            //  Toast.makeText(getActivity(),address+phone+website , Toast.LENGTH_LONG).show();
                            try {
                             //   JSONObject opening_ours=jsonObject.getJSONObject("opening_hours");
                                JSONArray reviews = jsonObject.getJSONArray("reviews");
                                reviewslist = new ArrayList<>();
                                for (int i = 0; i < reviews.length(); i++) {
                                    JSONObject c = reviews.getJSONObject(i);
                                    String author_name = c.getString("author_name");
                                    String text = c.getString("text");
                                    String time=c.getString("relative_time_description");
                                    Double personrating = c.getDouble("rating");
                                    //  Toast.makeText(getActivity(), " " + personrating, Toast.LENGTH_LONG).show();
                                    reviewslist.add(new ReviewBean(author_name, time,text, personrating));
                                    adapter = new ReviewAdapter(getActivity(), R.layout.listviewreview, reviewslist);
                                    listreview.setAdapter(adapter);

                                }

                            }catch(Exception e){}
                            try {
                                JSONObject geometry = jsonObject.getJSONObject("geometry");
                                JSONObject location = geometry.getJSONObject("location");
                                Util.reslatitude = location.getDouble("lat");
                                Util.reslogitide = location.getDouble("lng");
                            }
                            catch (Exception e
                                    ){}

                            } catch (Exception e) {
//                            pd.dismiss();
                            Toast.makeText(getActivity(), "Some JSON Parsing Error", Toast.LENGTH_LONG).show();

                        }
                    }
                },
                // failure
                new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), "Some Volley Error", Toast.LENGTH_LONG).show();

                    }
                }
        );
        //     Toast.makeText(MainActivity.this,"1" , Toast.LENGTH_LONG).show();
        // Execute the StringRequest
        requestQueue.add(stringRequest);
        //    Toast.makeText(MainActivity.this,"2" , Toast.LENGTH_LONG).show();


    }


   @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapFragment != null) {
            mapFragment = null;
            }
        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       // Toast.makeText(getActivity(), Util.reslatitude + "   " + Util.reslogitide+"  "+Util.resname, Toast.LENGTH_LONG).show();

        LatLng locations = new LatLng(Util.reslatitude,Util.reslogitide);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations, 13));

        mMap.addMarker(new MarkerOptions()
                .title(Util.resname)
                .position(locations));
    }
    @Override
    public void onPause() {
        super.onPause();

        getChildFragmentManager().beginTransaction()
                .remove(mapFragment)
                .commit();
    }
    }
