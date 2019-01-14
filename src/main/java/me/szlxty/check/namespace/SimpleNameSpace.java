package me.szlxty.check.namespace;


import java.util.Objects;

public class SimpleNameSpace implements NameSpace {
    private final String name;

    public SimpleNameSpace(String name) {
        Objects.requireNonNull(name, "名称空间代码不能为null");
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }
}
