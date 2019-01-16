package me.szlx.check.constraint.container;

import me.szlx.check.constraint.Constraint;
import me.szlx.check.constraint.Ordered;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>对Error可进行优先级别管理的错误容器。优先级高的错误描述将替换优先级低的错误描述。</p>
 * <p>一般的，错误可以通过实现接口{@code me.szlx.check.constraint.Ordered}表明自身的优先级，序号越大，则优先级越高。</p>
 * <p>没有实现接口{@code me.szlx.check.constraint}时，默认序号为0。</p>
 * <p>表明优先级别的序号相同时，则后安装的优先。</p></p>
 */
public class PriorityContainer extends AbstractContainer {
    private Logger logger = LoggerFactory.getLogger(PriorityContainer.class);

    protected void doInstall(Constraint constraint) {
        if (constraint != null) {
            Constraint ruleExisted = getErrorMap().get(constraint.code());
            if (ruleExisted == null) {
                getErrorMap().put(constraint.code(), constraint);
                logger.debug("装载约束：code={}，brief={}", constraint.code(), constraint.brief());
            } else if (Ordered.DESC.compare(constraint, ruleExisted) >= 0) {
                getErrorMap().put(constraint.code(), constraint);
                logger.debug("替换约束：code={}，brief={} >>> {}", constraint.code(), ruleExisted.brief(), constraint.brief());
            }
        }
    }
}