package com.nimbees.newdemo.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nimbees.newdemo.R;
import com.nimbees.newdemo.ui.activity.NavigationActivity;
import com.nimbees.platform.NimbeesClient;
import com.nimbees.platform.beans.NimbeesLocation;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * This MapFragment lets you see all geolocation notifications received in the map, together with the time that they
 * were received.
 * <p/>
 * See the bees around you!
 */
public class MapFragment extends Fragment {

    /** View with the Google Map. */
    private MapView mMapView;

    /** View shown when there is an error with the map. */
    private TextView mErrorView;

    /** Google Map. */
    private GoogleMap mGoogleMap;

    /** Map of map markers. */
    private Map<Marker, CircleOptions> mMarkers;

    /** Circle to show the precision on the markers. */
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
        mErrorView = (TextView) rootView.findViewById(R.id.map_error);

        mMapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mErrorView.setVisibility(View.GONE);
                mGoogleMap = googleMap;
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mGoogleMap.setMyLocationEnabled(false);

                // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
                MapsInitializer.initialize(MapFragment.this.getActivity());

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
     *
     * @param mergedMarkers Whether to show merged markers or standard ones.
     */
    private void createMarkers(boolean mergedMarkers) {
        mMarkers = new HashMap<Marker, CircleOptions>();
        List<NimbeesLocation> locations;
        String name;
        int color;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        mGoogleMap.clear();

        if (!mergedMarkers) {
            color = getResources().getColor(R.color.ics_red_light);
            locations = NimbeesClient.getLocationManager().getLocationHistory(null, null);
        } else {
            color = getResources().getColor(R.color.ics_blue_light);
            locations = NimbeesClient.getLocationManager().getMergedLocationHistory(null, null);
        }

        Collections.reverse(locations);

        for (NimbeesLocation nimbeesLocation : locations) {

            CircleOptions circleOptions;

            name = formatter.format(nimbeesLocation.getStartDate());

            circleOptions = new CircleOptions()
                    .center(new LatLng(nimbeesLocation.getLatitude(), nimbeesLocation.getLongitude()))
                    .radius(nimbeesLocation.getRadius()).fillColor(0x33ffffff & color).strokeWidth(1.0f)
                    .strokeColor(getResources().getColor(R.color.ics_red_light));

            mMarkers.put(
                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(nimbeesLocation.getLatitude(), nimbeesLocation.getLongitude()))
                            .title(name).draggable(false)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_vertical))),
                    circleOptions);

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
