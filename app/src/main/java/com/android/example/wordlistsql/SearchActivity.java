package com.android.example.wordlistsql;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.example.wordlistsql.EditWordActivity;
import com.android.example.wordlistsql.R;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = EditWordActivity.class.getSimpleName();
    private TextView mTextView;
    private EditText mEditWordView;
    private DBHelper mDB;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mTextView = (TextView) findViewById(R.id.searchResult);
        mEditWordView = (EditText) findViewById(R.id.search);
        mDB = new DBHelper(this);
    }

    public void showResult(View view){
        String word = mEditWordView.getText().toString();
        mTextView.setText("Result for " + word + ":\n\n");
        Cursor cursor = mDB.search(word);
        if (cursor != null & cursor.getCount() > 0) {
            cursor.moveToFirst();
            int index;
            String result;
            do {

                index = cursor.getColumnIndex("WORD");
                result = cursor.getString(index);
                mTextView.append(result + "\n");
            } while (cursor.moveToNext()); // Returns true or false
            cursor.close();
        }
    }


}