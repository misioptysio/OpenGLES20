package com.games;

import android.app.Application;
import android.content.Context;

/**
 * Created by piotr.plys on 2015-11-25.
 */
public class OpenGLApplication extends Application
{
  private static OpenGLApplication sInstance;
  private static Globals sGlobals;

  public OpenGLApplication()
  {
    sInstance = this;
  }

  public static Context getContext()
  {
    return sInstance;
  }

  public static Globals getSGlobals()
  {
    return sGlobals;
  }

  public static Globals initSGlobals()
  {
    sGlobals = new Globals();

    return sGlobals;
  }
}
