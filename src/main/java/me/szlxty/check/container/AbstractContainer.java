package me.szlxty.check.container;

import me.szlxty.check.Constraint;
import me.szlxty.check.bundle.ConstraintBundle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * {@code Constraint.buildins()} 的内容将装入容器。
 */
public abstract class AbstractContainer implements ConstraintContainer {
    private final Map<String, Constraint> errorMap = new HashMap<>();

    public AbstractContainer() {
        for (Constraint constraint : Constraint.buildins()) {
            errorMap.put(constraint.code(), constraint);
        }
    }

    public <T extends Constraint> T find(String constraintCode) {
        Objects.requireNonNull(constraintCode, "检查约束代码不能为null");
        return (T) errorMap.get(constraintCode);
    }

    public void install(ConstraintBundle constraintBundle) {
        if (constraintBundle != null) {
            constraintBundle.getConstraints().stream().forEach(constraint -> {
                if (constraint != null) {
                    doInstall(constraint);
                }
            });
        }
    }

    public void install(Constraint... constraints) {
        if (constraints != null) {
            Arrays.asList(constraints).stream().forEach(constraint -> {
                if (constraint != null) {
                    doInstall(constraint);
                }
            });
        }
    }

    protected Map<String, Constraint> getErrorMap() {
        return errorMap;
    }

    abstract protected void doInstall(Constraint constraint);
}