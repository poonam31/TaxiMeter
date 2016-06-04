package com.example.shwetlana.project;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.animation.Interpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;

public class HomeMapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;
    private Button btnFindPath;
    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;


    private List<LatLng> animatemarkers = new ArrayList<>();
    final Interpolator interpolator = new AccelerateDecelerateInterpolator();

    private List<Marker> markers = new ArrayList<Marker>();
    private Marker selectedMarker;

    Handler handler = new Handler();
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        etOrigin = (EditText) findViewById(R.id.etOrigin);
        etDestination = (EditText) findViewById(R.id.etDestination);

        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

    }

    private void addDefaultLocations() {

        addMarkerToMap(new LatLng(34.078632, -118.105986));
        addMarkerToMap(new LatLng(34.080267, -118.105847));
        addMarkerToMap(new LatLng(34.080898, -118.108272));
        addMarkerToMap(new LatLng(34.080836, -118.103959));
        addMarkerToMap(new LatLng(34.079841, -118.104592));
    }

    public void addMarkerToMap(LatLng latLng) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
        markers.add(marker);

    }

    int currentPt;

    /**
     *
     * Callback that highlights the current marker and keeps animating to the next marker, providing a "next marker" is still available.
     * If we've reached the end-marker the animation stops.
     *
     */
    CancelableCallback simpleAnimationCancelableCallback =
            new CancelableCallback(){

                @Override
                public void onCancel() {
                }

                @Override
                public void onFinish() {

                    if(++currentPt < markers.size()){
                    LatLng targetLatLng = markers.get(currentPt).getPosition();

                        CameraPosition cameraPosition =
                                new CameraPosition.Builder()
                                        .target(targetLatLng)
                                        .tilt(currentPt<markers.size()-1 ? 90 : 0)
                                                //.bearing((float)heading)
                                        .zoom(mMap.getCameraPosition().zoom)
                                        .build();


                        mMap.animateCamera(
                                CameraUpdateFactory.newCameraPosition(cameraPosition),
                                3000,
                                simpleAnimationCancelableCallback);

                        highLightMarker(currentPt);

                    }
                }
            };

    private void sendRequest() {
        /*-----------------------------------Changes Implemented for GetOrigin-----------*/
        //String origin = mMap.getMyLocation().toString();
        String origin = etOrigin.getText().toString();
        String destination = etDestination.getText().toString();

        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            if(mMap != null){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(false);
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);

        //addDefaultLocations();
        //startAnimation();
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
           /* ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);*/
            /*Log.i("------------------------>>",route.startLocation.toString());
            Log.i("------------------------>>",route.endLocation.toString());*/

            /*originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));*/

           /* PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).*//*
                    color(Color.BLUE).*//*
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));*/
            //addMarkerToMap(new LatLng(34.06156,-118.17325));

            for (int i = 0; i < route.points.size(); i++){

                //addMarkerToMap(route.points.get(i));
                //Marker marker = mMap.addMarker(new MarkerOptions().position(route.points.get(i)));
                animatemarkers.add(route.points.get(i));

            }
            MarkerOptions a = new MarkerOptions()
                    .position(animatemarkers.get(0));
            Marker m = mMap.addMarker(a);

            for (int i = 1; i < animatemarkers.size(); i++) {

                animateMarker(m, animatemarkers.get(i), true);

            }

        }
    }



    private void startAnimation() {

        MarkerOptions a = new MarkerOptions()
                .position(animatemarkers.get(0));
        Marker m = mMap.addMarker(a);
        for (int i = 0; i < animatemarkers.size(); i++) {

            m.setPosition(animatemarkers.get(i));
        }
       /* MarkerOptions a = new MarkerOptions()
                .position(animatemarkers.get(0));
        for (int i = 0; i < animatemarkers.size(); i++) {

            Marker m = mMap.addMarker().setPosition(animatemarkers.get(i));
        }*/
       /* mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(animatemarkers.get(0).getPosition(), 16),
                5000,
                simpleAnimationCancelableCallback);

        currentPt = 0 - 1;*/
    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        for(int j=0;j<1000000;j++){

        }
        marker.setPosition(toPosition);
        handler.post(new Runnable() {
            @Override
            public void run() {
                marker.setPosition(toPosition);
                marker.setVisible(true);
                handler.postDelayed(this, 2500);
                Log.i("--???",marker.toString());

            }
        });
    }

    private void highLightMarker(int index) {
        highLightMarker(markers.get(index));
    }
    private void highLightMarker(Marker marker) {
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        marker.showInfoWindow();
        //Utils.bounceMarker(googleMap, marker);
        this.selectedMarker=marker;
    }
}
