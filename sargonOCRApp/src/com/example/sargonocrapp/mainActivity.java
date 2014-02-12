package com.example.sargonocrapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class mainActivity extends Activity

{
	// main thread activity to control flow of the application
	// written by Charles Waldron 
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        
        //retrieve a reference to the UI button and set up listener to go and get picture
        Button addNewItem = (Button)findViewById(R.id.addNewItem);
        addNewItem.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
                Intent intent = new Intent(mainActivity.this, ImageCapture.class);
                startActivity(intent);      
                finish();
        	}
        });
        
    }
	
}
