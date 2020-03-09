
package com.lvbby.flowable.core;

import com.lvbby.flowable.core.anno.FlowAction;
import com.lvbby.flowable.core.utils.FlowableHelper;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowAction.java, v 0.1 2020年03月06日 下午5:14 dushang.lp Exp $
 */
public interface IFlowAction<IContext extends FlowContext> {
    void invoke(IContext context);

    default String actionName() {
        FlowAction annotation = FlowableHelper.getAnnotation(getClass(), FlowAction.class);
        return annotation == null ? null : annotation.id();
    }

}