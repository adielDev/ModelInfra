package adiel.modelinfra.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import adiel.modelinfra.R;
import adiel.modelinfra.mydata.DatabaseHelper;


import static adiel.modelinfra.mydata.UsersContract.*;
public class UsersListActivity extends AppCompatActivity {


    ListView lv;
    ArrayList <User>usersNames;
    ArrayAdapter<User> namesAdapter;

    EditText etName;
    EditText etAge;

    private static class User{
        String name;
        int age;

        public User( String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        etName = (EditText) findViewById(R.id.etName);
        etAge= (EditText) findViewById(R.id.etAge);

        lv = (ListView) findViewById(R.id.lv);
        usersNames = new ArrayList<>();
        namesAdapter = new ArrayAdapter<User>(this,android.R.layout.simple_list_item_1,usersNames);
        lv.setAdapter(namesAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                updateRow(usersNames.get(i).name);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteRow(usersNames.get(i).name);
                return false;
            }
        });
        readData(null);
    }

    private void deleteRow(String name) {
        DatabaseHelper helper  = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] args = {name};
        int numRows = db.delete(UsersEntry.TABLE_NAME, UsersEntry.COLUMN_NAME + " =?", args);
        Log.d("Delete Rows", String.valueOf(numRows));
        readData(null);
    }

    private void updateRow(String name) {
        DatabaseHelper helper  = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] args = {name};
        ContentValues values = new ContentValues();
        values.put(UsersEntry.COLUMN_NAME, etName.getText().toString());
        int numRows = db.update(UsersEntry.TABLE_NAME, values, UsersEntry.COLUMN_NAME + " =?", args);
        Toast.makeText(this, "numRows:"+numRows, Toast.LENGTH_SHORT).show();
        Log.d("Update Rows", String.valueOf(numRows));
        db.close();
        readData(null);
    }

    public void readData(View view) {
        DatabaseHelper helper  = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] projection = {
                UsersEntry.COLUMN_NAME,
                UsersEntry.COLUMN_AGE};
        Cursor c = db.query(UsersEntry.TABLE_NAME,
                projection, null, null, null, null, null);
        int i = c.getCount();
        Log.d("Record Count", String.valueOf(i));
        String rowContent = "";
        usersNames.clear();
        while (c.moveToNext()) {
            usersNames.add(new User(c.getString(0),c.getInt(1)) );
            rowContent += c.getString(0) +//name
                    " - "+c.getInt(1);    // age
            //very bad practice there are betters and convenient ways for this
            Log.i("Row " + String.valueOf(c.getPosition()), rowContent);
            rowContent = "";
        }
        c.close();
        namesAdapter = new ArrayAdapter<User>(this,android.R.layout.simple_list_item_1,usersNames);
        lv.setAdapter(namesAdapter);

    }

    public void updateRow(View view) {
        Toast.makeText(UsersListActivity.this, "press the item you wanna edit", Toast.LENGTH_SHORT).show();
    }

    public void deleteRow(View view) {
        Toast.makeText(UsersListActivity.this, "long press on item you wanna delete", Toast.LENGTH_SHORT).show();
    }

    public void insertData(View view) {
        DatabaseHelper helper  = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UsersEntry.COLUMN_NAME, etName.getText().toString());
        values.put(UsersEntry.COLUMN_AGE, etAge.getText().toString());
        long user_id = db.insert(UsersEntry.TABLE_NAME, null, values);
        Toast.makeText(this, "user_id "+user_id, Toast.LENGTH_SHORT).show();
        readData(null);
    }
}
