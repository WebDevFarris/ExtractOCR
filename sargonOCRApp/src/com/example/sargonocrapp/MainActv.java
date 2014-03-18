package com.example.sargonocrapp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

public class MainActv extends Activity
{
	// splash screen and loads tess data
	// written by Charles Waldron 

	public static final String lang = "eng";
	private static final String TAG = "ImageOCR";
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/sargonOCRapp/";
	File appDir;
	protected Button button1;
	protected Button button2;
	protected LinearLayout ProgressLayout;
	protected TextView text;
	protected ProgressBar pBar;
	public static TessBaseAPI baseAPI;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        
        //retrieve a reference to the buttons, text, and Progress Bar
        button1 = (Button)findViewById(R.id.addNewItem);
        button2 = (Button)findViewById(R.id.getFromGallery);
        text = (TextView)findViewById(R.id.textView1);
        pBar = (ProgressBar)findViewById(R.id.progressBar1);
        ProgressLayout = (LinearLayout)findViewById(R.id.ProgressLayout);

        //Set Button Onclick Listeners
        button1.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
                Intent intent = new Intent(MainActv.this, ImageCaptureActv.class);
                startActivity(intent);      
                finish();
        	}
        });
        
        button2.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
                Intent intent = new Intent(MainActv.this, GetFromGalleryActv.class);
                startActivity(intent);      
                finish();
        	}
        });
        
		File externalStorageDirectory = Environment.getExternalStorageDirectory();
    	appDir = new File(externalStorageDirectory, "sargonOCRapp");
        File dir = new File(appDir.getPath() + "tessdata\\");
               
        //If the tessdata folder doesn't exist then we need to add the files
        if(!dir.exists())
        {
        	loadData load = new loadData(this);
            load.execute(); 
        }
        else
        {
        	//tessdata exists, initialize the Tesseract Object
        	baseAPI = new TessBaseAPI(); 
            baseAPI.init(appDir.getPath(), "eng",2); //using the most accurate setting by using cubes
            baseAPI.setVariable("load_system_dawg", "false");
            baseAPI.setVariable("load_freq_dawg", "false");
        }                
    }
    
    /*
     * Asynchronously loads the necessary Tesseract files
     * 
     * Post: User's device has sargonOCRapp\tessdata directory holding 10 Tesseract Files
     */
	private class loadData extends AsyncTask<Void,String,Void>
	{
		Context context;
		
		//We have 10 files to install
		int numTotalFiles = 10;
		float completedPercentage = 0;
		int numCompletedFiles = 0;
		
		loadData(Activity activity) 
		{
			context = activity.getBaseContext();
		}
				
		@Override
		protected Void doInBackground(Void... params) 
		{
			//Make the directory if it is not already on the user's device
		    String[] paths = new String[] { DATA_PATH + "tessdata/" };
		    
			for (String path : paths) 
			{
				File dir = new File(path);
				if (!dir.exists()) 
				{
					if (!dir.mkdirs()) 
					{
						Log.v(TAG, "ERROR: Creation of directory " + path + " failed");
					} 
					else 
					{
						Log.v(TAG, "Created directory " + path + " successfully");
						
						//Path had to be created, lets add the files from the assets folder
						try 
						{
							Log.d(TAG, "Checking for language data (TesseractData.zip) in application assets...");
							
							boolean installed = installZipFromAssets("TesseractData.zip", dir);
							
							if(installed)
								{
									Log.v(TAG, "Copied " + lang + " traineddata successfully");
									 publishProgress("Initializing Tesseract...");
								}
							else Log.v(TAG,"Error occured during installation from assets");
						} 
						catch(IOException e)
						{
							//Error occurred
							Log.e(TAG, "Error occured during installion from assets: " + e.getMessage());
						}
					}
				}	
			}
			
			
			baseAPI = new TessBaseAPI(); 
            baseAPI.init(appDir.getPath(), "eng",2);
            baseAPI.setVariable("load_system_dawg", "false");
            baseAPI.setVariable("load_freq_dawg", "false");
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(String... update)
		{
			super.onProgressUpdate(update);
			if(update[0] != "Initializing Tesseract...")
			{
				Log.i(TAG, "onProgressUpdate(): " + String.valueOf(update[0]));
				text.setText(update[0]  + update[1] + "%");
				pBar.setProgress(Integer.parseInt(update[1]));
			}
			else
			{
				Log.i(TAG, "onProgressUpdate(): " + String.valueOf(update[0]));
				text.setText(update[0]);
			}
			
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			setDisplay();
		}
			
		/**
		   * Unzip the given Zip file, located in application assets, into the given
		   * destination file.
		   * 
		   * @param sourceFilename
		   *          Name of the file in assets
		   * @param destinationDir
		   *          Directory to save the destination file in
		   * @param destinationFile
		   *          File to unzip into, excluding path
		   * @return
		   * @throws IOException
		   * @throws FileNotFoundException
		 */
		  private boolean installZipFromAssets(String sourceFilename, File destinationDir) throws IOException, FileNotFoundException 
		  {
			  
			publishProgress("Begin Uncompressing...", "0");		    
			  
		    ZipInputStream inputStream = new ZipInputStream(context.getAssets().open(sourceFilename));
	
		    // Loop through all the files and folders in the zip archive
		    for (ZipEntry entry = inputStream.getNextEntry(); entry != null; entry = inputStream.getNextEntry()) 
		    {
		      File destinationFile = new File(destinationDir, entry.getName());
	
		      if (entry.isDirectory()) 
		      {
		        destinationFile.mkdirs();
		      } 
		      else 
		      {
		        // Note getSize() returns -1 when the zipfile does not have the size set
		        long zippedFileSize = entry.getSize();
	
		        // Create a file output stream
		        FileOutputStream outputStream = new FileOutputStream(destinationFile);
		        final int BUFFER = 8192;
	
		        // Buffer the output to the file
		        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, BUFFER);
		        int unzippedSize = 0;
	
		        // Write the contents
		        int count = 0;
		        Integer percentComplete = 0;
		        Integer percentCompleteLast = 0;
		        byte[] data = new byte[BUFFER];
		        while ((count = inputStream.read(data, 0, BUFFER)) != -1) 
		        {
		          bufferedOutputStream.write(data, 0, count);
		          unzippedSize += count;
		          percentComplete = (int) ((unzippedSize / (long) zippedFileSize) * 100);
		          if (percentComplete > percentCompleteLast) 
		          {
		        	numCompletedFiles++; 
		        	percentComplete = (int) (((float) numCompletedFiles / (float) numTotalFiles) * 100);
		            publishProgress("Uncompressing data...", percentComplete.toString());
		            //percentCompleteLast = percentComplete;
		          }
		        }
		        
		        bufferedOutputStream.close();
		      }
		      
		      inputStream.closeEntry();
		    }
		    
		    inputStream.close();
		    return true;
		  }
			  
	}//end LoadData Task
	
	void setDisplay()
	{
		//pBar.setVisibility(View.GONE);
		//text.setVisibility(View.GONE);
		ProgressLayout.setVisibility(View.GONE);
		button1.setVisibility(View.VISIBLE);
		button2.setVisibility(View.VISIBLE);
	}
}
