package com.sargon.labelreader.dbpush;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.sargonocrapp.R;
@SuppressLint("NewApi")
public class CheckBoxSelectionDialog extends DialogFragment implements View.OnClickListener
{
	Button ok,cancel;
	Communicator communicator;
	String items[];
	ArrayList<CheckBox> boxes;
	public String OCRtxt;
	
	public CheckBoxSelectionDialog() {
		// TODO Auto-generated constructor stub
	}

	public void setOCRText(String OCRText)
	{
	OCRtxt = OCRText;
	}
	
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		communicator = (Communicator)activity;
	}
	TableLayout table;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		getDialog().setTitle("Label Information");
		View view =inflater.inflate(R.layout.string_checkbox_dialog,null);
		ok = (Button) view.findViewById(R.id.ok);
		cancel = (Button) view.findViewById(R.id.cancel);
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
		setCancelable(false);
		boxes = new ArrayList<CheckBox>();
		table = (TableLayout)view.findViewById(R.id.ocr_result_list);
		Activity getResults = getActivity();
		items = OCRtxt.split("\\s+");
		TableRow[] tr = new TableRow[items.length]; 
		for(int i = 0; i<items.length;i++)
		{
			tr[i] = new TableRow(view.getContext());
			CheckBox item = new CheckBox(view.getContext());
			item.setText(items[i]);
			item.setId(i);
			boxes.add(item);
			tr[i].addView(item);
	        table.addView(tr[i]);
		}
		
		return view; 
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view.getId()==R.id.ok)
		{
			String result = "";
			for(CheckBox item : boxes)
				if(item.isChecked())
					result += item.getText();
			dismiss();
			//Toast.makeText(getActivity(),selected.getText(), Toast.LENGTH_LONG).show();
			communicator.onCheckBoxMessage(result);
		}
		else if(view.getId()==R.id.cancel)
		{
			dismiss();
		}
	}
	interface Communicator
	{
		public void onCheckBoxMessage(String data);
	}
	
}
