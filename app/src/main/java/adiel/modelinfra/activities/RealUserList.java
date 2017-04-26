package adiel.modelinfra.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.UUID;

import adiel.modelinfra.R;
import adiel.modelinfra.realmdata.RealmUser;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmResults;

public class RealUserList extends AppCompatActivity {

    private static final String TAG = "adiel";
    ListView lv;

    EditText etName;
    EditText etAge;

    private Realm myRealm;
    private RealmAsyncTask realmAsyncTask;
    RealmResults<RealmUser> userList;
    ArrayAdapter<RealmUser> realmUserArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myRealm = Realm.getDefaultInstance();

        setContentView(R.layout.activity_realm_user_list);

        etName = (EditText) findViewById(R.id.etName);
        etAge= (EditText) findViewById(R.id.etAge);

        lv = (ListView) findViewById(R.id.lv);

        userList = myRealm.where(RealmUser.class).findAll();
        realmUserArrayAdapter = new ArrayAdapter<RealmUser>(this, android.R.layout.simple_list_item_1, userList);

        lv.setAdapter(realmUserArrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                update(position);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteUser(position);
                return false;
            }
        });
    }

    private void deleteUser(final int position) {
        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                userList.deleteFromRealm(position);
                realmUserArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    public void addUserToRealm_Synchronously(View view) {

        final String id = UUID.randomUUID().toString();
        final String name 				= etName.getText().toString();
        final int age 					= Integer.valueOf(etAge.getText().toString());

//		try {
//			myRealm.beginTransaction();
//			myRealm.commitTransaction();
//		} catch (Exception e) {
//			myRealm.cancelTransaction();
//		}

        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                RealmUser user = realm.createObject(RealmUser.class, id);
                user.setName(name);
                user.setAge(age);
            }
        });
    } // Add data to Realm using Main UI Thread. Be Careful: As it may BLOCK the UI.
    public void addUserToRealm_ASynchronously(View view) {

        final String id = UUID.randomUUID().toString();
        final String name 				= etName.getText().toString();
        final int age 					= Integer.valueOf(etAge.getText().toString());

        realmAsyncTask = myRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                RealmUser user = realm.createObject(RealmUser.class, id);
                user.setName(name);
                user.setAge(age);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(RealUserList.this, "Added Successfully", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(RealUserList.this, "Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    } // Add Data to Realm in the Background Thread.

    public void displayAllUsers(View view) {

        userList = myRealm.where(RealmUser.class).findAll();
        realmUserArrayAdapter.notifyDataSetChanged();
        displayQueriedUsers(userList);
    }

    public void update(int position) {
        final RealmUser realmUser = userList.get(position);
        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmUser.setName(etName.getText().toString());
                realmUser.setAge(Integer.valueOf(etAge.getText().toString()));
            }

        });
    }

    private void displayQueriedUsers(RealmResults<RealmUser> userList) {

        StringBuilder builder = new StringBuilder();

        for (RealmUser user : userList) {
            builder.append("ID: ").append(user.getId());
            builder.append("\nName: ").append(user.getName());
            builder.append(", Age: ").append(user.getAge());

        }

        Log.i(TAG + " UserList", builder.toString());
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (realmAsyncTask != null && !realmAsyncTask.isCancelled()) {
            realmAsyncTask.cancel();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }

    public void updateRow(View view) {
        Toast.makeText(this, "click item to update", Toast.LENGTH_SHORT).show();
    }
}
