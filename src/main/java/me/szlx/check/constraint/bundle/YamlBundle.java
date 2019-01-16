package me.szlx.check.constraint.bundle;

import me.szlx.check.constraint.Constraint;
import me.szlx.check.constraint.ConstraintFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class YamlBundle extends AbstractResourceBundle {
    @Override
    public List<Constraint> getConstraints() {
        Yaml yaml = new Yaml();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(getResource());
        Object object = yaml.load(inputStream);
        object = sikpPrefix(object, getPrefix());
        return getConstraints(object, getNamespace());
    }

    @SuppressWarnings("unchecked")
    private List<Constraint> getConstraints(Object yamlObject, String namespace) {
        if (yamlObject == null) {
            return Collections.EMPTY_LIST;
        }

        if (List.class.isAssignableFrom(yamlObject.getClass())) {
            return ((List<Map<String, ?>>) yamlObject).stream()
                    .map(r -> createConstraint(r, namespace))
                    .distinct()
                    .collect(Collectors.toList());
        }

        List<Constraint> ruleList = new ArrayList<>();
        if (Map.class.isAssignableFrom(yamlObject.getClass())) {
            Map<?, ?> ruleMap = (Map<?, ?>) yamlObject;
            ruleMap.forEach((key, value) -> {
                String theNamespace = namespace;
                if (!RULES.equals(key)) {
                    theNamespace = namespace != null ? namespace + "." + key : key.toString();
                }
                ruleList.addAll(getConstraints(value, theNamespace));
            });
        }
        return ruleList;
    }

    private Constraint createConstraint(Map<String, ?> ruleMap, String namespace) {
        String brief = String.valueOf(ruleMap.get(getBriefKey()));
        String code = String.valueOf(ruleMap.get(getCodeKey()));
        code = namespace != null ? namespace + "." + code : code;
        return ConstraintFactory.create(code, brief);
    }

    @SuppressWarnings("unchecked")
    private Object sikpPrefix(Object yamlObject, String prefix) {
        if (prefix == null || "".equals(prefix)) {
            return yamlObject;
        }
        if (yamlObject == null) {
            return null;
        }

        Object result = yamlObject;
        if (prefix.charAt(prefix.length() - 1) != '.') {
            prefix = prefix + ".";
        }

        int fromPosition = 0;
        while (fromPosition < prefix.length()) {
            int delimiterIndex = prefix.indexOf('.', fromPosition);
            if (delimiterIndex != -1) {
                if (fromPosition != delimiterIndex) {
                    String thePrefix = prefix.substring(fromPosition, delimiterIndex);
                    if (thePrefix.length() > 0) {
                        try {
                            Map<String, Object> theMap = (Map<String, Object>) result;
                            result = theMap.get(thePrefix);
                        } catch (ClassCastException ex) {
                            logger.debug("不存在指定的前缀：{}({})", prefix, thePrefix);
                        }
                    }
                }
                fromPosition = delimiterIndex + 1;
            }
        }
        return result;
    }

    public String getResource() {
        String resource = super.getResource();
        if (resource == null) {
            resource = "constraints.yml";
        }
        return resource;
    }
}
