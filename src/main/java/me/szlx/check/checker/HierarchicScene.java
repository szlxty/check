package me.szlx.check.checker;


import java.util.Objects;

class HierarchicScene implements Scene {
    private final String name;
    private final Scene parent;

    public HierarchicScene(String name, Scene parent) {
        this.name = Objects.requireNonNull(name, "场景名称不能为null");
        this.parent = Objects.requireNonNull(parent, "父场景不能为null");
    }


    @Override
    public String name() {
        return parent != null ? parent.decorate(name) : name;
    }
}
