package com.dandy.helper.gles;

import android.content.Context;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.android.res.AssetsHelper;
import com.dandy.helper.java.basedata.StringHelper;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/4/17.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public class Material {
    private static final String TAG = "Material";
    private String mMaterailName;
    private int mProgramID;
    private ConcurrentHashMap<String, Integer> mPropertyNameHandlerMap = new ConcurrentHashMap<String, Integer>();

    public Material(final Context context, String materialFile) {
        String parentDir = StringHelper.getParentDirectory(materialFile);
        MaterialParser parser = new MaterialParser();
        parser.parse(AssetsHelper.getInputStream(context, materialFile));
        String vertexFile = parentDir + File.separator + parser.getVertexFileName();
        String fragmentFile = parentDir + File.separator + parser.getFragmentFileName();
        LogHelper.d(TAG, "vertexFile=" + vertexFile + " fragmentFile=" + fragmentFile);
        mProgramID = ShaderHelper.getProgramFromAsset(context, vertexFile, fragmentFile);
        Map map = parser.getHandlerNameAuthority();
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            final String propertyName = (String) entry.getKey();
            final String propertyAuthority = (String) entry.getValue();
            int handler = -1;
            if (propertyAuthority.equals("attribute")) {
                handler = GLES20.glGetAttribLocation(mProgramID, propertyName);
            } else if (propertyAuthority.equals("uniform")) {
                handler = GLES20.glGetUniformLocation(mProgramID, propertyName);
            }
            mPropertyNameHandlerMap.put(propertyName, handler);
        }
    }

    public int getHandlerByPropertyName(String propertyName) {
        Integer result = mPropertyNameHandlerMap.get(propertyName);
        return result == null ? -1 : result;
    }

    public int getProgramID() {
        return mProgramID;
    }

    public String getMaterailName() {
        return mMaterailName;
    }

    public void setmMaterailName(String materailSource) {
        this.mMaterailName = materailSource;
    }
}
