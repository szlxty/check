package me.szlx.check.constraint.bundle;

import me.szlx.check.constraint.Constraint;
import me.szlx.check.constraint.Ordered;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>复合约束簇。可以指定子约束簇的优先级别</p>
 */
public class CombinedBundle extends AbstractBundle {
    private Logger logger = LoggerFactory.getLogger(CombinedBundle.class);
    private List<ConstraintBundle> constraintBundleList = new ArrayList<>();
    private boolean priority = true;

    @Override
    public List<Constraint> getConstraints() {
        if (priority) {
            constraintBundleList.sort(Ordered.DESC);
        }
        Map<String, Constraint> resultMap = new HashMap<>();
        for (ConstraintBundle constraintBundle : constraintBundleList) {
            for (Constraint constraint : constraintBundle.getConstraints()) {
                if (!resultMap.containsKey(constraint.code())) {
                    resultMap.put(constraint.code(), constraint);
                } else {
                    logger.debug("约束先级较低被忽略：bundle = {}, code = {}", constraintBundle.getClass().getCanonicalName(), constraint.code());
                }
            }
        }
        return new ArrayList<>(resultMap.values());
    }

    public List<ConstraintBundle> getConstraintBundleList() {
        return constraintBundleList;
    }

    public void setConstraintBundleList(List<ConstraintBundle> constraintBundleList) {
        if (constraintBundleList != null) {
            this.constraintBundleList.clear();
            this.constraintBundleList.addAll(constraintBundleList);
        }
    }

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }
}