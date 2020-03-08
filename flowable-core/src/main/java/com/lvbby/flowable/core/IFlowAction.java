
package com.lvbby.flowable.core;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowAction.java, v 0.1 2020年03月06日 下午5:14 dushang.lp Exp $
 */
public interface IFlowAction<IContext extends FlowContext> {
    void invoke(IContext context);

    default String actionName() {
        return null;
    }

    default Class<? extends IFlowActionExtension>[] extensions() {
        return null;
    }

}