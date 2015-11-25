package com.games;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glShaderSource;

/**
 * Created by piotr.plys on 2015-11-12.
 */
public class GLShaders
{
  public final int SHADER_TYPE_UNKNOWN = -1;
  public final int SHADER_TYPE_VERTEX = 0;
  public final int SHADER_TYPE_FRAGMENT = 1;

  public final int SHADER_NAME_UNKNOWN = -1;
  public final int SHADER_NAME_DEFAULT = 0;
  public final int SHADER_NAME_SIMPLE = 1;
  public final int SHADER_NAME_MATRIX = 2;
  public final int SHADER_NAME_PHONG = 3;

  private final String VERTEX_SHADER_DEFAULT_CODE = "attribute vec4 aPosition; void main() { gl_Position = aPosition; }";
  private final String FRAGMENT_SHADER_DEFAULT_CODE = "precision mediump float; void main() { gl_FragColor = vec4(1., 0., 0., 1.); }";

  private String[][] shaderCode = new String[100][2];

  public GLShaders()
  {
    shaderCode[SHADER_NAME_DEFAULT][SHADER_TYPE_VERTEX] = VERTEX_SHADER_DEFAULT_CODE;
    shaderCode[SHADER_NAME_DEFAULT][SHADER_TYPE_FRAGMENT] = FRAGMENT_SHADER_DEFAULT_CODE;

    //read shaders.glsl
    readShaders(OpenGLApplication.getContext(), R.raw.shaders);
  }

  public int findShaderNameID(String name)
  {
    int res;

    switch (name)
    {
      case "DEFAULT":
        res = SHADER_NAME_DEFAULT;
        break;

      case "SIMPLE":
        res = SHADER_NAME_SIMPLE;
        break;

      case "MATRIX":
        res = SHADER_NAME_MATRIX;
        break;

      case "PHONG":
        res = SHADER_NAME_PHONG;
        break;

      default:
        res = SHADER_NAME_UNKNOWN;
    }

    return res;
  }

  public int findShaderTypeID(String name)
  {
    int res;

    switch (name)
    {
      case "FRAGMENT":
        res = SHADER_TYPE_FRAGMENT;
        break;

      case "VERTEX":
        res = SHADER_TYPE_VERTEX;
        break;

      default:
        res = SHADER_TYPE_UNKNOWN;
    }

    return res;
  }

  public void setShaderCode(int nameID, int typeId, String code)
  {
    if ((nameID != SHADER_NAME_UNKNOWN) && (typeId != SHADER_TYPE_UNKNOWN))
    {
      if (code.length() != 0)
      {
        shaderCode[nameID][typeId] = code;
      }
      else
      {
        shaderCode[nameID][typeId] = shaderCode[SHADER_NAME_DEFAULT][typeId];
      }
    }
  }

  public void readShaders(final Context context, final int resourceId)
  {
    final InputStream inputStream = context.getResources().openRawResource(resourceId);
    final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

    Pattern patShader = Pattern.compile("\\#(VERTEX|FRAGMENT)\\s+(.+)");
    Matcher matcher;

    String nextLine;
    String shaderName;
    String shaderType;
    int shaderNameID;
    int shaderTypeID;
    int currentShaderNameID = SHADER_NAME_UNKNOWN;
    int currentShaderTypeID = SHADER_TYPE_UNKNOWN;

    final StringBuilder body = new StringBuilder();

    try
    {
      while ((nextLine = bufferedReader.readLine()) != null)
      {
        matcher = patShader.matcher(nextLine);
        if (matcher.matches())
        {
          shaderName = matcher.group(2).trim().toUpperCase();
          shaderType = matcher.group(1).trim().toUpperCase();
          shaderNameID = findShaderNameID(shaderName);
          shaderTypeID = findShaderTypeID(shaderType);

          setShaderCode(currentShaderNameID, currentShaderTypeID, body.toString().trim());
          currentShaderNameID = shaderNameID;
          currentShaderTypeID = shaderTypeID;

          body.delete(0, body.length());
          Utils.log("vertex " + "[" + shaderName + "]");
        }
        else
        {
          body.append(nextLine + '\n');
        }
      }
    }
    catch (IOException e)
    {
    }

    setShaderCode(currentShaderNameID, currentShaderTypeID, body.toString().trim());
  }

  public String getVertexShader(int id)
  {
    String res = shaderCode[id][SHADER_TYPE_VERTEX];

    if (res == null)
    {
      res = shaderCode[0][SHADER_TYPE_VERTEX];
    }

    return res;
  }

  public String getFragmentShader(int id)
  {
    String res = shaderCode[id][SHADER_TYPE_FRAGMENT];

    if (res == null)
    {
      res = shaderCode[0][SHADER_TYPE_FRAGMENT];
    }

    return res;
  }

  public int loadShader(int type, String shaderCode)
  {
    int shader = glCreateShader(type);

    glShaderSource(shader, shaderCode);
    glCompileShader(shader);
    String log = GLES20.glGetShaderInfoLog(shader);
    Utils.log("Shader compile: " + (log.length() == 0 ? "CLEAN" : log));

    return shader;
  }
}
