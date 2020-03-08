
package com.lvbby.flowable.core.test;

import com.lvbby.flowable.core.Flow;
import com.lvbby.flowable.core.FlowScript;
import com.lvbby.flowable.core.FlowContext;
import com.lvbby.flowable.core.FlowNode;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static com.lvbby.flowable.core.FlowNode.node;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowableExample.java, v 0.1 2020年03月06日 21:33 dushang.lp Exp $
 */
public class FlowableExample {
    InitAction    initAction    = new InitAction();
    ProcessAction processAction = new ProcessAction();
    EndAction     endAction     = new EndAction();

    @Test
    public void name() {

        /** 1. 定义流程编排*/
        FlowNode script = node(initAction)
                .next(node(processAction)
                                .when(ctx -> StringUtils.equals(ctx.getValueString("props"), "test")),
                        node(initAction)
                                .when(ctx -> StringUtils.isBlank(ctx.getValueString("props")))
                )
                .next(endAction);

        /** 2. 定义场景：流程+扩展点 */
        Flow.addFlowConfig(FlowScript.of("example", script).add(InitActionExtension.class, () -> "example title"));
        Flow.addFlowConfig(FlowScript.of("test", script).add((InitActionExtension) () -> "test title"));
        Flow.addFlowConfig(FlowScript.of("pipeline")
                        .flowScript(node(initAction).next())
                        .add((InitActionExtension) () -> "test title"))
        ;

        /** 启动流程 */
        Flow.exec(new FlowContext("example"));
        System.out.println("=================");
        Flow.exec(new FlowContext("test").putValue("props", "test"));
    }
}