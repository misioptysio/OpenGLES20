package com.games;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCreateProgram;

import static com.games.Const.*;

/**
 * Created by piotr.plys on 2015-11-12.
 */

// make a descendant as GLPackedObject, which will use packed vertex arrays
//This one uses separate buffers for each attribute
public abstract class GLObject
{
  protected boolean[] buffersUsed = {false, false, false, false};

  protected int vertexCount = 3;

  protected Globals mGlobals;
  protected GLMaterial mMaterial;
  protected FloatBuffer positionBuffer;
  protected FloatBuffer colorBuffer;
  protected FloatBuffer textureBuffer;
  protected FloatBuffer normalBuffer;
  protected ShortBuffer drawListBuffer;

  protected int mProgram;
  protected int mVertexShader;
  protected int mFragmentShader;

  public void setGlobals(Globals globals)
  {
    mGlobals = globals;
  }

  public void initShaders(int shaderID)
  {
    mVertexShader = mGlobals.glShaders.loadShader(GL_VERTEX_SHADER, mGlobals.getVertexShader(shaderID));
    mFragmentShader = mGlobals.glShaders.loadShader(GL_FRAGMENT_SHADER, mGlobals.getFragmentShader(shaderID));
  }

  public void initProgram()
  {
    mProgram = glCreateProgram();

    glAttachShader(mProgram, mVertexShader);
    glAttachShader(mProgram, mFragmentShader);
  }

  public void createBuffer(int count, byte aType)
  {
    int byteCount;
    FloatBuffer floatBuffer;

    buffersUsed[aType] = true;

    switch (aType)
    {
      case V_POSITION:
        byteCount = count * VALUES_PER_V_POSITION * BYTES_PER_FLOAT;
        break;

      case V_NORMAL:
        byteCount = count * VALUES_PER_V_NORMAL * BYTES_PER_FLOAT;
        break;

      case V_COLOR:
        byteCount = count * VALUES_PER_V_COLOR * BYTES_PER_FLOAT;
        break;

      case V_TEXTURE:
      default:
        byteCount = count * VALUES_PER_V_TEXTURE * BYTES_PER_FLOAT;
        break;
    }

    ByteBuffer bb = ByteBuffer.allocateDirect(byteCount).order(ByteOrder.nativeOrder());
    floatBuffer = bb.asFloatBuffer();
    floatBuffer.position(0);

    switch (aType)
    {
      case V_POSITION:
        positionBuffer = floatBuffer;
        break;

      case V_NORMAL:
        normalBuffer = floatBuffer;
        break;

      case V_COLOR:
        colorBuffer = floatBuffer;
        break;

      case V_TEXTURE:
      default:
        textureBuffer = floatBuffer;
        break;
    }
  }

  public void createDrawListBuffer(int drawListCount)
  {
    ByteBuffer bb = ByteBuffer.allocateDirect(drawListCount * BYTES_PER_SHORT);
    bb.order(ByteOrder.nativeOrder());

    drawListBuffer = bb.asShortBuffer();
    drawListBuffer.position(0);
  }

  public void setPosition(int i, float x, float y, float z)
  {
    positionBuffer.put(i * VALUES_PER_V_POSITION + 0, x);
    positionBuffer.put(i * VALUES_PER_V_POSITION + 1, y);
    positionBuffer.put(i * VALUES_PER_V_POSITION + 2, z);
  }

  public void setColor(int i, float r, float g, float b, float a)
  {
    colorBuffer.put(i * VALUES_PER_V_COLOR + 0, r);
    colorBuffer.put(i * VALUES_PER_V_COLOR + 1, g);
    colorBuffer.put(i * VALUES_PER_V_COLOR + 2, b);
    colorBuffer.put(i * VALUES_PER_V_COLOR + 3, a);
  }

  public void setNormal(int i, float x, float y, float z)
  {
    normalBuffer.put(i * VALUES_PER_V_NORMAL + 0, x);
    normalBuffer.put(i * VALUES_PER_V_NORMAL + 1, y);
    normalBuffer.put(i * VALUES_PER_V_NORMAL + 2, z);
  }

  public void setTexture(int i, float t, float s)
  {
    textureBuffer.put(i * VALUES_PER_V_TEXTURE + 0, t);
    textureBuffer.put(i * VALUES_PER_V_TEXTURE + 1, s);
  }

  public abstract void init();
}
