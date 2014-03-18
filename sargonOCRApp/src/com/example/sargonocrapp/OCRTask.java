/**
 * 
 */
package com.example.sargonocrapp;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.googlecode.leptonica.android.*;
/**
 * @author CJ
 *
 */
public class OCRTask extends AsyncTask<Void, Void, String> 
{
	public OCRTaskResponse listener;
	private Bitmap OCRImage;;
	private String TXTOCR; 

	/**
	 * This background thread will handle the OCR functionality of the application.
	 */
	public OCRTask(Context context, String imagePath) 
	{
	     BitmapFactory.Options options = new BitmapFactory.Options();       
	     options.inSampleSize = 4;
	     options.inPreferQualityOverSpeed = true;
	     options.inMutable = true;
	     OCRImage = BitmapFactory.decodeFile(imagePath, options);
	     listener = (OCRTaskResponse) context;
		 Pix pix1 = ReadFile.readBitmap(OCRImage);
		 pix1 = Binarize.otsuAdaptiveThreshold(pix1);
		 OCRImage = WriteFile.writeBitmap(pix1);
		 pix1.recycle();
//		 contrast();
		 Pix pix2 = ReadFile.readBitmap(OCRImage);		 
		 float fraction = (float) 0.4;
		 pix2 = Enhance.unsharpMasking(pix2, 5, fraction);
		 float skew = Skew.findSkew(pix2);
		 pix2 = Rotate.rotate(pix2, skew);
		 OCRImage = WriteFile.writeBitmap(pix2);
		 pix2.recycle();
         ImageOCRActv.OCRimageview.setImageBitmap(OCRImage);
         listener = (OCRTaskResponse) context;

	}
	
	@Override 
	protected void onPreExecute()
	{
		  
          MainActv.baseAPI.setImage(OCRImage);
	}
	
	@Override
	protected String doInBackground(Void... params)
	{
		TXTOCR = MainActv.baseAPI.getUTF8Text();
        return TXTOCR;
	}
	
	@Override
	protected void onPostExecute(String TXTOCR)
	{
		listener.sendResults(TXTOCR);
		MainActv.baseAPI.clear();
		this.cancel(true);
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
    
    private void grayscale()
    {
    	//Uncomment to GrayScale Image
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
    }//end grayscale
}
