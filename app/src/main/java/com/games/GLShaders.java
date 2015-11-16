package com.games;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;

import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glShaderSource;

/**
 * Created by piotr.plys on 2015-11-12.
 */
public class GLShaders
{
	//
	public final int TYPE_VS = 0;
	public final int TYPE_FS = 1;

	public final int SHADER_DEFAULT = 0;
	public final int SHADER_SIMPLE = 1;
	public final int SHADER_MATRIX = 2;
	public final int SHADER_PHONG = 3;

	private final String VS_DEFAULT = "attribute vec4 aPosition; void main() { gl_Position = aPosition; }";
	private final String FS_DEFAULT = "precision mediump float; void main() { gl_FragColor = vec4(1., 0., 0., 1.); }";

	private String[][] shaderCode = new String[100][2];

	public GLShaders(Context context)
	{
		Resources resources = context.getResources();

		shaderCode[SHADER_DEFAULT][TYPE_VS] = VS_DEFAULT;
		shaderCode[SHADER_DEFAULT][TYPE_FS] = FS_DEFAULT;

		shaderCode[SHADER_SIMPLE][TYPE_VS] = resources.getString(R.string.vsSimple);
		shaderCode[SHADER_SIMPLE][TYPE_FS] = resources.getString(R.string.fsSimple);

		shaderCode[SHADER_MATRIX][TYPE_VS] = resources.getString(R.string.vsMatrix);
		shaderCode[SHADER_MATRIX][TYPE_FS] = resources.getString(R.string.fsMatrix);

		shaderCode[SHADER_PHONG][TYPE_VS] = resources.getString(R.string.vsPhong);
		shaderCode[SHADER_PHONG][TYPE_FS] = resources.getString(R.string.fsPhong);
	}

	public String getVertexShader(int id)
	{
		String res = shaderCode[id][TYPE_VS];

		if (res == null)
			res = shaderCode[0][TYPE_VS];

		return res;
	}

	public String getFragmentShader(int id)
	{
		String res = shaderCode[id][TYPE_FS];

		if (res == null)
			res = shaderCode[0][TYPE_FS];

		return res;
	}

	public int loadShader(int type, String shaderCode)
	{
		int shader = glCreateShader(type);

		glShaderSource(shader, shaderCode);
		glCompileShader(shader);
		String log = GLES20.glGetShaderInfoLog(shader);
		Utils.log("Shader compile: " + log);

		return shader;
	}
}
