package me.szlx.check.checker;

import me.szlx.check.constraint.Constraint;
import me.szlx.check.constraint.system.ConstraintSystem;

import java.util.function.Predicate;

class SimpleChecker<T> implements Checker<T> {
    private Constraint constraint;
    private Predicate<T> predicate;

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
    public void check(T target) {
        if (!predicate.test(target)) {
            ConstraintSystem.get().getInvalidationHandler().handle(constraint);
        }
    }

    @Override
    public void check(T target, Object... context) {
        Object oldContext = CheckContext.get();
        CheckContext.set(context);
        try {
            check(target);
        } finally {
            CheckContext.set(oldContext);
        }
    }
}
