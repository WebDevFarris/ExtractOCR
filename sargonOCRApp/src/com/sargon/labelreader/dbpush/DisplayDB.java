package com.sargon.labelreader.dbpush;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.*;

import com.example.sargonocrapp.R;
import com.sargon.labelreader.database.Equipment;
import com.sargon.labelreader.database.MySQLiteHelper;

public class DisplayDB extends Activity {
	private TextView records;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_activity);
		
		records = (TextView)findViewById(R.id.result);
		MySQLiteHelper db = new MySQLiteHelper(this);
		
		List<Equipment> list = db.getAll();
		records.setText(list.toString());
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    public void addPart(View view)
    {
    	final Context context = this;
    	Intent intent = new Intent(context, AddItem.class);
    	finish();
    	startActivity(intent);
    }

}
