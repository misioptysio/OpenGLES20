package com.games;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glBindAttribLocation;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glUniform3fv;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.games.Const.VALUES_PER_V_COLOR;
import static com.games.Const.VALUES_PER_V_NORMAL;
import static com.games.Const.VALUES_PER_V_POSITION;
import static com.games.Const.V_COLOR;
import static com.games.Const.V_NORMAL;
import static com.games.Const.V_POSITION;

/**
 * Created by piotr.plys on 2015-11-13.
 */
public class GLCube extends GLObject
{
  public GLCube()
  {
    vertexCount = 24;
    createBuffer(vertexCount, V_POSITION);
    createBuffer(vertexCount, V_COLOR);
    createBuffer(vertexCount, V_NORMAL);
    createDrawListBuffer(36);

    float[] positionArray = {
      -1.0f, -1.0f, 1.0f, //0X 00
      -1.0f, -1.0f, 1.0f, //0Y 01
      -1.0f, -1.0f, 1.0f, //0Z 02

      1.0f, -1.0f, 1.0f, //1X 03
      1.0f, -1.0f, 1.0f, //1Y 04
      1.0f, -1.0f, 1.0f, //1Z 05

      -1.0f, -1.0f, -1.0f, //2X 06
      -1.0f, -1.0f, -1.0f, //2Y 07
      -1.0f, -1.0f, -1.0f, //2Z 08

      1.0f, -1.0f, -1.0f, //3X 09
      1.0f, -1.0f, -1.0f, //3Y 10
      1.0f, -1.0f, -1.0f, //3Z 11

      -1.0f, 1.0f, 1.0f, //4X 12
      -1.0f, 1.0f, 1.0f, //4Y 13
      -1.0f, 1.0f, 1.0f, //4Z 14

      1.0f, 1.0f, 1.0f, //5X 15
      1.0f, 1.0f, 1.0f, //5Y 16
      1.0f, 1.0f, 1.0f, //5Z 17

      -1.0f, 1.0f, -1.0f, //6X 18
      -1.0f, 1.0f, -1.0f, //6Y 19
      -1.0f, 1.0f, -1.0f, //6Z 20

      1.0f, 1.0f, -1.0f, //7X 21
      1.0f, 1.0f, -1.0f, //7Y 22
      1.0f, 1.0f, -1.0f, //7Z 23
    };
    positionBuffer.put(positionArray).position(0);

    float[] normalArray = {
      -1.0f, 0.0f, 0.0f, //0X 00
      0.0f, -1.0f, 0.0f, //0Y 01
      0.0f, 0.0f, 1.0f, //0Z 02

      1.0f, 0.0f, 0.0f, //1X 03
      0.0f, -1.0f, 0.0f, //1Y 04
      0.0f, 0.0f, 1.0f, //1Z 05

      -1.0f, 0.0f, 0.0f, //2X 06
      0.0f, -1.0f, 0.0f, //2Y 07
      0.0f, 0.0f, -1.0f, //2Z 08

      1.0f, 0.0f, 0.0f, //3X 09
      0.0f, -1.0f, 0.0f, //3Y 10
      0.0f, 0.0f, -1.0f, //3Z 11

      -1.0f, 0.0f, 0.0f, //4X 12
      0.0f, 1.0f, 0.0f, //4Y 13
      0.0f, 0.0f, 1.0f, //4Z 14

      1.0f, 0.0f, 0.0f, //5X 15
      0.0f, 1.0f, 0.0f, //5Y 16
      0.0f, 0.0f, 1.0f, //5Z 17

      -1.0f, 0.0f, 0.0f, //6X 18
      0.0f, 1.0f, 0.0f, //6Y 19
      0.0f, 0.0f, -1.0f, //6Z 20

      1.0f, 0.0f, 0.0f, //7X 21
      0.0f, 1.0f, 0.0f, //7Y 22
      0.0f, 0.0f, -1.0f, //7Z 23
    };
    normalBuffer.put(normalArray).position(0);

    float[] colorArray = {
      0.0f, 0.0f, 1.0f, 1.0f, //B
      0.0f, 0.0f, 1.0f, 1.0f, //B
      0.0f, 0.0f, 1.0f, 1.0f, //B

      1.0f, 0.0f, 1.0f, 1.0f, //RB
      1.0f, 0.0f, 1.0f, 1.0f, //RB
      1.0f, 0.0f, 1.0f, 1.0f, //RB

      0.0f, 0.0f, 0.0f, 1.0f, //
      0.0f, 0.0f, 0.0f, 1.0f, //
      0.0f, 0.0f, 0.0f, 1.0f, //

      1.0f, 0.0f, 0.0f, 1.0f, //R
      1.0f, 0.0f, 0.0f, 1.0f, //R
      1.0f, 0.0f, 0.0f, 1.0f, //R

      0.0f, 1.0f, 1.0f, 1.0f, //GB
      0.0f, 1.0f, 1.0f, 1.0f, //GB
      0.0f, 1.0f, 1.0f, 1.0f, //GB

      1.0f, 1.0f, 1.0f, 1.0f, //RGB
      1.0f, 1.0f, 1.0f, 1.0f, //RGB
      1.0f, 1.0f, 1.0f, 1.0f, //RGB

      0.0f, 1.0f, 0.0f, 1.0f, //G
      0.0f, 1.0f, 0.0f, 1.0f, //G
      0.0f, 1.0f, 0.0f, 1.0f, //G

      1.0f, 1.0f, 0.0f, 1.0f, //RG
      1.0f, 1.0f, 0.0f, 1.0f, //RG
      1.0f, 1.0f, 0.0f, 1.0f //RG
    };
    for (int i=0; i < 24; i++)
    {
      //colorArray[i * 4 + 0] = 1.0f;
      //colorArray[i * 4 + 1] = 0.8f;
      //colorArray[i * 4 + 2] = 0.0f;
    }
    colorBuffer.put(colorArray).position(0);

    short drawListArray1[] =
    {
      0, 1, 5, 0, 5, 4, //+Z
      1, 3, 7, 1, 7, 5, //+X
      3, 2, 6, 3, 6, 7, //-Z
      2, 0, 4, 2, 4, 6, //-X
      2, 3, 1, 2, 1, 0, //-Y
      5, 7, 6, 5, 6, 4  //+Y
    };
    short drawListArray[] =
      {
        2, 5, 17, 2, 17, 14, //+Z
        3, 9, 21, 3, 21, 15, //+X
        11, 8, 20, 11, 20, 23, //-Z
        6, 0, 12, 6, 12, 18, //-X
        7, 10, 4, 7, 4, 1, //-Y
        16, 22, 19, 16, 19, 13  //+Y
      };

    drawListBuffer.put(drawListArray).position(0);
  }

  @Override
  public void init()
  {
    int[] res = new int[16];

    initShaders(mGlobals.glShaders.SHADER_PHONG);
    initProgram();

    // Bind attributes
    glBindAttribLocation(mProgram, 0, "aPosition");
    glBindAttribLocation(mProgram, 1, "aColor");
    glBindAttribLocation(mProgram, 2, "aNormal");

    glLinkProgram(mProgram);
    glGetProgramiv(mProgram, GL_LINK_STATUS, IntBuffer.wrap(res));
    String log = glGetProgramInfoLog(mProgram);
    Utils.log("Linking program: " + (log.length() == 0 ? "CLEAN" : log));
  }

  public void draw(float[] mVPMatrix)
  {
    int uMVPMatrixHandle;
    int uRSMatrixHandle;
    int uCameraPositionHandle;
    int uLightPositionHandle;
    int uLightColorHandle;
    int aPositionHandle;
    int aColorHandle;
    int aNormalHandle;
    float[] mMVPMatrix = new float[16];

    aColorHandle = glGetAttribLocation(mProgram, "aColor");
    aPositionHandle = glGetAttribLocation(mProgram, "aPosition");
    aNormalHandle = glGetAttribLocation(mProgram, "aNormal");

    glUseProgram(mProgram);

    glEnableVertexAttribArray(aPositionHandle);
    glEnableVertexAttribArray(aColorHandle);
    glEnableVertexAttribArray(aNormalHandle);

    positionBuffer.position(0);
    glVertexAttribPointer(aPositionHandle, VALUES_PER_V_POSITION, GL_FLOAT, false, 0, positionBuffer);
    glEnableVertexAttribArray(aPositionHandle);

    colorBuffer.position(0);
    glVertexAttribPointer(aColorHandle, VALUES_PER_V_COLOR, GL_FLOAT, false, 0, colorBuffer);
    glEnableVertexAttribArray(aColorHandle);

    normalBuffer.position(0);
    glVertexAttribPointer(aNormalHandle, VALUES_PER_V_NORMAL, GL_FLOAT, false, 0, normalBuffer);
    glEnableVertexAttribArray(aNormalHandle);

    Matrix.multiplyMM(mMVPMatrix, 0, mVPMatrix, 0, mRSMatrix, 0);
    // get handle to shape's transformation matrix
    uMVPMatrixHandle = glGetUniformLocation(mProgram, "uMVPMatrix");
    glUniformMatrix4fv(uMVPMatrixHandle, 1, false, mMVPMatrix, 0);

    uRSMatrixHandle = glGetUniformLocation(mProgram, "uRSMatrix");
    glUniformMatrix4fv(uRSMatrixHandle, 1, false, mRSMatrix, 0);

    uCameraPositionHandle = glGetUniformLocation(mProgram, "uCameraPosition");
    uLightPositionHandle = glGetUniformLocation(mProgram, "uLightPosition");
    uLightColorHandle = glGetUniformLocation(mProgram, "uLightColor");

		/*
    mGlobals.resetBufferPositions();
    if (uCameraPositionHandle != -1)
      glUniform3fv(uCameraPositionHandle, 1, mGlobals.cameraPosition);

    if (uLightPositionHandle != -1)
      glUniform3fv(uLightPositionHandle, mGlobals.glLights.lightCount, mGlobals.glLights.lightPosition);

    if (uLightColorHandle != -1)
      glUniform4fv(uLightColorHandle, mGlobals.glLights.lightCount, mGlobals.glLights.lightColor);
*/
		float[] lightPos = new float[] {-10.0f, 30.0f, 40.0f};
		float[] lightCol = new float[] {1.0f, 1.0f, 1.0f, 1.0f};

		if (uCameraPositionHandle != -1)
			glUniform3fv(uCameraPositionHandle, 1, mGlobals.cameraPosition, 0);

		if (uLightPositionHandle != -1)
			glUniform3fv(uLightPositionHandle, 1, lightPos, 0);

		if (uLightColorHandle != -1)
			glUniform4fv(uLightColorHandle, 1, lightCol, 0);

		// Draw the cube
    glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_SHORT, drawListBuffer);

    // Disable vertex array
    glDisableVertexAttribArray(aPositionHandle);
    glDisableVertexAttribArray(aColorHandle);
    glDisableVertexAttribArray(aNormalHandle);
  }
}
