package com.sargon.labelreader.dbpush;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.sargonocrapp.R;
@SuppressLint("NewApi")
public class BoxSelectionDialog extends DialogFragment implements View.OnClickListener
{
	RadioGroup group;
	Button ok,cancel;
	Communicator communicator;
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		communicator = (Communicator)activity;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		getDialog().setTitle("Box Selection");
		View view =inflater.inflate(R.layout.box_selection_prompt,null);
		ok = (Button) view.findViewById(R.id.ok);
		cancel = (Button) view.findViewById(R.id.cancel);
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
		group = (RadioGroup) view.findViewById(R.id.radioGroup1);
		setCancelable(false);
		return view; 
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view.getId()==R.id.ok)
		{
			int checkedButtonId = group.getCheckedRadioButtonId();
			RadioButton selected = (RadioButton)group.findViewById(checkedButtonId);
			dismiss();
			communicator.onDialogMessage(selected.getText().toString());
		}
		else if(view.getId()==R.id.cancel)
		{
			dismiss();
		}
	}
	interface Communicator
	{
		public void onDialogMessage(String data);
	}
	
}
