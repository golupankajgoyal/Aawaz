package com.example.aawaz.Database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class PhoneContract {

    private PhoneContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.aawaz";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ITEMS = "items";

    public static final class ItemEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);

        /**
         * The MIME type for the list of the Items
         */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        /**
         * The MIME type for the a single item of table
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        public final static String TABLE_NAME = "items";

        public final static String LOCAL_TABLE_NAME = "info";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_PHONE_NO = "phone";

        public final static String COLUMN_CONTACT_NO = "contact";

        public final static String COLUMN_USER_ID = "userId";

        public final static String COLUMN_RELATIVE_1 = "r1";

        public final static String COLUMN_RELATIVE_2 = "r2";

    }
}
