package me.szlx.check.constraint.container;

import me.szlx.check.constraint.Constraint;
import me.szlx.check.constraint.bundle.ConstraintBundle;

/**
 * 约束容器。
 */
public interface ConstraintContainer {
    /**
     * 安装约束信息。
     *
     * @param constraintBundle 约束簇。
     */
    void install(ConstraintBundle constraintBundle);

    /**
     * 安装约束信息。
     *
     * @param constraints 约束信息。
     */
    void install(Constraint... constraints);

    /**
     * 查找约束信息。
     *
     * @param constraintCodeOrBrief 待查找的约束代码。
     */
    Constraint find(String constraintCodeOrBrief);
}
