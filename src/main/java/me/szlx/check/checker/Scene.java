package me.szlx.check.checker;

import me.szlx.check.constraint.Constraint;
import me.szlx.check.constraint.ConstraintFactory;
import me.szlx.check.constraint.system.ConstraintSystem;

import java.util.Objects;
import java.util.function.Predicate;

public interface Scene {

    String name();

    /**
     * 获取约束代码在场景中的具体代码。如果不是枚举类型，则以{@link #name()}值为名称空间；否则如果是枚举类，则将类名当成父名称空间，实例名当成子名称空间（SCENE 和 SCENES 除外。）。
     * 当约束代码已经
     *
     * @param constraintCode 约束代码。
     * @return 约束在场景中的全路径代码。
     */
    default String decorate(String constraintCode) {
        Objects.requireNonNull(constraintCode, "检查约束的代码或描述不能为null");
        String name = name();
        if (getClass().isEnum()) {
            String className = getClass().getSimpleName();
            name = "SCENE".equals(name) || "SCENES".equals(name) ? className : className + "." + name;
        }
        //TODO 去掉中间重叠部分b.c.: a.b.c.b.c.d
        return name + "." + constraintCode;
    }

    /**
     * 根据指定的约束信息与断言创建新的checker。
     *
     * @param constraintCodeOrBrief 约束信息，约束的代码或描述。
     * @param predicate             检测断言。
     * @param <T>                   待检测对象的类型。
     * @return 新的检测器。
     */
    default <T> Checker<T> checker(String constraintCodeOrBrief, Predicate<T> predicate) {
        if (constraintCodeOrBrief == null) {
            throw new IllegalArgumentException("检测约束代码不能为null");
        }
        String constraintCode = decorate(constraintCodeOrBrief);
        Constraint constraint = ConstraintSystem.get().getConstraintContainer().find(constraintCode);
        if (constraint == null) {
            constraint = ConstraintFactory.create(Constraint.UNCODED.code(), constraintCode);
        }
        return Checker.create(constraint, predicate);
    }

    /**
     * 根据指定的约束信息与断言创建新的checker。
     *
     * @param constraint 约束信息，约束的代码或描述。
     * @param predicate             检测断言。
     * @param <T>                   待检测对象的类型。
     * @return 新的检测器。
     */
    default <T> Checker<T> checker(Constraint constraint, Predicate<T> predicate) {
        if (constraint == null) {
            throw new IllegalArgumentException("检测约束不能为null");
        }
        return checker(constraint.code(), predicate);
    }

    /**
     * 创建一个新的场景。
     *
     * @param name 场景名称，不能为null。
     * @return 新的场景。
     */
    static Scene of(String name) {
        return new SimpleScene(name);
    }

    /**
     * 创建一个新的子场景。
     *
     * @param name   场景名称，不能为null。
     * @param parent 待创建场景的父场景。如果为则表示没有父场景，同 {@link #of(String)}。
     * @return 新的场景。
     */
    static Scene of(String name, Scene parent) {
        return parent != null ? new HierarchicScene(name, parent) : of(name);
    }
}
