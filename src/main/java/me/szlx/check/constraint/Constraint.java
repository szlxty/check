package me.szlx.check.constraint;

import me.szlx.check.checker.Checker;
import me.szlx.check.constraint.system.ConstraintSystem;

import java.io.Serializable;
import java.util.function.Predicate;

public interface Constraint extends Serializable {
    /**
     * @return 错误代码。
     */
    String code();

    /**
     * @return 错误概要描述。
     */
    String brief();

    /**
     * 创建检测器。
     *
     * @param predicate 检测断言。
     * @param <T>       待检测的对象类型。
     * @return 新的检测器。
     */
    default <T> Checker<T> checkerOf(Predicate<T> predicate) {
        return Checker.create(this, predicate);
    }

    Constraint UNCODED = ConstraintFactory.create("-10", "约束尚未编码");
    Constraint RUNTIME = ConstraintFactory.create("-20", "强制为运行时异常...");

    static Constraint[] buildins() {
        return new Constraint[]{UNCODED, RUNTIME};
    }

    static Constraint of(String constraintCodeOrBrief) {
        Constraint managedConstraint = ConstraintSystem.get().getConstraintContainer().find(constraintCodeOrBrief);
        return managedConstraint != null ? managedConstraint : ConstraintFactory.create(Constraint.UNCODED.code(), constraintCodeOrBrief);
    }
}
