package me.szlxty.check;

import me.szlxty.check.namespace.NameSpace;

import java.util.Objects;

public final class Checkers {
    private Checkers() {
    }

    /**
     * 获取指定约束的校验器。
     *
     * @param constraintCodeOrBrief 约束代码或描述。
     * @return 为指定约束创建新的校验器。
     */
    public static Checker of(String constraintCodeOrBrief) {
        Objects.requireNonNull(constraintCodeOrBrief, "检查的约束代码或描述不能为null");
        return of(Constraint.of(constraintCodeOrBrief));
    }

    /**
     * 获取指定约束的校验器。
     *
     * @param constraint 约束代码或描述。
     * @return 为指定约束创建新的校验器。
     */
    public static Checker of(Constraint constraint) {
        Objects.requireNonNull(constraint, "检查的约束不能为null");
        return new SimpleChecker(constraint);
    }

    public static void invalid(String constraintCodeOrBrief) {
        Objects.requireNonNull(constraintCodeOrBrief, "检查约束的代码或描述不能为null");
        of(constraintCodeOrBrief).invalid();
    }

    public static void invalid(String constraintCodeOrBrief, NameSpace nameSpace) {
        Objects.requireNonNull(constraintCodeOrBrief, "检查约束的代码或描述不能为null");
        of(constraintCodeOrBrief).invalid(nameSpace);
    }

    public static void invalid(String constraintCodeOrBrief, Throwable throwable) {
        Objects.requireNonNull(constraintCodeOrBrief, "检查约束的代码或描述不能为null");
        of(constraintCodeOrBrief).invalid(throwable);
    }

    public static void invalid(String constraintCodeOrBrief, NameSpace nameSpace, Throwable throwable) {
        Objects.requireNonNull(constraintCodeOrBrief, "检查约束的代码或描述不能为null");
        of(constraintCodeOrBrief).invalid(nameSpace, throwable);
    }

    public static void invalid(Constraint constraint) {
        Objects.requireNonNull(constraint, "检查的约束不能为null");
        of(constraint).invalid();
    }

    public static void invalid(Constraint constraint, NameSpace nameSpace) {
        Objects.requireNonNull(constraint, "检查的约束不能为null");
        of(constraint).invalid(nameSpace);
    }

    public static void invalid(Constraint constraint, Throwable throwable) {
        Objects.requireNonNull(constraint, "检查的约束不能为null");
        of(constraint).invalid(throwable);
    }

    public static void invalid(Constraint constraint, NameSpace nameSpace, Throwable throwable) {
        Objects.requireNonNull(constraint, "检查的约束不能为null");
        of(constraint).invalid(nameSpace, throwable);
    }

    public static void invalidIf(boolean predicate, String constraintCodeOrBrief) {
        if (predicate) {
            invalid(constraintCodeOrBrief);
        }
    }

    public static void invalidIf(boolean predicate, String constraintCodeOrBrief, NameSpace nameSpace) {
        if (predicate) {
            invalid(constraintCodeOrBrief, nameSpace);
        }
    }

    public static void invalidIf(boolean predicate, Constraint constraint) {
        Objects.requireNonNull(constraint, "检查的约束不能为null");
        if (predicate) {
            invalid(constraint);
        }
    }

    public static void invalidIf(boolean predicate, Constraint constraint, NameSpace nameSpace) {
        Objects.requireNonNull(constraint, "检查的约束不能为null");
        if (predicate) {
            invalid(constraint, nameSpace);
        }
    }

    public static <T> T notNull(T value, String constraintCodeOrBrief) {
        if (value == null) {
            invalid(constraintCodeOrBrief);
        }
        return value;
    }

    public static <T> T notNull(T value, String constraintCodeOrBrief, NameSpace nameSpace) {
        if (value == null) {
            invalid(constraintCodeOrBrief, nameSpace);
        }
        return value;
    }

    public static <T> T notNull(T value, Constraint constraint) {
        Objects.requireNonNull(constraint, "检查的约束不能为null");
        if (value == null) {
            invalid(constraint);
        }
        return value;
    }

    public static <T> T notNull(T value, Constraint constraint, NameSpace nameSpace) {
        Objects.requireNonNull(constraint, "检查的约束不能为null");
        if (value == null) {
            invalid(constraint, nameSpace);
        }
        return value;
    }

    public static <T> void invalidIf(String constraintCodeOrBrief, CheckerPredicate<T> predicate, T... values) {
        of(constraintCodeOrBrief).invalidIf(predicate, values);
    }

    public static <T> void invalidIf(String constraintCodeOrBrief, CheckerPredicate<T> predicate, NameSpace nameSpace, T... values) {
        of(constraintCodeOrBrief).invalidIf(predicate, nameSpace, values);
    }

    public static <T> void invalidIf(Constraint constraint, CheckerPredicate<T> predicate, T... values) {
        Objects.requireNonNull(constraint, "检查的约束不能为null");
        constraint.checker().invalidIf(predicate, values);
    }

    public static <T> void invalidIf(Constraint constraint, CheckerPredicate<T> predicate, NameSpace nameSpace, T... values) {
        Objects.requireNonNull(constraint, "检查的约束不能为null");
        constraint.checker().invalidIf(predicate, nameSpace, values);
    }

    private static class SimpleChecker implements Checker {
        private final Constraint constraint;

        private SimpleChecker(Constraint constraint) {
            this.constraint = constraint;
        }

        @Override
        public Constraint getConstraint() {
            return constraint;
        }
    }
}
