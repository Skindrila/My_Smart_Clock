package com.example.skindrila.my_smart_clock;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

import java.util.Random;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "solvers.db";
    private static final String TABLE_SOLVERS = "solvers";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_QUESTION = "question";
    private static final String COLUMN_CORRECTANSWER = "correctAnswer";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_SOLVERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_QUESTION + " TEXT, " +
                COLUMN_CORRECTANSWER + " TEXT " +
                ");";
        sqLiteDatabase.execSQL(query);

        String[] ques = new String[50];
        String[] ans = new String[50];
        ques[0] = "(12+38)*14";
        ans[0] = "700";
        ques[1] = "14*12+32";
        ans[1] = "200";
        ques[2] = "68/4 + 13";
        ans[2] = "30";
        ques[3] = "(298-154)*2 + 12";
        ans[3] = "300";
        ques[4] = "2*4*5+60";
        ans[4] = "100";
        ques[5] = "((19+12)+2)/11-1";
        ans[5] = "2";
        ques[6] = "5*5*5-75";
        ans[6] = "50";
        ques[7] = "15+300/3/10/2";
        ans[7] = "20";
        ques[8] = "((5*23)+5)/3";
        ans[8] = "40";
        ques[9] = "(14-2+3)*5+15";
        ans[9] = "90";
        ques[10] = "14+18+27+84-7";
        ans[10] = "136";
        ques[11] = "2*4*6*8";
        ans[11] = "384";
        ques[12] = "750/5*3-47";
        ans[12] = "403";
        ques[13] = "96-58*7-4";
        ans[13] = "262";
        ques[14] = "76*10-27/9";
        ans[14] = "757";

        for(int i = 15; i < 21; i++)
        {
            Random r = new Random();
            int first = r.nextInt(1000);
            int second = r.nextInt(1000);
            int third = r.nextInt(1000);
            int fourth = r.nextInt(1000);
            int res = first - second * third + fourth;
            ques[i] = String.valueOf(first) + "-" + String.valueOf(second) + "*" +String.valueOf(third) + "+" +String.valueOf(fourth);
            ans[i] = String.valueOf(res);
        }

        for(int i = 21; i < 31; i++)
        {
            Random r = new Random();
            int first = r.nextInt(1000);
            int second = r.nextInt(1000);
            int third = r.nextInt(20);
            while(third == 0)
            {
                third = r.nextInt(20);
            }
            int a = r.nextInt(100);
            int fourth = third * a;
            int res = fourth/third + first*second;
            ques[i] = String.valueOf(fourth) + "/" + String.valueOf(third) + "+" +String.valueOf(first) + "*" +String.valueOf(second);
            ans[i] = String.valueOf(res);
        }

        for(int i = 31; i < 50; i++)
        {
            Random r = new Random();
            int first = r.nextInt(1000);
            int second = r.nextInt(1000);
            int third = r.nextInt(10);
            int fourth = r.nextInt(10);
            int res = first*third + second*fourth;
            ques[i] = String.valueOf(first) + "*" + String.valueOf(third) + "+" +String.valueOf(second) + "*" +String.valueOf(fourth);
            ans[i] = String.valueOf(res);
        }

        for(int i = 0; i < 50; i++)
        {
            ContentValues values = new ContentValues();
            values.put(COLUMN_QUESTION, ques[i]);
            values.put(COLUMN_CORRECTANSWER, ans[i]);
            sqLiteDatabase.insert(TABLE_SOLVERS,null,values);

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query = "DROP TABLE IF EXISTS " + TABLE_SOLVERS;
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_SOLVERS;
        db.execSQL(query);
        onCreate(db);
    }

    public String getQuestion(int ID)
    {
        SQLiteDatabase db = getWritableDatabase();
        String q = "";
        String query = "SELECT * FROM " + TABLE_SOLVERS +
                " WHERE " + COLUMN_ID + "=\"" + ID + "\";";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        q += c.getString(c.getColumnIndex("question"));
        db.close();
        return q;

    }

    public String getAnswer(int ID)
    {
        SQLiteDatabase db = getWritableDatabase();
        String q = "";
        String query = "SELECT * FROM " + TABLE_SOLVERS +
                " WHERE " + COLUMN_ID + "=\"" + ID + "\";";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        q += c.getString(c.getColumnIndex("correctAnswer"));
        db.close();
        return q;

    }
}