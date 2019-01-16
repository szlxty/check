package me.szlx.check.constraint.bundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractResourceBundle extends AbstractBundle {
    public static final String DEFAULT_RULE_PREFIX = "com.quantdo.constraint.constraint";
    public static final String KEY_DEFAULT_RULE_CODE = "code";
    public static final String KEY_DEFAULT_RULE_BRIEF = "brief";

    protected static final String RULES = "rules";
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private String codeKey = KEY_DEFAULT_RULE_CODE;
    private String briefKey = KEY_DEFAULT_RULE_BRIEF;
    private String prefix = DEFAULT_RULE_PREFIX;
    private String namespace = null;
    private String resource;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix != null && !prefix.endsWith(".") ? prefix + "." : prefix;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getCodeKey() {
        return codeKey;
    }

    public void setCodeKey(String codeKey) {
        this.codeKey = codeKey;
    }

    public String getBriefKey() {
        return briefKey;
    }

    public void setBriefKey(String briefKey) {
        this.briefKey = briefKey;
    }
}
