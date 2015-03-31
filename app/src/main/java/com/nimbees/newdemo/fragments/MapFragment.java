package com.nimbees.newdemo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nimbees.newdemo.R;
import com.nimbees.newdemo.activity.NavigationActivity;
import com.nimbees.platform.NimbeesClient;
import com.nimbees.platform.beans.NimbeesLocation;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * This MapFragment lets see all notifications received in the map
 * in the moment that you received it!
 *
 * See the bees around you!
 */

public class MapFragment extends Fragment {

    /**
     * View for the map.
     */
    private MapView mMapView;

    /**
     * Map.
     */
    private GoogleMap mGoogleMap;

    /**
     * Map of markers for the map.
     */
    private Map<Marker, CircleOptions> mMarkers;

    /**
     * Circle to show in the markers.
     */
    private Circle mCircle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_section5));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);



        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mGoogleMap = mMapView.getMap();
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap.setMyLocationEnabled(false);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {
                createMarkers(false);
                showMarkers();
                mGoogleMap.setOnCameraChangeListener(null);
            }
        });

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                CircleOptions options = mMarkers.get(marker);
                mCircle.setCenter(options.getCenter());
                mCircle.setFillColor(options.getFillColor());
                mCircle.setStrokeColor(options.getStrokeColor());
                mCircle.setRadius(options.getRadius());
                return false;
            }
        });

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /**
     * Creates the markers for the user locations.
     */
    private void createMarkers(boolean mergedMarkers) {
        mMarkers = new HashMap<Marker, CircleOptions>();
        String name;
        int color;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        mGoogleMap.clear();

        if (!mergedMarkers) {
            color = getResources().getColor(R.color.fab_material_red_500);
            List<NimbeesLocation> locations = NimbeesClient.getLocationManager().getLocationHistory(null, null);

            Collections.reverse(locations);

            for (NimbeesLocation nimbeesLocation : locations) {

                CircleOptions circleOptions;

                name = formatter.format(nimbeesLocation.getStartDate());

                circleOptions = new CircleOptions()
                        .center(new LatLng(nimbeesLocation.getLatitude(), nimbeesLocation.getLongitude()))
                        .radius(nimbeesLocation.getRadius()).fillColor(0x33ffffff & color).strokeWidth(1.0f)
                        .strokeColor(getResources().getColor(R.color.fab_material_red_500));

                mMarkers.put(
                        mGoogleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(nimbeesLocation.getLatitude(), nimbeesLocation.getLongitude()))
                                .title(name).draggable(false)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_vertical))),
                        circleOptions);


            }
        } else {
            color = getResources().getColor(R.color.fab_material_blue_500);
            List<NimbeesLocation> locations = NimbeesClient.getLocationManager().getMergedLocationHistory(null, null);

            Collections.reverse(locations);

            for (NimbeesLocation nimbeesLocation : locations) {

                CircleOptions circleOptions;

                name = formatter.format(nimbeesLocation.getStartDate()) + " / "
                        + formatter.format(nimbeesLocation.getEndDate());

                circleOptions = new CircleOptions()
                        .center(new LatLng(nimbeesLocation.getLatitude(), nimbeesLocation.getLongitude()))
                        .radius(nimbeesLocation.getRadius()).fillColor(0x33ffffff & color).strokeWidth(1.0f)
                        .strokeColor(getResources().getColor(R.color.fab_material_blue_500));

                mMarkers.put(
                        mGoogleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(nimbeesLocation.getLatitude(), nimbeesLocation.getLongitude()))
                                .title(name).draggable(false)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))),
                        circleOptions);
            }
        }
    }

    /**
     * Shows the marker and moves the map accordingly.
     */
    private void showMarkers() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (mMarkers.size() > 0) {
            Map.Entry<Marker, CircleOptions> lastEntry = null;
            for (Map.Entry<Marker, CircleOptions> entry : mMarkers.entrySet()) {
                builder.include(entry.getKey().getPosition());
                lastEntry = entry;
            }

            if (lastEntry != null) {
                lastEntry.getKey().showInfoWindow();
                mCircle = mGoogleMap.addCircle(lastEntry.getValue());
            }

            LatLngBounds bounds = builder.build();

            int padding = 100; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            mGoogleMap.animateCamera(cu);
        }

    }

}
