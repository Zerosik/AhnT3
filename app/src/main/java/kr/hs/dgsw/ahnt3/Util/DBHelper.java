package kr.hs.dgsw.ahnt3.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import kr.hs.dgsw.ahnt3.JsonClass.ResponseOutJson;

public class DBHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "AhnT.db";
    private final static String DB_TOKEN = "TOKEN";
    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tokensql = "create table TOKEN(idx integer primary key autoincrement, token text);";
        String leavesql = "create table LEAVE(idx integer primary key autoincrement, start_date text, end_date text, reason text)";
        db.execSQL(tokensql);
        db.execSQL(leavesql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String tokensql = "drop table if exists TOKEN;";
        String leavesql = "drop table if exists LEAVE;";
        db.execSQL(tokensql);
        db.execSQL(leavesql);
        onCreate(db);
    }
    public boolean insertTokenData(String token){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("token", token);
        long result;
        if(CheckTokenExist()){
            result = db.update(DB_TOKEN, contentValues, "idx = ?", new String[]{"1"});
        }else {
            result = db.insert(DB_TOKEN, null, contentValues);
        }

        if(result == -1)
            return false;
        return true;
    }
    public boolean updateTokenData(String token){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("token", token);
        db.update(DB_TOKEN, contentValues, "idx = ?", new String[]{"1"});
        long result = db.insert(DB_TOKEN, null, contentValues);
        if(result == -1)
            return false;
        return true;
    }
    public boolean CheckTokenExist(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + DB_TOKEN, null);
        if(res.getCount() == 0)
            return false;
        return true;
    }
    public String getDbToken(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + DB_TOKEN, null);
        res.moveToNext();
        String token = res.getString(1);
        return token;
    }
    public Cursor getLeaveList(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from LEAVE ", null);
        return res;
    }
    public boolean insertLeaveData(ResponseOutJson json){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("start_date", json.getStartDate().replace("T", " ").substring(0, 16));
        contentValues.put("end_date", json.getEndDate().replace("T", " ").substring(0, 16));
        contentValues.put("reason", json.getReason());
        long result =  db.insert("LEAVE", null, contentValues);

        if(result == -1)
            return false;
        return true;
    }
}