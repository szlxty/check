package me.szlx.check.constraint;

import me.szlx.check.constraint.system.ConstraintSystem;

import java.io.Serializable;

public interface Constraint extends Serializable {
    /**
     * @return 错误代码。
     */
    String code();

    /**
     * @return 错误概要描述。
     */
    String brief();

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
