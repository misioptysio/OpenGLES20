package com.games;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;
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
			Utils.log("GLES20Activity.onCreate");
			mGLView = new GLSurfaceView(this);
			mGLView.setEGLContextClientVersion(2);
//			mGLView.setPreserveEGLContextOnPause(true);

			Utils.log("Before renderer creation");
			mRenderer = new GLES20Renderer();
//			mGLView.setRenderer(new GLES20Renderer());
			mGLView.setRenderer(mRenderer);
			Utils.log("After renderer creation");
//			mGLView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		}
		else
		{
			// Time to get a new phone, OpenGL ES 2.0 not
			// supported.
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
//			mGLView.onResume();
		}
		Utils.log("onResume RED in activity");
		GLES20.glClearColor(0.5f, 0.0f, 0.0f, 1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (mGLView != null)
		{
//			mGLView.onPause();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		return super.dispatchTouchEvent(ev);
	}
}