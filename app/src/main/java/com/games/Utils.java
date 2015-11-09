package com.games;

import android.util.Log;

/**
 * Created by piotr.plys on 2015-10-26.
 */
public class Utils
{
	public static final boolean DEBUG = true;
	public static final String LOG_TAG = "[PTYSIO]";

	public static void log(String format, Object... args)
	{
		if (DEBUG)
			Log.d(LOG_TAG, String.format(format, args));
	}

	public static void log(String msg)
	{
		if (DEBUG)
			Log.d(LOG_TAG, msg);
	}
}
