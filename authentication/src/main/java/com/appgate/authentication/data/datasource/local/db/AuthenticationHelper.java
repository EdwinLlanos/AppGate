package com.appgate.authentication.data.datasource.local.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.appgate.authentication.data.datasource.local.entity.AttemptEntity;
import java.util.ArrayList;
import java.util.List;

import static com.appgate.authentication.data.datasource.local.entity.AttemptContract.AttemptEntry.COLUMN_NAME_CURRENT_LOCAL_TIME;
import static com.appgate.authentication.data.datasource.local.entity.AttemptContract.AttemptEntry.COLUMN_NAME_ID;
import static com.appgate.authentication.data.datasource.local.entity.AttemptContract.AttemptEntry.COLUMN_NAME_STATUS;
import static com.appgate.authentication.data.datasource.local.entity.AttemptContract.AttemptEntry.COLUMN_NAME_TIME_ZONE;
import static com.appgate.authentication.data.datasource.local.entity.AttemptContract.AttemptEntry.TABLE_NAME;

public class AuthenticationHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Authentication.db";
    private static final String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_NAME_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME_STATUS + " BOOLEAN, " + COLUMN_NAME_TIME_ZONE + " TEXT,"
            + COLUMN_NAME_CURRENT_LOCAL_TIME + " TEXT" + ")";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public AuthenticationHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public void addAttempt(AttemptEntity attemptEntity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TIME_ZONE, attemptEntity.getTimeZone());
        values.put(COLUMN_NAME_CURRENT_LOCAL_TIME, attemptEntity.getCurrentLocalTime());
        values.put(COLUMN_NAME_STATUS, attemptEntity.getStatus());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<AttemptEntity> getAllAttempts() {
        List<AttemptEntity> attemptList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                AttemptEntity attempt = new AttemptEntity();
                attempt.setStatus(cursor.getInt(1));
                attempt.setTimeZone(cursor.getString(2));
                attempt.setCurrentLocalTime(cursor.getString(3));
                attemptList.add(attempt);
            } while (cursor.moveToNext());
        }

        return attemptList;
    }
}
