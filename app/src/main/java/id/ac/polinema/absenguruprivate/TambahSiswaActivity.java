package id.ac.polinema.absenguruprivate;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import id.ac.polinema.absenguruprivate.model.Siswa;
import id.ac.polinema.absenguruprivate.restapi.ApiClient;
import id.ac.polinema.absenguruprivate.restapi.InterfaceApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahSiswaActivity extends AppCompatActivity {
    private EditText inputNim, inputNama, inputAlamat, inputTglLahir, inputKelas;
    private RadioGroup radioGroup;
    private RadioButton selected;
    private Button btnSimpan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_siswa);

        inputNim = findViewById(R.id.edt_nim);
        inputNama = findViewById(R.id.edt_nama_siswa);
        inputAlamat = findViewById(R.id.edt_alamat_siswa);
        radioGroup = findViewById(R.id.group_jk_siswa);
        inputTglLahir = findViewById(R.id.edt_tgl_lahir);
        inputKelas = findViewById(R.id.edt_kelas);
        btnSimpan = findViewById(R.id.btn_tambah_data_siswa);

        inputTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog picker = new DatePickerDialog(TambahSiswaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        inputTglLahir.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                    }
                }, year, month, day);
                picker.show();
            }
        });
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahData();
            }
        });
    }

    private void tambahData() {
        String nim = inputNim.getText().toString();
        String nama = inputNama.getText().toString();
        String alamat = inputAlamat.getText().toString();
        selected = findViewById(radioGroup.getCheckedRadioButtonId());
        String jenis_kelamin = "";
        if (selected != null) {
            jenis_kelamin = selected.getText().toString();
        }
        String tanggal_lahir = inputTglLahir.getText().toString();
        String kelas = inputKelas.getText().toString();

        InterfaceApi interfaceApi = ApiClient.getClient().create(InterfaceApi.class);
        Call<ResponseBody> call = interfaceApi.tambahSiswa(new Siswa(nim, nama, alamat, jenis_kelamin, tanggal_lahir, kelas));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FragmentSiswa.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
