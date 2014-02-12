/**
 * 
 */
package com.example.sargonocrapp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.sargon.labelreader.dbpush.AddItem;

/**
 * @author Bryan, Charles
 *
 */
public class ImageOCR extends Activity 
{
	
	private TessBaseAPI baseAPI;	
	public static Bitmap OCRImage;
	private String TXTOCR = "";
	final int OCR_RESULT = 3;
	public static final String lang = "eng";
	private static final String TAG = "ImageOCR";
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/sargonOCRApp/";
	public String imagePath;
		
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orcr_layout);
        
	   	 Button savedata = (Button)findViewById(R.id.savedata);
	     savedata.setOnClickListener(new OnClickListener()
	     {
	     	public void onClick(View v)
	     	{
	             Intent intent = new Intent(ImageOCR.this, AddItem.class);
		         intent.putExtra("OCRtext", TXTOCR);
	             startActivity(intent);      
	             finish();
	     	}
	     });
        
        
	    String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };
		for (String path : paths) 
		{
			File dir = new File(path);
			if (!dir.exists()) 
			{
				if (!dir.mkdirs()) 
				{
					Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
					return;
				} 
				else 
				{
					Log.v(TAG, "Created directory " + path + " on sdcard");
				}
			}	
		}
	
		// lang.traineddata file with the app (in assets folder)
		// You can get them at:
		// http://code.google.com/p/tesseract-ocr/downloads/list
		// This area needs work and optimization
		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists())
		{
			try 
			{
	
				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
				//GZIPInputStream gin = new GZIPInputStream(in);
				OutputStream out = new FileOutputStream(DATA_PATH
						+ "tessdata/" + lang + ".traineddata");
	
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				//while ((lenf = gin.read(buff)) > 0) {
				while ((len = in.read(buf)) > 0) 
				{
					out.write(buf, 0, len);
				}
				in.close();
				//gin.close();
				out.close();
				
				Log.v(TAG, "Copied " + lang + " traineddata");
			} 
			catch (IOException e) {
				Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
			}
		}
	         
	        baseAPI = new TessBaseAPI();
	        
	        File externalStorageDirectory = Environment.getExternalStorageDirectory();
	
	        File appDir = new File(externalStorageDirectory, "sargonOCRapp");
	        if (!appDir.isDirectory())
	          appDir.mkdir();
	
	        final File baseDir = new File(appDir, "tessdata");
	        if (!baseDir.isDirectory())
	            baseDir.mkdir();
	        
	        //get image path 
	        imagePath = getIntent().getStringExtra("imagePath");      
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;       
	        options.inSampleSize = 8;
//	        this options allow android to claim the bitmap memory if it runs low on memory
	        options.inPurgeable = true;
	        options.inInputShareable = true;
	        options.inTempStorage = new byte[16 * 1024];
	        options.inJustDecodeBounds = false;	        
	        OCRImage = BitmapFactory.decodeFile(imagePath, options);
//	        contrast();
	        
	        baseAPI.init(appDir.getPath(), "eng",2);
	        baseAPI.setImage(OCRImage);
	        TXTOCR = baseAPI.getUTF8Text();
	        EditText editText = (EditText)findViewById(R.id.editText1);
	        editText.setText(TXTOCR);
	} 
    //functions to process image
    private void contrast()
    {
	 	final double GS_RED = 0.299;
	    final double GS_GREEN = 0.587;
	    final double GS_BLUE = 0.114;
	 
	    int A, R, G, B;
	    int pixel;
	 
	    // get image size
	    int width = OCRImage.getWidth();
	    int height = OCRImage.getHeight();
	 
	    // scan through every single pixel
	    for(int x = 0; x < width; ++x) 
	    {
	        for(int y = 0; y < height; ++y) 
	        {
	            // get one pixel color
	            pixel = OCRImage.getPixel(x, y);
	            // retrieve color of all channels
	            A = Color.alpha(pixel);
	            R = Color.red(pixel);
	            G = Color.green(pixel);
	            B = Color.blue(pixel);
	            // take conversion up to one single value
	            R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
	            // set new pixel color to output bitmap
	            OCRImage.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }	
    }//end of contrast   
}//end class
