package me.szlx.check.constraint.bundle;

import me.szlx.check.constraint.Constraint;
import me.szlx.check.constraint.ConstraintFactory;

import java.util.*;

/**
 * 从属性文件中装载“简单对象”。其中规则代码为属性名称，规则描述为属性值。
 */
public class PropertiesBundle extends AbstractBundle {
    private Map<String, String> errorMap = new HashMap<>();

    public PropertiesBundle() {
    }

    public PropertiesBundle(Properties... properties) {
        addProperties(properties);
    }

    public void addProperties(Properties... properties) {
        if (properties != null && properties.length > 0) {
            addProperties(Arrays.asList(properties));
        }
    }

    public void addProperties(Iterable<Properties> iterProperties) {
        if (iterProperties != null) {
            for (Properties property : iterProperties) {
                if (property != null) {
                    doAddProperties(property);
                }
            }
        }
    }

    private void doAddProperties(Properties properties) {
        for (String errorCode : properties.stringPropertyNames()) {
            errorMap.put(errorCode, properties.getProperty(errorCode));
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
