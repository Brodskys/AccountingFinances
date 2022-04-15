package com.example.limb;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.io.IOException;
import java.util.List;
public class Map extends FragmentActivity implements OnMapReadyCallback {
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng userLatLong;
    GoogleMap mMap;
    SupportMapFragment mapFragment;
    SearchView searchView;
    View mapView;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private Location mLastKnownLocation;
    private final float DEFAULT_ZOOM = 15;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        imageView = (ImageView)findViewById(R.id.imageView21);
        imageView.setVisibility(View.GONE);
        searchView = findViewById(R.id.sv_location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapView = mapFragment.getView();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Map.this);
        Places.initialize(Map.this, getString(R.string.map_key));
        placesClient = Places.createClient(this);
        mapFragment.getMapAsync(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    mMap.clear();
                    Geocoder geocoder = new Geocoder(Map.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace(); }
                    try {
                        Address address = addressList.get(0);
                        userLatLong = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(userLatLong).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLong, 17));
                        imageView.setVisibility(View.VISIBLE);
                    } catch (Throwable e) {
                        e.printStackTrace(); } }
                return false; }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }});
        mapFragment.getMapAsync(this); }
    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        searchView = findViewById(R.id.sv_location);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0); }
        return super.dispatchTouchEvent(event); }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) { }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override
            public void onProviderEnabled(String provider) { }
            @Override
            public void onProviderDisabled(String provider) { }};
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180); }
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient settingsClient = LocationServices.getSettingsClient(Map.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(Map.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation(); }});
        askLocation(); }
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    mLastKnownLocation = task.getResult();
                    if (mLastKnownLocation != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM)); } } }}); }
    private void askLocation() {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return; }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener); }
            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) { }
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }}).check(); }
    public void onClick_Back(View v) {
        onBackPressed(); }
    public void Save(View v) {
        String data = searchView.getQuery().toString();
        SharedPreferences.Editor editor = getSharedPreferences("Locale", MODE_PRIVATE).edit();
        editor.putString("local", data);
        editor.apply();
        onBackPressed(); }}