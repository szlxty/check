package me.szlxty.check.namespace;

import me.szlxty.check.Checker;
import me.szlxty.check.CheckerPredicate;
import me.szlxty.check.Checkers;
import me.szlxty.check.Constraint;
import me.szlxty.check.system.ConstraintSystem;

import java.util.Objects;

public interface NameSpace {

    String name();

    /**
     * 获取名称空间包裹后的约束全名称代码。如果不是枚举类型，则以{@link #name()}值为名称空间；否则如果是枚举类，则将类名当成父名称空间，实例名当成子名称空间（CONSTRAINT和CONSTRAINTS除外。）。
     *
     * @param constraintCodeOrBrief 约束代码。
     * @return 约束的全名称代码。
     */
    default String decorate(String constraintCodeOrBrief) {
        Objects.requireNonNull(constraintCodeOrBrief, "检查约束的代码或描述不能为null");
        String name = name();
        if (getClass().isEnum()) {
            String className = getClass().getSimpleName();
            name = "CONSTRAINT".equals(name) || "CONSTRAINTS".equals(name) ? className : className + "." + name;
        }
        return name + "." + constraintCodeOrBrief;
    }

    /**
     * 获取名称空间是否有指定的约束。任何名称空间不拥有{@code null}约束。
     *
     * @param constraint 待查询的约束。
     * @return {@code true}，如果名称空间有指定的约束，否则返回{@code false}。
     */
    default boolean canOwn(Constraint constraint) {
        return constraint != null ? constraint.code().startsWith(name()) : false;
    }

    NameSpace DUCK = new NameSpace() {
        @Override
        public String name() {
            return "DUCK";
        }

        @Override
        public String decorate(String constraintCodeOrBrief) {
            Objects.requireNonNull(constraintCodeOrBrief, "检查约束的代码或描述不能为null");
            return constraintCodeOrBrief;
        }

        @Override
        public boolean canOwn(Constraint constraint) {
            return constraint != null;
        }
    };

    static NameSpace of(String name) {
        return new SimpleNameSpace(name);
    }

    static NameSpace of(String name, NameSpace parent) {
        return new HierarchicNameSpace(name, parent);
    }

    /**
     * 获取简单约束。首先在约束系统中查找，如果查找到则直接查找到的约束对象，否则以{@code Rule.UNREGISTERED.code()}为代码，
     * {@code constraintCodeOrBrief}为描述创建一个返回。
     *
     * @param constraintCodeOrBrief 约束代码或描述。
     * @return 查找到的或新创建的约束对象。
     */
    default Constraint constraint(String constraintCodeOrBrief) {
        Objects.requireNonNull(constraintCodeOrBrief, "检查约束的代码或描述不能为null");
        Constraint managedConstraint = ConstraintSystem.get().getConstraintContainer().find(decorate(constraintCodeOrBrief));
        if (managedConstraint != null) {
            return managedConstraint;
        }
        return Constraint.of(constraintCodeOrBrief);
    }

    default Checker checker(Constraint constraint) {
        Objects.requireNonNull(constraint, "检查的约束不能为null");
        return checker(constraint.code());
    }

    default Checker checker(String constraintCodeOrBrief) {
        return Checkers.of(constraint(constraintCodeOrBrief));
    }

    /**
     * 立即抛出当前约束的异常。
     *
     * @param constraint 使用约束时所处的名称空间。
     */
    default void invalid(Constraint constraint) {
        Objects.requireNonNull(constraint, "检查的约束不能为null");
        checker(constraint).invalid(this);
    }

    /**
     * 立即抛出当前约束的异常。
     *
     * @param cause 导致约束触发异常的原因。
     */
    default void invalid(Constraint constraint, Throwable cause) {
        Objects.requireNonNull(constraint, "检查的约束不能为null");
        checker(constraint).invalid(this, cause);
    }

    default void invalid(String constraintCodeOrBrief) {
        Objects.requireNonNull(constraintCodeOrBrief, "检查约束的代码或描述不能为null");
        checker(constraintCodeOrBrief).invalid(this);
    }

    default void invalid(String constraintCodeOrBrief, Throwable cause) {
        Objects.requireNonNull(constraintCodeOrBrief, "检查约束的代码或描述不能为null");
        checker(constraintCodeOrBrief).invalid(this, cause);
    }

    /**
     * 立即抛出当前约束的异常。
     *
     * @param constraint 使用约束时所处的名称空间。
     */
    default void invalidIf(boolean predicate, Constraint constraint) {
        Objects.requireNonNull(constraint, "检查的约束不能为null");
        checker(constraint).invalidIf(predicate, this);
    }

    default void invalidIf(boolean predicate, String constraintCodeOrBrief) {
        Objects.requireNonNull(constraintCodeOrBrief, "检查约束的代码或描述不能为null");
        checker(constraintCodeOrBrief).invalidIf(predicate, this);
    }

    default <T> void invalidIf(Constraint constraint, CheckerPredicate<T> predicate, T... values) {
        checker(constraint).invalidIf(predicate, this, values);
    }

    default <T> void invalidIf(String constraintCodeOrBrief, CheckerPredicate<T> predicate, T... values) {
        checker(constraintCodeOrBrief).invalidIf(predicate, this, values);
    }

    default <T> T notNull(T value, Constraint constraint) {
        return checker(constraint).notNull(value, this);
    }
}
