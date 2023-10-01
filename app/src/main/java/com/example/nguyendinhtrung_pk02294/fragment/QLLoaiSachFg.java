package com.example.nguyendinhtrung_pk02294.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nguyendinhtrung_pk02294.R;
import com.example.nguyendinhtrung_pk02294.adapter.LoaiSachAdapter;
import com.example.nguyendinhtrung_pk02294.dao.LoaiSachDAO;
import com.example.nguyendinhtrung_pk02294.model.ItemClick;
import com.example.nguyendinhtrung_pk02294.model.LoaiSach;

import java.util.ArrayList;

public class QLLoaiSachFg extends Fragment {
    RecyclerView recyclerLoaiSach;
    LoaiSachDAO dao;
    EditText edtLoaiSach;
    int maloai;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_qlloaisach,container,false);

        recyclerLoaiSach = view.findViewById(R.id.recyclerLoaiSach);
        edtLoaiSach = view.findViewById(R.id.edtLoaiSach);
        Button btnThem = view.findViewById(R.id.btnThem);
        Button btnSua = view.findViewById(R.id.btnSua);

         dao = new LoaiSachDAO(getContext());

         loadData();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenloai = edtLoaiSach.getText().toString();

                if (dao.themLoaiSach(tenloai)){
                    loadData();
                    edtLoaiSach.setText("");
                }else {
                    Toast.makeText(getContext(), "Thêm loại sách không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenloai = edtLoaiSach.getText().toString();
                LoaiSach loaiSach = new LoaiSach(maloai, tenloai);
                if (dao.thayDoiLoaiSach(loaiSach)){
                    loadData();
                    edtLoaiSach.setText("");
                }else {
                    Toast.makeText(getContext(), "Thay đổi thông tin không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void loadData(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerLoaiSach.setLayoutManager(linearLayoutManager);
        ArrayList<LoaiSach> list = dao.getDSLoaiSach();
        LoaiSachAdapter adapter = new LoaiSachAdapter(getContext(), list, new ItemClick() {
            @Override
            public void onClickLoaiSach(LoaiSach loaiSach) {
                edtLoaiSach.setText(loaiSach.getTenloai());
                maloai = loaiSach.getId();
            }
        });
        recyclerLoaiSach.setAdapter(adapter);
    }
}
