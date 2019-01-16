package me.szlx.check.constraint.bundle;

import com.typesafe.config.*;
import me.szlx.check.constraint.Constraint;
import me.szlx.check.constraint.ConstraintFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>使用TypeSafe的Config作为约束配置引擎。</p>
 * <p>默认资源名称遵循Config的默认约束。</p>
 */
public class TypeSafeBundle extends AbstractResourceBundle {
    @Override
    public List<Constraint> getConstraints() {
        Config config = getResource() != null ? ConfigFactory.load(getResource()) : ConfigFactory.load();

        String prefix = getPrefix();
        while (prefix != null && prefix.endsWith(".")) {
            prefix = prefix.substring(0, prefix.length() - 1);
        }
        if (prefix != null) {
            if (config.hasPath(prefix)) {
                config = config.getConfig(prefix);
            } else {
                return Collections.emptyList();
            }
        }
        return getConstraints(config, getNamespace());
    }

    @SuppressWarnings("unchecked")
    private List<Constraint> getConstraints(Config config, String namespace) {
        List<Constraint> result = new ArrayList<>();
        for (Map.Entry<String, ConfigValue> entry : config.entrySet()) {
            String key = entry.getKey();
            ConfigValue value = entry.getValue();
            if (ConfigValueType.LIST.equals(value.valueType())) {
                ((ConfigList) value).unwrapped().forEach(item -> {
                    Map<String, Object> itemMap = (Map<String, Object>) item;
                    if (itemMap.containsKey(getCodeKey()) && itemMap.containsKey(getBriefKey())) {
                        Object itemCode = itemMap.get(getCodeKey());
                        Object itemBrief = itemMap.get(getBriefKey());
                        Constraint constraint = createConstraint(namespace, String.valueOf(itemCode), itemBrief);
                        result.add(constraint);
                    } else {
                        logger.debug("约束配置需要键：{} 和 {}", getCodeKey(), getBriefKey());
                    }
                });
            } else if (ConfigValueType.OBJECT.equals(value.valueType())) {
                Config subConfig = ((ConfigObject) value).toConfig();
                String subNamespace = namespace != null ? namespace + "." + key : key;
                result.addAll(getConstraints(subConfig, subNamespace));
            } else {
                Constraint constraint = createConstraint(namespace, key, value.unwrapped());
                result.add(constraint);
            }
        }

        return result;
    }

    private Constraint createConstraint(String namespace, String code, Object brief) {
        String theCode = namespace != null ? namespace + "." + code : code;
        String theBrief = String.valueOf(brief);
        return ConstraintFactory.create(theCode, theBrief);
    }
}
