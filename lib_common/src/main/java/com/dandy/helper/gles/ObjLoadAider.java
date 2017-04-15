package com.dandy.helper.gles;

import android.annotation.SuppressLint;
import android.content.Context;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.java.Vector3DimentionHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * <pre>
 *      obj文件加载类
 *      注意，文件可能不带有法向量，那这个法向量就需要由各个点的向量叉乘之后得到法向量了 eg:
 *          f 4/4 5/5 6/6 不带法向量索引
 *          f 4/4/4 5/5/5 6/6/6 带有法向量索引
 *          f 4 4 4 还有只带有顶点索引的
 *          f 4//4 5//5 6//6  还有只带顶点和法向量的，不带有纹理坐标的
 * </pre>
 * <pre>
 *     部分资料参考自《Android 3D游戏开发技术宝典——OpenGL ES 2.0》，需要详解请购买正版书籍
 * </pre>
 * @deprecated
 * @author dengchukun 2016年12月12日
 */
@SuppressLint("UseSparseArrays")
public class ObjLoadAider {

    private static final String TAG = ObjLoadAider.class.getSimpleName();
    private float[] mVertexXYZ;
    private float[] mNormalVectorXYZ;
    private float[] mTextureVertexST;

    /**
     * objIns is the 3d_obj file InputStream you can get it from assets "r.getAssets().open(objIns)" or from raw
     */
    public ObjLoadAider(Context context, InputStream objIns, OnLoadListener onLoadListener) {
        mOnLoadListener = onLoadListener;
        initDatas(objIns);
    }

    private void initDatas(InputStream objIns) {
        LogHelper.d(TAG, LogHelper.getThreadName());
        ArrayList<Float> alv = new ArrayList<Float>();// 原始顶点坐标列表--直接从obj文件中加载
        ArrayList<Float> alt = new ArrayList<Float>(); // 原始纹理坐标列表
        ArrayList<Float> aln = new ArrayList<Float>();  // 原始法向量列表
        ArrayList<Integer> alFaceIndex = new ArrayList<Integer>();// 顶点组装面索引列表--根据面的信息从文件中加载
        ArrayList<Float> alvResult = new ArrayList<Float>(); // 结果顶点坐标列表--按面组织好
        ArrayList<Float> altResult = new ArrayList<Float>(); // 纹理坐标结果列表
        ArrayList<Float> alnResult = new ArrayList<Float>(); // 法向量结果列表,notice:如果obj文件不带有法向量也就是没有vn组（如果有但是f中没有引用该索引，那这个也没用），那这个就没什么用
        // 平均前各个索引对应的点的法向量集合Map
        // 此HashMap的key为点的索引， value为点所在的各个面的法向量的集合
        HashMap<Integer, HashSet<Normal3Dimension>> hmn = new HashMap<Integer, HashSet<Normal3Dimension>>();

        try {
            InputStreamReader isr = new InputStreamReader(objIns);
            BufferedReader br = new BufferedReader(isr);
            String temps = null;
            boolean useObjNormal = true;//是否启用obj文件中的法向量，默认为使用
            boolean judgedUseObjNormal = false;
            // 扫面文件，根据行类型的不同执行不同的处理逻辑
            while ((temps = br.readLine()) != null) {
                // 用空格分割行中的各个组成部分
                String[] tempsa = temps.split("[ ]+");// [ ]+正则匹配多个连在一起的空格
                if (tempsa[0].trim().equals("v")) {// 此行为顶点坐标
                    // 若为顶点坐标行则提取出此顶点的XYZ坐标添加到原始顶点坐标列表中
                    alv.add(Float.parseFloat(tempsa[1]));
                    alv.add(Float.parseFloat(tempsa[2]));
                    alv.add(Float.parseFloat(tempsa[3]));
                } else if (tempsa[0].trim().equals("vt")) {// 此行为纹理坐标行
                    // 若为纹理坐标行则提取ST坐标并添加进原始纹理坐标列表中
                    float value1 = Float.parseFloat(tempsa[1]) / 1.0f;
                    float value2 = Float.parseFloat(tempsa[2]) / 1.0f;
                    alt.add(value1);
                    alt.add(value2);
                } else if (tempsa[0].trim().equals("vn")) {// 此行为法向量
                    aln.add(Float.parseFloat(tempsa[1]));
                    aln.add(Float.parseFloat(tempsa[2]));
                    aln.add(Float.parseFloat(tempsa[3]));
                } else if (tempsa[0].trim().equals("f")) {// 此行为三角形面
                    if (!judgedUseObjNormal && tempsa[1].split("/").length < 3) {//个数小于3，说明没有法向量
                        useObjNormal = false;
                        judgedUseObjNormal = true;//useObjNormal只判断一次
                    }
                    /*
                     *若为三角形面行则根据 组成面的顶点的索引从原始顶点坐标列表中
                     *提取相应的顶点坐标值添加到结果顶点坐标列表中，同时根据三个
                     *顶点的坐标计算出此面的法向量并添加到平均前各个索引对应的点
                     *的法向量集合组成的Map中
                    */
                    int[] index = new int[3];// 三个顶点索引值的数组

                    // 计算第0个顶点的索引，并获取此顶点的XYZ三个坐标
                    index[0] = Integer.parseInt(tempsa[1].split("/")[0]) - 1;
                    float x0 = alv.get(3 * index[0]);
                    float y0 = alv.get(3 * index[0] + 1);
                    float z0 = alv.get(3 * index[0] + 2);
                    alvResult.add(x0);
                    alvResult.add(y0);
                    alvResult.add(z0);

                    // 计算第1个顶点的索引，并获取此顶点的XYZ三个坐标
                    index[1] = Integer.parseInt(tempsa[2].split("/")[0]) - 1;
                    float x1 = alv.get(3 * index[1]);
                    float y1 = alv.get(3 * index[1] + 1);
                    float z1 = alv.get(3 * index[1] + 2);
                    alvResult.add(x1);
                    alvResult.add(y1);
                    alvResult.add(z1);

                    // 计算第2个顶点的索引，并获取此顶点的XYZ三个坐标
                    index[2] = Integer.parseInt(tempsa[3].split("/")[0]) - 1;
                    float x2 = alv.get(3 * index[2]);
                    float y2 = alv.get(3 * index[2] + 1);
                    float z2 = alv.get(3 * index[2] + 2);
                    alvResult.add(x2);
                    alvResult.add(y2);
                    alvResult.add(z2);

                    // 记录此面的顶点索引
                    alFaceIndex.add(index[0]);
                    alFaceIndex.add(index[1]);
                    alFaceIndex.add(index[2]);

                    if (!useObjNormal) {
                        // 通过三角形面两个边向量0-1，0-2求叉积得到此面的法向量
                        // 求0号点到1号点的向量
                        float vxa = x1 - x0;
                        float vya = y1 - y0;
                        float vza = z1 - z0;
                        // 求0号点到2号点的向量
                        float vxb = x2 - x0;
                        float vyb = y2 - y0;
                        float vzb = z2 - z0;
                        // 通过求两个向量的叉积计算法向量

                        float[] crossProduct = Vector3DimentionHelper.getCrossProduct(vxa, vya, vza, vxb, vyb, vzb);
                        float[] vNormal = Vector3DimentionHelper.vectorNormal(crossProduct);
                        for (int tempInxex : index) {// 记录每个索引点的法向量到平均前各个索引对应的点的法向量集合组成的Map中
                            // 获取当前索引对应点的法向量集合
                            HashSet<Normal3Dimension> hsn = hmn.get(tempInxex);
                            if (hsn == null) {// 若集合不存在则创建
                                hsn = new HashSet<Normal3Dimension>();
                            }
                            // 将此点的法向量添加到集合中
                            // 由于Normal类重写了equals方法，因此同样的法向量不会重复出现在此点
                            // 对应的法向量集合中
                            hsn.add(new Normal3Dimension(vNormal[0], vNormal[1], vNormal[2]));
                            // 将集合放进HsahMap中
                            hmn.put(tempInxex, hsn);
                        }
                    } else {
                        Vec3 vn0 = new Vec3();
                        Vec3 vn1 = new Vec3();
                        Vec3 vn2 = new Vec3();
                        int indexNormal = Integer.parseInt(tempsa[1].split("/")[2]) - 1;
                        vn0.x = aln.get(3 * indexNormal);//x
                        vn0.y = aln.get(3 * indexNormal + 1);//y
                        vn0.z = aln.get(3 * indexNormal + 2);//z
                        indexNormal = Integer.parseInt(tempsa[2].split("/")[2]) - 1;
                        vn1.x = aln.get(3 * indexNormal);
                        vn1.y = aln.get(3 * indexNormal + 1);
                        vn1.z = aln.get(3 * indexNormal + 2);
                        indexNormal = Integer.parseInt(tempsa[3].split("/")[2]) - 1;
                        vn2.x = aln.get(3 * indexNormal);
                        vn2.y = aln.get(3 * indexNormal + 1);
                        vn2.z = aln.get(3 * indexNormal + 2);
//                        Vec3 averageNormal = vn0.normalize().add(vn1.normalize()).add(vn2.normalize());//直接求出这个平面三个顶点的法向量的平均向量，不不不，不能直接先单位化，应该先加和再单位化
                        Vec3 averageNormal = vn0.add(vn1).add(vn2).normalize();
                        alnResult.add(averageNormal.x);
                        alnResult.add(averageNormal.y);
                        alnResult.add(averageNormal.z);
                    }

                    // 将纹理坐标组织到结果纹理坐标列表中
                    // 第0个顶点的纹理坐标
                    int indexTex = Integer.parseInt(tempsa[1].split("/")[1]) - 1;
                    altResult.add(alt.get(indexTex * 2));
                    altResult.add(alt.get(indexTex * 2 + 1));
                    // 第1个顶点的纹理坐标
                    indexTex = Integer.parseInt(tempsa[2].split("/")[1]) - 1;
                    altResult.add(alt.get(indexTex * 2));
                    altResult.add(alt.get(indexTex * 2 + 1));
                    // 第2个顶点的纹理坐标
                    indexTex = Integer.parseInt(tempsa[3].split("/")[1]) - 1;
                    altResult.add(alt.get(indexTex * 2));
                    altResult.add(alt.get(indexTex * 2 + 1));
                }
            } // end of while

            // 生成顶点数组
            int size = alvResult.size();
            float[] vXYZ = new float[size];
            for (int i = 0; i < size; i++) {
                vXYZ[i] = alvResult.get(i);
            }

            // 生成法向量数组
            float[] nXYZ = new float[alFaceIndex.size() * 3];
            int c = 0;
            if (!useObjNormal) {
                for (Integer i : alFaceIndex) {
                    // 根据当前点的索引从Map中取出一个法向量的集合
                    HashSet<Normal3Dimension> hsn = hmn.get(i);
                    // 求出平均法向量
                    float[] tn = Normal3Dimension.getAverage(hsn);
                    // 将计算出的平均法向量存放到法向量数组中
                    nXYZ[c++] = tn[0];
                    nXYZ[c++] = tn[1];
                    nXYZ[c++] = tn[2];
                }
            } else {
                size = alnResult.size();
                if (alFaceIndex.size() != size) {
                    throw new RuntimeException(TAG + " altResult.size()!=alFaceIndex.size()");
                }
                for (int i = 0; i < size - 3; i += 3) {
                    for (int j = 0; j < 3; j++) {
                        nXYZ[c++] = alnResult.get(i + 0);
                        nXYZ[c++] = alnResult.get(i + 1);
                        nXYZ[c++] = alnResult.get(i + 2);
                    }
                }
            }
            // 生成纹理数组
            size = altResult.size();
            float[] tST = new float[size];
            for (int i = 0; i < size; i++) {
                tST[i] = altResult.get(i);
            }
            mVertexXYZ = vXYZ;
            mNormalVectorXYZ = nXYZ;
            mTextureVertexST = tST;
            if (mOnLoadListener != null) {
                mOnLoadListener.onLoadOK(this);
            }
        } catch (Exception e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " load error e=" + e.getMessage());
            if (mOnLoadListener != null) {
                mOnLoadListener.onLoadFailed(e.getMessage());
            }
            e.printStackTrace();
        }
    }

    public float[] getVertexXYZ() {
        return mVertexXYZ;
    }

    public float[] getNormalVectorXYZ() {
        return mNormalVectorXYZ;
    }

    public float[] getTextureVertexST() {
        return mTextureVertexST;
    }

    private OnLoadListener mOnLoadListener;

    public interface OnLoadListener {
        void onLoadOK(ObjLoadAider aider);

        void onLoadFailed(String failedMsg);
    }
}
