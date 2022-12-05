package com.example.slpolicemobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class internalDB extends SQLiteOpenHelper {
    public static final String dbname = "slpolice";
    public static final String TBL = "rememberPass";

    //columns for remember pass
    public static final String COL1 = "lID";
    public static final String COL2 = "email";
    public static final String COL3 = "password";

    public internalDB(@Nullable Context context) {
        super(context, dbname, null, 1);
        SQLiteDatabase db =this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TBL+"(lID INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE iF EXISTS "+TBL);
        onCreate(db);
    }

    //############################ Password Remember #############################
    public boolean addLoginDet(String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues login = new ContentValues();
        login.put(COL2,email);
        login.put(COL3,password);
        long result = db.insert(TBL,null,login);

        return result != -1;
    }

    public Cursor getloginfromDB()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor email = db.rawQuery("SELECT * FROM " +TBL,null);
        return email;
    }
    //############################ end password remember #############################
}
