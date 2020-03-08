
package com.lvbby.flowable.core;

import java.util.List;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowSceneDefinition.java, v 0.1 2020年03月07日 12:05 dushang.lp Exp $
 */
public interface FlowSceneDefinition {

    String scene();

    FlowNode  script();

    List<IFlowActionExtension> extensions();

}