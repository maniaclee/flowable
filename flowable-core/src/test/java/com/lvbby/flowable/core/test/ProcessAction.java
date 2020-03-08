
package com.lvbby.flowable.core.test;

import com.lvbby.flowable.core.IFlowAction;
import com.lvbby.flowable.core.FlowContext;
import com.lvbby.flowable.core.anno.FlowAction;

/**
 *
 * @author dushang.lp
 * @version $Id: ProcessAction.java, v 0.1 2020年03月06日 21:44 dushang.lp Exp $
 */
@FlowAction(id = "ProcessAction")
public class ProcessAction implements IFlowAction {
    @Override
    public void invoke(FlowContext context) {
        System.out.println("process action:"+context);
    }
}