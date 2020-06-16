package com.examtimer1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public final String ID="id";
    public static final String DATABASE_NAME="Record.db";
    public String TABLE_NAME;
    public final String TITLE="title";
    public final String TIME="time";
    public final String DATE="date";
    public static final int DATABASE_VERSION=2;

    public DBHelper(Context context, String tableName){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        TABLE_NAME=tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME
                +"("
                +ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +TITLE+" TEXT,"
                +TIME+" TEXT " + "NOT NULL,"
                +DATE+ " TEXT NOT NULL)");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+"TABLE_SUBJECT_KOREAN");
        db.execSQL("DROP TABLE IF EXISTS "+"TABLE_SUBJECT_MATH");
        onCreate(db);
    }

    public Cursor LoadSQLiteDBCursor(){
        SQLiteDatabase db=this.getReadableDatabase();
        db.beginTransaction();

        String selectQuery="SELECT id, title, time, date FROM "+TABLE_NAME;
        Cursor cursor=null;

        try{
            cursor=db.rawQuery(selectQuery, null);
            db.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.endTransaction();
        }
        return cursor;
    }

    public void dbDelete(SQLiteDatabase db, Long dbId){
        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE "+ID+"="+dbId);
    }

}
