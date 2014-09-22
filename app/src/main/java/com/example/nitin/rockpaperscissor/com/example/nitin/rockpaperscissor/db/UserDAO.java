package com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by nitin on 9/19/14.
 */
public class UserDAO implements UserDaoInterface {

    Context context=null;
    SQLiteDatabase sqLiteDatabase=null;
    RPSDbHelper rpsDbHelper=null;


  public  UserDAO(Context context)
    {
        this.context=context;
        rpsDbHelper=new RPSDbHelper(context);
    }

    @Override
    public long saveScore(ScoresModel scoresModel) {
        sqLiteDatabase=rpsDbHelper.getWritableDatabase();
        ContentValues cv2= new ContentValues();
        cv2.put(Contract.ScoresTable.COLUMN_WINS,scoresModel.getWins());
        cv2.put(Contract.ScoresTable.COLUMN_LOSSES,scoresModel.getLosses());
        sqLiteDatabase.beginTransaction();
        long rowId=sqLiteDatabase.insert(Contract.ScoresTable.TABLE_NAME,null,cv2);
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
        return rowId;
    }

    @Override
    public long saveUser(UserModel user) {
        sqLiteDatabase=rpsDbHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        ContentValues cv=new ContentValues();
        cv.put(Contract.UsersTable.COLUMN_USER_NAME,user.getUserName());
        cv.put(Contract.UsersTable.COLUMN_AGE,user.getAge());
        cv.put(Contract.UsersTable.COLUMN_SEX,user.getSex());
        long rowId=sqLiteDatabase.insert(Contract.UsersTable.TABLE_NAME,null,cv);
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
        return  rowId;
    }

    @Override
    public boolean checkUser(UserModel user) {
        String selArgs[]= new String[1];
        selArgs[0]=user.getUserName();
        sqLiteDatabase=rpsDbHelper.getWritableDatabase();
        String query= "Select * from "+Contract.UsersTable.TABLE_NAME+" where "+
                Contract.UsersTable.COLUMN_USER_NAME+"=?";
        Cursor cursor=sqLiteDatabase.rawQuery(query, selArgs);
        if(cursor.getCount()<1)
            return false;

        cursor.close();
        sqLiteDatabase.close();
        return true;

    }

    @Override
    public UserModel findUser(String userName) {
        String query="SELECT * FROM "+Contract.UsersTable.TABLE_NAME+" where "+Contract.UsersTable.COLUMN_USER_NAME
                +"=?";
        String selArgs[]= new String[1];
        selArgs[0]=userName;
        sqLiteDatabase=rpsDbHelper.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(query, selArgs);
        if(cursor.getCount()<1)
            return null;
        cursor.moveToFirst();
        UserModel user= cursorToUser(cursor);
        cursor.close();
        cursor=null;
        int userId=cursor.getPosition();
        String scoreQuery= "SELECT * FROM "+Contract.ScoresTable.TABLE_NAME+" WHERE "+Contract.ScoresTable._ID
                +"=?";
         selArgs[0]=Integer.toString(userId);

       cursor=sqLiteDatabase.rawQuery(scoreQuery,selArgs);
        cursor.moveToFirst();
        ScoresModel scoresModel= cursorToScores(cursor);
        user.setScore(scoresModel);

        cursor.close();
        sqLiteDatabase.close();
        return user;
    }

    @Override
    public List<UserModel> getAllUsers() {
        return null;
    }

    public UserModel cursorToUser(Cursor cursor)
    {
        UserModel userModel= new UserModel();
        int uNameIdx=cursor.getColumnIndex(Contract.UsersTable.COLUMN_USER_NAME);
        int ageIdx=cursor.getColumnIndex(Contract.UsersTable.COLUMN_AGE);
        int sexIdx=cursor.getColumnIndex(Contract.UsersTable.COLUMN_SEX);
        userModel.setAge(cursor.getInt(ageIdx));
        userModel.setSex(cursor.getString(sexIdx));
        userModel.setUserName(cursor.getString(uNameIdx));

            return userModel;

    }
    public ScoresModel cursorToScores(Cursor cursor)
    {
        ScoresModel scoresModel= new ScoresModel();
        int winsIdx=cursor.getColumnIndex(Contract.ScoresTable.COLUMN_WINS);
        int lossesIdx=cursor.getColumnIndex(Contract.ScoresTable.COLUMN_LOSSES);
        scoresModel.setWins(cursor.getInt(winsIdx));
        scoresModel.setLosses(cursor.getInt(lossesIdx));
        return scoresModel;
    }
}
