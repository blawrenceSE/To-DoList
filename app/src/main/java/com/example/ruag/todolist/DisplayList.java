package com.example.ruag.todolist;

/**
 * Created by ruag on 19.03.2018.
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class DisplayList extends Activity {
    private DBHelper listdb;
    EditText task;
    EditText content;
    private ListView obj;
    int id_To_Update = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_tasks);
        task = findViewById(R.id.editTextTask);
        content = findViewById(R.id.editTextContent);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        listdb = new DBHelper(this);
        ImageView iv = (ImageView)findViewById(R.id.imageViewOK);
        iv.setVisibility(View.VISIBLE);
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int tempValue = extras.getInt("id");

            if(tempValue>0){
                //means this is the view part not the add contact part.
                Cursor rs = listdb.getData(tempValue);
                id_To_Update = tempValue;
                rs.moveToFirst();
                String tempTask = rs.getString(rs.getColumnIndex(DBHelper.TASKS_COLUMN_TASK));
                String tempContent = rs.getString(rs.getColumnIndex(DBHelper.TASKS_COLUMN_CONTENT));

                if (!rs.isClosed())  {
                    rs.close();
                }
                task.setText((CharSequence)tempTask);
                task.setFocusable(true);
                task.setClickable(true);

                content.setText((CharSequence)tempContent);
                content.setFocusable(true);
                content.setClickable(true);
            }
        }
    }

    public void saveTask(View view) {

        Bundle extras = getIntent().getExtras();
        Pattern sPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$)$\n");
        if(extras !=null && sPattern.matcher(task.getText().toString()).matches()){
            int tempValue = extras.getInt("id");
            if(tempValue>0){
                if(listdb.updateTask(id_To_Update,task.getText().toString(),content.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                   backToMainActivity(view);
                } else{
                    Toast.makeText(getApplicationContext(), "Not Updated", Toast.LENGTH_SHORT).show();
                }
            } else if (listdb.isTaskDataAvailable(task.getText().toString())==0){
                if(listdb.insertTask(task.getText().toString(),content.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Added",
                            Toast.LENGTH_SHORT).show();
                    backToMainActivity(view);
                } else{
                    Toast.makeText(getApplicationContext(), "Not Added",
                            Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(getApplicationContext(), "Enter different task",
                        Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Enter valid values",
                    Toast.LENGTH_SHORT).show();
        }

    }
    public void backToMainActivity(View view){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }
}
