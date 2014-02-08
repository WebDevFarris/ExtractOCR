package com.sargon.labelreader.dbpush;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.sargonocrapp.R;
@SuppressLint("NewApi")
public class SubmitConfirmDialog extends DialogFragment implements View.OnClickListener
{
	Button ok,cancel;
	Communicator communicator;
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		communicator = (Communicator)activity;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		getDialog().setTitle("Confirm Submission");
		View view =inflater.inflate(R.layout.submit_confirm_dialog,null);
		ok = (Button) view.findViewById(R.id.ok);
		cancel = (Button) view.findViewById(R.id.cancel);
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
		setCancelable(false);
		return view; 
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view.getId()==R.id.ok)
		{
			dismiss();
			communicator.onConfirmMessage();
		}
		else if(view.getId()==R.id.cancel)
		{
			dismiss();
		}
	}
	interface Communicator
	{
		public void onConfirmMessage();
	}
	
}
