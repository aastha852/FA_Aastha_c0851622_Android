package com.example.fa_aastha_c0851622_android;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.fa_aastha_c0851622_android.databinding.ActivityMapsBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private Marker currentLocation;
    List<Marker> locations = new ArrayList();

    DBHelper DB;

    FloatingActionButton favList;
    MaterialButton btnSatelite;
    MaterialButton btnHybrid;
    MaterialButton btnTerrian;

    SearchView searchView;

    boolean locationChanged=false;

    private static final int LOCATION_PERMISSION_CODE=1;


    protected void onCreate(Bundle savedInstanceState) {
        DB = new DBHelper(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        searchView = findViewById(R.id.idSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // on below line we are getting the
                // location name from search view.
                String location = searchView.getQuery().toString();

                // below line is to create a list of address
                // where we will store the list of all address.
                List<Address> addressList = null;

                // checking if the entered location is null or not.
                if (location != null || location.equals("")) {
                    // on below line we are creating and initializing a geo coder.
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        // on below line we are getting location from the
                        // location name and adding that location to address list.
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    System.out.println(addressList.get(0));


                    // on below line we are creating a variable for our location
                    // where we will add our locations latitude and longitude.
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    DB.insertuserdata(location,latLng.latitude,latLng.longitude);

                    // on below line we are adding marker to that position.
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));

//inserting user data


                    // below line is to animate camera to that position.
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    locationChanged=true;

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        // at last we calling our map fragment to update.
        mapFragment.getMapAsync(this);

        favList = findViewById(R.id.fabList);
        favList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
        btnSatelite = findViewById(R.id.button_satelite);
        btnHybrid = findViewById(R.id.button_hybrid);
        btnTerrian = findViewById(R.id.button_terrain);

//        fabList.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), PlaceList.class)));

        btnSatelite.setOnClickListener(view -> mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE));
        btnHybrid.setOnClickListener(view -> mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID));
        btnTerrian.setOnClickListener(view -> mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN));

    }

    LocationManager manager1;
    LocationListener listener1;

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        manager1 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener1 = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setHomeMarker(location);
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
        };

        if (!isLocationpermissionGranted()){
            requestLocationPermission();
        }else{
            startUpdateLocation();
        }





        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                setMarker(latLng);
            }
            private void setMarker(LatLng latLng) {
                MarkerOptions options = new MarkerOptions().position(latLng)
                        .title("Dropped Pin");

                locations.add(mMap.addMarker(options));
            }

        });

    }
    private boolean isLocationpermissionGranted(){
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
    }

    @SuppressLint("MissingPermission")
    private void startUpdateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager1.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,0,listener1);
        Location lastlocation= manager1.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        setHomeMarker(lastlocation);
    }
    private void setHomeMarker(Location location) {
        if (location == null) {
            System.out.println("***********LOCATION IS NULL***************");

        } else {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions options = new MarkerOptions().position(userLocation)
                    .title("You are here")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .snippet("Your Location");
            currentLocation = mMap.addMarker(options);
            System.out.println("Location Updated from setHomemarker");
            if (!locationChanged) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                System.out.println("Location not changed from setHomeMarker");
            }
        }
    }

}