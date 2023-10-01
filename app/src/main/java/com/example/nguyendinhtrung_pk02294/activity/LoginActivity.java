package com.example.nguyendinhtrung_pk02294.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nguyendinhtrung_pk02294.Home_Screen;
import com.example.nguyendinhtrung_pk02294.R;
import com.example.nguyendinhtrung_pk02294.dao.ThuThuDAO;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUser, edtPass;
    private Button btnLogin;
    boolean passwordEye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);

        ThuThuDAO thuThuDAO = new ThuThuDAO(this);

        edtPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int Right = 2;
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if (event.getRawX() >= edtPass.getRight() - edtPass.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = edtPass.getSelectionEnd();
                        if (passwordEye){
                            // set drawable image here
                            edtPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.eye_off,0);
                            // for hide password
                            edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordEye = false;
                        }else{
                            // set drawable image here
                            edtPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.eye_onl,0);
                            // for show password
                            edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordEye = true;
                        }
                        edtPass.setSelection(selection);
                        return true;
                    }
                }

                return false;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edtUser.getText().toString();
                String pass = edtPass.getText().toString();
                if (thuThuDAO.checkDangNhap(user,pass)){
                    startActivity(new Intent(LoginActivity.this,Home_Screen.class));
                }else {
                    Toast.makeText(LoginActivity.this, "Username và Mật khẩu không đúng !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
