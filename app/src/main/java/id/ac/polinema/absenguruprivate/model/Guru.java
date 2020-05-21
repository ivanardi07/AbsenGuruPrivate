package id.ac.polinema.absenguruprivate.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import id.ac.polinema.absenguruprivate.GuruActivity;
import id.ac.polinema.absenguruprivate.R;

public class Guru extends AbstractItem<Guru, Guru.ViewHolder> {
    private String nama;
    private String alamat;
    private String jenis_kelamin;
    private String no_telp;
    private String foto;
    private String username;
    private String password;

    public Guru(String nama, String alamat, String jenis_kelamin, String no_telp, String foto, String username, String password) {
        this.nama = nama;
        this.alamat = alamat;
        this.jenis_kelamin = jenis_kelamin;
        this.no_telp = no_telp;
        this.foto = foto;
        this.username = username;
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public String getFoto() {
        return foto;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @NonNull
    @Override
    public Guru.ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.rv_guru;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_guru;
    }

    public class ViewHolder extends FastAdapter.ViewHolder<Guru> {
        ImageView foto;
        TextView nama, alamat, jenis_kelamin, no_telp, username, password;

        public ViewHolder(final View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.foto_profil);
            nama = itemView.findViewById(R.id.nama_guru);
            alamat = itemView.findViewById(R.id.alamat_guru);
            jenis_kelamin = itemView.findViewById(R.id.jenis_kelamin_guru);
            no_telp = itemView.findViewById(R.id.telp_guru);
            username = itemView.findViewById(R.id.username_view_guru);
            password = itemView.findViewById(R.id.password_view_guru);

        }

        @Override
        public void bindView(final Guru item, List<Object> payloads) {
            Picasso.get().load(item.getFoto()).into(foto);
            nama.setText(item.nama);
            alamat.setText(item.alamat);
            jenis_kelamin.setText(item.jenis_kelamin);
            no_telp.setText(item.no_telp);
            username.setText(item.username);
            password.setText(item.password);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, GuruActivity.class);
                    intent.putExtra("username", item.username);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void unbindView(Guru item) {
            foto.setImageBitmap(null);
            nama.setText(null);
            alamat.setText(null);
            jenis_kelamin.setText(null);
            no_telp.setText(null);
            username.setText(null);
            password.setText(null);
        }
    }
}
