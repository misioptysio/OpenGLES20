package com.games;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glActiveTexture;
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
import static com.games.Const.VALUES_PER_V_TEXTURE;
import static com.games.Const.V_COLOR;
import static com.games.Const.V_NORMAL;
import static com.games.Const.V_POSITION;
import static com.games.Const.V_TEXTURE;

/**
 * Created by piotr.plys on 2015-11-13.
 */
public class GLCube extends GLObject
{
  protected FloatBuffer tangentBuffer;
  protected FloatBuffer cotangentBuffer;

  public GLCube()
  {
    vertexCount = 24;
    positionBuffer = createBuffer(vertexCount, V_POSITION);
    colorBuffer = createBuffer(vertexCount, V_COLOR);
    normalBuffer = createBuffer(vertexCount, V_NORMAL);
    textureBuffer = createBuffer(vertexCount, V_TEXTURE);
    tangentBuffer = createBuffer(vertexCount, V_NORMAL);
    cotangentBuffer = createBuffer(vertexCount, V_NORMAL);

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

    float[] tangentArray = {
      0.0f, 1.0f, 0.0f, //0X 00
      -1.0f, 0.0f, 0.0f, //0Y 01
      1.0f, 0.0f, 0.0f, //0Z 02

      0.0f, -1.0f, 0.0f, //1X 03
      -1.0f, 0.0f, 0.0f, //1Y 04
      1.0f, 0.0f, 0.0f, //1Z 05

      0.0f, 1.0f, 0.0f, //2X 06
      -1.0f, 0.0f, 0.0f, //2Y 07
      1.0f, 0.0f, 0.0f, //2Z 08

      0.0f, -1.0f, 0.0f, //3X 09
      -1.0f, 0.0f, 0.0f, //3Y 10
      1.0f, 0.0f, 0.0f, //3Z 11

      0.0f, 1.0f, 0.0f, //4X 12
      1.0f, 0.0f, 0.0f, //4Y 13
      1.0f, 0.0f, 0.0f, //4Z 14

      0.0f, -1.0f, 0.0f, //5X 15
      1.0f, 0.0f, 0.0f, //5Y 16
      1.0f, 0.0f, 0.0f, //5Z 17

      0.0f, 1.0f, 0.0f, //6X 18
      1.0f, 0.0f, 0.0f, //6Y 19
      1.0f, 0.0f, 0.0f, //6Z 20

      0.0f, -1.0f, 0.0f, //7X 21
      1.0f, 0.0f, 0.0f, //7Y 22
      1.0f, 0.0f, 0.0f, //7Z 23
    };
    tangentBuffer.put(tangentArray).position(0);

    float[] cotangentArray = {
      0.0f, 0.0f, 1.0f, //0X 00
      0.0f, 0.0f, 1.0f, //0Y 01
      0.0f, -1.0f, 0.0f, //0Z 02

      0.0f, 0.0f, 1.0f, //1X 03
      0.0f, 0.0f, 1.0f, //1Y 04
      0.0f, -1.0f, 0.0f, //1Z 05

      0.0f, 0.0f, 1.0f, //2X 06
      0.0f, 0.0f, 1.0f, //2Y 07
      0.0f, 1.0f, 0.0f, //2Z 08

      0.0f, 0.0f, 1.0f, //3X 09
      0.0f, 0.0f, 1.0f, //3Y 10
      0.0f, 1.0f, 0.0f, //3Z 11

      0.0f, 0.0f, 1.0f, //4X 12
      0.0f, 0.0f, -1.0f, //4Y 13
      0.0f, -1.0f, 0.0f, //4Z 14

      0.0f, 0.0f, 1.0f, //5X 15
      0.0f, 0.0f, -1.0f, //5Y 16
      0.0f, -1.0f, 0.0f, //5Z 17

      0.0f, 0.0f, 1.0f, //6X 18
      0.0f, 0.0f, -1.0f, //6Y 19
      0.0f, 1.0f, 0.0f, //6Z 20

      0.0f, 0.0f, 1.0f, //7X 21
      0.0f, 0.0f, -1.0f, //7Y 22
      0.0f, 1.0f, 0.0f, //7Z 23
    };
    cotangentBuffer.put(cotangentArray).position(0);

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
      colorArray[i * 4 + 0] = 1.0f;
      colorArray[i * 4 + 1] = 1.0f;
      colorArray[i * 4 + 2] = 1.0f;
      //colorArray[i * 4 + 3] = 0.2f;
    }
    colorBuffer.put(colorArray).position(0);

    float[] textureArray = {
      0.00f, 0.50f, //0X 00
      1.00f, 0.50f, //0Y 01
      0.25f, 0.75f, //0Z 02

      0.75f, 0.50f, //1X 03
      0.75f, 0.50f, //1Y 04
      0.50f, 0.75f, //1Z 05

      0.00f, 0.25f, //2X 06
      1.00f, 0.25f, //2Y 07
      0.25f, 0.00f, //2Z 08

      0.75f, 0.25f, //3X 09
      0.75f, 0.25f, //3Y 10
      0.50f, 0.00f, //3Z 11

      0.25f, 0.50f, //4X 12
      0.25f, 0.50f, //4Y 13
      0.25f, 0.50f, //4Z 14

      0.50f, 0.50f, //5X 15
      0.50f, 0.50f, //5Y 16
      0.50f, 0.50f, //5Z 17

      0.25f, 0.25f, //6X 18
      0.25f, 0.25f, //6Y 19
      0.25f, 0.25f, //6Z 20

      0.50f, 0.25f, //7X 21
      0.50f, 0.25f, //7Y 22
      0.50f, 0.25f, //7Z 23
    };
    textureBuffer.put(textureArray).position(0);

    short drawListArray[] =
    {
      2, 5, 17, 2, 17, 14, //+Z
      3, 9, 21, 3, 21, 15, //+X
      11, 8, 20, 11, 20, 23, //-Z
      6, 0, 12, 6, 12, 18, //-X
      7, 10, 4, 7, 4, 1, //-Y
      13, 16, 22, 13, 22, 19  //+Y
    };
    drawListBuffer.put(drawListArray).position(0);
  }

  @Override
  public void init()
  {
    int[] res = new int[16];
    GLShaders glShaders = mGlobals.getGlShaders();

    initShaders(glShaders.SHADER_NAME_PHONG);
    initProgram();

    // Bind attributes
    glBindAttribLocation(mProgram, 0, "aPosition");
    glBindAttribLocation(mProgram, 1, "aColor");
    glBindAttribLocation(mProgram, 2, "aNormal");
    glBindAttribLocation(mProgram, 3, "aTexture");
    glBindAttribLocation(mProgram, 4, "aTangent");
    glBindAttribLocation(mProgram, 5, "aCotangent");

    glLinkProgram(mProgram);
    glGetProgramiv(mProgram, GL_LINK_STATUS, IntBuffer.wrap(res));
    String log = glGetProgramInfoLog(mProgram);
    Utils.log("Linking program: " + (log.length() == 0 ? "CLEAN" : log));
  }

  public void draw(float[] mCameraMatrix)
  {
    int uMVPMatrixHandle;
    int uNormalMatrixHandle;
    int uModelMatrixHandle;
    int uCameraMatrixHandle;
    int uTextureColorHandle;
    int uTextureSpecularHandle;
    int uTextureNormalHandle;
    int uCameraPositionHandle;
    int uLightPositionHandle;
    int uLightColorHandle;

    int aPositionHandle;
    int aColorHandle;
    int aNormalHandle;
    int aTextureHandle;
    int aTangentHandle;
    int aCotangentHandle;
    float[] mMVPMatrix = new float[16];

    GLTextures glTextures = mGlobals.getGlTextures();
    GLLights glLights = mGlobals.getGlLights();

    aColorHandle = glGetAttribLocation(mProgram, "aColor");
    aPositionHandle = glGetAttribLocation(mProgram, "aPosition");
    aNormalHandle = glGetAttribLocation(mProgram, "aNormal");
    aTextureHandle = glGetAttribLocation(mProgram, "aTexture");
    aTangentHandle = glGetAttribLocation(mProgram, "aTangent");
    aCotangentHandle = glGetAttribLocation(mProgram, "aCotangent");

    glUseProgram(mProgram);

    glEnableVertexAttribArray(aPositionHandle);
    glEnableVertexAttribArray(aColorHandle);
    glEnableVertexAttribArray(aNormalHandle);
    glEnableVertexAttribArray(aTextureHandle);
    glEnableVertexAttribArray(aTangentHandle);
    glEnableVertexAttribArray(aCotangentHandle);

    positionBuffer.position(0);
    glVertexAttribPointer(aPositionHandle, VALUES_PER_V_POSITION, GL_FLOAT, false, 0, positionBuffer);
    glEnableVertexAttribArray(aPositionHandle);

    colorBuffer.position(0);
    glVertexAttribPointer(aColorHandle, VALUES_PER_V_COLOR, GL_FLOAT, false, 0, colorBuffer);
    glEnableVertexAttribArray(aColorHandle);

    normalBuffer.position(0);
    glVertexAttribPointer(aNormalHandle, VALUES_PER_V_NORMAL, GL_FLOAT, false, 0, normalBuffer);
    glEnableVertexAttribArray(aNormalHandle);

    textureBuffer.position(0);
    glVertexAttribPointer(aTextureHandle, VALUES_PER_V_TEXTURE, GL_FLOAT, false, 0, textureBuffer);
    glEnableVertexAttribArray(aTextureHandle);

    tangentBuffer.position(0);
    glVertexAttribPointer(aTangentHandle, VALUES_PER_V_NORMAL, GL_FLOAT, false, 0, tangentBuffer);
    glEnableVertexAttribArray(aTangentHandle);

    cotangentBuffer.position(0);
    glVertexAttribPointer(aCotangentHandle, VALUES_PER_V_NORMAL, GL_FLOAT, false, 0, cotangentBuffer);
    glEnableVertexAttribArray(aCotangentHandle);

    Matrix.multiplyMM(mMVPMatrix, 0, mCameraMatrix, 0, mModelMatrix, 0);
    // get handle to shape's transformation matrix
    uMVPMatrixHandle = glGetUniformLocation(mProgram, "uMVPMatrix");
    glUniformMatrix4fv(uMVPMatrixHandle, 1, false, mMVPMatrix, 0);

    uNormalMatrixHandle = glGetUniformLocation(mProgram, "uNormalMatrix");
    glUniformMatrix4fv(uNormalMatrixHandle, 1, false, mNormalMatrix, 0);

    uModelMatrixHandle = glGetUniformLocation(mProgram, "uModelMatrix");
    glUniformMatrix4fv(uModelMatrixHandle, 1, false, mModelMatrix, 0);

    uCameraMatrixHandle = glGetUniformLocation(mProgram, "uCameraMatrix");
    glUniformMatrix4fv(uCameraMatrixHandle, 1, false, mCameraMatrix, 0);

    uTextureColorHandle = glGetUniformLocation(mProgram, "uTextureColor");
    glActiveTexture(GLES20.GL_TEXTURE0 + 0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glTextures.getTextureHandle(GLTextures.TEXTURE_COLOR)); // Bind the texture to this unit.
    GLES20.glUniform1i(uTextureColorHandle, 0); // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.

    uTextureSpecularHandle = glGetUniformLocation(mProgram, "uTextureSpecular");
    glActiveTexture(GLES20.GL_TEXTURE0 + 1);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glTextures.getTextureHandle(GLTextures.TEXTURE_SPECULAR));
    GLES20.glUniform1i(uTextureSpecularHandle, 1);

    uTextureNormalHandle = glGetUniformLocation(mProgram, "uTextureNormal");
    glActiveTexture(GLES20.GL_TEXTURE0 + 2);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glTextures.getTextureHandle(GLTextures.TEXTURE_NORMAL));
    GLES20.glUniform1i(uTextureNormalHandle, 2);

    uCameraPositionHandle = glGetUniformLocation(mProgram, "uCameraPosition");
    uLightPositionHandle = glGetUniformLocation(mProgram, "uLightPosition");
    uLightColorHandle = glGetUniformLocation(mProgram, "uLightColor");

		if (uCameraPositionHandle != -1)
			glUniform3fv(uCameraPositionHandle, 1, mGlobals.getCameraPosition(), 0);

		if (uLightPositionHandle != -1)
			glUniform3fv(uLightPositionHandle, glLights.mLightCount, glLights.mLightPosition, 0);

		if (uLightColorHandle != -1)
			glUniform4fv(uLightColorHandle, glLights.mLightCount, glLights.mLightColor, 0);

		// Draw the cube
    glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_SHORT, drawListBuffer);

    // Disable vertex array
    glDisableVertexAttribArray(aPositionHandle);
    glDisableVertexAttribArray(aColorHandle);
    glDisableVertexAttribArray(aNormalHandle);
    glDisableVertexAttribArray(aTextureHandle);
    glDisableVertexAttribArray(aTangentHandle);
    glDisableVertexAttribArray(aCotangentHandle);
  }
}
