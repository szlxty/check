package me.szlxty.check.system;

import me.szlxty.check.container.ConstraintContainer;
import me.szlxty.check.container.ModularContainer;
import me.szlxty.check.container.PriorityContainer;

/**
 * 约束系统的默认实现，能满足大多数场景的使用要求。
 */
public class DefaultConstraintSystem implements ConstraintSystem {
    private final ConstraintContainer constraintContainer;
    private final InvalidationHandler invalidationHandler;

    public DefaultConstraintSystem() {
        this(new ModularContainer(new PriorityContainer()), new CheckException.InvalidationHandler());
    }

    public DefaultConstraintSystem(ConstraintContainer constraintContainer, InvalidationHandler invalidationHandler) {
        this.constraintContainer = constraintContainer != null ? constraintContainer : new ModularContainer(new PriorityContainer());
        this.invalidationHandler = invalidationHandler != null ? invalidationHandler : new CheckException.InvalidationHandler();
    }

    @Override
    public ConstraintContainer getConstraintContainer() {
        return constraintContainer;
    }

    public InvalidationHandler getInvalidationHandler() {
        return invalidationHandler;
    }
}