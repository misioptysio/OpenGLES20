package com.games;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by piotr.plys on 2015-11-12.
 */
public class GLShader {

    private final Resources resources;

    public GLShader(Context context) {
        resources = context.getResources();

        resources.getString(R.string.dupaJasiu);
    }

    public String getShader(int id) {
        return resources.getString(id);
    }
}
