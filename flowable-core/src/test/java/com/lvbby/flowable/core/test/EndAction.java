package com.lvbby.flowable.core.test;

import com.lvbby.flowable.core.IFlowAction;
import com.lvbby.flowable.core.FlowContext;
import com.lvbby.flowable.core.anno.FlowAction;
import com.lvbby.flowable.core.anno.WithExtensionProperty;

/**
 *
 * @author dushang.lp
 * @version $Id: InitAction.java, v 0.1 2020年03月06日 21:37 dushang.lp Exp $
 */
@FlowAction(id = "EndAction")
public class EndAction implements IFlowAction {
    @Override
    public void invoke(FlowContext context) {
        System.out.println("end");
    }



    @WithExtensionProperty("EndAction.title")
    public String getTitle(){
        return "defaultValue";
    }
}