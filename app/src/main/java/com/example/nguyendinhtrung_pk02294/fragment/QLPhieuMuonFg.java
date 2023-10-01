package com.example.nguyendinhtrung_pk02294.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nguyendinhtrung_pk02294.R;
import com.example.nguyendinhtrung_pk02294.adapter.PhieuMuonAdapter;
import com.example.nguyendinhtrung_pk02294.dao.PhieuMuonDAO;
import com.example.nguyendinhtrung_pk02294.dao.SachDAO;
import com.example.nguyendinhtrung_pk02294.dao.ThanhVienDAO;
import com.example.nguyendinhtrung_pk02294.model.PhieuMuon;
import com.example.nguyendinhtrung_pk02294.model.Sach;
import com.example.nguyendinhtrung_pk02294.model.ThanhVien;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class QLPhieuMuonFg extends Fragment {
    private PhieuMuonDAO phieuMuonDAO;
    private RecyclerView recyclerQLPhieuMuon;
    private ArrayList<PhieuMuon> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_qlphieumuon, container, false);

        recyclerQLPhieuMuon = view.findViewById(R.id.recyclerQLPhieuMuon);
        FloatingActionButton floatAdd = view.findViewById(R.id.floatAdd);

        loadData();

        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        return view;
    }

    private void loadData(){

        // data
        phieuMuonDAO = new PhieuMuonDAO(getContext());
        list = phieuMuonDAO.getDSPhieuMuon();

        // adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerQLPhieuMuon.setLayoutManager(linearLayoutManager);
        PhieuMuonAdapter adapter = new PhieuMuonAdapter(list, getContext());
        recyclerQLPhieuMuon.setAdapter(adapter);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_them_phieumuon, null);
        Spinner spnThanhVien = view.findViewById(R.id.spnThanhVien);
        Spinner spnSach = view.findViewById(R.id.spnSach);
        getDataThanhVien(spnThanhVien);
        getDataSach(spnSach);
        builder.setView(view);

        builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // lấy mã thành viên
                HashMap<String, Object> hsTV = (HashMap<String, Object>) spnThanhVien.getSelectedItem();
                int matv = (int) hsTV.get("matv");

                //Lấy mã sách
                HashMap<String, Object> hsSach = (HashMap<String, Object>) spnSach.getSelectedItem();
                int masach = (int) hsSach.get("masach");

                int tien = (int) hsSach.get("giathue");

                themPhieuMuon(matv, masach, tien);
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void getDataThanhVien(Spinner spnThanhVien) {
        ThanhVienDAO thanhVienDAO = new ThanhVienDAO(getContext());
        ArrayList<ThanhVien> list = thanhVienDAO.getDSThanhVien();

        ArrayList<HashMap<String, Object>> listHM = new ArrayList<>();
        for (ThanhVien tv : list) {
            HashMap<String, Object> hs = new HashMap<>();
            hs.put("matv", tv.getMatv());
            hs.put("hoten", tv.getHoten());
            listHM.add(hs);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getContext(),
                listHM,
                android.R.layout.simple_list_item_1,
                new String[]{"hoten"},
                new int[]{android.R.id.text1});
        spnThanhVien.setAdapter(simpleAdapter);
    }

    private void getDataSach(Spinner spnSach) {
        SachDAO sachDAO = new SachDAO(getContext());
        ArrayList<Sach> list = sachDAO.getDSDauSach();

        ArrayList<HashMap<String, Object>> listHM = new ArrayList<>();
        for (Sach sc : list) {
            HashMap<String, Object> hs = new HashMap<>();
            hs.put("masach", sc.getMasach());
            hs.put("tensach", sc.getTensach());
            hs.put("giathue",sc.getGiathue());
            listHM.add(hs);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getContext(),
                listHM,
                android.R.layout.simple_list_item_1,
                new String[]{"tensach"},
                new int[]{android.R.id.text1});
        spnSach.setAdapter(simpleAdapter);
    }

    private void themPhieuMuon(int matv, int masach, int tien) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("THONGTIN", Context.MODE_PRIVATE);
        String matt = sharedPreferences.getString("matt", "");

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String ngay = simpleDateFormat.format(currentTime);

        PhieuMuon phieuMuon = new PhieuMuon(matv, matt, masach, ngay, 0, tien);
        boolean kiemtra = phieuMuonDAO.themPhieuMuon(phieuMuon);
        if (kiemtra){
            Toast.makeText(getContext(), "Thêm phiếu mượn thành công !", Toast.LENGTH_SHORT).show();
            loadData();
        }else {
            Toast.makeText(getContext(), "Thêm phiếu mượn thất bại !", Toast.LENGTH_SHORT).show();
        }
    }
}
