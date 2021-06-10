package tr.edu.yildiz.ekremkamaz;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import tr.edu.yildiz.ekremkamaz.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Marker marker;
    private FloatingActionButton selectLocationFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        selectLocationFAB = findViewById(R.id.selectLocationFAB);
        selectLocationFAB.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location;
        boolean hasLocation = getIntent().hasExtra("location");
        if (hasLocation) {
            location = getIntent().getParcelableExtra("location");
        } else {
            location = new LatLng(41.0283717, 28.8893776);
        }
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.setOnMapClickListener(this);
        if (hasLocation) {
            onMapClick(location);
        }
    }

    @Override
    public void onMapClick(@NonNull @NotNull LatLng latLng) {
        if (marker != null) marker.remove();
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Etkinlik konumu"));
        selectLocationFAB.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.selectLocationFAB) {
            Intent _intent = new Intent();
            _intent.putExtra("location", marker.getPosition());
            setResult(RESULT_OK, _intent);
            finish();
        }
    }
}