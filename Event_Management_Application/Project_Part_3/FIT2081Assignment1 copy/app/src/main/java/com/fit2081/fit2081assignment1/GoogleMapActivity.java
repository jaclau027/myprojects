package com.fit2081.fit2081assignment1;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.fit2081.fit2081assignment1.databinding.ActivityGoogleMapBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityGoogleMapBinding binding;

    public String country;

    private SupportMapFragment mapFragment;

    private Geocoder geocoder;

    private String message;

    Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGoogleMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        country = getIntent().getExtras().getString("location", "Malaysia");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geocoder = new Geocoder(this, Locale.getDefault());



    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        googleMap.getUiSettings().setMapToolbarEnabled(true);

        // place latitude-longitude values in the order specified
        LatLng monashMsia = new LatLng(0, 0);

        // adds a marker to the specified latitude-longitude
        mMap.addMarker(new MarkerOptions().position(monashMsia).title("Malaysia"));

        // use moveCamera method to move current map viewing angle to Malaysia campus
        mMap.moveCamera(CameraUpdateFactory.newLatLng(monashMsia));

        findCountryMoveCamera();

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(@NonNull LatLng point) {

                //The results of getFromLocation are a best guess and are not guaranteed to be meaningful or correct.
                // It may be useful to call this method from a thread separate from your primary UI thread.
                geocoder.getFromLocation(point.latitude, point.longitude, 1, addresses -> {
                    Log.d("size", String.valueOf(addresses.size()));
                    if (addresses.size() == 0) {
                        message = "No Country at this location!! Sorry";
                        uiHandler.post(() -> {

                            //it is safe to Toast here (and do anything else UI related here)
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        });
                    } else {
                        android.location.Address address = addresses.get(0);
                        String selectedCountry = address.getCountryName();
                        message = "The selected country is " + selectedCountry;
                        uiHandler.post(() -> {

                            //it is safe to Toast here (and do anything else UI related here)
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        });
                    }
                });

            }
        });
    }

    private void findCountryMoveCamera() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        if (country.equals("")) {
            String message = "Category address not found";
            Snackbar.make(mapFragment.getView(), message, Snackbar.LENGTH_LONG).show();

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            /**
             * countryToFocus: String value, any string we want to search
             * maxResults: how many results to return if search was successful
             * successCallback method: if results are found, this method will be executed
             *                          runs in a background thread
             */
            geocoder.getFromLocationName(country, 1, addresses -> {

                // if there are results, this condition would return true
                if (!addresses.isEmpty()) {
                    // run on UI thread as the user interface will update once set map location
                    runOnUiThread(() -> {
                        // define new LatLng variable using the first address from list of addresses
                        LatLng newAddressLocation = new LatLng(
                                addresses.get(0).getLatitude(),
                                addresses.get(0).getLongitude()
                        );

                        // repositions the camera according to newAddressLocation
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(newAddressLocation));

                        // just for reference add a new Marker
                        mMap.addMarker(
                                new MarkerOptions()
                                        .position(newAddressLocation)
                                        .title(country)
                        );

                        // set zoom level to 8.5f or any number between range of 2.0 to 21.0
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(10f));
                    });

                    uiHandler.post(() -> {

                        //it is safe to Toast here (and do anything else UI related here)
                        Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();

                    });
                }
                else {
                    String message = "Category address not found";
                    Snackbar.make(mapFragment.getView(), message, Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }


}