package me.szlx.check.checker;

import me.szlx.check.constraint.Constraint;
import me.szlx.check.constraint.system.ConstraintSystem;

import java.util.function.Predicate;

class SimpleChecker<T> implements Checker<T> {
    private final Constraint constraint;
    private final Predicate<T> predicate;

    SimpleChecker(Constraint constraint, Predicate<T> predicate) {
        if (constraint == null) {
            throw new IllegalArgumentException("检测约束不能为null");
        }
        if (predicate == null) {
            throw new IllegalArgumentException("检测断言不能为null");
        }
        this.constraint = constraint;
        this.predicate = predicate;
    }

    @Override
    public T check(T target, Object... context) {
        Object oldContext = CheckContext.get();
        if (isSingleValue(context)) {
            CheckContext.set(context[0]);
        } else {
            CheckContext.set(context);
        }
        try {
            if (!predicate.test(target)) {
                ConstraintSystem.get().getInvalidationHandler().handle(constraint);
            }
            return target;
        } finally {
            CheckContext.set(oldContext);
        }
    }

    private boolean isSingleValue(Object[] context) {
        return context != null && context.length == 1;
    }
}
