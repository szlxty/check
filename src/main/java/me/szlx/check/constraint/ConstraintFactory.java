package me.szlx.check.constraint;

import java.util.Objects;

public class ConstraintFactory {
    public static Constraint create(String code, String brief){
        return new SimpleConstraint(code,brief);
    }

    public static Constraint create(String code, String brief, int order){
        return new SimpleConstraint(code,brief, order);
    }

    private static class SimpleConstraint implements Constraint, Ordered {
        private String code;
        private String brief;
        private int order;

        public SimpleConstraint(String code, String brief, int order) {
            this.code = code;
            this.brief = brief;
            this.order = order;
        }

        public SimpleConstraint(String code, String brief) {
            this(code, brief, 0);
        }

        public String code() {
            return code;
        }

        public String brief() {
            return brief;
        }

        public int getOrder() {
            return order;
        }

        @Override
        public String toString() {
            return "Constraint{" +
                    "code='" + code + '\'' +
                    ", brief='" + brief + '\'' +
                    ", order=" + order +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SimpleConstraint)) return false;
            SimpleConstraint that = (SimpleConstraint) o;
            return Objects.equals(code, that.code) &&
                    Objects.equals(brief, that.brief);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code, brief);
        }
    }
}
