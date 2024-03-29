/*
 * SUBMITTED FOR AN ASSIGNMENT UNDER THE BACHELOR OF COMPUTER SCIENCE MODULE
 * AT INTERNATIONAL UNIVERSITY OF MALAYA-WALES. (c) 2020.
 */

package com.iumw.widget;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Description:
 * ChronometerActivity.java source program for starting and stopping all three clocks.
 * This particular version uses Multithreading by implementing the features in
 * the core Android {@link android.os.Handler} class (accessible by
 * pressing CTRL+B in Android Studio) which is an encapsulation of the core Java
 * {@link java.lang.Thread} class.
 *
 * <p>Subsequently, a {@link android.os.Looper} object is
 * used for inter-thread communication with the {@link android.os.MessageQueue} utility class.
 * A {@link java.lang.Runnable} instance is eventually used to execute all Handler threads.
 * Synchronization features are not required but will be added to meet assignment requirements.
 *
 * @author Awwal Mohammed
 * subject: BCS 613 System Software
 * project title: ${enclosing_project}

 * @since 10-04-20	// creation date
 * @since ${DATE} 	// last compiled date
 * FIXME: 14-04-2020 settle remaining javadoc comments... or not
 * FIXED on 29-12-2022
 */

public class ChronometerActivity extends Activity
{
	// we need to monitor the states of all three clocks, similarly
	// the variable for the (to-be) references need to created here
	private boolean running; // is first clock running?
	private boolean running2; // is second clock running?
	private boolean running3; // is third clock running?

	private long pauseOffset;
	private long pauseOffset2;
	private long pauseOffset3;

	private AnalogChronometer chronometer;
	private AnalogChronometer chronometer2;
	private AnalogChronometer chronometer3;

	private Handler mainHandler = new Handler(); // thread handler (core Android class)

	// onCreate method that inflates the GUI layout once the application is started
	// it is usually an autogenerated method where the initialization functions ought to be placed
	// here, the buttons we need for the operations and all three clocks are instantiated
	// subsequently, the related functions are accounted for
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// @TODO: currently not in use, to be removed in case Dr. Ashley asks why
		// assign the buttons being used
		Button resumeButtonAll = (Button) findViewById(R.id.resumeButtonAll);
		Button suspendButtonAll = (Button) findViewById(R.id.suspendButtonAll);
		Button resumeButton = (Button) findViewById(R.id.resumeButton);
		Button suspendButton = (Button) findViewById(R.id.suspendButton);

		// three clocks (chronometers) are assigned and initialize to their Android context
		chronometer = (AnalogChronometer) findViewById(R.id.chronometer);
		chronometer2 = (AnalogChronometer) findViewById(R.id.chronometer2);
		chronometer3 = (AnalogChronometer) findViewById(R.id.chronometer3);

		// immediately set the default elapsed time for all chronometers
		chronometer.setBase(SystemClock.elapsedRealtime());
		chronometer2.setBase(SystemClock.elapsedRealtime());
		chronometer3.setBase(SystemClock.elapsedRealtime());

	} /*end OnCreate() function*/


	//--------------------------------------------------------------------------------
	// function to start all three clocks

	public synchronized void startAllChronometers(View v)
	{
		startChronometer(getCurrentFocus());
		startSecondChronometer(getCurrentFocus());
		startThirdChronometer(getCurrentFocus());

	} /*end startAllChronometers() function*/

	// function to pause or suspend all three clocks
	public synchronized void pauseAllChronometers(View v)
	{
		pauseChronometer(getCurrentFocus());
		pauseSecondChronometer(getCurrentFocus());
		pauseThirdChronometer(getCurrentFocus());
	}

	//--------------------------------------------------------------------------------

	// function to start or resume first chronometer
	public synchronized void startChronometer(View v)
	{
		// check if the first chronometer is running before updating it
		if (!running)
		{
			// returns milliseconds since boot,including time spent in sleep
			chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
			chronometer.start();
			running = true;
		}

	} /*end startChronometer()*/

	// function to start or resume second chronometer
	// literally the same style as above so the comments are neglected for the next two
	public synchronized void startSecondChronometer(View v)
	{
		if (!running2)
		{
			chronometer2.setBase(SystemClock.elapsedRealtime() - pauseOffset2);
			chronometer2.start();
			running2 = true;
		}
	}

	// function to start or resume third chronometer
	public synchronized void startThirdChronometer(View v)
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
	public synchronized void pauseChronometer(View v)
	{
		// this operation should only be performed if the clock had previously been running
		if (running)
		{
			chronometer.stop();
			// save the current running time (by subtracting the base) in pauseOffset
			pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
			// tell the program that clock is no longer running
			running = false;
			// display toast
			String threadNote = "1st clock paused in new thread";
			Toast.makeText(
				ChronometerActivity.this, threadNote, Toast.LENGTH_SHORT).show();
		}

	} /*end pauseChronometer()*/

	// pause second chronometer
	// the same logic as above is repeated for the other two
	public synchronized void pauseSecondChronometer(View v)
	{
		if (running2)
		{
			chronometer2.stop();
			pauseOffset2 = SystemClock.elapsedRealtime() - chronometer2.getBase();
			running2 = false;
			String threadNote = "2nd clock paused in new thread";
			Toast.makeText(
				ChronometerActivity.this, threadNote, Toast.LENGTH_SHORT).show();
		}

	} /*end pauseSecondChronometer()*/

	// pause third chronometer
	public synchronized void pauseThirdChronometer(View v)
	{
		if (running3)
		{
			chronometer3.stop();
			pauseOffset3 = SystemClock.elapsedRealtime() - chronometer3.getBase();
			running3 = false;
			String threadNote = "3rd clock paused in new thread";
			Toast.makeText(
				ChronometerActivity.this, threadNote, Toast.LENGTH_SHORT).show();
		}

	} /**end {@link #pauseThirdChronometer(View v)}*/


	/////////////////////////////////////////////////////////////////////////////////


    /** use {@link FirstClockRunnable} inner class thread object to
	 *  start first clock*/
    public synchronized void startFirstClockThread(View view)
    {
        FirstClockRunnable fcr = new FirstClockRunnable();
        new Thread(fcr).start();
    }

    /** use {@link PauseFirstClockRunnable} to pause first clock in new thread*/
    public synchronized void pauseChronometerThread(View view)
	{
		PauseFirstClockRunnable pfcr = new PauseFirstClockRunnable();
		new Thread(pfcr).start();
	}

	/** use {@link SecondClockRunnable} inner class thread object to
	 *  start second clock*/
    public synchronized void startSecondClockThread(View view)
    {
		SecondClockRunnable scr = new SecondClockRunnable();
		new Thread(scr).start();
    }

	/** use {@link PauseSecondClockRunnable} to pause second clock in new thread*/
	public synchronized void pauseSecondChronometerThread(View view)
	{
		PauseSecondClockRunnable pscr = new PauseSecondClockRunnable();
		new Thread(pscr).start();
	}

	/** use {@link ThirdClockRunnable} inner class thread object to
	 *  start third clock*/
    public synchronized void startThirdClockThread(View view)
    {
		ThirdClockRunnable tcr = new ThirdClockRunnable();
		new Thread(tcr).start();
    }

	/** use {@link PauseThirdClockRunnable} to pause third clock in new thread*/
	public synchronized void pauseThirdChronometerThread(View view)
	{
		PauseThirdClockRunnable ptcr = new PauseThirdClockRunnable();
		new Thread(ptcr).run();
	}

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    // inner class for starting first clock
    // the runnable class itself internally extends the Thread class
    // but it is more powerful and allows stronger and more explicit multi-threading requests
    // this is made possible by communicating with the message queue (more details in the report)
    public class FirstClockRunnable implements Runnable
    {
        // mandatory empty constructor (because no data parsing is required)
		// this is because we're utilizing the existing functions and instead
		// EXPLICITLY calling them in unique threads
        FirstClockRunnable() {}

        @Override
        public void run() {
            // use mainHandler thread to send work request into the current UI thread
            // so no errors occur, this is called a POST request
			// in fact, a new Handler instance can even be created for this purpose!
			// @TODO: this line can be modified during presentation if Dr. Ashley doesn't like it
			// a Handler object needs a looper attachment otherwise, the thread request message is
			// not sent to the message queue (please refer to either the report or
			// the official Android Documentation to understand this routine better - Awwal)
			Handler mainHandler = new Handler(Looper.getMainLooper());

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    startChronometer(getCurrentFocus());
                    // display a short notification to the user
                    String threadNote = "1st clock started or resumed in new thread";
                    Toast.makeText(
                        ChronometerActivity.this, threadNote, Toast.LENGTH_SHORT).show();
                }

            });	/*end thread POST request*/
        }
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	// inner class for starting second clock
	// the same procedure as above is required so the comments are neglected
	// @TODO: add extra comments if Dr. Ashley demands them (but this is unnecessary anyway)
	public class SecondClockRunnable implements Runnable
	{
		SecondClockRunnable() {	}

		@Override
		public void run()
		{
			Handler mainHandler = new Handler(Looper.getMainLooper());
			mainHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					startSecondChronometer(getCurrentFocus());
					String secondThreadNote = "2nd clock started or resumed in new thread";
					Toast.makeText(
					ChronometerActivity.this, secondThreadNote, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	// inner class for starting third clock, same flow as above, just as discussed
	public class ThirdClockRunnable implements Runnable
	{
		ThirdClockRunnable() {	}

		@Override
		public void run()
		{
			Handler mainHandler = new Handler(Looper.getMainLooper());
			mainHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					startThirdChronometer(getCurrentFocus());
					String thirdThreadNote = "3rd clock started or resumed in new thread";
					Toast.makeText(
					ChronometerActivity.this, thirdThreadNote, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	//******************************************************************************
	//******************************************************************************

	// inner class for also stopping or pausing each clock on a separate thread
	public class PauseFirstClockRunnable implements Runnable
	{
		PauseFirstClockRunnable() {	}

		@Override
		public void run()
		{
			Handler mainHandler = new Handler(Looper.getMainLooper());
			mainHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					pauseChronometer(getCurrentFocus());
				}
			});
		}
	}

	// for stopping second clock in separate thread
	public class PauseSecondClockRunnable implements Runnable
	{
		PauseSecondClockRunnable() {	}

		@Override
		public void run()
		{
			Handler mainHandler = new Handler(Looper.getMainLooper());
			mainHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					pauseSecondChronometer(getCurrentFocus());
				}
			});
		}
	}

	// for stopping third clock in separate thread
	public class PauseThirdClockRunnable implements Runnable
	{
		PauseThirdClockRunnable() {	  }

		@Override
		public void run()
		{
			Handler mainHandler = new Handler(Looper.getMainLooper());
			mainHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					pauseThirdChronometer(getCurrentFocus());
				}
			});
		}
	}


} /**end main {@link com.iumw.widget.ChronometerActivity} class*/