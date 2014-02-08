package com.sargon.labelreader.dbpush;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.sargonocrapp.R;
import com.sargon.labelreader.database.Equipment;
import com.sargon.labelreader.database.MySQLiteHelper;
@SuppressLint("NewApi")
public class AddItem extends Activity implements BoxSelectionDialog.Communicator, SubmitConfirmDialog.Communicator, CheckBoxSelectionDialog.Communicator{
	
	private FragmentManager manager;
	private EditText target;
	private String ocrResults;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		manager = getFragmentManager();
		//get ocr text results
		Intent intentResults = getIntent();
		ocrResults = intentResults.getExtras().getString("resultTxt");
		linearParse();
	}
	
	public String sendResults()
	{
		return ocrResults;
	}
	
	public void linearParse()
	{

		ParseData linear = new ParseData( ocrResults );
		HashMap results = linear.linearMatch();
		EditText set = (EditText)findViewById(R.id.SERIAL_NUMBER);
		set.setText((String)results.get("serial"));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void insert(View view)
    {
    	SubmitConfirmDialog alert = new SubmitConfirmDialog();
    	alert.show(manager, "SubmitConfirm");
    }
   
	public void editField(View view)
    {
    	BoxSelectionDialog boxSelection = new BoxSelectionDialog();
    	boxSelection.show(manager, "boxSelection");
    }
		
    public void toDB(View view)
    {
    	Intent intent = new Intent(AddItem.this, DisplayDB.class);
    	//finish();
    	startActivity(intent);
    }
    
	@Override
	public void onDialogMessage(String selected) {
    	if(selected.equalsIgnoreCase(getResources().getString(R.string.serial_number)))
    		target = (EditText)findViewById(R.id.SERIAL_NUMBER);
    	else if(selected.equalsIgnoreCase(getResources().getString(R.string.asset_number)))
    		target = (EditText)findViewById(R.id.ASSET_NUMBER);
    	else if(selected.equalsIgnoreCase(getResources().getString(R.string.part_number)))
    		target = (EditText)findViewById(R.id.PART_NUMBER);
    	else
    		target = (EditText)findViewById(R.id.ENGINE_MODEL_TYPE);
    	//target.setText(selected);
    	CheckBoxSelectionDialog boxSelection = new CheckBoxSelectionDialog();
    	boxSelection.setOCRText(ocrResults);
    	boxSelection.show(manager, "CheckBoxSelection");
	}

	@Override
	public void onConfirmMessage() {
		// TODO Auto-generated method stub
		MySQLiteHelper db = new MySQLiteHelper(this);
    	EditText SerialEntry = (EditText)findViewById(R.id.SERIAL_NUMBER);
		EditText PartEntry = (EditText)findViewById(R.id.PART_NUMBER);
		EditText AssetEntry = (EditText)findViewById(R.id.ASSET_NUMBER);
		EditText EngineEntry = (EditText)findViewById(R.id.ENGINE_MODEL_TYPE);
		Equipment entry = new Equipment(SerialEntry.getText().toString(),
				PartEntry.getText().toString(),
				AssetEntry.getText().toString(),
				EngineEntry.getText().toString());
		db.add(entry);
		db.close();
		Toast.makeText(this,"Submission Complete", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCheckBoxMessage(String data) {
		// TODO Auto-generated method stub
		target.setText(data);
	}
}