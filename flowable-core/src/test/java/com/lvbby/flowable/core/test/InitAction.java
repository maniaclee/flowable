package com.lvbby.flowable.core.test;

import com.lvbby.flowable.core.IFlowAction;
import com.lvbby.flowable.core.FlowContext;
import com.lvbby.flowable.core.anno.FlowAction;
import com.lvbby.flowable.core.utils.FlowableHelper;

/**
 *
 * @author dushang.lp
 * @version $Id: InitAction.java, v 0.1 2020年03月06日 21:37 dushang.lp Exp $
 */
@FlowAction(value = {InitActionExtension.class},id = "initAction")
public class InitAction implements IFlowAction {
    @Override
    public void invoke(FlowContext context) {
        InitActionExtension extension = FlowableHelper.getExtension(context, InitActionExtension.class);
        System.out.println("init "+extension.getTitle());
    }

}