package com.games;

import android.opengl.Matrix;
import android.util.Log;

/**
 * Created by piotr.plys on 2015-10-26.
 */
public final class Utils
{
  public static final int INTERPOLATE_LINEAR = 0;
  public static final int INTERPOLATE_QUADRATIC = 1;
  public static final int INTERPOLATE_CUBIC = 2;
  public static final int INTERPOLATE_ROOT = 3;
  public static final int INTERPOLATE_INV_QUADRATIC = 4;
  public static final int INTERPOLATE_PERLIN = 5;

  public static final boolean DEBUG = true;
  public static final String LOG_TAG = "[PTYSIO]";

  public static void log(String format, Object... args)
  {
    if (DEBUG)
    {
      Log.d(LOG_TAG, String.format(format, args));
    }
  }

  public static void log(String msg)
  {
    if (DEBUG)
    {
      Log.d(LOG_TAG, msg);
    }
  }

  public static double interpolationFunctionPerlin(double t)
  {
    return (t * t * t * (t * (t * 6 - 15) + 10));
  }

  //input x = <0, 1>, output <0, 1>
  public static double interpolationFunction(double t, int type)
  {
    double res;

    if (t <= 0.0)
    {
      res = 0.0;
    }
    else if (t >= 1.0)
    {
      res = 1.0;
    }
    else
    {
      switch (type)
      {
        case INTERPOLATE_CUBIC:
          res = (3.0 - 2.0 * t) * t * t;
          break;

        case INTERPOLATE_PERLIN:
          res = interpolationFunctionPerlin(t);
          break;

        case INTERPOLATE_QUADRATIC:
          res = t * t;
          break;

        case INTERPOLATE_INV_QUADRATIC:
          res = (2 - t) * t;
          break;

        case INTERPOLATE_ROOT:
          res = (float) Math.sqrt(t);
          break;

        default: //linear
        case INTERPOLATE_LINEAR:
          res = t;
      }
    }

    return res;
  }

  public static double interpolateValues(double x1, double x2, double t, int type)
  {
    double res;

    res = x1 + (x2 - x1) * interpolationFunction(t, type);

    return res;
  }

  public static double interpolateValues(double x1, double x2, double t)
  {
    double res;

    res = x1 + (x2 - x1) * t;

    return res;
  }

  //interpolates value x from range <xMin, xMax> and maps it to range <vMin, vMax> using type interpolation
  public static double mapValues(double x, double xMin, double xMax, double vMin, double vMax, int type)
  {
    double res;
    double xInterpolated;

    xInterpolated = interpolateValues(xMin, xMax, (x - xMin) / (xMax - xMin), INTERPOLATE_LINEAR);
    res = interpolateValues(vMin, vMax, (xInterpolated - xMin) / (xMax - xMin), type);

    return res;
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

    c[0] = a[1] * b[2] - a[2] * b[1];
    c[1] = a[2] * b[0] - a[0] * b[2];
    c[2] = a[0] * b[1] - a[1] * b[0];

    return c;
  }

  public static float vLength(float[] a)
  {
    float len = (float) Math.sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2]);
    if (len == 0)
    {
      len = 1.0f;
    }

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
    Matrix.setLookAtM(mViewMatrix, 0, cameraPosition[0], cameraPosition[1], cameraPosition[2], cameraLookAt[0], cameraLookAt[1], cameraLookAt[2], cameraUp[0], cameraUp[1], cameraUp[2]);
  }
}
