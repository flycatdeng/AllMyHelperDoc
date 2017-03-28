package com.dandy.module.cleanaccelerate;

public interface MemoryActionListener {

    /**
     * <pre>
     * 清理到哪里来了
     * </pre>
     * 
     * @param current
     *            当前第几个进程了
     * @param max
     *            一共多少个进程
     * @param processName
     *            processName or packageName
     */
    public void onCleanProgressUpdated(int current, int max, String processName);

    /**
     * <pre>
     * 清理完成
     * </pre>
     * 
     * @param cachsize
     *            清理了多少内存
     * @param totalsize
     *            清理之前一共占多少内存
     * @param costTime
     *            清理用了多长时间
     */
    public void onCleanCompleted(long cachsize, long totalsize, long costTime);

    /**
     * <pre>
     * 出错了
     * </pre>
     * 
     * @param msg
     *            错误信息
     */
    public void onCanClean(boolean canable, String msg);
}
