package adiel.modelinfra.mydata;

import android.net.Uri;
import android.provider.BaseColumns;

public final class UsersContract {
    //URI section
    public static final String CONTENT_AUTHORITY = "adiel.modelinfra.users";
    public static final String PATH_USERS="users";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);



    public static final class UsersEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USERS);

        // Table name
        public static final String TABLE_NAME = "users";
        //column names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_AGE = "age";
    }
}
