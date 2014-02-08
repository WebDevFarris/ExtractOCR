package com.sargon.labelreader.dbpush;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;

@SuppressLint("NewApi")
public class ParseData {
	private String serial, model, engine, asset;
	private ArrayList<String> data;
	private HashMap matchTags;
	public ParseData(String dataString)
	{ 
		data = new ArrayList<String>();
		String[] temp = dataString.split(" ");
		for(String t:temp)
			data.add(t);
		matchTags = new HashMap();
		matchTags.put("serial", new String[]{"SN","S//N","serial"});
	}
	public HashMap linearMatch()
	{
		HashMap results = new HashMap();
		getSerial();
		results.put("serial",serial);
		return results;
	}
	public void getSerial()
	{
		int i;
		boolean match = false;
		int j = 0;
		String target;
		String[] serialTerms = (String[])matchTags.get("serial");
		for(i=0; i<data.size();i++)
		{
			String word = data.get(i);
			j=0;
			while(!match &&j<3)
			{
				if(word.equalsIgnoreCase(serialTerms[j]))
					match = true;
				else
					j++;
			}
			if(match)
				break;
		}
		if(match)
		{
			target = data.get(i+1);
			if(target.equalsIgnoreCase("NO") 
					|| target.equalsIgnoreCase("number")
					|| target.equalsIgnoreCase("NO."))
				serial =data.get(i+2);
			else
				serial = target;
		}
	}
}
