package adiel.modelinfra.activities;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


import adiel.modelinfra.mydata.UsersContract;


public class UsersCursorAdapter extends CursorAdapter {
    public UsersCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                android.R.layout.simple_list_item_1, parent, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView todoTextView = (TextView) view.findViewById(android.R.id.text1);
        int textColumn = cursor.getColumnIndex(UsersContract.UsersEntry.COLUMN_NAME);
        String text = cursor.getString(textColumn);
        todoTextView.setText(text);
    }
}
