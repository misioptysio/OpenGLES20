package com.games;

import android.opengl.Matrix;

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
  protected float[] mRotationMatrix = new float[16];
  protected float[] mScaleMatrix = new float[16];
  protected float[] mTranslationMatrix = new float[16];
  protected float[] mNormalMatrix = new float[16];
  protected float[] mModelMatrix = new float[16];
  protected float[] mInvNormalMatrix = new float[16];
  protected float[] mInvTransposedNormalMatrix = new float[16];

  protected FloatBuffer positionBuffer;
  protected FloatBuffer colorBuffer;
  protected FloatBuffer textureBuffer;
  protected FloatBuffer normalBuffer;
  protected ShortBuffer drawListBuffer;

  protected int mProgram;
  protected int mVertexShader;
  protected int mFragmentShader;

  public GLObject()
  {
    mNormalMatrix = identityMatrix.clone();
    mModelMatrix = identityMatrix.clone();
    mTranslationMatrix = identityMatrix.clone();
    mScaleMatrix = identityMatrix.clone();
    mRotationMatrix = identityMatrix.clone();
  }

  public void setGlobals(Globals globals)
  {
    mGlobals = globals;
  }

  public void initShaders(int shaderID)
  {
    GLShaders glShaders;

    glShaders = mGlobals.getGlShaders();
    mVertexShader = glShaders.loadShader(GL_VERTEX_SHADER, mGlobals.getVertexShader(shaderID));
    mFragmentShader = glShaders.loadShader(GL_FRAGMENT_SHADER, mGlobals.getFragmentShader(shaderID));
  }

  public void initProgram()
  {
    mProgram = glCreateProgram();

    glAttachShader(mProgram, mVertexShader);
    glAttachShader(mProgram, mFragmentShader);
  }

  public FloatBuffer createBuffer(int count, byte aType)
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

    return floatBuffer;
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

  public void setTranslation(float tx, float ty, float tz)
  {
    Matrix.translateM(mTranslationMatrix, 0, identityMatrix, 0, tx, ty, tz);
    recalculateMatrices();
  }

  public void setRotation(float angle, float x, float y, float z, boolean multiply)
  {
    float[] tempRotationMatrix = new float[16];

    Matrix.setRotateM(tempRotationMatrix, 0, angle, x, y, z);
    if (multiply)
    {
      float[] tempMultMatrix = new float[16];
      Matrix.multiplyMM(tempMultMatrix, 0, mRotationMatrix, 0, tempRotationMatrix, 0);
      mRotationMatrix = tempMultMatrix;
    }
    else
      mRotationMatrix = tempRotationMatrix;

    recalculateMatrices();
  }

  public void setScale(float sx, float sy, float sz)
  {
    Matrix.scaleM(mScaleMatrix, 0, identityMatrix, 0, sx, sy, sz);
    recalculateMatrices();
  }

  public void recalculateMatrices()
  {
    Matrix.multiplyMM(mNormalMatrix, 0, mRotationMatrix, 0, mScaleMatrix, 0);
    Matrix.multiplyMM(mModelMatrix, 0, mTranslationMatrix, 0, mNormalMatrix, 0);

    Matrix.invertM(mInvNormalMatrix, 0, mNormalMatrix, 0);
    Matrix.transposeM(mInvTransposedNormalMatrix, 0, mInvNormalMatrix, 0);
  }

  public abstract void init();
}
