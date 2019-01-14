package me.szlxty.check.bundle;

import me.szlxty.check.Constraint;
import me.szlxty.check.container.ConstraintContainer;

import java.util.List;

/**
 * 约束簇。
 */
public interface ConstraintBundle {
    /**
     * 配置到指定的约束管理系统。
     *
     * @param constraintContainer 待配置的约束管理容器。如果为{@code null}，则表示全局约束管理容器，即{@code Rule.getSystem()}的返回值。
     */
    void bindTo(ConstraintContainer constraintContainer);

    /**
     * 获取约束簇中的全部约束。
     *
     * @return 约束簇中的全部约束。
     */
    List<Constraint> getConstraints();
}
