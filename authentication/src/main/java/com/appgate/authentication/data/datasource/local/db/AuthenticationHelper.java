package com.appgate.authentication.data.datasource.local.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.appgate.authentication.data.datasource.local.entity.AttemptContract.LocationEntry;
import com.appgate.authentication.data.datasource.local.entity.AttemptEntity;
import com.appgate.authentication.data.datasource.local.entity.LocationEntity;
import java.util.ArrayList;
import java.util.List;

import static com.appgate.authentication.data.datasource.local.entity.AttemptContract.AttemptEntry.COLUMN_NAME_CURRENT_LOCAL_TIME;
import static com.appgate.authentication.data.datasource.local.entity.AttemptContract.AttemptEntry.COLUMN_NAME_ID;
import static com.appgate.authentication.data.datasource.local.entity.AttemptContract.AttemptEntry.COLUMN_NAME_STATUS;
import static com.appgate.authentication.data.datasource.local.entity.AttemptContract.AttemptEntry.COLUMN_NAME_TIME_ZONE;
import static com.appgate.authentication.data.datasource.local.entity.AttemptContract.AttemptEntry.TABLE_NAME;
import static com.appgate.authentication.data.datasource.local.entity.AttemptContract.LocationEntry.COLUMN_NAME_LATITUDE;
import static com.appgate.authentication.data.datasource.local.entity.AttemptContract.LocationEntry.COLUMN_NAME_LONGITUDE;

public class AuthenticationHelper extends SQLiteOpenHelper {

    private static final int NUMBER_ONE = 1;
    private static final int NUMBER_TWO = 2;
    private static final int NUMBER_THREE = 3;
    private static final int DATABASE_VERSION = NUMBER_ONE;
    private static final String DATABASE_NAME = "Authentication.db";
    private static final String CREATE_ATTEMPTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_NAME_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME_STATUS + " BOOLEAN, " + COLUMN_NAME_TIME_ZONE + " TEXT,"
            + COLUMN_NAME_CURRENT_LOCAL_TIME + " TEXT" + ")";

    private static final String CREATE_LOCATION_TABLE = "CREATE TABLE " + LocationEntry.TABLE_NAME + "("
            + LocationEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME_LATITUDE + " TEXT, " + COLUMN_NAME_LONGITUDE + " TEXT " + ")";
    private static final String SQL_DELETE_ATTEMPTS_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SQL_DELETE_LOCATION_ENTRIES = "DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME;


    public AuthenticationHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ATTEMPTS_TABLE);
        sqLiteDatabase.execSQL(CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ATTEMPTS_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DELETE_LOCATION_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public void addLocation(LocationEntity locationEntity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_LATITUDE, locationEntity.getLatitude());
        values.put(COLUMN_NAME_LONGITUDE, locationEntity.getLongitude());
        db.insert(LocationEntry.TABLE_NAME, null, values);
        db.close();
    }

    public LocationEntity getLastLocation() {
        String selectQuery = "SELECT  * FROM " + LocationEntry.TABLE_NAME + " ORDER BY id DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setLatitude(cursor.getString(NUMBER_ONE));
        locationEntity.setLongitude(cursor.getString(NUMBER_TWO));
        cursor.close();
        return locationEntity;
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
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY id DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                AttemptEntity attempt = new AttemptEntity();
                attempt.setStatus(cursor.getInt(NUMBER_ONE));
                attempt.setTimeZone(cursor.getString(NUMBER_TWO));
                attempt.setCurrentLocalTime(cursor.getString(NUMBER_THREE));
                attemptList.add(attempt);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return attemptList;
    }
}
