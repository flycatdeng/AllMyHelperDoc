package com.dandy.module;

/**
 * <pre>
 * 这个接口的设计是用于表明某一个功能模块是否可用
 * This interface is designed to indicate if a function module is can be used or not
 * </pre>
 * 
 * @author dengchukun 2016年11月23日
 */
public interface IModuleInterface {
    /**
     * <pre>
     * 返回true则表明这个功能模块可用，否则表明这个模块不可用，一般不可用的模块中的细节可以使用空实现
     * The module can be used if the method returns a true value,
     * otherwise the module is useless so we do not need to realize its function
     * </pre>
     * 
     * @return true:represent the module can be used
     */
    boolean isModuleTurnedOn();
}
