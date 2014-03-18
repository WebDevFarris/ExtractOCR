
package com.example.sargonocrapp;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sargonocrapp.R;
import com.sargon.labelreader.dbpush.AddItem;


/**
 * @author Bryan, Charles
 *
 */
public class ImageOCRActv extends Activity implements OCRTaskResponse
{	
	public static Bitmap OCRImage;
	private String TXTOCR = "";
	final int OCR_RESULT = 3;
	public static final String lang = "eng";
	private static final String TAG = "ImageOCR";
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/sargonOCRApp/";
	public String imagePath;
	public Button tryagain;
	public Button savedata;
	public ProgressBar pBar;
	File appDir;
	static ImageView OCRimageview;
		
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_layout);
       
        //get image path 
        imagePath = getIntent().getStringExtra("imagePath");      
        BitmapFactory.Options options = new BitmapFactory.Options();       
        options.inSampleSize = 8;
        OCRImage = BitmapFactory.decodeFile(imagePath, options);  
        OCRimageview = (ImageView)findViewById(R.id.ocrimage);
        
        //progress bar
        pBar = (ProgressBar)findViewById(R.id.progressBar2);
        
        //use button
	   	 savedata = (Button)findViewById(R.id.savedata);
	     savedata.setOnClickListener(new OnClickListener()
	     {
	     	public void onClick(View v)
	     	{
	             Intent intent = new Intent(ImageOCRActv.this, AddItem.class);
		         intent.putExtra("OCRtext", TXTOCR);
	             startActivity(intent);      
	             finish();
	     	}
	     });        
        
	     //try again button
		 tryagain = (Button)findViewById(R.id.tryagain);
		 tryagain.setOnClickListener(new OnClickListener()
		 {
		    public void onClick(View v)
		    {
		    	tryAgain();
		        new OCRTask(ImageOCRActv.this, imagePath).execute();
		    }
		}); 
		 
        new OCRTask(ImageOCRActv.this, imagePath).execute();
        
//      grayscale();
//      contrast(); 

 	  
	} 
 
    @Override
    public void onBackPressed() 
    {
    }

	@Override
	public void sendResults(String results) 
	{
		TXTOCR = results; 
 		if(TXTOCR == null)
 			TXTOCR = "No Text Was Found";
        TextView textView = (TextView)findViewById(R.id.ocrresults);
        textView.setText(TXTOCR);
        pBar = (ProgressBar)findViewById(R.id.progressBar2);
		pBar.setVisibility(View.GONE);
		tryagain.setVisibility(View.VISIBLE);
		savedata.setVisibility(View.VISIBLE);
		
	}
	
	public void tryAgain()
	{
		savedata.setVisibility(View.INVISIBLE);
    	tryagain.setVisibility(View.INVISIBLE);
    	pBar.setVisibility(View.VISIBLE);
	}

}//end class
