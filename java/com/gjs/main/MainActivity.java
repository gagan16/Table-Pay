package com.gjs.opentable;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gjs.opentable.Adapter.Restaurentadapter;
import com.gjs.opentable.Bean.PhotosBean;
import com.gjs.opentable.Bean.RestaurentBean;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements LocationListener, AdapterView.OnItemClickListener{

    LocationManager locationManager;
    Place place;
    private FirebaseAuth auth;

    private ArrayList<RestaurentBean> restaurantlist;
    RestaurentBean restaurentBean;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    ListView listview;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Restaurentadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        setTitle("Ludhiana");
        preferences = getSharedPreferences(Util.PREFS_NAME,MODE_PRIVATE);
        editor = preferences.edit();
        initialize();
        location();


    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initialize() {
        restaurentBean = new RestaurentBean();
        listview = (ListView) findViewById(R.id.mylistview);
        requestQueue = Volley.newRequestQueue(this);
    }

    private void location() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Please grant permissions in Settings", Toast.LENGTH_LONG).show();
            } else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 5, (LocationListener) this);
                Location location = (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
                if (location != null) {
                     //   Toast.makeText(MainActivity.this, "location " + location, Toast.LENGTH_LONG).show();
                    Util.latitude = location.getLatitude();
                    Util.longitide = location.getLongitude();

                    getRestaurant();

                }

            }
        } else {
            Toast.makeText(this, "Please enable Location in Settings", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(i);
        }

    }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.mymenu, menu);
            return super.onCreateOptionsMenu(menu);
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.app_bar_search: {
                item.setTitle(Util.searchbox);
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                            .build();
                    // Toast.makeText(MainActivity.this,"  mnu ", Toast.LENGTH_LONG).show();
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .setFilter(typeFilter)
                                    .build(this);
                    startActivityForResult(intent, 1);

                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
            case R.id.logout: {
                auth.signOut();
                editor.clear();
                editor.commit();


// this listener will be called when there is change in firebase user session
                FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user == null) {
                            // user auth state is changed - user is null
                            // launch login activity
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        editor.clear();
                        finish();
                    }
                };

            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                place = PlaceAutocomplete.getPlace(this, data);

              //  Toast.makeText(MainActivity.this, place.getName(), Toast.LENGTH_LONG).show();

                getLocationFromAddress(this, (String) place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                //  Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode != 1) {

        }
        // getLocationFromAddress(this, (String) place.getName());

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }


    @Override
    public void onProviderDisabled(String provider) {

    }

    //display restaurants
    private void getRestaurant() {
      //  Toast.makeText(MainActivity.this,Util.latitude+"  2312 "+Util.longitide, Toast.LENGTH_LONG).show();

        stringRequest = new StringRequest(com.android.volley.Request.Method.GET, "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + Util.latitude + "," + Util.longitide + "&radius=5000&type=restaurant&key=" + Util.API_KEY,
                new com.android.volley.Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);

                            Gson gson = new Gson();
                            JSONArray jsonArray = jsonObject.getJSONArray("results");

                            Type listType = new TypeToken<List<RestaurentBean>>() {
                            }.getType();
                            restaurantlist = (ArrayList<RestaurentBean>) gson.fromJson(jsonArray.toString(), listType);

                            adapter = new Restaurentadapter(MainActivity.this, R.layout.listviewmain, restaurantlist);
                        //     Toast.makeText(MainActivity.this, "1 ", Toast.LENGTH_LONG).show();
                            listview.setAdapter(adapter);
                           //    Toast.makeText(MainActivity.this, "2 ", Toast.LENGTH_LONG).show();
                            listview.setOnItemClickListener(MainActivity.this) ;
                           //    Toast.makeText(MainActivity.this, " 1 ", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                            Toast.makeText(MainActivity.this, "Some JSON Parsing Error", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                // failure
                new com.android.volley.Response.ErrorListener() {
                    //  new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MainActivity.this, "Some Volley Error", Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(stringRequest);

    }
    public void getLocationFromAddress(Context context, String strAddress) {
       setTitle(strAddress);
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
            }
            Address location = address.get(0);
            Util.latitude = location.getLatitude();
            Util.longitide = location.getLongitude();
            Util.reslatitude=Util.latitude;
            Util.reslogitide=Util.longitide;

            adapter.clearData();
            adapter.notifyDataSetChanged();
              //   Toast.makeText(MainActivity.this, Util.latitude + "  " + Util.longitide, Toast.LENGTH_LONG).show();
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
            getRestaurant();
        } catch (Exception ex) {

            ex.printStackTrace();
        }


    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
     //   Toast.makeText(MainActivity.this, "a ", Toast.LENGTH_LONG).show();

        RestaurentBean p=restaurantlist.get(position);
        try{ PhotosBean p1 = p.photos.get(0);
            // Util.photo_reference=p1.getPhoto_reference();
            Util.photo="https://maps.googleapis.com/maps/api/place/photo?maxwidth=70&photoreference=" + p1.getPhoto_reference() + "&key=AIzaSyDGtQbIzeOGRk0kGq3rCy3xAvo1xVNB304";
        }
        catch (Exception e){}
        Util.placeid=p.getPlaceid();
        Util.resaddress=p.getVicinity();
     // Util.reslatitude=p.geometry.locationBean.getLat();
      //   Util.reslogitide=p.geometry.locationBean.getLng();
        Util.resRating=p.getRating();
        Util.resname=p.getName();
        // PhotosBean p1 = p.photos.get(0);

       // Toast.makeText(MainActivity.this, "b ", Toast.LENGTH_LONG).show();

           // Intent i=new Intent(MainActivity.this,HomeActivity.class);

        startActivity(new Intent(MainActivity.this,HomeActivity.class));

    }

}
