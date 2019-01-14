package me.szlxty.check.system;

import me.szlxty.check.Constraint;
import me.szlxty.check.ConstraintFactory;

import java.util.Objects;

public abstract class AbstractInvalidationHandler implements InvalidationHandler {
    @Override
    public void handle(Constraint constraint) {
        Objects.requireNonNull(constraint, "监测的约束不能为null");
        doHandle(constraint, null);
    }

    @Override
    public void handle(Constraint constraint, String brief) {
        Objects.requireNonNull(constraint, "监测的约束不能为null");
        if (brief != null && !brief.equals(constraint.brief())) {
            handle(ConstraintFactory.create(constraint.code(), brief));
        }
        handle(constraint);
    }

    @Override
    public void handle(Constraint constraint, Throwable cause) {
        Objects.requireNonNull(constraint, "监测的约束不能为null");
        doHandle(constraint, cause);
    }

    @Override
    public void handle(Constraint constraint, String brief, Throwable cause) {
        Objects.requireNonNull(constraint, "监测的约束不能为null");
        if (brief != null && !brief.equals(constraint.brief())) {
            handle(ConstraintFactory.create(constraint.code(), brief), cause);
        }
        handle(constraint, cause);
    }

    abstract protected void doHandle(Constraint constraint, Throwable cause);
}
