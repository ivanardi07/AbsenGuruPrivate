package id.ac.polinema.absenguruprivate.restapi;

import java.util.List;
import java.util.Map;

import id.ac.polinema.absenguruprivate.model.Absen;
import id.ac.polinema.absenguruprivate.model.Guru;
import id.ac.polinema.absenguruprivate.model.Login;
import id.ac.polinema.absenguruprivate.model.Siswa;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface InterfaceApi {
    @POST("loginAdmin")
    Call<ResponseBody> loginAdmin(@Body Login login);

    @POST("loginGuru")
    Call<ResponseBody> loginGuru(@Body Login login);

    @GET("guru")
    Call<List<Guru>> getGuru();

    @GET("guru")
    Call<List<Guru>> getGuruByUsername(@Query("username") String username);

    @GET("siswa")
    Call<List<Siswa>> getSiswa();

    @POST("siswa")
    Call<ResponseBody> tambahSiswa(@Body Siswa siswa);

    @GET("absenGuru")
    Call<List<Absen>> getAbsenByUsername(
            @Query("username") String username
    );

    @POST("absenGuru")
    Call<ResponseBody> absenGuru(@Body Absen absen);

    @Multipart
    @POST("guru")
    Call<ResponseBody> tambahGuru(
            @Part MultipartBody.Part photo,
            @PartMap Map<String, RequestBody> text);
}
