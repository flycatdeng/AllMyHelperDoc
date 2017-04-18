package com.dandy.helper.gles;

import com.dandy.helper.android.LogHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/4/17.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public class MaterialParser {
    private static final String TAG = "MaterialParser";
    private String vertexFileName = "";
    private String fragmentFileName = "";
    private ConcurrentHashMap<String, String> mHandlerNameAuthority = new ConcurrentHashMap<String, String>();

    public void parse(InputStream ins) {
        try {
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader br = new BufferedReader(isr);
            String temps = null;
            // 扫面文件，根据行类型的不同执行不同的处理逻辑
            while ((temps = br.readLine()) != null) {//一行
                // 用空格分割行中的各个组成部分
                String[] tempsa = temps.split("[ ]+");// [ ]+正则匹配多个连在一起的空格
                String typeFlag = tempsa[0].trim();
                if (typeFlag.equals("vertex_source_file")) {
                    vertexFileName = tempsa[1];//eg. vertex_source_file default_simple.vert
                } else if (typeFlag.equals("fragment_source_file")) {
                    fragmentFileName = tempsa[1];//eg. fragment_source_file default_simple.frag
                } else if (typeFlag.equals("attribute") || typeFlag.equals("uniform")) {
                    mHandlerNameAuthority.put(tempsa[3], typeFlag);//attribute/uniform
                }
            }
        } catch (Exception e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " load error e=" + e.getMessage());
            e.printStackTrace();
        }
    }

    public ConcurrentHashMap<String, String> getHandlerNameAuthority() {
        return mHandlerNameAuthority;
    }

    public String getVertexFileName() {
        return vertexFileName;
    }

    public String getFragmentFileName() {
        return fragmentFileName;
    }
}
