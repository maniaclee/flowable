
package com.lvbby.flowable.core;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowEngine.java, v 0.1 2020年03月06日 下午5:16 dushang.lp Exp $
 */
public class Flow {

    public static <IContext extends FlowContext> void execSimple(IContext context, IFlowAction... actions) {
        FlowNode flowNode = FlowNode.ofArray(actions);
        context.setConfig(FlowScript.anonymous(flowNode));
        exec(context);
    }

    public static <IContext extends FlowContext> void exec(IContext context) {
        /** 初始化上下文 */
        if (context.getConfig() == null) {
            context.setConfig(FlowContainer.getFlowConfig(context.getCode()));
        }
        FlowContext.buildCurrentContext(context);
        try {
            _doExec(context, context.getConfig().getPipeline());
        } finally {
            FlowContext.cleanContext();
        }
    }

    private static <IContext extends FlowContext> void _doExec(IContext context, FlowNode node) {

        /** 标记当前的节点 */
        context.put(FlowFrameWorkKeys.currentNode, node);
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
                    _doExec(context, child);
                }
            }
        }
    }


}