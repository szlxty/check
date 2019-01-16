package me.szlx.check.constraint.bundle;

import me.szlx.check.constraint.container.ConstraintContainer;
import me.szlx.check.constraint.system.ConstraintSystem;

/**
 * 实现接口要求的默认出错管理系统的逻辑。
 */
public abstract class AbstractBundle implements ConstraintBundle {
    @Override
    public void bindTo(ConstraintContainer constraintContainer) {
        ConstraintContainer container = constraintContainer != null ? constraintContainer : ConstraintSystem.get().getConstraintContainer();
        container.install(this);
    }
}
