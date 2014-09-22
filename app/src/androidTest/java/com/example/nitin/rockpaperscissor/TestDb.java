package com.example.nitin.rockpaperscissor;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db.Contract;
import com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db.RPSDbHelper;
import com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db.UserDAO;
import com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db.UserModel;

/**
 * Created by nitin on 9/20/14.
 */
public class TestDb extends AndroidTestCase {

    public String DATABASE_NAME="rpsdb";
    /*
    public void testCreateDb() {

        //making sure that if a database alreary exists, it is deleted
        mContext.deleteDatabase(DATABASE_NAME);
        SQLiteDatabase sqLiteDatabase = new RPSDbHelper(mContext).getReadableDatabase();
        assertTrue(sqLiteDatabase.isOpen());

        sqLiteDatabase.close();
    }
*/


    public void testInsertAndRead()
    {
        mContext.deleteDatabase(DATABASE_NAME);
        SQLiteDatabase sqLiteDatabase= new RPSDbHelper(mContext).getWritableDatabase();
        UserModel user= new UserModel().generateTestUser();
        ContentValues cv=new ContentValues();
        cv.put(Contract.UsersTable.COLUMN_USER_NAME,user.getUserName());
        cv.put(Contract.UsersTable.COLUMN_AGE,user.getAge());
        cv.put(Contract.UsersTable.COLUMN_SEX,user.getSex());
        sqLiteDatabase.beginTransaction();
        long rowId=sqLiteDatabase.insert(Contract.UsersTable.TABLE_NAME,null,cv);

        assertEquals(1, rowId);

        //ideally should not be inserted
        long rowId2=sqLiteDatabase.insert(Contract.UsersTable.TABLE_NAME,null,cv);

        sqLiteDatabase.endTransaction();
        //checking it was not re-inserted
        assertEquals(-1,rowId2);


        ContentValues cv2= new ContentValues();
        cv2.put(Contract.ScoresTable.COLUMN_WINS,user.getScore().getWins());
        cv2.put(Contract.ScoresTable.COLUMN_LOSSES,user.getScore().getLosses());
        sqLiteDatabase.beginTransaction();
        rowId=sqLiteDatabase.insert(Contract.ScoresTable.TABLE_NAME,null,cv2);
        sqLiteDatabase.endTransaction();
        assertEquals(1,rowId);

        sqLiteDatabase.close();

        String projection[]={Contract.UsersTable.COLUMN_USER_NAME,Contract.UsersTable.COLUMN_AGE,Contract.UsersTable.COLUMN_SEX};
        sqLiteDatabase=new RPSDbHelper(mContext).getReadableDatabase();

        Cursor usersCursor=sqLiteDatabase.rawQuery("SELECT * FROM "+Contract.UsersTable.TABLE_NAME,null);


        //TODO: Find out why this did not work.
//        Cursor usersCursor = sqLiteDatabase.query(
//                Contract.UsersTable.TABLE_NAME,  // Table to Query
//                projection, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null, // columns to group by
//                null, // columns to filter by row groups
//                null  // sort order
//        );

        assertNotNull(usersCursor);

        if(usersCursor!=null)
        {
           // usersCursor.moveToFirst();
            assertTrue(usersCursor.moveToFirst());
            int uNameIdx=usersCursor.getColumnIndex(Contract.UsersTable.COLUMN_USER_NAME);
            int ageIdx=usersCursor.getColumnIndex(Contract.UsersTable.COLUMN_AGE);
            int sexIdx=usersCursor.getColumnIndex(Contract.UsersTable.COLUMN_SEX);


            Log.d("TEST", Integer.toString(uNameIdx) + "" + Integer.toString(ageIdx) + Integer.toString(sexIdx));

            assertFalse(uNameIdx == -1);
            assertFalse(ageIdx == -1);
            assertFalse(sexIdx == -1);

           int age= usersCursor.getInt(ageIdx);
            String sex= usersCursor.getString(sexIdx);
            String userName=usersCursor.getString(uNameIdx);

            Log.d("TEST","age is"+Integer.toString(age)+" Name is: "+userName+" sex is "+sex);

            assertEquals(cv.get(Contract.UsersTable.COLUMN_USER_NAME), userName);
            assertEquals(cv.get(Contract.UsersTable.COLUMN_SEX),sex);
            assertEquals(cv.get(Contract.UsersTable.COLUMN_AGE),age);
        }
        usersCursor.close();
        sqLiteDatabase.close();
    }


    public void testDaoInterface()
    {

        UserModel user= new UserModel().generateTestUser();
        UserDAO userDao= new UserDAO(mContext);
        long rowId=userDao.saveUser(user);
        assertEquals(1, rowId);
        long rowId2=userDao.saveScore(user.getScore());
        assertEquals(1,rowId2);

    }
}
