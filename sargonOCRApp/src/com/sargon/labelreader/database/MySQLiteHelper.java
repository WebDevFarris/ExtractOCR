package com.sargon.labelreader.database;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class MySQLiteHelper extends SQLiteOpenHelper {
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "LabelReader";
 
    // Books table name
    private static final String TABLE_EQUIPMENT = "Equipment";

    // Books Table Columns names
	private static final String KEY_SERIAL_NUMBER = "SERIAL_NUMBER";
	private static final String KEY_PART_NUMBER = "PART_NUMBER";
	private static final String KEY_ASSET_NUMBER = "ASSET_NUMBER";
	private static final String KEY_ENGINE_MODEL_TYPE = "ENGINE_MODEL_TYPE";

    private static final String[] COLUMNS = {KEY_SERIAL_NUMBER,KEY_PART_NUMBER,KEY_ASSET_NUMBER,KEY_ENGINE_MODEL_TYPE};
    
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_EQUIPMENT_TABLE = "CREATE TABLE Equipment ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
                "SERIAL_NUMBER TEXT, "+
                "PART_NUMBER TEXT, "+
                "ASSET_NUMBER TEXT, "+
                "ENGINE_MODEL_TYPE TEXT "+
                " );";
 
        // create books table
        db.execSQL(CREATE_EQUIPMENT_TABLE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS books");
 
        // create fresh books table
        this.onCreate(db);
    }
 
    public void add(Equipment item){
		        //for logging
		Log.d("addItem", item.toString()); 
		
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		
		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_SERIAL_NUMBER, item.SERIAL_NUMBER); 
		values.put(KEY_PART_NUMBER, item.PART_NUMBER);
		values.put(KEY_ASSET_NUMBER, item.ASSET_NUMBER);
		values.put(KEY_ENGINE_MODEL_TYPE, item.ENGINE_MODEL_TYPE);
		
		// 3. insert
		db.insert(TABLE_EQUIPMENT, // table
		        null, //nullColumnHack
		        values); // key/value -> keys = column names/ values = column values
		
		// 4. close
		db.close(); 
	}
    
    public List<Equipment> getAll() {
        List<Equipment> items = new LinkedList<Equipment>();
  
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_EQUIPMENT;
  
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
  
        // 3. go over each row, build book and add it to list
        Equipment item = null;
        if (cursor.moveToFirst()) {
            do {
                item = new Equipment();
                item.id=(Integer.parseInt(cursor.getString(0)));
                item.SERIAL_NUMBER=(cursor.getString(1));
                item.PART_NUMBER=(cursor.getString(2));
                item.ASSET_NUMBER=(cursor.getString(3));
                item.ENGINE_MODEL_TYPE=(cursor.getString(4));
  
                // Add book to books
                items.add(item);
            } while (cursor.moveToNext());
        }
  
        Log.d("getAllBooks()", items.toString());
  
        // return books
        return items;
    }
}