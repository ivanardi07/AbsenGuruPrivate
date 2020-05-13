package id.ac.polinema.absenguruprivate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

import id.ac.polinema.absenguruprivate.helper.Session;
import id.ac.polinema.absenguruprivate.model.Guru;
import id.ac.polinema.absenguruprivate.restapi.ApiClient;
import id.ac.polinema.absenguruprivate.restapi.InterfaceApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Session session;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new Session(getApplicationContext());
        final RecyclerView guruView = findViewById(R.id.rv_guru);
        final ItemAdapter itemAdapter = new ItemAdapter<>();
        final FastAdapter fastAdapter = FastAdapter.with(itemAdapter);

        final List guru = new ArrayList<>();

        InterfaceApi interfaceApi = ApiClient.getClient().create(InterfaceApi.class);
        Call<List<Guru>> call = interfaceApi.getGuru();

        call.enqueue(new Callback<List<Guru>>() {
            @Override
            public void onResponse(Call<List<Guru>> call, Response<List<Guru>> response) {
                List<Guru> guruItems = response.body();
                if (response.isSuccessful()) {
                    for (Guru item : guruItems) {
                        guru.add(new Guru(item.getNama(), item.getAlamat(), item.getJenis_kelamin(),
                                item.getNo_telp(), item.getFoto(), item.getUsername(), item.getPassword()));
                    }

                    itemAdapter.add(guru);
                    guruView.setAdapter(fastAdapter);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    guruView.setLayoutManager(layoutManager);
                } else {
                    Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Guru>> call, Throwable t) {
                error.setText(t.getMessage());
            }
        });
    }

    public void onKlikTambah(View view) {
        Intent intent = new Intent(getApplicationContext(), TambahActivity.class);
        startActivity(intent);
    }

    public void onKlikLogout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setMessage("Anda yakin ingin keluar?");
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                session.logout();
                Toast.makeText(getApplicationContext(), "Berhasil Keluar", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), LoginAdminActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
