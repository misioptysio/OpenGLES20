package com.games;

/**
 * Created by piotr.plys on 2015-11-25.
 */
public class GLFont
{
  //minimal character code available
  public int FONT_MIN_INDEX;

  //maximal character code available
  public int FONT_MAX_INDEX;

  //font name used to generate the class
  public String FONT_NAME;

  //font size used to generate the class (in pixels)
  public int FONT_SIZE;

  //texture dimensions
  public int FONT_TEXTURE_WIDTH;
  public int FONT_TEXTURE_HEIGHT;

  //texture coordinates
  public float[] FONT_TEXTURE_COORDS;

  //characters' height in pixels
  public int FONT_CHAR_HEIGHT ;

  //characters' width in pixels
  public int[] FONT_CHAR_WIDTH;

  //file name with the texture
  public String FONT_TEXTURE_NAME;

  public int getFontMinIndex()
  {
    return FONT_MIN_INDEX;
  }
}
