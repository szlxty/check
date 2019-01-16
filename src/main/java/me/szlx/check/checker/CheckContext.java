package me.szlx.check.checker;

class CheckContext {
    private static ThreadLocal holder = new ThreadLocal();

    @SuppressWarnings("unchecked")
    public static <T> T get() {
        return (T) holder.get();
    }

    @SuppressWarnings("unchecked")
    public static <T> void set(T value) {
        holder.set(value);
    }

    public static void remove() {
        holder.remove();
    }
}
