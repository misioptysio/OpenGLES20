package com.games;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class GLES20Activity extends Activity
{

  private GLSurfaceView mGLView;
  private GLES20Renderer mRenderer;

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

      mRenderer = new GLES20Renderer();
      mGLView.setRenderer(mRenderer);
//      mGLView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    else
    {
      Utils.log("No support for OpenGL ES 2.0 :(");
    }

    setContentView(mGLView);
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
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    if (mGLView != null)
    {
			mGLView.onPause();
    }
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev)
  {
//    Utils.log("Event " + ev.toString());
    return super.dispatchTouchEvent(ev);
  }
}