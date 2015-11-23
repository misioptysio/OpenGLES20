package com.games;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class GLActivity extends Activity implements SensorEventListener
{
  private GLSurfaceView mGLView;
  private GLRenderer mRenderer;
  private SensorManager sensorManager;
  private long timeLastUpdate;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    if (hasGLES20())
    {
      mGLView = new GLSurfaceView(this);
      mGLView.setEGLContextClientVersion(2);
      mGLView.setPreserveEGLContextOnPause(true);

      mRenderer = new GLRenderer(this);
      mGLView.setRenderer(mRenderer);
//      mGLView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    else
    {
      Utils.log("No support for OpenGL ES 2.0 :(");
    }

    setContentView(mGLView);
    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    timeLastUpdate = 0;
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
  }

  private boolean hasGLES20()
  {
    ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    ConfigurationInfo info = am.getDeviceConfigurationInfo();
    return info.reqGlEsVersion >= 0x20000;
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    if (mGLView != null)
    {
      mGLView.onResume();
    }

    sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    if (mGLView != null)
    {
      mGLView.onPause();
    }
    sensorManager.unregisterListener(this);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev)
  {
//    Utils.log("Event " + ev.toString());
    return super.dispatchTouchEvent(ev);
  }

  @Override
  public void onSensorChanged(SensorEvent event)
  {
    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
    {
      getAccelerometer(event);
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy)
  {

  }

  private void getAccelerometer(SensorEvent event)
  {
    long timeThisUpdate = event.timestamp;
    if ((timeThisUpdate - timeLastUpdate < 200) && (timeLastUpdate != 0))
    {
      return;
    }
    timeLastUpdate = timeThisUpdate;
    mRenderer.setGravityValues(event.values);
  }
}