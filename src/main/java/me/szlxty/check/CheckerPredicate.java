package me.szlxty.check;

public interface CheckerPredicate<T> {
    boolean check(T... values);
}
