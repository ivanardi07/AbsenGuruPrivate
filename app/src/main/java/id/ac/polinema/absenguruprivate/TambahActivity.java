package id.ac.polinema.absenguruprivate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import id.ac.polinema.absenguruprivate.restapi.ApiClient;
import id.ac.polinema.absenguruprivate.restapi.InterfaceApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahActivity extends AppCompatActivity {
    private Button btnTambah, btnUpload;
    private EditText inputNama, inputAlamat, inputTelp, inputUsername, inputPassword;
    private RadioGroup radioGroup;
    private RadioButton selected;
    private ImageView fotoProfil;
    private Bitmap foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        btnTambah = findViewById(R.id.btn_tambah_data_guru);
        btnUpload = findViewById(R.id.btn_upload_foto);
        fotoProfil = findViewById(R.id.img_foto);
        inputNama = findViewById(R.id.et_nama);
        inputAlamat = findViewById(R.id.et_alamat);
        radioGroup = findViewById(R.id.jk_grup);
        inputTelp = findViewById(R.id.et_notelp);
        inputUsername = findViewById(R.id.et_input_username);
        inputPassword = findViewById(R.id.et_input_password);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahData();
            }
        });
    }

    protected void selectImage(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            foto = photo;
            fotoProfil.setImageBitmap(foto);
        }
    }

    private File createTempFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() +"_image.jpeg");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG,85,stream);

        byte[] bitmapdata = stream.toByteArray();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private void tambahData() {
        String nama = inputNama.getText().toString();
        String alamat = inputAlamat.getText().toString();
        selected = findViewById(radioGroup.getCheckedRadioButtonId());
        String jenis_kelamin = "";
        if (selected != null) {
            jenis_kelamin = selected.getText().toString();
        }
        String no_telp = inputTelp.getText().toString();
        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("nama", createPartFromString(nama));
        map.put("alamat", createPartFromString(alamat));
        map.put("jenis_kelamin", createPartFromString(jenis_kelamin));
        map.put("no_telp", createPartFromString(no_telp));
        map.put("username", createPartFromString(username));
        map.put("password", createPartFromString(password));

        File file = createTempFile(foto);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("foto", file.getName(), reqFile);

        InterfaceApi interfaceApi = ApiClient.getClient().create(InterfaceApi.class);
        Call<ResponseBody> call = interfaceApi.tambahGuru(body, map);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private RequestBody createPartFromString(String deskripsi) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, deskripsi);
    }
}

