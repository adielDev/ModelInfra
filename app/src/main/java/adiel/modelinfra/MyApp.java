package adiel.modelinfra;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by adiel on 26/04/2017.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("myFirstRealm.realm") // By default the name of db is "default.realm"
                .build();

        Realm.setDefaultConfiguration(configuration);
    }
}
