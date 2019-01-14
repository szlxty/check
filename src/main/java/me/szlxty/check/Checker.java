package me.szlxty.check;

import me.szlxty.check.namespace.NameSpace;
import me.szlxty.check.system.ConstraintSystem;

import java.util.Objects;

/**
 * 有条件的出错约束。一般地，当条件成立时，则出错。
 */
public interface Checker {
    /**
     * 获取校验的约束。
     *
     * @return 校验的约束。
     */
    Constraint getConstraint();

    /**
     * 立即抛出当前约束的异常。
     */
    default void invalid() {
        ConstraintSystem.get().getInvalidationHandler().handle(getConstraint());
    }

    /**
     * 立即抛出当前约束的异常。
     *
     * @param nameSpace 使用约束时所处的名称空间。
     */
    default void invalid(NameSpace nameSpace) {
        String constraintCode = nameSpace.decorate(getConstraint().code());
        Constraint constraint = nameSpace != null ? Constraint.of(constraintCode) : getConstraint();
        ConstraintSystem.get().getInvalidationHandler().handle(constraint);
    }

    /**
     * 立即抛出当前约束的异常。
     *
     * @param cause 导致约束触发异常的原因。
     */
    default void invalid(Throwable cause) {
        ConstraintSystem.get().getInvalidationHandler().handle(getConstraint(), cause);
    }

    /**
     * 根据默认触发条件抛出当前约束的异常。
     *
     * @param nameSpace 使用约束时所处的名称空间。
     * @param cause     导致约束触发异常的原因。
     */
    default void invalid(NameSpace nameSpace, Throwable cause) {
        String constraintCode = nameSpace.decorate(getConstraint().code());
        Constraint constraint = nameSpace != null ? Constraint.of(constraintCode) : getConstraint();
        ConstraintSystem.get().getInvalidationHandler().handle(constraint, cause);
    }

    /**
     * 根据条件触发异常或设什么也不做。
     *
     * @param predicate 触发约束条件。如果为 {code true}， 则触发约束，否则什么也不做。
     */
    default void invalidIf(boolean predicate) {
        if (predicate) {
            ConstraintSystem.get().getInvalidationHandler().handle(getConstraint());
        }
    }

    /**
     * 根据条件触发异常或设什么也不做。
     *
     * @param predicate 触发约束条件。如果为 {code true}， 则触发约束，否则什么也不做。
     * @param nameSpace 约束使用时所处的名称空间。
     */
    default void invalidIf(boolean predicate, NameSpace nameSpace) {
        if (predicate) {
            String constraintCode = nameSpace.decorate(getConstraint().code());
            Constraint constraint = nameSpace != null ? Constraint.of(constraintCode) : getConstraint();
            ConstraintSystem.get().getInvalidationHandler().handle(constraint);
        }
    }

    /**
     * 根据监测谓词的监测结果触发异常或设什么也不做。
     *
     * @param predicate 触发约束条件。如果谓词判断结果为 {code true}， 则触发约束，否则什么也不做。
     * @param values    约束谓词将使用的参数。
     */
    default <T> void invalidIf(CheckerPredicate<T> predicate, T... values) {
        Objects.requireNonNull(predicate, "监测谓词不能为null");
        invalidIf(predicate.check(values));
    }

    /**
     * 根据监测谓词的监测结果触发异常或设什么也不做。
     *
     * @param predicate 触发约束条件。如果谓词判断结果为 {code true}， 则触发约束，否则什么也不做。
     * @param nameSpace 约束使用时所处的名称空间。
     * @param values    约束谓词将使用的参数。
     */
    default <T> void invalidIf(CheckerPredicate<T> predicate, NameSpace nameSpace, T... values) {
        Objects.requireNonNull(predicate, "监测谓词不能为null");
        invalidIf(predicate.check(values), nameSpace);
    }

    /**
     * 如果待监测的参数为null则触发异常，否则什么也不做。
     *
     * @param value 待监测的值
     */
    default <T> T notNull(T value) {
        invalidIf(value == null);
        return value;
    }

    /**
     * 如果待监测的参数为null则触发异常，否则什么也不做。
     *
     * @param value     待监测的值
     * @param nameSpace 约束使用时所处的名称空间。
     */
    default <T> T notNull(T value, NameSpace nameSpace) {
        invalidIf(value == null, nameSpace);
        return value;
    }

}
