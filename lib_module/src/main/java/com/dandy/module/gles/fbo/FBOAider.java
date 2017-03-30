package com.dandy.module.gles.fbo;

import android.content.Context;
import android.opengl.GLES20;

import com.dandy.gles.engine.Texture;

/**
 * <pre>
 *  FBO帮助类
 *  因为需要初始化FBO的宽和高，所以需要传入宽高进行初始化，最好是在onSurfaceChanged调用时初始化
 *  {@link #getFBOTextureId()}提供FBO的TextureID，这个是已经绘制好了的，可以提供给外部使用
 *  {@link #drawSelf(Texture)}为FBO绘制，具体的绘制有Texture提供
 * </pre>
 * Created by flycatdeng on 2017/3/30.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */
public class FBOAider {
    private int fboId = -1;
    private int rboId = -1;
    private int fboTextureId = -1;
    private int fboWidth = -1;
    private int fboHeight = -1;

    public void init(Context context, int fboWidth, int fboHeight) {
        this.fboWidth = fboWidth;
        this.fboHeight = fboHeight;

        int[] temp = new int[1];
        GLES20.glGenFramebuffers(1, temp, 0);//产生FBO ID generate fbo id
        fboId = temp[0];
        GLES20.glGenTextures(1, temp, 0);//产生纹理ID generate texture
        fboTextureId = temp[0];
        GLES20.glGenRenderbuffers(1, temp, 0);//产生RBO ID generate render buffer
        rboId = temp[0];
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);//绑定FBO Bind Frame buffer
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fboTextureId);//绑定纹理 Bind texture
        //定义纹理参数 Define texture parameters
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, fboWidth, fboHeight, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);

        //加上纹理FBO Color附属物 Attach texture FBO color attachment
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, fboTextureId, 0);
        //加上RBO depth附属物 Attach render buffer to depth attachment
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, rboId);
        //绑定好了就释放 we are done, reset
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public int getFBOTextureId() {
        return fboTextureId;
    }

    public void drawSelf(Texture texture) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);
        GLES20.glViewport(0, 0, fboWidth, fboHeight);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);//神了，这句话还不能放在上两句之前
        /******Rendering Code*******/
        texture.drawSelf();
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }
}
