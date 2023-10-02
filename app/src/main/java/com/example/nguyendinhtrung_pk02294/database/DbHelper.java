package com.example.nguyendinhtrung_pk02294.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context){
        super(context, "DANGKYMONHOC", null,3);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String dbThuThu = "CREATE TABLE THUTHU(matt text primary key, hoten text, matkhau text, loaitaikhoan text)";
        sqLiteDatabase.execSQL(dbThuThu);

        String dbThanhVien = "CREATE TABLE THANHVIEN(matv integer primary key autoincrement, hoten text, namsinh text)";
        sqLiteDatabase.execSQL(dbThanhVien);

        String dbLoai = "CREATE TABLE LOAISACH(maloai integer primary key autoincrement, tenloai text)";
        sqLiteDatabase.execSQL(dbLoai);

        String dbSach = "CREATE TABLE SACH (masach integer primary key autoincrement, tensach text, giathue integer, maloai integer references LOAISACH(maloai))";
        sqLiteDatabase.execSQL(dbSach);

        String dbPhieuMuon = "CREATE TABLE PHIEUMUON(mapm integer primary key autoincrement, matv integer references THANHVIEN(matv), matt text references THUTHU(matt), masach integer references SACH(masach), ngay text, trasach integer, tienthue integer)";
        sqLiteDatabase.execSQL(dbPhieuMuon);

        // data
        sqLiteDatabase.execSQL("INSERT INTO LOAISACH VALUES (1, 'Chính trị - pháp luật'),(2,'Tiểu thuyết, truyện - triết học'),(3,'Kinh dị - viễn tưởng'),(4,'Văn hóa xã hội - Lịch sử - Khoa học'),(5,'Tâm lý - tâm linh - tôn giáo'),(6,'Sách thiếu nhi'),(7,'Giáo trình'),(8,'Tự giúp bản thân')");
        sqLiteDatabase.execSQL("INSERT INTO SACH VALUES (1,'Đắc Nhân Tâm', 5000,8),(2,'Người Dưng', 3500,2),(3,'Thế Giới Như Tôi Thấy', 2800,4),(4,'Nhà Giả Kim', 4000,2),(5,'Con Bò Tía', 3200,2),(6,'Tôi Là Ai ? và nếu vậy thì bao nhiêu ?', 3500, 2),(7,'Rừng Na Uy', 4100,2),(8,'Tôi Tài Giỏi, bạn cũng thế !', 4500,8)");
        sqLiteDatabase.execSQL("INSERT INTO THUTHU VALUES ('admin','Nguyễn Văn A','123456','Admin'),('thuthu01','Hồ Văn B','123456','Thủ thư')");
        sqLiteDatabase.execSQL("INSERT INTO THANHVIEN VALUES (1,'Cao Thu Trang','2000'),(2,'Trần Mỹ Kim','2000')");
        //trả sách: 1: đã trả - 0: chưa trả
        sqLiteDatabase.execSQL("INSERT INTO PHIEUMUON VALUES (1,1,'thuthu01', 1, '19/03/2022', 1, 2500),(2,1,'thuthu01', 3, '19/03/2022', 0, 2000),(3,2,'thuthu02', 1, '19/03/2022', 1, 2000)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i != i1){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS THUTHU");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS THANHVIEN");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS LOAISACH");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS SACH");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS PHIEUMUON");
            onCreate(sqLiteDatabase);
        }
    }
}
