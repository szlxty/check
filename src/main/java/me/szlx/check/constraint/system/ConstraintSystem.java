package me.szlx.check.constraint.system;

import me.szlx.check.constraint.container.ConstraintContainer;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public interface ConstraintSystem {
    /**
     * 获取约束系统的受管约束的容器。
     *
     * @return 约束系统的受管约束的容器。
     */
    ConstraintContainer getConstraintContainer();

    InvalidationHandler getInvalidationHandler();

    static ConstraintSystem get() {
        if (Helper.constraintSystem != null) {
            return Helper.constraintSystem;
        }
        if (Helper.defaultConstraintSystem == null) {
            Helper.defaultConstraintSystem = new DefaultConstraintSystem();
        }
        return Helper.defaultConstraintSystem;
    }

    class Helper {
        private static ConstraintSystem defaultConstraintSystem;
        private static ConstraintSystem constraintSystem;

        public static void unmount() {
            if (constraintSystem != null) {
                LoggerFactory.getLogger(ConstraintSystem.class).info("卸载约束管理系统：{}", constraintSystem);
                constraintSystem = null;
            }
        }

        public static void mount(ConstraintSystem constraintSystem) {
            Objects.requireNonNull(constraintSystem, "待安装的约束管理系统不能为null");
            if (Helper.constraintSystem == null) {
                Helper.constraintSystem = constraintSystem;
                LoggerFactory.getLogger(ConstraintSystem.class).info("安装约束管理系统：{}", constraintSystem);
            } else {
                LoggerFactory.getLogger(ConstraintSystem.class).info("不能重复约束管理系统", constraintSystem);
            }
        }
    }
}
