package com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nitin on 9/19/14.
 */
public class RPSDbHelper extends SQLiteOpenHelper {

    public final String TAG=getClass().getSimpleName().toString();
    public final static String DATABASE_NAME="rpsdb";
    public final static int DATABASE_VERSION=1;

    public RPSDbHelper(Context context)
    {
        super(context, context.getExternalFilesDir(null).getAbsolutePath() + "/" + DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG,context.getExternalFilesDir(null).getAbsolutePath() + "/" + DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


         final String SQL_QUERY_CREATE_USER_TABLE="CREATE TABLE "+Contract.UsersTable.TABLE_NAME+
                "("+
                 Contract.UsersTable._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+Contract.UsersTable.COLUMN_USER_NAME+" TEXT UNIQUE NOT NULL, "+
                Contract.UsersTable.COLUMN_AGE+" INTEGER, "+Contract.UsersTable.COLUMN_SEX+" TEXT "+")";

        Log.d(TAG,SQL_QUERY_CREATE_USER_TABLE);

        final String SQL_QUERY_CREATE_SCORES_TABLE="CREATE TABLE "+Contract.ScoresTable.TABLE_NAME+"("+
                Contract.ScoresTable._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +Contract.ScoresTable.COLUMN_WINS+" INTEGER NOT NULL,"+
                Contract.ScoresTable.COLUMN_LOSSES+" INTEGER NOT NULL, "+"FOREIGN KEY("+Contract.ScoresTable._ID+")"
                +"REFERENCES "+Contract.UsersTable.TABLE_NAME+"("+Contract.UsersTable._ID+")"+")";


        Log.d(TAG,SQL_QUERY_CREATE_SCORES_TABLE);

        sqLiteDatabase.execSQL(SQL_QUERY_CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(SQL_QUERY_CREATE_SCORES_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
