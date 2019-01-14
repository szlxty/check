package me.szlxty.check;


import java.util.Comparator;

/**
 * 排序
 */
public interface Ordered {
    int HIGHEST_PRECEDENCE = -2147483648;
    int LOWEST_PRECEDENCE = 2147483647;
    int DEFAULT_PRECEDENCE = 0;

    /**
     * 返回 顺序号。
     *
     * @return 顺序号。
     */
    int getOrder();

    Comparator DESC = new Comparator() {
        public int compare(Object o1, Object o2) {
            int s1 = (o1 instanceof Ordered) ? ((Ordered) o1).getOrder() : 0;
            int s2 = (o2 instanceof Ordered) ? ((Ordered) o2).getOrder() : 0;
            return s1 == s2 ? 0 : s1 > s2 ? 1 : -1;
        }
    };

    Comparator ASC = new Comparator() {
        public int compare(Object o1, Object o2) {
            int s1 = (o1 instanceof Ordered) ? ((Ordered) o1).getOrder() : 0;
            int s2 = (o2 instanceof Ordered) ? ((Ordered) o2).getOrder() : 0;
            return s1 == s2 ? 0 : s1 > s2 ? -1 : 1;
        }
    };
}
