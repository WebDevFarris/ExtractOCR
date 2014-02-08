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
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

/**
 * @author Bryan, Charles
 *
 */
public class ImageOCR extends Activity {
	
	TessBaseAPI baseAPI;
	
	public static Bitmap OCRImage;
	private String TXTOCR = "";
	final int OCR_RESULT = 1;
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
        String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };
        imagePath = getIntent().getStringExtra("imagePath");
        OCRImage = BitmapFactory.decodeFile(imagePath);

	for (String path : paths) {
		File dir = new File(path);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
				return;
			} else {
				Log.v(TAG, "Created directory " + path + " on sdcard");
			}
		}

	}
	
	// lang.traineddata file with the app (in assets folder)
	// You can get them at:
	// http://code.google.com/p/tesseract-ocr/downloads/list
	// This area needs work and optimization
	if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
		try {

			AssetManager assetManager = getAssets();
			InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
			//GZIPInputStream gin = new GZIPInputStream(in);
			OutputStream out = new FileOutputStream(DATA_PATH
					+ "tessdata/" + lang + ".traineddata");

			// Transfer bytes from in to out
			byte[] buf = new byte[1024];
			int len;
			//while ((lenf = gin.read(buff)) > 0) {
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			//gin.close();
			out.close();
			
			Log.v(TAG, "Copied " + lang + " traineddata");
		} catch (IOException e) {
			Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
		}
	}
        
     
        baseAPI = new TessBaseAPI();
        
        File externalStorageDirectory = Environment.getExternalStorageDirectory();

        File appDir = new File(externalStorageDirectory, "SargonOCR");
        if (!appDir.isDirectory())
          appDir.mkdir();

        final File baseDir = new File(appDir, "tessdata");
        if (!baseDir.isDirectory())
          baseDir.mkdir();
        
        baseAPI.init(appDir.getPath(), "eng");
        //send text back
        Intent sendTextBack = new Intent();
        TXTOCR = inspectFromBitmap(OCRImage);
        sendTextBack.putExtra("OCRText", TXTOCR);       
        setResult(0, sendTextBack);
        finish();
    }
        
    
    
    private String inspectFromBitmap(Bitmap bitmap)
    {   	  	
    	//We can do stuff to bitmap here before processing it in the library
        baseAPI.setImage(bitmap);
        String text = baseAPI.getUTF8Text();
        Toast.makeText(this, "Found this: " + text, Toast.LENGTH_LONG).show(); 
        return text;
     }   
}
