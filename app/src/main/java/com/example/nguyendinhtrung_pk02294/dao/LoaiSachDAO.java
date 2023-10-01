package com.example.nguyendinhtrung_pk02294.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nguyendinhtrung_pk02294.database.DbHelper;
import com.example.nguyendinhtrung_pk02294.model.LoaiSach;

import java.util.ArrayList;

public class LoaiSachDAO {
    DbHelper dbHelper;
    public LoaiSachDAO (Context context){
        dbHelper = new DbHelper(context);
    }

    public ArrayList<LoaiSach> getDSLoaiSach(){
        ArrayList<LoaiSach> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM LOAISACH",null);
        if (cursor.getCount() != 0){
            cursor.moveToFirst();
            do {
                list.add(new LoaiSach(cursor.getInt(0),cursor.getString(1)));
            }while (cursor.moveToNext());
        }

        return list;
    }

    // thêm loại sách
    public boolean themLoaiSach(String tenloai){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tenloai", tenloai);
        long check = sqLiteDatabase.insert("LOAISACH",null,contentValues);
        if (check == -1)
            return false;
        return true;
    }

    public int xoaLoaiSach(int id){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM SACH WHERE maloai = ?", new String[]{String.valueOf(id)});
        if (cursor.getCount() != 0){
            return -1;

        }

        long check = sqLiteDatabase.delete("LOAISACH", "maloai = ?", new String[]{String.valueOf(id)});
        if (check == -1)
            return 0;
        return 1;
    }

    public boolean thayDoiLoaiSach (LoaiSach loaiSach){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tenloai", loaiSach.getTenloai());
        long check = sqLiteDatabase.update("LOAISACH", contentValues,"maloai = ?", new String[]{String.valueOf(loaiSach.getId())});
        if (check == -1)
            return false;
        return true;
    }
}
