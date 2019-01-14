package me.szlxty.check.namespace;


import java.util.Objects;

public class HierarchicNameSpace implements NameSpace {
    private final String name;
    private final NameSpace parent;

    public HierarchicNameSpace(String name, NameSpace parent) {
        Objects.requireNonNull(name, "名称空间名称不能为null");
        this.name = name;
        this.parent = parent;
    }

    public HierarchicNameSpace(String name) {
        this(name, null);
    }

    @Override
    public String name() {
        return parent != null ? parent.decorate(name) : name;
    }
}
