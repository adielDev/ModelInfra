package adiel.modelinfra.mydata;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;


public class UsersQueryHandler extends AsyncQueryHandler {
    public UsersQueryHandler(ContentResolver cr) {
        super(cr);
    }
}
