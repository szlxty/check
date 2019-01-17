package me.szlx.check.checker;

import me.szlx.check.constraint.Constraint;

import java.util.function.Predicate;

public interface Checker<T> {
    /**
     * 对指定的值进行检测，如果检测通过，则继续，否则如果检测不通过，则抛出异常 {@link me.szlx.check.CheckException CheckException}。
     *
     * @param target  待检测的对象。
     * @param context 检测时的上下文。
     */
    T check(T target, Object... context);

    /**
     * 创建检测器。
     *
     * @param constraint 检测约束。
     * @param predicate  检测断言。
     * @param <T>        待检测对象的类型。
     * @return 用于执行检测的检测器。
     */
    static <T> Checker<T> create(Constraint constraint, Predicate<T> predicate) {
        return new SimpleChecker<>(constraint, predicate);
    }
}
