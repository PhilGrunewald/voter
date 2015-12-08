// Phil Grunewald 
// log
// June 14 - commented out buttons, auto-start at startup
// July 14 - shorten file names, only store two columns, experiment with high sample rates
// 16 July 14 - major change: instead of summing 100 readings and do an RMS with the 441 readings per second, the RMS is taken of the 100 samples (over 1/441s) and the 441 readings per second are AVERAGED.
// May 15 - added ID edit field
// Added this file to GitHub repository
// 19 Jul 15 - removed the redundant 'draw text'
// 2 Aug 15 based on DEMon - converted to 'voter'
//
package com.Phil.voter;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

//import android.media.AudioFormat;
//import android.media.AudioRecord;
//import android.media.MediaRecorder;
//import android.media.MediaRecorder.AudioSource;

import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;

//Main Class
public class MainActivity extends Activity implements android.view.View.OnClickListener{

	private int running = 1;

	private FileWriter logWriter;
	File logFile;
	private File root;
	File folder;
	String formattedDateString;

	private SimpleDateFormat formatter_date_time = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
	       	 
	// private TextView mylog;

	// private Button btnLog;
	private Button btn_a1;
	private Button btn_a2;

	// private EditText edit_log;
	private String LogText;			// text taken from edit field
	private String LogEntry;		// the line to be added to file (included time and tags)

	private String LogList="Welcome!";		// the history of LogEntries from this session
	private Vibrator vib;

@Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);   
    btn_a1 = (Button) findViewById(R.id.btn_a1);
    btn_a1.setOnClickListener((OnClickListener) this);
    btn_a2 = (Button) findViewById(R.id.btn_a2);
    btn_a2.setOnClickListener((OnClickListener) this);

	// Get instance of Vibrator from current Context
	vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


       btn_a1.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
 				vib.vibrate(50);
                return false;
            }
        });
       btn_a2.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
 				vib.vibrate(50);
                return false;
            }
        });





    root = Environment.getExternalStorageDirectory();
    folder = new File(root,""); // reinstated 3 Nov 14

	String log_type = getString(R.string.log_type);
	String logFileName = log_type + "_vote.txt";

	logFile = new File(folder, logFileName);
	try{  
		try{
			logFile.createNewFile();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		logWriter = new FileWriter(logFile, true);
	}
	catch (IOException e) 
	{
		e.printStackTrace();
	}
}


@Override
public void onClick(View v) {
	if (v==btn_a1) {
		LogText = getString(R.string.a1);
	} else if (v==btn_a2) {
		LogText = getString(R.string.a2);
	}
	Date thisMoment = new Date();
	final String formatedDate = formatter_date_time.format(thisMoment);
	LogEntry = formatedDate + ": "+ LogText +"\n";
	printToAndroid(LogEntry);
	try {
			logWriter.write(LogEntry);
	} catch (IOException e) {
			e.printStackTrace();
	}
	try {
		logWriter.flush();
	} catch (IOException e) {
		e.printStackTrace();
	}
}


@Override
public void onStart(){
    super.onStart();  
	Log.d("micro", "in onstart");
	Log.d("micro", "start running main()");
	if (running!=1) {	// 14_07_26_ got the impression new instances are created when waking up the phone	
	      running=1;
	      new Thread(new Runnable() { 
		  public void run(){	    
		  main();
		  }
	      }).start();
	}
 }


private void printToAndroid(final String newEntry){	
// display on screen
	runOnUiThread(new Runnable() {
	     public void run() {
		   // String ContactID = getString(R.string.contactID);
	       LogList = newEntry + LogList; 
	       // mylog.setText(LogList);
	    }
	});
}

private void main() {
}
}
