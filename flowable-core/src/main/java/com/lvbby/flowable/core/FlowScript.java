
package com.lvbby.flowable.core;

import com.google.common.collect.Maps;
import com.lvbby.flowable.core.utils.FlowableHelper;

import java.util.Map;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowConfig.java, v 0.1 2020年03月06日 下午5:10 dushang.lp Exp $
 */
public class FlowScript {
    /** 流程id */
    private String   code;
    /** 流程 */
    private FlowNode flowScript;

    /***
     * 扩展点
     */
    private Map<String, IFlowActionExtension> extensions = Maps.newHashMap();

    public <Ext extends IFlowActionExtension> Ext getExtension(Class<Ext> extClass) {
        return (Ext) extensions.get(FlowableHelper.getFlowExtName(extClass));
    }



    public static FlowScript of(String code) {
        return of(code, null);
    }
    public static FlowScript of(String code, FlowNode flowScript) {
        FlowScript re = new FlowScript();
        re.setCode(code);
        re.setFlowScript(flowScript);
        return re;
    }

    public FlowScript flowScript(FlowNode flowScript){
        setFlowScript(flowScript);
        return this;
    }

    public FlowScript add(IFlowActionExtension extension) {
        extensions.put(FlowableHelper.getFlowExtName(extension), extension);
        return this;
    }
    public <T extends IFlowActionExtension> FlowScript add(Class<T> clz, T extension) {
        extensions.put(FlowableHelper.getFlowExtName(extension), extension);
        return this;
    }

    /**
     * Getter method for property   code.
     *
     * @return property value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter method for property   code .
     *
     * @param code  value to be assigned to property code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Getter method for property   flowScript.
     *
     * @return property value of flowScript
     */
    public FlowNode getFlowScript() {
        return flowScript;
    }

    /**
     * Setter method for property   flowScript .
     *
     * @param flowScript  value to be assigned to property flowScript
     */
    public void setFlowScript(FlowNode flowScript) {
        this.flowScript = flowScript;
    }

}