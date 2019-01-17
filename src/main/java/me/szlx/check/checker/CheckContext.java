package me.szlx.check.checker;

public class CheckContext {
    private static ThreadLocal holder = new ThreadLocal();

    @SuppressWarnings("unchecked")
    public static <T> T get() {
        return (T) holder.get();
    }

    @SuppressWarnings("unchecked")
    static <T> void set(T value) {
        holder.set(value);
    }

    static void remove() {
        holder.remove();
    }
}
