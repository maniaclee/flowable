package com.lvbby.flowable.spring.test.action;

import com.lvbby.flowable.core.anno.FlowAction;
import com.lvbby.flowable.core.utils.FlowableHelper;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 *
 * @author dushang.lp
 * @version $Id: InitAction.java, v 0.1 2020年03月06日 21:37 dushang.lp Exp $
 */
@Component
@FlowAction(value = {CreateOrderActionExtension.class})
public class CreateOrderAction implements AbstractOrderAction {
    @Override
    public void invoke(OrderContext context) {

        CreateOrderActionExtension extension = FlowableHelper.getExtension(context, CreateOrderActionExtension.class);
        String title = Optional.ofNullable(extension).map(CreateOrderActionExtension::getTitle).orElse("default-title");
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTitle(title);
        orderDTO.setStatus("init");
        context.putValue("order", orderDTO);
        System.out.println("init " + title);
    }

}