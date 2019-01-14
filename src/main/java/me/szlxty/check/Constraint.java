package me.szlxty.check;

import me.szlxty.check.system.ConstraintSystem;

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
    Constraint TODO = ConstraintFactory.create("-200", "正在实现，敬请等待...");
    Constraint RUNTIME = ConstraintFactory.create("-201", "强制为运行时异常...");

    static Constraint[] buildins() {
        return new Constraint[]{UNCODED, TODO, RUNTIME};
    }

    default Checker checker() {
        return Checkers.of(this);
    }

    default Checker checker(String brief) {
        return Checkers.of(ConstraintFactory.create(this.code(), brief));
    }

    static Constraint of(String constraintCodeOrBrief) {
        Constraint managedConstraint = ConstraintSystem.get().getConstraintContainer().find(constraintCodeOrBrief);
        return managedConstraint != null ? managedConstraint : ConstraintFactory.create(Constraint.UNCODED.code(), constraintCodeOrBrief);
    }
}
