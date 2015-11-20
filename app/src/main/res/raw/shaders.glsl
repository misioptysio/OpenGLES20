#VERTEX SIMPLE
	attribute vec4 aPosition;

	void main()
	{
		gl_Position = aPosition;
	}

#FRAGMENT SIMPLE
	precision mediump float;

	uniform vec4 uColor;

	void main()
	{
		gl_FragColor = uColor;
	}

#VERTEX MATRIX
	uniform mat4 uMVPMatrix;

	attribute vec4 aPosition;
	attribute vec4 aColor;
	attribute vec4 aNormal;

	varying vec4 vColor;
	varying vec4 vNormal;

	void main()
	{
		vColor = aColor;
		vNormal = aNormal;
		gl_Position = uMVPMatrix * aPosition;
	}

#FRAGMENT MATRIX
	precision mediump float;

	varying vec4 vColor;

	void main()
	{
		gl_FragColor = vColor;
	}

#VERTEX PHONG
	uniform mat4 uMVPMatrix;
	uniform vec3 uCameraPosition;
	uniform vec3 uLightPosition[LIGHT_COUNT];

	attribute vec4 aPosition;
	attribute vec4 aColor;
	attribute vec3 aNormal;

	varying vec3 vNormal;
	varying vec4 vColor;
	varying vec3 vCameraVector;
	varying vec3 vLightVector[LIGHT_COUNT];

	vec3 mNormal;

	void main()
	{
		vNormal = aNormal.xyz;
		vCameraVector = uCameraPosition - aPosition.xyz;

		for (int i = 0; i < LIGHT_COUNT; i++)
			vLightVector[i] = uLightPosition[i] - aPosition.xyz;

		vColor = aColor;
		gl_Position = uMVPMatrix * aPosition;
	}

#FRAGMENT PHONG
	precision mediump float;

	uniform vec4 uLightColor[LIGHT_COUNT];
	uniform mat4 uRSMatrix;

	varying vec3 vCameraVector;
	varying vec3 vLightVector[LIGHT_COUNT];

	varying vec4 vColor;
	varying vec3 vNormal;

	void main()
	{
		vec4 diffuse = vec4(0.0, 0.0, 0.0, 1.0);
		vec4 specular = vec4(0.0, 0.0, 0.0, 1.0);

		vec3 normalDir = normalize(mat3(uRSMatrix) * vNormal);
		//vec3 normalDir = normalize(vNormal);
		vec3 cameraDir = normalize(vCameraVector);

		for (int i = 0; i < LIGHT_COUNT; i++)
		{
			vec3 lightDir = normalize(vLightVector[i]);
			float mDiffuseDot = dot(normalDir, lightDir);

			diffuse += uLightColor[i] * clamp(mDiffuseDot, 0.0, 1.0);

			//vec3 mHalfAngle = normalize(cameraDir + lightDir);

			vec3 mReflected = 2.0 * normalDir * mDiffuseDot - lightDir;

			vec4 mSpecularColor = uLightColor[i];
			//float mSpecularDot = dot(normalDir, mHalfAngle);
			float mSpecularDot = dot(cameraDir, mReflected);

			specular += mSpecularColor * pow(clamp(mSpecularDot, 0.0, 1.0), 50.0);
		}

		gl_FragColor = clamp(vColor * diffuse + specular, 0.0, 1.0);
	}

