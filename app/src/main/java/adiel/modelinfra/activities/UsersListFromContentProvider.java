package adiel.modelinfra.activities;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.app.LoaderManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import static adiel.modelinfra.mydata.UsersContract.*;

import adiel.modelinfra.R;
import adiel.modelinfra.mydata.UsersQueryHandler;

public class UsersListFromContentProvider extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int URL_LOADER = 0;

    Cursor cursor;
    UsersCursorAdapter adapter;

    EditText etName;
    EditText etAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list_from_content_provider);
        getLoaderManager().initLoader(URL_LOADER, null, this);

        final ListView lv = (ListView) findViewById(R.id.lv);

        etName = (EditText) findViewById(R.id.etName);
        etAge= (EditText) findViewById(R.id.etAge);

        adapter = new UsersCursorAdapter(this, cursor, false);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(UsersListFromContentProvider.this, "press read for refresh", Toast.LENGTH_SHORT).show();
                updateRow(i);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteRow(i);
                return false;
            }
        });

    }

    private void deleteRow(int position) {
        cursor.moveToPosition(position);
        int columnIndex= cursor.getColumnIndex(UsersEntry._ID);
        int id = cursor.getInt(columnIndex);

        String[] args = new String[]{String.valueOf(id)};
        UsersQueryHandler handler = new UsersQueryHandler(
                this.getContentResolver());
        Uri uri =  Uri.withAppendedPath(UsersEntry.CONTENT_URI, String.valueOf(id));
        handler.startDelete(1,
                null,
                uri,
                UsersEntry._ID + " =?",
                args);

    }

    private void updateRow(int position) {
        cursor.moveToPosition(position);
        int columnIndex= cursor.getColumnIndex(UsersEntry._ID);
        int id = cursor.getInt(columnIndex);
        String[] args = {String.valueOf(id)};
        ContentValues values = new ContentValues();
        values.put(UsersEntry.COLUMN_NAME, etName.getText().toString());
        int numRows = getContentResolver().update(UsersEntry.CONTENT_URI, values,
                UsersEntry._ID + "=?", args);
        values.put(UsersEntry.COLUMN_AGE, etAge.getText().toString());

        Log.d("Update Rows ", String.valueOf(numRows));

    }
private void updateRowAsync(int position) {
        cursor.moveToPosition(position);
        int columnIndex= cursor.getColumnIndex(UsersEntry._ID);
        int id = cursor.getInt(columnIndex);
        String[] args = {String.valueOf(id)};
        ContentValues values = new ContentValues();
        values.put(UsersEntry.COLUMN_NAME, etName.getText().toString());
        UsersQueryHandler handler = new UsersQueryHandler(
                this.getContentResolver());
        handler.startUpdate(1,
                null,
                UsersEntry.CONTENT_URI,
                values,
                UsersEntry._ID + "=?",
                args);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                UsersEntry._ID ,
                UsersEntry.COLUMN_NAME ,
                UsersEntry.COLUMN_AGE};

        return new CursorLoader(
                this,
                UsersEntry.CONTENT_URI,
                projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        this.cursor =cursor;
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    public void insertData(View view) {
        ContentValues values = new ContentValues();
        values.put(UsersEntry.COLUMN_NAME, etName.getText().toString());
        values.put(UsersEntry.COLUMN_AGE, etAge.getText().toString());
        UsersQueryHandler handler = new UsersQueryHandler(
                this.getContentResolver());
        handler.startInsert(1, null, UsersEntry.CONTENT_URI,
                values );
    }

    public void updateRow(View view) {
        Toast.makeText(UsersListFromContentProvider.this, "press the item you wanna edit", Toast.LENGTH_SHORT).show();
    }

    public void deleteRow(View view) {
        Toast.makeText(UsersListFromContentProvider.this, "long press on item you wanna delete", Toast.LENGTH_SHORT).show();
    }

    public void readData(View view) {
        getLoaderManager().restartLoader(URL_LOADER, null, this);
    }
}
