package com.example.sargonocrapp;


import com.example.sargonocrapp.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;



public class DisplayImageActv extends Activity 
{
	public String imagePath;
	public Bitmap bitmap;
	final int PIC_CROP = 1;
	ImageView imageview;
	ImageCrop myImageCrop;
	byte[] cropped;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_image);
        
        //get image path 
        imagePath = getIntent().getStringExtra("imagePath");      
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;       
        options.inSampleSize = 0;
//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        options.inJustDecodeBounds = false;
        options.inMutable = true;
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        imageview = (ImageView)findViewById(R.id.displayimage_imageview);
        imageview.setImageBitmap(bitmap);
        
        Button crop = (Button)findViewById(R.id.crop);
        crop.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
                Intent intent = new Intent(DisplayImageActv.this, ImageCropActv.class);
                intent.putExtra("imagePath", imagePath);
                startActivity(intent);
        	}
        });
              
        //retrieve a reference to the UI button and set up listener retake picture
        Button retake = (Button)findViewById(R.id.retake);
        retake.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
        		clearBitmap();
                Intent intent = new Intent(DisplayImageActv.this, ImageCaptureActv.class);
                startActivity(intent);      
                finish();
        	}
        }); 

   }

    @Override
    public void onBackPressed() 
    {
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    		//user is returning from capturing an image using the camera
    		 if(requestCode == PIC_CROP)
    		 {
    			//get the returned data
    			Bundle extras = data.getExtras();
    			//get the cropped bitmap
    			Bitmap thePic = extras.getParcelable("data");
    			//retrieve a reference to the ImageView
    	        imageview = (ImageView)findViewById(R.id.displayimage_imageview);
    			//display the returned cropped image
    			imageview.setImageBitmap(thePic);
    		}
    }
	private void clearBitmap()
	{
		if(bitmap != null)
		{
			bitmap.recycle();
			bitmap = null;
		}
	}
	public Bitmap getBitmap()
	{
		return bitmap;
	}
}//end of activity