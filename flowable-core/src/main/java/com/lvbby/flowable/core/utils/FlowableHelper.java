
package com.lvbby.flowable.core.utils;

import com.lvbby.flowable.core.FlowScript;
import com.lvbby.flowable.core.FlowContainer;
import com.lvbby.flowable.core.FlowContext;
import com.lvbby.flowable.core.IFlowAction;
import com.lvbby.flowable.core.IFlowActionExtension;
import com.lvbby.flowable.core.anno.FlowAction;
import com.lvbby.flowable.core.anno.FlowExt;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowableHelper.java, v 0.1 2020年03月06日 22:09 dushang.lp Exp $
 */
public class FlowableHelper {

    public static <Ext extends IFlowActionExtension> Ext getExtension(FlowContext context, Class<Ext> clz) {
        FlowScript flowConfig = FlowContainer.getFlowConfig(context.getCode());
        if(flowConfig==null){
            return null;
        }
        return flowConfig.getExtension(clz);
    }

    public static String getFlowActionName(IFlowAction action) {
        String actionName = action.actionName();
        if (StringUtils.isBlank(actionName)) {
            FlowAction annotation = getAnnotation(action.getClass(), FlowAction.class);
            if (annotation != null) {
                actionName = annotation.id();
            }
        }
        return actionName;
    }

    public static String getFlowExtName(Class clz) {
        FlowExt annotation = getAnnotation(clz, FlowExt.class);
        return annotation == null ? null : annotation.value();
    }

    public static String getFlowExtName(IFlowActionExtension extension) {
        FlowExt annotation = getAnnotation(extension.getClass(), FlowExt.class);
        return annotation == null ? null : annotation.value();
    }

    public static void isTrue(boolean expr, String msg) {
        if (!expr) {
            throw new RuntimeException(msg);
        }
    }

    public static <T> T newInstance(Class<T> clz) {
        try {
            return clz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("failed to instance %s", clz.getName()), e);
        }
    }

    public static <A extends Annotation> A getAnnotation(Class clz, Class<A> annotation) {
        A re = (A) clz.getAnnotation(annotation);
        if (re != null) { return re; }
        re = ClassUtils.getAllInterfaces(clz).stream().filter(aClass -> aClass.isAnnotationPresent(annotation)).findAny()
                .map(aClass -> aClass.getAnnotation(annotation)).orElse(null);
        if (re == null) {
            re = ClassUtils.getAllSuperclasses(clz).stream().filter(aClass -> aClass.isAnnotationPresent(annotation)).findAny()
                    .map(aClass -> aClass.getAnnotation(annotation)).orElse(null);
        }
        return re;
    }

}