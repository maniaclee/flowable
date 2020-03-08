
package com.lvbby.flowable.core;

import com.lvbby.flowable.core.utils.FlowableHelper;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowEngine.java, v 0.1 2020年03月06日 下午5:16 dushang.lp Exp $
 */
public class Flow {

    public static Flow instance = new Flow();

    public static <IContext extends FlowContext> void execSimple(IContext context, IFlowAction... actions) {
        FlowNode flowNode = FlowNode.ofArray(actions);
        doExec(context, flowNode);
    }

    public static <IContext extends FlowContext> void exec(IContext context) {
        String code = context.getCode();
        FlowScript flowConfig = FlowContainer.getFlowConfig(code);
        FlowableHelper.isTrue(flowConfig != null, "flow process is not found for code:" + code);
        doExec(context, flowConfig.getFlowScript());
    }

    /***
     * 触发流程
     * @param context
     * @param node
     * @param <IContext>
     */
    private static  <IContext extends FlowContext> void doExec(IContext context, FlowNode node) {
        if (node.getCondition() == null || node.getCondition().test(context)) {
            String actionId = node.getActionId();
            IFlowAction flowAction = FlowContainer.getFlowAction(actionId);
            if (flowAction == null) {
                throw new RuntimeException("flowAction unknown: " + actionId);
            }
            flowAction.invoke(context);
            List<FlowNode> children = node.getChildren();
            /** invoke children */
            if (CollectionUtils.isNotEmpty(children)) {
                for (FlowNode child : children) {
                    doExec(context, child);
                }
            }
        }
    }

    /***
     * facade代理
     * @param config
     * @return
     */
    public static  void  addFlowConfig(FlowScript config) {
        FlowContainer.addFlowConfig(config);
    }

    public static  void  addFlowConfig(String code, FlowNode node, IFlowActionExtension... iFlowActionExtensions) {
        FlowScript flowConfig = new FlowScript();
        flowConfig.setFlowScript(node);
        flowConfig.setCode(code);
        if (iFlowActionExtensions!=null && iFlowActionExtensions.length>0) {
            for (IFlowActionExtension iFlowActionExtension : iFlowActionExtensions) {
                flowConfig.add(iFlowActionExtension);
            }
        }
        FlowContainer.addFlowConfig(flowConfig);
    }

    public static  void  addFlowConfig(FlowSceneDefinition def) {
        FlowScript flowConfig = new FlowScript();
        flowConfig.setCode(def.scene());
        flowConfig.setFlowScript(def.script());
        List<IFlowActionExtension> extensions = def.extensions();
        if (CollectionUtils.isNotEmpty(extensions)) {
            extensions.forEach(e -> flowConfig.add(e));
        }
        FlowContainer.addFlowConfig(flowConfig);
    }

}