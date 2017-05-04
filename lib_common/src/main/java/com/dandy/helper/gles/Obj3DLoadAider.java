package com.dandy.helper.gles;

import android.content.Context;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.android.res.AssetsHelper;
import com.dandy.helper.java.basedata.StringHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <pre>
 *      obj文件加载类
 *      注意，文件可能不带有法向量，那这个法向量就需要由各个点的向量叉乘之后得到法向量了 eg:
 *          f 4/4 5/5 6/6 不带法向量索引
 *          f 4/4/4 5/5/5 6/6/6 带有法向量索引
 *          f 4 4 4 还有只带有顶点索引的
 *          f 4//4 5//5 6//6  还有只带顶点和法向量的，不带有纹理坐标的
 *          所以，文件可能不带法向量数据，也可能不带纹理坐标数据，还有可能都带了，但是我就是不使用
 * </pre>
 * <pre>
 *     部分资料参考自《Android 3D游戏开发技术宝典——OpenGL ES 2.0》，需要详解请购买正版书籍
 * </pre>
 * Created by flycatdeng on 2017/4/14.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public class Obj3DLoadAider {
    private static final String TAG = Obj3DLoadAider.class.getSimpleName();
    private float[] mVertexXYZ;
    private float[] mNormalVectorXYZ;
    private float[] mTextureVertexST;
    private boolean mUseNormalVectorIfExistInObj = true;//如果在obj文件中带有法向量信息，我是否使用？默认为使用
    private ArrayList<Vec3> verticesList = new ArrayList<Vec3>();// 原始顶点坐标列表--直接从obj文件中加载
    private ArrayList<Vec2> textureCoorList = new ArrayList<Vec2>();//纹理坐标列表
    private ArrayList<Vec3> normalVecCoorList = new ArrayList<Vec3>();//法向量坐标列表
    private ArrayList<FacePointsInfo> facePointInfoList = new ArrayList<FacePointsInfo>();//面列表（字符串信息）
    private boolean useCountNormal = false;//是否使用计算出来的法向量
    private volatile int numVerts = 0, numTex = 0, numNorms = 0, numFaces = 0;
    private int verticesCount = 0, textureCoorCount = 0, normalCount = 0;
    private long mStartParseTime = 0L;
    private long mParseTime = 0L;

    public Obj3DLoadAider() {
    }

    /**
     * @param useNormalVectorIfExist 如果在obj文件中带有法向量信息，我是否使用？默认为使用,不使用的话则会使用通过顶点坐标叉乘得到的法向量
     */
    public Obj3DLoadAider(boolean useNormalVectorIfExist) {
        mUseNormalVectorIfExistInObj = useNormalVectorIfExist;
    }

    /**
     * 从字符串（内容）加载数据
     *
     * @param context        上下文
     * @param content        要加载的字符串内容
     * @param onLoadListener 监听加载成功与否
     */
    public void loadFromContent(Context context, String content, OnLoadListener onLoadListener) {
        initDatas(StringHelper.convertStringToStream(content), onLoadListener);
    }

    /**
     * 从raw加载数据
     *
     * @param context        上下文
     * @param resID          文件ID
     * @param onLoadListener 监听加载成功与否
     */
    public void loadFromRaw(Context context, int resID, OnLoadListener onLoadListener) {
        initDatas(context.getResources().openRawResource(resID), onLoadListener);
    }

    /**
     * 从assets加载数据
     *
     * @param context        上下文
     * @param fileName       obj文件在assets目录下的全路劲名称
     * @param onLoadListener 监听加载成功与否
     */
    public void loadFromAsset(Context context, String fileName, OnLoadListener onLoadListener) {
        initDatas(AssetsHelper.getInputStream(context, fileName), onLoadListener);
    }

    /**
     * 从obj文件流加载数据
     *
     * @param objIns         obj文件流
     * @param onLoadListener 监听加载成功与否
     */
    public void loadFromInputStream(InputStream objIns, OnLoadListener onLoadListener) {
        initDatas(objIns, onLoadListener);
    }

    private void initDatas(final InputStream objIns, final OnLoadListener onLoadListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mStartParseTime = System.currentTimeMillis();
                loadContent(objIns, onLoadListener);
                try {
                    initConditions();
                    dealDetailData();
                    mParseTime = System.currentTimeMillis() - mStartParseTime;
                    if (onLoadListener != null) {
                        onLoadListener.onLoadOK(new LoadResult());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (onLoadListener != null) {
                        onLoadListener.onLoadFailed(e.getMessage());
                    }
                } finally {
                    verticesList.clear();
                    textureCoorList.clear();
                    normalVecCoorList.clear();
                    facePointInfoList.clear();
                }
            }
        }).start();
    }

    private void dealDetailData() {
        //处理面中的索引
        for (FacePointsInfo facePointsInfo : facePointInfoList) {
            //顶点数据时必须有的
            int verticesIndex = Integer.parseInt(facePointsInfo.firstPointInfo.split("/")[0]) - 1;
            Vec3 firstVertice = verticesList.get(verticesIndex);
            mVertexXYZ[verticesCount++] = firstVertice.x;
            mVertexXYZ[verticesCount++] = firstVertice.y;
            mVertexXYZ[verticesCount++] = firstVertice.z;
            verticesIndex = Integer.parseInt(facePointsInfo.secondPointInfo.split("/")[0]) - 1;
            Vec3 secondVertice = verticesList.get(verticesIndex);
            mVertexXYZ[verticesCount++] = secondVertice.x;
            mVertexXYZ[verticesCount++] = secondVertice.y;
            mVertexXYZ[verticesCount++] = secondVertice.z;
            verticesIndex = Integer.parseInt(facePointsInfo.thirdPointInfo.split("/")[0]) - 1;
            Vec3 thirdVertice = verticesList.get(verticesIndex);
            mVertexXYZ[verticesCount++] = thirdVertice.x;
            mVertexXYZ[verticesCount++] = thirdVertice.y;
            mVertexXYZ[verticesCount++] = thirdVertice.z;
            //法向量数据，useCountNormal已经综合判断过了，true则表示使用计算出来的数据，否则使用自带的数据
            if (useCountNormal) {
                Vec3 vec2To1 = secondVertice.minus(firstVertice);//第二个顶点到第一个顶点的向量
                Vec3 vec3To1 = thirdVertice.minus(firstVertice);//第三个顶点到第一个顶点的向量
                //叉乘得到法向量
                Vec3 faceNormal = vec2To1.crossProduct(vec3To1).normalize();
                for (int i = 0; i < 3; i++) {
                    mNormalVectorXYZ[normalCount++] = faceNormal.x;
                    mNormalVectorXYZ[normalCount++] = faceNormal.y;
                    mNormalVectorXYZ[normalCount++] = faceNormal.z;
                }
            } else {
                int normalIndex = Integer.parseInt(facePointsInfo.firstPointInfo.split("/")[2]) - 1;//如果值不能取到2，那再前面就已经被判断过了，就不会走到这里来的
                Vec3 firstNormal = normalVecCoorList.get(normalIndex).normalize();
                mNormalVectorXYZ[normalCount++] = firstNormal.x;
                mNormalVectorXYZ[normalCount++] = firstNormal.y;
                mNormalVectorXYZ[normalCount++] = firstNormal.z;
                normalIndex = Integer.parseInt(facePointsInfo.secondPointInfo.split("/")[2]) - 1;//如果值不能取到2，那再前面就已经被判断过了，就不会走到这里来的
                Vec3 secondNormal = normalVecCoorList.get(normalIndex).normalize();
                mNormalVectorXYZ[normalCount++] = secondNormal.x;
                mNormalVectorXYZ[normalCount++] = secondNormal.y;
                mNormalVectorXYZ[normalCount++] = secondNormal.z;
                normalIndex = Integer.parseInt(facePointsInfo.thirdPointInfo.split("/")[2]) - 1;//如果值不能取到2，那再前面就已经被判断过了，就不会走到这里来的
                Vec3 thirdNormal = normalVecCoorList.get(normalIndex).normalize();
                mNormalVectorXYZ[normalCount++] = thirdNormal.x;
                mNormalVectorXYZ[normalCount++] = thirdNormal.y;
                mNormalVectorXYZ[normalCount++] = thirdNormal.z;
            }
            //纹理坐标数据可能没有
            if (mTextureVertexST == null) {//不使用纹理坐标
                continue;
            } else {
                int texCoorIndex = Integer.parseInt(facePointsInfo.firstPointInfo.split("/")[1]) - 1;
                Vec2 firstTexCoor = textureCoorList.get(texCoorIndex);
                mTextureVertexST[textureCoorCount++] = firstTexCoor.x;
                mTextureVertexST[textureCoorCount++] = firstTexCoor.y;
                texCoorIndex = Integer.parseInt(facePointsInfo.secondPointInfo.split("/")[1]) - 1;
                Vec2 secondTexCoor = textureCoorList.get(texCoorIndex);
                mTextureVertexST[textureCoorCount++] = secondTexCoor.x;
                mTextureVertexST[textureCoorCount++] = secondTexCoor.y;
                texCoorIndex = Integer.parseInt(facePointsInfo.thirdPointInfo.split("/")[1]) - 1;
                Vec2 thirdTexCoor = textureCoorList.get(texCoorIndex);
                mTextureVertexST[textureCoorCount++] = thirdTexCoor.x;
                mTextureVertexST[textureCoorCount++] = thirdTexCoor.y;
            }
        }
    }

    private void initConditions() {
        numVerts = verticesList.size();
        numTex = textureCoorList.size();
        numNorms = normalVecCoorList.size();
        numFaces = facePointInfoList.size();
        LogHelper.d(TAG, LogHelper.getThreadName() + " numVerts=" + numVerts + " numTex=" + numTex + " numNorms=" + numNorms + " numFaces=" + numFaces);
        if (numFaces == 0) {//没有面
            throw new RuntimeException("this obj has no face");
        }

        mVertexXYZ = new float[numFaces * 3 * 3];
        mTextureVertexST = new float[numFaces * 3 * 2];
        mNormalVectorXYZ = new float[numFaces * 3 * 3];

        if (numNorms == 0) {//一开始文件中就没有
            useCountNormal = true;
        }
        String fistFirstPointInfo = facePointInfoList.get(0).firstPointInfo;
        String[] indexValue = fistFirstPointInfo.split("/");
        int faceIndexCount = indexValue.length;//一个点有几个索引
        if (faceIndexCount == 0) {
            throw new RuntimeException("this obj face has no index ");
        }
        if (faceIndexCount < 3) {//说明没有用到法向量，4/4 或4这样的
            useCountNormal = true;
            mTextureVertexST = null;//如果索引个数只有1或2，说明只有顶点坐标或和法向量，而没有纹理坐标，只有当索引等于3时才有纹理坐标
        } else {//=3
            if (indexValue[1].trim().equals("")) {//纹理坐标木有用到 eg. 4//4
                mTextureVertexST = null;
                textureCoorList.clear();//如果有数据都要清除掉
                textureCoorList = null;
            }
            if (indexValue[2].trim().equals("")) {//法向量木有用到 eg. 4// 或4/4/
                useCountNormal = true;
            }
        }
    }

    private void loadContent(InputStream objIns, OnLoadListener onLoadListener) {
        try {
            InputStreamReader isr = new InputStreamReader(objIns);
            BufferedReader br = new BufferedReader(isr);
            String temps = null;
            // 扫面文件，根据行类型的不同执行不同的处理逻辑
            while ((temps = br.readLine()) != null) {//一行
                // 用空格分割行中的各个组成部分
                String[] tempsa = temps.split("[ ]+");// [ ]+正则匹配多个连在一起的空格
                String typeFlag = tempsa[0].trim();
                if (typeFlag.equals("v")) {// 此行为顶点坐标 v 0.228932 8.415517 -2.602092
                    // 若为顶点坐标行则提取出此顶点的XYZ坐标添加到原始顶点坐标列表中
                    Vec3 vec3 = new Vec3(Float.parseFloat(tempsa[1]), Float.parseFloat(tempsa[2]), Float.parseFloat(tempsa[3]));
                    verticesList.add(vec3);
                } else if (typeFlag.equals("vt")) {// 此行为纹理坐标行 vt 0.679628 0.828256
                    // 若为纹理坐标行则提取ST坐标并添加进原始纹理坐标列表中
                    Vec2 vec2 = new Vec2(Float.parseFloat(tempsa[1]), Float.parseFloat(tempsa[2]));
                    textureCoorList.add(vec2);
                } else if (mUseNormalVectorIfExistInObj && typeFlag.equals("vn")) {// 此行为法向量 vn 1.000000 -0.000000 -0.000001, mUseNormalVectorIfExistInObj如果用户不想用文件中的法向量，那我也没必要去加载啊
                    Vec3 vec3 = new Vec3(Float.parseFloat(tempsa[1]), Float.parseFloat(tempsa[2]), Float.parseFloat(tempsa[3]));
                    normalVecCoorList.add(vec3);
                } else if (typeFlag.equals("f")) {// 此行为三角形面
                    FacePointsInfo facePointInfo = new FacePointsInfo(tempsa[1], tempsa[2], tempsa[3]);
                    facePointInfoList.add(facePointInfo);
                }
            }
        } catch (Exception e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " load error e=" + e.getMessage());
            if (onLoadListener != null) {
                onLoadListener.onLoadFailed(e.getMessage());
            }
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     *      一个面的三个点的信息
     *      可能是4/4/4 5/5/5 6/6/6 正常的
     *      也可能是 4 5 6   只有顶点坐标
     *      也有可能是 4/4 5/5 6/6
     *      也可能是 4//4 5//5 6//6
     * </pre>
     */
    class FacePointsInfo {
        String firstPointInfo;
        String secondPointInfo;
        String thirdPointInfo;

        public FacePointsInfo(String firstPointInfo, String secondPointInfo, String thirdPointInfo) {
            this.firstPointInfo = firstPointInfo;
            this.secondPointInfo = secondPointInfo;
            this.thirdPointInfo = thirdPointInfo;
        }
    }

    /**
     * <pre>
     *      解析obj文件之后得到的结果，包括
     *          顶点坐标个数，及对应的每个坐标的xyz值
     *          法向量个数及其对应每个向量的xyz值
     *          纹理坐标个数及其每个坐标对应的st值
     *          面（三角形）的个数
     *          加载的时间
     * </pre>
     */
    public class LoadResult {

        /**
         * 顶点坐标每个坐标的xyz值
         *
         * @return
         */
        public float[] getVertexXYZ() {
            return mVertexXYZ;
        }

        /**
         * 法向量每个坐标的xyz值
         *
         * @return
         */
        public float[] getNormalVectorXYZ() {
            return mNormalVectorXYZ;
        }

        /**
         * 纹理坐标每个坐标的st值
         *
         * @return
         */
        public float[] getTextureVertexST() {
            return mTextureVertexST;
        }

        /**
         * 顶点坐标个数
         *
         * @return
         */
        public int getVextexCount() {
            return numVerts;
        }

        /**
         * 法向量个数
         *
         * @return
         */
        public int getNormalVectorCount() {
            return numNorms;
        }

        /**
         * 纹理坐标个数
         *
         * @return
         */
        public int getTextureCoorCount() {
            return numTex;
        }

        /**
         * 面（三角形）的个数
         *
         * @return
         */
        public int getTrianglesCount() {
            return numFaces;
        }

        /**
         * 加载的时间,以ms记
         *
         * @return
         */
        public long getLoadTime() {
            return mParseTime;
        }

        @Override
        public String toString() {
            return "LoadResult{" +
                    "VertexXYZ=" + mVertexXYZ +
                    ", NormalVectorXYZ=" + mNormalVectorXYZ +
                    ", TextureVertexST=" + mTextureVertexST +
                    ", Verts count=" + numVerts +
                    ", Texcoor count=" + numTex +
                    ", Normal count=" + numNorms +
                    ", Triangles count=" + numFaces +
                    ", Load Time=" + mParseTime +
                    '}';
        }
    }


    /**
     * <pre>
     *      加载成功与否
     *      成功了则将Obj3DLoadAider返回出去，
     *      没有成功则将失败原因返回
     * </pre>
     */
    public interface OnLoadListener {
        /**
         * 文件数据加载成功
         *
         * @param result
         */
        void onLoadOK(LoadResult result);

        /**
         * 文件数据加载失败
         *
         * @param failedMsg
         */
        void onLoadFailed(String failedMsg);
    }
}
