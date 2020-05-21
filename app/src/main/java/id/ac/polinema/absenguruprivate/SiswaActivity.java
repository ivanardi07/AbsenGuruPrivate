package id.ac.polinema.absenguruprivate;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import id.ac.polinema.absenguruprivate.helper.Session;
import id.ac.polinema.absenguruprivate.restapi.InterfaceApi;

public class SiswaActivity extends AppCompatActivity {
    private Session session;
    private double latitude, longitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siswa);

        session = new Session(getApplicationContext());
        TextView result = findViewById(R.id.result);
        String nim = getIntent().getStringExtra("nim");
        String nama = getIntent().getStringExtra("nama");
        String alamat = getIntent().getStringExtra("alamat");

        session.setNimSiswa(nim);
        session.setNamaSiswa(nama);
        session.setAlamatSiswa(alamat);

        result.setText("Anda yakin ingin mengunjungi " + nama + " di " + alamat + " \nKonfirmasi jika anda sudah di lokasi");

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(SiswaActivity.this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(SiswaActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    session.setLocLatitude(latitude);
                    session.setLocLongitude(longitude);
                }
            }
        });
    }

    public void onKlikMaps(View view) {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
    }
}
