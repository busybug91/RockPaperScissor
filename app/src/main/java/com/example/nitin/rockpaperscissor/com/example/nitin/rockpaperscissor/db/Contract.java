package com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db;

import android.provider.BaseColumns;

/**
 * Created by nitin on 9/19/14.
 */
public final class Contract {

    public static final class UsersTable implements BaseColumns{

        public static final String TABLE_NAME="users";
        public static final String COLUMN_USER_NAME="username";
        public static final String COLUMN_USER_ID="userid";
        public static final String COLUMN_PASSWORD="password";
        public static final String COLUMN_AGE="age";
        public static final String COLUMN_SEX="sex";

    }

    public static final class ScoresTable implements BaseColumns{

        public static final String TABLE_NAME="scores";
        public static final String COLUMN_USER_ID="userid";
        public static final String COLUMN_WINS="wins";
        public static final String COLUMN_LOSSES="losses";
    }


}
