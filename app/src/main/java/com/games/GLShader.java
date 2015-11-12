package com.games;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by piotr.plys on 2015-11-12.
 */
public class GLShader
{
	//
	public final int TYPE_VS = 0;
	public final int TYPE_FS = 1;

	public final int SHADER_DEFAULT = 0;
	public final int SHADER_SIMPLE = 1;
	public final int SHADER_MATRIX = 2;

	private final String VS_DEFAULT = "attribute vec4 aPosition; void main() { gl_Position = aPosition; }";
	private final String FS_DEFAULT = "precision mediump float; void main() { gl_FragColor = vec4(1., 0., 0., 1.); }";

	private String[][] shaderCode = new String[100][2];
	private final Resources resources;

	public GLShader(Context context)
	{
		resources = context.getResources();

		shaderCode[SHADER_DEFAULT][TYPE_VS] = VS_DEFAULT;
		shaderCode[SHADER_DEFAULT][TYPE_FS] = FS_DEFAULT;

		shaderCode[SHADER_SIMPLE][TYPE_VS] = resources.getString(R.string.vsSimple);
		shaderCode[SHADER_SIMPLE][TYPE_FS] = resources.getString(R.string.fsSimple);

		shaderCode[SHADER_MATRIX][TYPE_VS] = resources.getString(R.string.vsMatrix);
		shaderCode[SHADER_MATRIX][TYPE_FS] = resources.getString(R.string.fsMatrix);
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
}
