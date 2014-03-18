package com.example.sargonocrapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ImageCropActv extends Activity
{	
	public Bitmap bitmap;
	public Bitmap cropped;
	public ImageView imageview;
	public ImageCrop myImageCrop;
	protected Button done;
	protected Button selectall;
	private Intent intent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_crop);			
        final String imagePath = getIntent().getStringExtra("imagePath");
        Bitmap temp = BitmapFactory.decodeFile(imagePath);
        Drawable d = new BitmapDrawable(getResources(),temp);
        myImageCrop = new ImageCrop(this);
        myImageCrop = (ImageCrop)findViewById(R.id.cropimageview);
        myImageCrop.setImageDrawable(d);
        
        done = (Button)findViewById(R.id.done);
        done.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
        		removeButton();
        		getFilePath();
                startActivity(intent);      
                finish();   
        	}
        }); 
        selectall = (Button)findViewById(R.id.selectall);
        selectall.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
                intent = new Intent(ImageCropActv.this, ImageOCRActv.class);
                intent.putExtra("imagePath", imagePath);
                startActivity(intent);
                finish();
        	}
        });

	}
        @SuppressLint("SimpleDateFormat")
		public File getImage() throws IOException
        {
        	cropped = myImageCrop.getCroppedImage();
        	
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "sargonOCRApp");   
          // Create a media file name
          String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
          File mediaFile;
          mediaFile = new File(mediaStorageDir.getPath() + File.separator +
              "IMG_"+ timeStamp + ".jpg");
          FileOutputStream fos = new FileOutputStream(mediaFile);
          ByteArrayOutputStream stream = new ByteArrayOutputStream();
          cropped.compress(Bitmap.CompressFormat.PNG, 100, stream);
          byte[] data = stream.toByteArray();
          fos.write(data);
          fos.close();
          return mediaFile;
        }	
        void removeButton()
        {
      		done.setVisibility(View.GONE);
        }
        void getFilePath()
        {
    		File filePathtoImage = null;
			try {
				filePathtoImage = getImage();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            intent = new Intent(ImageCropActv.this, ImageOCRActv.class);
            intent.putExtra("imagePath", filePathtoImage.getPath());	
        }
        @Override
        public void onBackPressed() 
        {
        }
}

