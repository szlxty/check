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

    /**
     * 在约束系统中查找以参数为<code>code</code>的约束，如果找到，则返回找到的约束，否则创建一个临时约束。
     * 临时约束以{@link #UNCODED}的<code>code</code>为<code>code</code>，参数指定值为<code>brief</code>。<p>
     * 临时约束不会自动添加到约束系统。</p>
     *
     * @param codeOrBrief 约束代码或描述。
     * @return 约束系统中code匹配参数的约束，或新建的临时约束。
     */
    static Constraint getOrCreate(String codeOrBrief) {
        Constraint managedConstraint = ConstraintSystem.get().getConstraintContainer().find(codeOrBrief);
        return managedConstraint != null ? managedConstraint : ConstraintFactory.create(Constraint.UNCODED.code(), codeOrBrief);
    }
}
