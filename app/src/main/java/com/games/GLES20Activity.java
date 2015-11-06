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

	private GLSurfaceView mSurfaceView;
	private GLSurfaceView mGLView;

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
			mGLView.setRenderer(new GLES20Renderer());
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
				/*
				 * The activity must call the GL surface view's
         * onResume() on activity onResume().
         */
		if (mSurfaceView != null)
		{
			mSurfaceView.onResume();
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();

        /*
         * The activity must call the GL surface view's
         * onPause() on activity onPause().
         */
		if (mSurfaceView != null)
		{
			mSurfaceView.onPause();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		return super.dispatchTouchEvent(ev);
	}
}

/*
public class MyActivity extends AppCompatActivity
{
	public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
	private GLSurfaceView mSurfaceView;
	private GLSurfaceView mGLView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);

		EditText editText = (EditText) findViewById(R.id.edit_message);
		editText.getText().clear();
		if (hasGLES20())
		{
			editText.getText().append("Has OpenGL ES 2.0 :)");
		}
		else
		{
			editText.getText().append("No OpenGL ES 2.0 :(");
		}
		initialize();
	}

	private void initialize()
	{
		if (hasGLES20())
		{
			mGLView = new GLSurfaceView(this);
			mGLView.setEGLContextClientVersion(2);
			mGLView.setPreserveEGLContextOnPause(true);
			mGLView.setRenderer(new GLES20Renderer());
		}
		else
		{
			// Time to get a new phone, OpenGL ES 2.0 not supported.
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mGLView.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mGLView.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_my, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings)
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private boolean hasGLES20()
	{
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		return info.reqGlEsVersion >= 0x20000;
	}

	public void sendMessage(View view)
	{
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String msgText = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, msgText);
		startActivity(intent);
	}
}
*/
