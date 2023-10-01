package com.example.nguyendinhtrung_pk02294;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyendinhtrung_pk02294.activity.LoginActivity;
import com.example.nguyendinhtrung_pk02294.dao.SachDAO;
import com.example.nguyendinhtrung_pk02294.dao.ThuThuDAO;
import com.example.nguyendinhtrung_pk02294.fragment.QLLoaiSachFg;
import com.example.nguyendinhtrung_pk02294.fragment.QLPhieuMuonFg;
import com.example.nguyendinhtrung_pk02294.fragment.QLSachFg;
import com.example.nguyendinhtrung_pk02294.fragment.QLThanhVienFg;
import com.example.nguyendinhtrung_pk02294.fragment.ThongKeDoanhThuFg;
import com.example.nguyendinhtrung_pk02294.fragment.ThongKeTop10Fg;
import com.google.android.material.navigation.NavigationView;

public class Home_Screen extends AppCompatActivity {
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Toolbar toolbar = findViewById(R.id.toolBar);
        FrameLayout frameLayout = findViewById(R.id.frameLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        View headerLayout = navigationView.getHeaderView(0);
        TextView txtTen = headerLayout.findViewById(R.id.txtTen);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.menu_icon);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.mQLPhieuMuon:
                        fragment = new QLPhieuMuonFg();
                        break;
                    case R.id.mQLLoaiSach:
                        fragment = new QLLoaiSachFg();
                        break;
                    case R.id.mDangXuat:
                        Intent intent = new Intent(Home_Screen.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    case R.id.mDoiMatKhau:
                        showDialogDoiMatKhau();
                        break;
                    case R.id.mTop10:
                        fragment = new ThongKeTop10Fg();
                        break;
                    case R.id.mDoanhThu:
                        fragment = new ThongKeDoanhThuFg();
                        break;
                    case R.id.mQLThanhVien:
                        fragment = new QLThanhVienFg();
                        break;
                    case R.id.mQLSach:
                        fragment = new QLSachFg();
                        break;
                    default:
                        fragment = new QLPhieuMuonFg();
                        break;
                }

                if (fragment != null){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout,fragment)
                            .commit();
                    toolbar.setTitle(item.getTitle());
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        // Hiển thị một số chức năng cho Admin
        SharedPreferences sharedPreferences = getSharedPreferences("THONGTIN", MODE_PRIVATE);
        String loaiTK = sharedPreferences.getString("loaitaikhoan","");
        if (!loaiTK.equals("Admin")){
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.mDoanhThu).setVisible(false);
            menu.findItem(R.id.mTop10).setVisible(false);
        }
        String hoten = sharedPreferences.getString("hoten", "");
        txtTen.setText("Xin chào, " + hoten);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogDoiMatKhau(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setNegativeButton("Cập nhật",null)
                .setPositiveButton("Hủy",null);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_doimatkhau,null);
        EditText edtOldPass = view.findViewById(R.id.edtPassOld);
        EditText edtNewPass = view.findViewById(R.id.edtNewPass);
        EditText edtReNewPass = view.findViewById(R.id.edtReNewPass);

        builder.setView(view);


        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = edtOldPass.getText().toString();
                String newPass = edtNewPass.getText().toString();
                String reNewPass = edtReNewPass.getText().toString();
                if (oldPass.equals("") || newPass.equals("") || reNewPass.equals("")){
                    Toast.makeText(Home_Screen.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else{
                    if (newPass.equals(reNewPass)){
                        SharedPreferences sharedPreferences = getSharedPreferences("THONGTIN",MODE_PRIVATE);
                        String matt = sharedPreferences.getString("matt","");
                        // Cập nhập
                        ThuThuDAO thuThuDAO = new ThuThuDAO(Home_Screen.this);
                        int check = thuThuDAO.capNhapMatKhau(matt,oldPass,newPass);
                        if (check == 1){
                            Toast.makeText(Home_Screen.this, "Cập nhập mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Home_Screen.this,LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else if (check == 0){
                            Toast.makeText(Home_Screen.this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Home_Screen.this, "Cập nhật mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Home_Screen.this, "Nhập mật khẩu không trùng với nhau", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}