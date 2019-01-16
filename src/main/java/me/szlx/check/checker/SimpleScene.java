package me.szlx.check.checker;


import java.util.Objects;

class SimpleScene implements Scene {
    private final String name;

    public SimpleScene(String name) {
        Objects.requireNonNull(name, "场景名称不能为null");
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }
}
