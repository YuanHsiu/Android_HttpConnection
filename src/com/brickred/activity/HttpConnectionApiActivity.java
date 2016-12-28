package com.brickred.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brickred.R;
import com.brickred.http.HttpHandler;
import com.brickred.http.HttpListner;

public class HttpConnectionApiActivity extends Activity implements HttpListner{
    /** Called when the activity is first created. */
	Handler handler = null;
	TextView textviewResponse;
	ProgressDialog progressDialog;
	EditText edtTextUrl = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button buttonClickMe = (Button)findViewById(R.id.btnclick);
        textviewResponse= (TextView)findViewById(R.id.txtshow);
        edtTextUrl= (EditText)findViewById(R.id.txturl);
        handler = new Handler();
        progressDialog = new ProgressDialog(this);
        edtTextUrl.setText("https://www.mikestoolbox.org");
        
        buttonClickMe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Log.i("", msg)
			if(edtTextUrl.getText().length() > 0 && edtTextUrl.getText().toString().startsWith("http")){	
				progressDialog.setMessage("Please wait for response ... ");
				progressDialog.show();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						HttpHandler handler = new HttpHandler(edtTextUrl.getText().toString().trim(),null, null, 123);
						handler.addHttpLisner(HttpConnectionApiActivity.this);
						handler.sendRequest();
					}
				}).start();
			}else{
				showAlert("Error", "Please enter valid url", null, "Ok");
			}
				
			}
		});
        
    }

	@Override
	public void notifyHTTPRespons(final HttpHandler http) {
		// TODO Auto-generated method stub
		progressDialog.cancel();
		Log.i("Log", "responce ==  "+http.getResCode());
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				textviewResponse.setText("");
				textviewResponse.setText(http.getResponse());
				Log.i("Log", "responce ==  "+http.getResponse());
				Log.i("Log", "android version ==  "+android.os.Build.VERSION.SDK_INT);
			}
		});
	}
	/**
	 * @param title
	 * @param Message
	 * @param NegButton
	 * @param posButton
	 */
	private void showAlert(String title,String Message, final String NegButton,
			final String posButton) {

		AlertDialog.Builder alertBuilder = new Builder(this);
		alertBuilder.setTitle(title);
		alertBuilder.setMessage(Message);
		if (NegButton != null) {
			alertBuilder.setNegativeButton(NegButton,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
		}
		if (posButton != null) {
			alertBuilder.setPositiveButton(posButton,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
		}
		 AlertDialog alert = alertBuilder.create();
		alert.show();

	}
}