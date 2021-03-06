package id.ac.polinema.absenguruprivate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import id.ac.polinema.absenguruprivate.helper.Session;
import id.ac.polinema.absenguruprivate.model.Login;
import id.ac.polinema.absenguruprivate.restapi.ApiClient;
import id.ac.polinema.absenguruprivate.restapi.InterfaceApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginGuruActivity extends AppCompatActivity {
    private EditText inputUsername, inputPassword;
    private TextView result;
    private Button loginButton;
    private ConstraintLayout loginForm;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_guru);

        session = new Session(getApplicationContext());
        inputUsername = findViewById(R.id.et_username_guru);
        inputPassword = findViewById(R.id.et_password_guru);
        result = findViewById(R.id.btn_tv_admin);
        loginButton = findViewById(R.id.btn_login_guru);
        loginForm = findViewById(R.id.login_guru);

        loginButton = findViewById(R.id.btn_login_guru);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin(inputUsername.getText().toString(), inputPassword.getText().toString());
            }
        });

        if (session.getLoggedInStatus()) {
            if (session.getLoggedInRole().equals("admin")) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(intent);
            } else if (session.getLoggedInRole().equals("guru")) {
                Intent intent = new Intent(getApplicationContext(), Guru1Activity.class);
                startActivity(intent);
            }
        }
    }

    private void userLogin(String username, String password) {
        InterfaceApi interfaceApi = ApiClient.getClient().create(InterfaceApi.class);

        Call<ResponseBody> call = interfaceApi.loginGuru(new Login(username, password));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONArray json = new JSONArray(response.body().string());
                        String username = json.getJSONObject(0).getString("username");
                        String password = json.getJSONObject(0).getString("password");
                        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                        session.setLoggedInStatus(true);
                        session.setLoggedInRole("guru");
                        session.setUsername(username);
                        session.setPassword(password);
                        session.setLoginTime(currentTime);
                        session.setDate(currentDate);

                        Intent intent = new Intent(getApplicationContext(), Guru1Activity.class);
                        startActivity(intent);
                    } catch (JSONException e){
                        e.printStackTrace();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Akun tidak terdaftar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void halamanAdmin(View view) {
        Intent intent = new Intent(LoginGuruActivity.this, LoginAdminActivity.class);
        startActivity(intent);
    }
}
