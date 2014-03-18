package com.sargon.labelreader.dbpush;
import java.util.ArrayList;
import java.util.HashMap;

//import android.annotation.SuppressLint;

//@SuppressLint("NewApi")
public class ParseData {
	private String serial, model, engine, asset;
	private ArrayList<String> data;
	public ArrayList<ArrayList<String>> dataTable;//
	private HashMap matchTags;
	public ParseData(String dataString)
	{ 
		data = new ArrayList<String>();
		/*String[] temp = dataString.split(" ");
		for(String t:temp)
			data.add(t);*/
		createTable(dataString);
		matchTags = new HashMap();
		matchTags.put("serial", new String[]{"SN","S//N","SERIAL"});
		matchTags.put("asset", new String[]{"ASSET"});
		matchTags.put("part", new String[]{"PART"});
		matchTags.put("engine", new String[]{});
	}
	public HashMap getData()
	{
		HashMap values = new HashMap();
		String match;
		match = findMatch((String[])matchTags.get("serial"));
		values.put("serial",match);
		match = findMatch((String[])matchTags.get("asset"));
		values.put("asset",match);
		match = findMatch((String[])matchTags.get("part"));
		values.put("part",match);
		match = findMatch((String[])matchTags.get("engine"));
		values.put("engine",match);
		return values;
	}
	public void createTable(String data)
	{
		data = data.toUpperCase();
		data = data.replaceAll("NUMBER","");
		data = data.replaceAll("NO.", "");
		data = data.replaceAll("NO", "");
		data = data.replaceAll("#", "");
		data = data.replaceAll("[,.)(:]", "");
		String lines[] = data.split("\n");
		dataTable = new ArrayList<ArrayList<String>>();
		for(int i= 0; i<lines.length;i++)
		{
			dataTable.add(new ArrayList<String>());
			String []temp = lines[i].split("\\s+");
			for(int j = 0; j<temp.length;j++)
				dataTable.get(i).add(temp[j]);
		}
	}
	public String findMatch(String[] terms)
	{
		String match ="";
		int x=-1,y=-1;
		for(int i = 0; i<dataTable.size();i++)
		{
			for(int j=0; j<terms.length;j++)
			{
				int index = dataTable.get(i).indexOf(terms[j]);
				if(index!=-1)
				{
					x = i;
					y = index;
					match = getMatch(x,y);
					if(match!="")
						return match;
					else
						match="";
				}
			}
		}
		return match;
	}
	public String getMatch(int x, int y)
	{
  		String regularExpression = "^([A-Z]*[0-9]+[A-Z]*-{0,1})+$";
		if((y+1)<dataTable.get(x).size()
				&&dataTable.get(x).get(y+1).matches(regularExpression))
		{
			return dataTable.get(x).get(y+1);
		}
		else if((x+1)<dataTable.size()
				&&y<dataTable.get(x+1).size()
				&&dataTable.get(x+1).get(y).matches(regularExpression))
		{
			return dataTable.get(x+1).get(y);
		}
		else if((y-1)>0
				&&(x+1)<dataTable.size()
				&&(y-1)<dataTable.get(x+1).size()
				&&dataTable.get(x+1).get(y-1).matches(regularExpression))
		{
			return dataTable.get(x+1).get(y-1);
		}
		else if((x+1)<dataTable.size()
				&&(y+1)<dataTable.get(x+1).size()
				&&dataTable.get(x+1).get(y+1).matches(regularExpression))
		{
			return dataTable.get(x+1).get(y+1);
		}
		else if((x-1)>0
				&&(y-1)>0
				&&(y-1)<dataTable.get(x-1).size()
				&&dataTable.get(x-1).get(y-1).matches(regularExpression))
		{
			return dataTable.get(x-1).get(y-1);
		}
		else if((x-1)>0
				&&y<dataTable.get(x-1).size()
				&&dataTable.get(x-1).get(y).matches(regularExpression))
		{
			return dataTable.get(x-1).get(y);
		}
		else if((x-1)>0
				&&(y+1)<dataTable.get(x-1).size()
				&&dataTable.get(x-1).get(y+1).matches(regularExpression))
		{
			return dataTable.get(x-1).get(y+1);
		}
		return "";
	}/*
	public HashMap linearMatch()
	{
		HashMap results = new HashMap();
		getSerial();
		results.put("serial",serial);
		return results;
	}*//*
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
	}*/
}
