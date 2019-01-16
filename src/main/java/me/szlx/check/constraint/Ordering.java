package me.szlx.check.constraint;

public final class Ordering {
    private final int order;

    public static Ordering of(int order) {
        return new Ordering(order);
    }

    public Ordering(int order) {
        this.order = order;
    }

    public <T extends Constraint> T order(T constraint) {
        if (constraint == null) {
            throw new IllegalArgumentException("待编号的约束不能为null");
        }
//        Rule.ALLWAYS.TODO.invalid("暂时不实现，以后考虑动态类实现方式");
        return constraint;
    }
}
