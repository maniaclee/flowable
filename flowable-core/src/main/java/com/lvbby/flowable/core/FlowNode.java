
package com.lvbby.flowable.core;

import com.google.common.collect.Lists;
import com.lvbby.flowable.core.utils.FlowableHelper;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowNode.java, v 0.1 2020年03月06日 下午5:11 dushang.lp Exp $
 */
public class FlowNode {
    private String                 actionId;
    private Predicate<FlowContext> condition;
    private List<FlowNode>         children;
    /***
     * 多个action复用时通过alias区分
     */
    private String                 alias;

    public static FlowNode node(IFlowAction action) {
        return ofAlias(action, null);
    }

    public static FlowNode ofAlias(IFlowAction action, String alias) {
        String flowActionName = FlowableHelper.getFlowActionName(action);
        FlowNode re = new FlowNode();
        re.setActionId(flowActionName);
        re.setAlias(alias);
        //注册Action,本地模式下action不会自动注册，这里插入
        FlowContainer.registerFlowAction(action);
        return re;
    }

    public static FlowNode ofArray(IFlowAction... actions) {
        FlowableHelper.isTrue(actions != null && actions.length > 0, "action can't be empty");
        return ofList(Lists.newArrayList(actions));
    }

    public static FlowNode ofList(List<IFlowAction> actions) {
        FlowableHelper.isTrue(CollectionUtils.isNotEmpty(actions), "action can't be empty");
        FlowNode re = node(actions.get(0));
        FlowNode cur = re;
        for (int i = 1; i < actions.size(); i++) {
            FlowNode node = node(actions.get(i));
            cur.next(node);
            cur = node;
        }
        return re;
    }

    /***
     * build
     * @param nodes
     * @return
     */
    public FlowNode next(FlowNode... nodes) {
        if (children == null) {
            children = Lists.newLinkedList();
        }
        for (FlowNode node : nodes) {
            children.add(node);
        }
        return this;
    }

    /***
     * 快速
     * @param action
     * @return
     */
    public FlowNode next(IFlowAction action) {
        return next(node(action));
    }

    public FlowNode when(Predicate<FlowContext> condition) {
        setCondition(condition);
        return this;
    }

    public FlowNode alias(String alias) {
        setAlias(alias);
        return this;
    }

    /**
     * Getter method for property   actionId.
     *
     * @return property value of actionId
     */
    public String getActionId() {
        return actionId;
    }

    /**
     * Setter method for property   actionId .
     *
     * @param actionId  value to be assigned to property actionId
     */
    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    /**
     * Getter method for property   condition.
     *
     * @return property value of condition
     */
    public Predicate<FlowContext> getCondition() {
        return condition;
    }

    /**
     * Setter method for property   condition .
     *
     * @param condition  value to be assigned to property condition
     */
    public void setCondition(Predicate<FlowContext> condition) {
        this.condition = condition;
    }

    /**
     * Getter method for property   children.
     *
     * @return property value of children
     */
    public List<FlowNode> getChildren() {
        return children;
    }

    /**
     * Setter method for property   children .
     *
     * @param children  value to be assigned to property children
     */
    public void setChildren(List<FlowNode> children) {
        this.children = children;
    }

    /**
     * Getter method for property   alias.
     *
     * @return property value of alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Setter method for property   alias .
     *
     * @param alias  value to be assigned to property alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }
}