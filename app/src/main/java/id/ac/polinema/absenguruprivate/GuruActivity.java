package id.ac.polinema.absenguruprivate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.ac.polinema.absenguruprivate.helper.Session;
import id.ac.polinema.absenguruprivate.model.Absen;
import id.ac.polinema.absenguruprivate.model.Guru;
import id.ac.polinema.absenguruprivate.restapi.ApiClient;
import id.ac.polinema.absenguruprivate.restapi.InterfaceApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuruActivity extends AppCompatActivity {
    private ImageView foto;
    private TextView nama, alamat, jenis_kelamin, no_telp, username, password;
    private Session session;
    private CardView guru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru);

        session = new Session(getApplicationContext());

//        guru = findViewById(R.id.profil_guru);
//        guru.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            }
//        });

//        if (session.getLoggedInRole().equals("admin"));

        final RecyclerView absenView = findViewById(R.id.rv_absen);
        final ItemAdapter itemAdapter = new ItemAdapter<>();
        final FastAdapter fastAdapter = FastAdapter.with(itemAdapter);

        final List absen = new ArrayList<>();

        foto = findViewById(R.id.foto_profil);
        nama = findViewById(R.id.nama_guru);
        alamat = findViewById(R.id.alamat_guru);
        jenis_kelamin = findViewById(R.id.jenis_kelamin_guru);
        no_telp = findViewById(R.id.telp_guru);
        username = findViewById(R.id.username_view_guru);
        password = findViewById(R.id.password_view_guru);

        InterfaceApi interfaceApi = ApiClient.getClient().create(InterfaceApi.class);
        Call<List<Guru>> call = interfaceApi.getGuruByUsername(getIntent().getStringExtra("username"));

        call.enqueue(new Callback<List<Guru>>() {
            @Override
            public void onResponse(Call<List<Guru>> call, Response<List<Guru>> response) {
                if (response.isSuccessful()) {
                    Guru item = response.body().get(0);

                    Picasso.get().load(item.getFoto()).into(foto);
                    nama.setText(item.getNama());
                    alamat.setText(item.getAlamat());
                    jenis_kelamin.setText(item.getJenis_kelamin());
                    no_telp.setText(item.getNo_telp());
                    username.setText(item.getUsername());
                    password.setText(item.getPassword());
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal menampilkan data Guru", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Guru>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Call<List<Absen>> call1 = interfaceApi.getAbsenByUsername(getIntent().getStringExtra("username"));

        call1.enqueue(new Callback<List<Absen>>() {
            @Override
            public void onResponse(Call<List<Absen>> call, Response<List<Absen>> response) {
                if (response.isSuccessful()) {
                    List<Absen> absenItems = response.body();

                    for (Absen item : absenItems) {
                        absen.add(new Absen(item.getUsername(), item.getPassword(), item.getJam_login(),
                                item.getJam_logout(), item.getTanggal(), item.getLokasi_latitude(), item.getLokasi_longitude(),
                                item.getNim_siswa(), item.getNama(), item.getAlamat()));
                    }
                }

                    if (session.getUsername().equals(getIntent().getStringExtra("username"))) {
                        absen.add(new Absen(session.getUsername(), session.getPassword(), session.getLoginTime(),
                                session.getLogoutTime(), session.getDate(), session.getLocLatitude(), session.getLongitude(),
                                session.getNimSiswa(), session.getNamaSiswa(), session.getAlamatSiswa()));
                    }
                    itemAdapter.add(absen);
                    absenView.setAdapter(fastAdapter);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    absenView.setLayoutManager(layoutManager);
                }

            @Override
            public void onFailure(Call<List<Absen>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onKlikLogoutGuru(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GuruActivity.this);
        builder.setCancelable(false);
        builder.setMessage("Anda yakin ingin keluar?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                session.setLogoutTime(currentTime);

                String username = session.getUsername();
                String password = session.getPassword();
                String jam_login = session.getLoginTime();
                String jam_logout = session.getLogoutTime();
                String tanggal = session.getDate();
                double lokasi_latitude = session.getLocLatitude();
                double lokasi_longitude = session.getLongitude();
                String nim_siswa = session.getNimSiswa();
                String nama_siswa = session.getNamaSiswa();
                String alamat_siswa = session.getAlamatSiswa();

                InterfaceApi interfaceApi = ApiClient.getClient().create(InterfaceApi.class);

                Call<ResponseBody> call = interfaceApi.absenGuru(new Absen(username, password, jam_login, jam_logout, tanggal, lokasi_latitude, lokasi_longitude, nim_siswa, nama_siswa, alamat_siswa));

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            session.logout();
                            Toast.makeText(getApplicationContext(), "Berhasil Keluar", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginGuruActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal Keluar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
