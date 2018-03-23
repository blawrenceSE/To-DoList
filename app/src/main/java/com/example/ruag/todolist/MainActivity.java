package com.example.ruag.todolist;

/**
 * Created by ruag on 19.03.2018.
 */

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "MESSAGE";
    DBHelper listdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitleColor(Color.BLACK);
        listdb = new DBHelper(this);
        reloadData();
    }

    public void reloadData(){
        final SwipeMenuListView smlv = (SwipeMenuListView) findViewById(R.id.listViewTasks);
        ArrayList tasks_list = listdb.getAllTasks();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, tasks_list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.CYAN);

                // Generate ListView Item using TextView
                return view;
            }
        };

        smlv.setAdapter(arrayAdapter);
        // Swipe Menu Creation
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // Add Item to Swipe Menu
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F,0x25)));

                deleteItem.setWidth(200);
                deleteItem.setIcon(R.drawable.ic_dustbin_white);
                menu.addMenuItem(deleteItem);
            }
        };
        smlv.setMenuCreator(creator);
        smlv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index){
                    case 0:
                        String selectedFromList = (String) smlv.getItemAtPosition(position);
                        listdb.deleteTask(selectedFromList);
                        reloadData();
                        break;
                    case 1:
                        // To add another item case
                        break;
                }


                return false;
            }
        });
        smlv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                int id_To_Search = arg2 + 1;

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id_To_Search);

                Intent intent = new Intent(getApplicationContext(),DisplayList.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }

    public void onClickAddTaskImageView(View view){
        Bundle dataBundle = new Bundle();
        dataBundle.putInt("id", 0);

        Intent intent = new Intent(getApplicationContext(), DisplayList.class);
        intent.putExtras(dataBundle);
        startActivity(intent);

    }
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }

}
