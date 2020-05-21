package id.ac.polinema.absenguruprivate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import id.ac.polinema.absenguruprivate.helper.Session;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap gMap;
    private Session session;
    private double latitude, longitude;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        session = new Session(getApplicationContext());
        fab = findViewById(R.id.btn_check);
        if (session.getLoggedInRole().equals("admin")){
            fab.setVisibility(View.INVISIBLE);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        Toast.makeText(MapsActivity.this, "Lat : " + latitude + "Long : " + longitude, Toast.LENGTH_LONG).show();
        gMap = googleMap;

        LatLng sydney = new LatLng(latitude, longitude);
        gMap.addMarker(new MarkerOptions().position(sydney).title("Lokasi sekarang"));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
    }

    public void onKlikCheck(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setCancelable(false);
        builder.setMessage("Apakah lokasi saat ini sudah sesuai?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MapsActivity.this, Guru1Activity.class);
                intent.putExtra("username", session.getUsername());
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
