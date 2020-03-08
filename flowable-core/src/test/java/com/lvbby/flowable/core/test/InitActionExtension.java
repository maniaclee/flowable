
package com.lvbby.flowable.core.test;

import com.lvbby.flowable.core.IFlowActionExtension;
import com.lvbby.flowable.core.anno.FlowExt;

/**
 *
 * @author dushang.lp
 * @version $Id: InitActionExtension.java, v 0.1 2020年03月06日 22:12 dushang.lp Exp $
 */

@FlowExt("InitActionExtension")
public interface InitActionExtension extends IFlowActionExtension {

    String getTitle();

}