package com.sargon.labelreader.dbpush;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;


public class UploadData extends  AsyncTask<String, Void, String>
{

	@Override
	protected String doInBackground(String... arg0) 
	{
		String message = arg0[0];

        // make sure the fields are not empty
        if (message.length()>0)
        {
	  	    HttpClient httpclient = new DefaultHttpClient();
	   	    HttpPost httppost = new HttpPost("http://www.charlesjared.com/OCRapp/OCRdisplayScrip.php");
	   	 try {
	   	   List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	       nameValuePairs.add(new BasicNameValuePair("id", "12345"));
	       nameValuePairs.add(new BasicNameValuePair("message", message));
	       httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	   	   HttpResponse response = httpclient.execute(httppost);
           String text = EntityUtils.toString(response.getEntity());          
           Log.i("","response = "+text); 
	   	 } 
	   	 catch (ClientProtocolException e) 
	   	 {  
	   		 Log.e("Error:",e+"");
	   		 e.printStackTrace();
	   	 }
	   	 catch (IOException e) 
	   	 {  
	   		 Log.e("Error:",e+"");
	   		 e.printStackTrace();
	   	 } 
      	 
        }
		return null;
	}
}



  