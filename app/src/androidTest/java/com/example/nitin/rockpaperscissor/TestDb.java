package com.example.nitin.rockpaperscissor;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db.Contract;
import com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db.RPSDbHelper;
import com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db.UserModel;

/**
 * Created by nitin on 9/20/14.
 */
public class TestDb extends AndroidTestCase {

    public String DATABASE_NAME="rpsdb";
    public void testCreateDb(){

        //making sure that if a database alreary exists, it is deleted
        mContext.deleteDatabase(DATABASE_NAME);
        SQLiteDatabase sqLiteDatabase= new RPSDbHelper(mContext).getReadableDatabase();
        assertTrue(sqLiteDatabase.isOpen());

    }

    public void testInsert()
    {
        mContext.deleteDatabase(DATABASE_NAME);
        SQLiteDatabase sqLiteDatabase= new RPSDbHelper(mContext).getReadableDatabase();
        UserModel user= new UserModel().generateTestUser();
        ContentValues cv=new ContentValues();
        cv.put(Contract.UsersTable.COLUMN_USER_NAME,user.getUserName());
        cv.put(Contract.UsersTable.COLUMN_AGE,user.getAge());
        cv.put(Contract.UsersTable.COLUMN_SEX,user.getSex());
        sqLiteDatabase.beginTransaction();
        long rowId=sqLiteDatabase.insert(Contract.UsersTable.TABLE_NAME,null,cv);
        //ideally should not be inserted
        long rowId2=sqLiteDatabase.insert(Contract.UsersTable.TABLE_NAME,null,cv);

        sqLiteDatabase.endTransaction();
        assertEquals(1, rowId);
        //checking it was not re-inserted
        assertEquals(-1,rowId2);

        ContentValues cv2= new ContentValues();
        cv2.put(Contract.ScoresTable.COLUMN_WINS,user.getScore().getWins());
        cv2.put(Contract.ScoresTable.COLUMN_LOSSES,user.getScore().getLosses());
        sqLiteDatabase.beginTransaction();
        rowId=sqLiteDatabase.insert(Contract.ScoresTable.TABLE_NAME,null,cv2);
        sqLiteDatabase.endTransaction();
        assertEquals(1,rowId);

    }
}
