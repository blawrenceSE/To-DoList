package com.example.ruag.todolist;

/**
 * Created by ruag on 19.03.2018.
 */

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "todolist";
    public static final String TASKS_TABLE_NAME = "TaskList";
    public static final String TASKS_COLUMN_TASK = "task";
    public static final String TASKS_COLUMN_CONTENT = "content";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String querySQL = "CREATE TABLE TaskList (id integer primary key, task text not null unique,content text)";
        db.execSQL(querySQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS TaskList");
        onCreate(db);
    }

    public boolean insertTask (String task, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);
        contentValues.put("content", content);
        db.insert("TaskList", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from TaskList where id="+id+"", null );
        return res;
    }

    public int isTaskDataAvailable(String task) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "SELECT * FROM TaskList WHERE task=\""+task+"\";";
        Cursor res =  db.rawQuery( sqlQuery, null );
        int bool = res.getCount();
        return bool;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TASKS_TABLE_NAME);
        return numRows;
    }

    public boolean updateTask (Integer id, String task, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);
        contentValues.put("content", content);
        db.update("TaskList", contentValues, "id = ?" , new String[] { Integer.toString(id) } );
        return true;
    }

    public void deleteTask (String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM TaskList WHERE task=\""+task+"\";";
        db.execSQL(query);
    }

    public ArrayList<String> getAllTasks() {
        ArrayList<String> array_list = new ArrayList<String>();
        hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from TaskList", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(TASKS_COLUMN_TASK)));
            res.moveToNext();

        }
        return array_list;
    }
}
