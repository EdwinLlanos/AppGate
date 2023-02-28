package com.appgate.authentication.data.datasource.local.entity;

import android.provider.BaseColumns;

public final class AttemptContract {
    private AttemptContract() {
    }

    public static class AttemptEntry implements BaseColumns {
        public static final String TABLE_NAME = "attempt";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TIME_ZONE = "timeZone";
        public static final String COLUMN_NAME_CURRENT_LOCAL_TIME = "currentLocalTime";
        public static final String COLUMN_NAME_STATUS = "status";
    }
}