
package com.lvbby.flowable.spring.test;

import com.alibaba.fastjson.JSON;
import com.lvbby.flowable.core.Flow;
import com.lvbby.flowable.core.FlowNode;
import com.lvbby.flowable.core.FlowScript;
import com.lvbby.flowable.core.IFlowAction;
import com.lvbby.flowable.core.utils.FlowableHelper;
import com.lvbby.flowable.spring.anno.EnableFlow;
import com.lvbby.flowable.spring.test.FlowableSpringExample.ConfigurationTest;
import com.lvbby.flowable.spring.test.action.CreateOrderAction;
import com.lvbby.flowable.spring.test.action.CreateOrderActionExtension;
import com.lvbby.flowable.spring.test.action.OrderContext;
import com.lvbby.flowable.spring.test.action.OrderDTO;
import com.lvbby.flowable.spring.test.action.OrderProcessAction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * flow流程引擎示例
 * @author dushang.lp
 * @version $Id: test.java, v 0.1 2020年03月07日 15:23 dushang.lp Exp $
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigurationTest.class)
public class FlowableSpringExample {

    @Resource
    IFlowAction createOrderActionInner;
    @Resource
    IFlowAction orderProcessActionInner;

    @Autowired
    private CreateOrderAction createOrderAction;
    @Autowired
    OrderProcessAction orderProcessAction;

    /***
     * 配置
     */
    @ComponentScan(basePackages = "com.lvbby")
    @Configuration
    @EnableFlow
    public static class ConfigurationTest {
        @Bean
        public IFlowAction createOrderActionInner() {
            return context -> {
                CreateOrderActionExtension extension = FlowableHelper.getExtension(context, CreateOrderActionExtension.class);
                String title = Optional.ofNullable(extension).map(CreateOrderActionExtension::getTitle).orElse("default-title");
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setTitle(title);
                orderDTO.setStatus("init");
                context.putValue("order", orderDTO);
                System.out.println("init " + title);
            };
        }

        @Bean
        public IFlowAction orderProcessActionInner() {
            return context -> {
                OrderDTO order = context.getValue("order");
                if (order == null || !order.getStatus().equals("init")) {
                    throw new RuntimeException("order is null or status invalid");
                }
                order.setStatus("finished");
                System.out.println("finish order: " + JSON.toJSONString(order, true));
            };
        }
    }

    /***
     * 最简单的pipeline
     */
    @Test
    public void simplePipeline() {
        Flow.execSimple(new OrderContext(), createOrderAction, orderProcessAction);
    }

    @Test
    public void pipelineWithScene() {
        /** 1. 创建流程 */
        FlowNode script = FlowNode.ofArray(createOrderAction, orderProcessAction);

        /** 2. 创建场景，实现业务扩展逻辑，并注册到框架里*/
        Flow.addFlowConfig(
                FlowScript.of("pipelineSingle", script)
                        .add(CreateOrderActionExtension.class, () -> "pipelineATitle")
        );

        /** 3. 按照场景进行调用 */
        Flow.exec(new OrderContext("pipelineSingle"));
    }

    @Test
    public void pipelineMultiScene() {
        FlowNode script = FlowNode.ofArray(createOrderAction, orderProcessAction);

        /** 创建两个场景，具有不同的业务逻辑实现*/
        Flow.addFlowConfig(
                FlowScript.of("pipelineA", script)
                        .add(CreateOrderActionExtension.class, () -> "pipelineATitle")
        );
        Flow.addFlowConfig(
                FlowScript.of("pipelineB", script)
                        .add(CreateOrderActionExtension.class, () -> "pipelineBTitle")
        );

        /** 实际调用两个场景 */
        Flow.exec(new OrderContext("pipelineA"));
        Flow.exec(new OrderContext("pipelineB"));
    }

    @Test
    public void innerBeanTest() {

        /** 1. 定义流程编排*/
        FlowNode script =
                FlowNode.node(createOrderActionInner)
                        .next(FlowNode.node(orderProcessActionInner).when(ctx -> ctx.hasValue("order"))
                        );

        /** 2. 定义场景：流程+扩展点 */
        Flow.instance
                .addFlowConfig("example", script);
        Flow.instance
                .addFlowConfig("error", FlowNode.node(orderProcessActionInner), null);

        /** 启动流程 */
        Flow.exec(new OrderContext("example"));
        System.out.println("=================");
        try {
            Flow.exec(new OrderContext("error"));
        }catch (Exception e){
            System.out.println("expected error");
        }
    }

    @Test
    public void normalBeanTest() {

        /** 1. 定义流程编排*/
        FlowNode script =
                FlowNode.node(createOrderAction)
                        .next(FlowNode.node(orderProcessAction).when(ctx -> ctx.hasValue("order"))
                        );

        /** 2. 定义场景：流程+扩展点 */

        Flow.instance
                .addFlowConfig(FlowScript.of("exampleBean", script).add(new CreateOrderActionExtension() {
                    @Override
                    public String getTitle() {
                        return "springOrder";
                    }
                }));

        /** 启动流程 */
        Flow.exec(new OrderContext("exampleBean"));
    }
}