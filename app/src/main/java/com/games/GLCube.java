package com.games;

import static android.opengl.GLES20.*;
import static com.games.Const.*;

/**
 * Created by piotr.plys on 2015-11-13.
 */
public class GLCube extends GLObject
{
  public GLCube()
  {
    vertexCount = 8;
    createBuffer(vertexCount, V_POSITION);
    createBuffer(vertexCount, V_COLOR);
    createDrawListBuffer(36);

    float[] positionArray = {-1.0f, -1.0f, 1.0f, //0
      1.0f, -1.0f, 1.0f, //1
      -1.0f, -1.0f, -1.0f, //2
      1.0f, -1.0f, -1.0f, //3

      -1.0f, 1.0f, 1.0f, //4
      1.0f, 1.0f, 1.0f, //5
      -1.0f, 1.0f, -1.0f, //6
      1.0f, 1.0f, -1.0f //7
    };
    positionBuffer.put(positionArray).position(0);

    short drawListArray[] =
    {
      0, 1, 5, 0, 5, 4, //+Z
      1, 3, 7, 1, 7, 5, //+X
      3, 2, 6, 3, 6, 7, //-Z
      2, 0, 4, 2, 4, 6, //-X
      2, 3, 1, 2, 1, 0, //-Y
      5, 7, 6, 5, 6, 4  //+Y
    };
    drawListBuffer.put(drawListArray).position(0);
  }

  @Override
  public void init()
  {
    initShaders(mShader.SHADER_MATRIX);
    initProgram();

    // Bind attributes
    glBindAttribLocation(mProgram, 0, "aPosition");
    glBindAttribLocation(mProgram, 1, "aColor");

    glLinkProgram(mProgram);
  }

  public void draw(float[] mvpMatrix)
  {
    int uMVPMatrixHandle;
    int aPositionHandle;
    int aColorHandle;

    aColorHandle = glGetAttribLocation(mProgram, "aColor");
    aPositionHandle = glGetAttribLocation(mProgram, "aPosition");

    glUseProgram(mProgram);

    glEnableVertexAttribArray(aPositionHandle);
    glEnableVertexAttribArray(aColorHandle);

    positionBuffer.position(0);
    glVertexAttribPointer(aPositionHandle, VALUES_PER_V_POSITION, GL_FLOAT, false, 0, positionBuffer);
    glEnableVertexAttribArray(aPositionHandle);

    colorBuffer.position(0);
    glVertexAttribPointer(aColorHandle, VALUES_PER_V_COLOR, GL_FLOAT, false, 0, colorBuffer);
    glEnableVertexAttribArray(aColorHandle);

    // get handle to fragment shader's vColor member
    //uColorHandle = glGetUniformLocation(mProgram, "uColor");

    // Set color for drawing the triangle
    //glUniform4fv(uColorHandle, 1, color, 0);

    // get handle to shape's transformation matrix
    uMVPMatrixHandle = glGetUniformLocation(mProgram, "uMVPMatrix");

    // Pass the projection and view transformation to the shader
    glUniformMatrix4fv(uMVPMatrixHandle, 1, false, mvpMatrix, 0);

    // Draw the cube
    glDrawElements(GL_TRIANGLES, drawListBuffer.capacity(), GL_UNSIGNED_SHORT, drawListBuffer);

    // Disable vertex array
    glDisableVertexAttribArray(aPositionHandle);
    glDisableVertexAttribArray(aColorHandle);
  }
}
