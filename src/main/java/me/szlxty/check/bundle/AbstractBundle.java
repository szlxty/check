package me.szlxty.check.bundle;

import me.szlxty.check.container.ConstraintContainer;
import me.szlxty.check.system.ConstraintSystem;

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
