package com.examtimer1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper_tt extends SQLiteOpenHelper {

    public final String ID="id";
    public static final String DATABASE_NAME="Record.db";
    public String TABLE_NAME="TABLE_TOTAL";
    public final String TITLE="title";
    public final String DATE="date";
    public static final int DATABASE_VERSION=2;
    public final String TIME_KOREAN="time_korean", TIME_MATH="time_math", TIME_ENGLISH="time_english", TIME_HISTORY="time_history", TIME_SCIENCE1="time_science1", TIME_SCIENCE2="time_science2", TIME_FOREIGN_LANGUAGE="time_foreign_language";

    public DBHelper_tt(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME
                +"("
                +ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +TITLE+" TEXT,"
                +DATE+ " TEXT NOT NULL, "
                +TIME_KOREAN+" TEXT, "
                +TIME_MATH+ " TEXT, "
                +TIME_ENGLISH+" TEXT, "
                +TIME_HISTORY+" TEXT, "
                +TIME_SCIENCE1+" TEXT, "
                +TIME_SCIENCE2+ " TEXT, "
                +TIME_FOREIGN_LANGUAGE+ " TEXT)");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+"TABLE_NAME");
        onCreate(db);
    }

    public Cursor LoadSQLiteDBCursor(){
        SQLiteDatabase db=this.getReadableDatabase();
        db.beginTransaction();

        String selectQuery="SELECT id, title, date, time_korean, time_math, time_english, time_history, time_science1, time_science2, time_foreign_language FROM "+TABLE_NAME;
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
