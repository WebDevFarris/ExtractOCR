package com.example.sargonocrapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class displayImage extends Activity 
{
	public String imagePath;
	Bitmap bitmap;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_image);
        
        //get image path 
        imagePath = getIntent().getStringExtra("imagePath");      
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;       
        options.inSampleSize = 8;
//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        
        //set imageview with image just captured
        ImageView imageview = (ImageView)findViewById(R.id.displayimage_imageview);
        imageview.setImageBitmap(bitmap);
        
        //retrieve a reference to the UI button and set up listener retake picture
        Button retake = (Button)findViewById(R.id.retake);
        retake.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
        		clearBitmap();
                Intent intent = new Intent(displayImage.this, ImageCapture.class);
                startActivity(intent);      
                finish();
        	}
        });
 
	    //retrieve a reference to the UI button and set up listener process image in ocr activity
	    Button use = (Button)findViewById(R.id.use);
	    use.setOnClickListener(new OnClickListener()
	    {
	    	public void onClick(View v)
	    	{
	    		clearBitmap();
                Intent intent = new Intent(displayImage.this, ImageOCR.class);
	            intent.putExtra("imagePath", imagePath);               
                startActivity(intent);      
                finish();
	    	}
	    });
    }
	private void clearBitmap()
	{
		if(bitmap != null)
		{
			bitmap.recycle();
			bitmap = null;
		}
	}
}