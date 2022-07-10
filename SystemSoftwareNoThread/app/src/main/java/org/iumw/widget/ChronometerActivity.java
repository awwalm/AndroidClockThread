/*
 * Copyright (c) 2020. SUBMITTED FOR AN ASSIGNMENT UNDER THE BACHELORS OF COMPUTER SCIENCE MODULE
 * IN INTERNATIONAL UNIVERSITY OF MALAYA-WALES.
 */

/**
 * @author: Awwal Mohammed
 * co-author: Zarina Usmanova
 * subject: BCS 613 System Software
 * project title: ${enclosing_project}
 * Description: ChronometerActivity.java source program for
 * starting and stopping all three clocks
 * @date: 10-04-20	// creation date
 * @date: ${date} 	// last compiled date for Dr. Ashley to verify
 * ${iso:date('yyyy-MM-dd HH:mm:ss Z')}
**/


package org.iumw.widget;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import com.timtak.widget.R;

public class ChronometerActivity extends Activity
{
	// we need to monitor the states of all three programs, similarly
	// the variables for the (to-be) references need to created here
	private boolean running; // is first clock running?
	private boolean running2; // is second clock running?
	private boolean running3; // is third clock running?

	private long pauseOffset;
	private long pauseOffset2;
	private long pauseOffset3;

	private AnalogChronometer chronometer;
	private AnalogChronometer chronometer2;
	private AnalogChronometer chronometer3;

	/**{@link #onCreate}*/ //method that inflates the GUI layout once the application is started
	// it is usually an autogenerated method where the initialization functions ought to be
	// placed here, the buttons we need for the operations and all three clocks are instantiated
	// subsequently, the related functions are accounted for
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// @TODO: currently not in use, to be removed in case Dr. Ashley asks
		// assign the buttons being used
		Button resumeButtonAll = (Button) findViewById(R.id.resumeButtonAll);
		Button suspendButtonAll = (Button) findViewById(R.id.suspendButtonAll);
		Button resumeButton = (Button) findViewById(R.id.resumeButton);
		Button suspendButton = (Button) findViewById(R.id.suspendButton);

		// three clocks (chronometers) are assigned and initialize to their Android context
		chronometer = (AnalogChronometer) findViewById(R.id.chronometer);
		chronometer2 = (AnalogChronometer) findViewById(R.id.chronometer2);
		chronometer3 = (AnalogChronometer) findViewById(R.id.chronometer3);

		// immediately set the elapsed time for all chronometers
		chronometer.setBase(SystemClock.elapsedRealtime());
		chronometer2.setBase(SystemClock.elapsedRealtime());
		chronometer3.setBase(SystemClock.elapsedRealtime());

	} /**end {@link #onCreate(Bundle)} function*/


	//--------------------------------------------------------------------------------
	// function to start all three clocks

	public void startAllChronometers(View v)
	{
		startChronometer(getCurrentFocus());
		startSecondChronometer(getCurrentFocus());
		startThirdChronometer(getCurrentFocus());

	} /**end {@link #startAllChronometers} function*/

	// function to pause or suspend all three clocks
	public void pauseAllChronometers(View v)
	{
		pauseChronometer(getCurrentFocus());
		pauseSecondChronometer(getCurrentFocus());
		pauseThirdChronometer(getCurrentFocus());

	} /**end {@link #pauseAllChronometers(View)}*/

	//--------------------------------------------------------------------------------

	// function to start or resume first chronometer
	public void startChronometer(View v)
	{
		/** check if the first chronometer is {@param #running} before updating it*/
		if (!running)
		{
			// returns milliseconds since boot,including time spent in sleep
			chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
			chronometer.start();
			running = true;
		}

	} /**end {@link #startChronometer}*/

	// function to start or resume second chronometer
	// literally the same style as above so the comments are neglected for the next two
	public void startSecondChronometer(View v)
	{
		if (!running2)
		{
			chronometer2.setBase(SystemClock.elapsedRealtime() - pauseOffset2);
			chronometer2.start();
			running2 = true;
		}
	}

	// function to start or resume third chronometer
	public void startThirdChronometer(View v)
	{
		if (!running3)
		{
			chronometer3.setBase(SystemClock.elapsedRealtime() - pauseOffset3);
			chronometer3.start();
			running3 = true;
		}
	}

	//-----------------------------------------------------------------------------------

	// function to pause or suspend first chronometer
	public void pauseChronometer(View v)
	{
		// this operation should only be performed if the clock had previously been running
		if (running)
		{
			chronometer.stop();
			// save the current running time (by subtracting the base) in pauseOffset
			pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
			// tell the program that clock is no longer running
			running = false;
		}

	} /**end {@link #pauseChronometer}*/

	// pause second chronometer
	// the same logic as above is repeated for the other two
	public void pauseSecondChronometer(View v)
	{
		if (running2)
		{
			chronometer2.stop();
			pauseOffset2 = SystemClock.elapsedRealtime() - chronometer2.getBase();
			running2 = false;
		}

	} /**end {@link #pauseSecondChronometer}*/

	// pause third chronometer
	public void pauseThirdChronometer(View v)
	{
		if (running3)
		{
			chronometer3.stop();
			pauseOffset3 = SystemClock.elapsedRealtime() - chronometer3.getBase();
			running3 = false;
		}

	} /**end {@link #pauseThirdChronometer}*/

	//-------------------------------------------------------------------------------

} /*end class*/