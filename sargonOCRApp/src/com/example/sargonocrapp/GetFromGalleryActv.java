package com.example.sargonocrapp;

//simple class to get an image from the gallery on the phone
//written by charles 

import com.example.sargonocrapp.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


public class GetFromGalleryActv extends Activity
{
	private static final int PICK_IMAGE = 1;
	String imageFilePath = "";
	Bitmap bitmap;
	ImageView imageview;
	

    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getfromgallery);
        
	    Button use = (Button)findViewById(R.id.ocr);
	    use.setOnClickListener(new OnClickListener()
	    {
	    	public void onClick(View v)
	    	{
                Intent intent = new Intent(GetFromGalleryActv.this, ImageCropActv.class);
	            intent.putExtra("imagePath", imageFilePath);               
                startActivity(intent);      
                finish();
	    	}
	    });
        
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,  "Select Picture"), PICK_IMAGE);
    }
    @Override
    public void onBackPressed() 
    {
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	Uri pic_uri = data.getData();
    	Cursor cursor = getContentResolver().query(pic_uri, new String[]
    			{ android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
    	cursor.moveToFirst();
    	imageFilePath = cursor.getString(0);
    	cursor.close();
    	
    	super.onActivityResult(requestCode, resultCode, data);
    
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;       
        options.inSampleSize = 8;
//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(imageFilePath, options);
            
        //set imageview with image just captured
        imageview = (ImageView)findViewById(R.id.getfromgallery);
        imageview.setImageBitmap(bitmap);
    }
}

