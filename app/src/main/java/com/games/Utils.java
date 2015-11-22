package com.games;

import android.opengl.Matrix;
import android.util.Log;

/**
 * Created by piotr.plys on 2015-10-26.
 */
public final class Utils
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

	public static float[] vAdd(float[] a, float[] b)
	{
		float[] c = new float[3];

		c[0] = a[0] + b[0];
		c[1] = a[1] + b[1];
		c[2] = a[2] + b[2];

		return c;
	}

	public static float[] vSub(float[] a, float[] b)
	{
		float[] c = new float[3];

		c[0] = a[0] - b[0];
		c[1] = a[1] - b[1];
		c[2] = a[2] - b[2];

		return c;
	}

	public static float[] vMul(float[] a, float f)
	{
		float[] c = new float[3];

		c[0] = a[0] * f;
		c[1] = a[1] * f;
		c[2] = a[2] * f;

		return c;
	}

	public static float[] vCross(float[] a, float[] b)
	{
		float[] c = new float[3];

		c[0] = a[1]*b[2] - a[2]*b[1];
		c[1] = a[2]*b[0] - a[0]*b[2];
		c[2] = a[0]*b[1] - a[1]*b[0];

		return c;
	}

	public static float vLength(float[] a)
	{
		float len = (float) Math.sqrt(a[0]*a[0] + a[1]*a[1] + a[2]*a[2]);
		if (len == 0)
			len = 1.0f;

		return len;
	}

	public static float[] vNormalize(float[] a)
	{
		float len = vLength(a);

		a[0] /= len;
		a[1] /= len;
		a[2] /= len;

		return a;
	}

	public static void setLookAt(float[] mViewMatrix, float[] cameraPosition, float[] cameraLookAt, float[] cameraUp)
	{
		Matrix.setLookAtM(mViewMatrix, 0,
			cameraPosition[0], cameraPosition[1], cameraPosition[2],
			cameraLookAt[0], cameraLookAt[1], cameraLookAt[2],
			cameraUp[0], cameraUp[1], cameraUp[2]);
	}
}
