package me.szlxty.check.bundle;

import me.szlxty.check.Constraint;
import me.szlxty.check.ConstraintFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用Map对象装载“简单约束”。其中约束代码为Map条目的键，约束描述为Map条目的值。
 */
public class MapBundle extends AbstractBundle {
    private Map<String, String> errorMap = new HashMap<>();

    public MapBundle() {
    }

    public MapBundle(Map<String, String> errorMap) {
        setErrorMap(errorMap);
    }


    public void setErrorMap(Map<String, String> errorMap) {
        if (errorMap != null) {
            this.errorMap.putAll(errorMap);
        }
    }

    @Override
    public List<Constraint> getConstraints() {
        List<Constraint> result = new ArrayList<>(errorMap.size());
        for (Map.Entry<String, String> entry : errorMap.entrySet()) {
            result.add(ConstraintFactory.create(entry.getKey(), entry.getValue()));
        }
        return result;
    }
}
