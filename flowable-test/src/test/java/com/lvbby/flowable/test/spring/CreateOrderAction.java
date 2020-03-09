package com.lvbby.flowable.test.spring;

import com.lvbby.flowable.core.anno.WithExtensionProperty;
import com.lvbby.flowable.core.utils.FlowableHelper;
import com.lvbby.flowable.test.AbstractOrderAction;
import com.lvbby.flowable.test.CreateOrderActionExtension;
import com.lvbby.flowable.test.OrderContext;
import com.lvbby.flowable.test.OrderDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 *
 * @author dushang.lp
 * @version $Id: InitAction.java, v 0.1 2020年03月06日 21:37 dushang.lp Exp $
 */
@Component
public class CreateOrderAction implements AbstractOrderAction {

    public static final String PROP = "flow.property";
    @Override
    public void invoke(OrderContext context) {

        CreateOrderActionExtension extension = FlowableHelper.getExtension(context, CreateOrderActionExtension.class);
        String title = Optional.ofNullable(extension).map(CreateOrderActionExtension::getTitle).orElse("default-title");
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTitle(title);
        orderDTO.setStatus("init");
        context.putValue("order", orderDTO);
        System.out.println("init " + title);

        System.out.println("========props=====:" + FlowableHelper.getProp(PROP));
    }

    @WithExtensionProperty("sdfssdfasdf")
    public String extValue() {
        return (String) FlowableHelper.getProp("exs");
    }

}