package me.szlxty.core;


import me.szlxty.check.Constraint;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * 统一异常处理的基类，一个运行时异常。不允许使用没有具体环境语义的异常。
 */
public abstract class BaseException extends RuntimeException {
    private final Constraint constraint;

    public BaseException(Supplier<String> codeOrBriefSupplier) {
        this(Objects.requireNonNull(codeOrBriefSupplier, "异常信息提供者不能为null").get());
    }

    public BaseException(String codeOrBrief) {
        this(Constraint.of(Objects.requireNonNull(codeOrBrief, "异常信息不能为null")));
    }

    public BaseException(Supplier<String> codeOrBriefSupplier, Throwable cause) {
        this(Objects.requireNonNull(codeOrBriefSupplier, "异常信息提供者不能为null").get(), cause);
    }

    public BaseException(String codeOrBrief, Throwable cause) {
        this(Constraint.of(Objects.requireNonNull(codeOrBrief, "异常信息不能为null")), cause);
    }

    /**
     * 根据指定的规则创建异常。
     *
     * @param constraint 规则.
     */
    public BaseException(Constraint constraint) {
        this(constraint, () -> constraint.brief(), null);
    }

    /**
     * 根据指定的规则和规则描述创建异常。在异常中，{@code brief} 指定的规则描述将取代 {@code constraint}中的规则描述。
     *
     * @param constraint 规则。
     * @param brief      规则描述。
     */
    public BaseException(Constraint constraint, String brief) {
        this(constraint, brief, null);
    }

    /**
     * 根据指定的规则和规则描述创建异常。在异常中，{@code brief} 指定的规则描述将取代 {@code constraint}中的规则描述。
     *
     * @param constraint    规则。
     * @param briefSupplier 规则描述提供者。
     */
    public BaseException(Constraint constraint, Supplier<String> briefSupplier) {
        this(constraint, briefSupplier, null);
    }

    /**
     * 根据指定的规则和异常创建异常，参数{@code cause}指定的异常将成为新异常因由异常。
     *
     * @param constraint 规则。
     * @param cause      出错原因。
     */
    public BaseException(Constraint constraint, Throwable cause) {
        this(constraint, () -> constraint.brief(), cause);
    }

    /**
     * 根据指定的规则、规则描述和异常创建异常，参数{@code cause}指定的异常将成为新异常因由异常。
     *
     * @param constraint 规则。
     * @param brief      规则描述。
     * @param cause      出错原因。
     */
    public BaseException(Constraint constraint, String brief, Throwable cause) {
        super(buildMessage(constraint, brief), cause);
        this.constraint = constraint;
    }

    /**
     * 根据指定的规则、规则描述和异常创建异常，参数{@code cause}指定的异常将成为新异常因由异常。
     *
     * @param constraint    规则。
     * @param briefSupplier 规则描述。
     * @param cause         出错原因。
     */
    public BaseException(Constraint constraint, Supplier<String> briefSupplier, Throwable cause) {
        super(buildMessage(constraint, briefSupplier != null ? briefSupplier.get() : null), cause);
        this.constraint = constraint;
    }

    public Constraint getConstraint() {
        return constraint;
    }

    /**
     * 根据出错规格返回出错字符串。
     *
     * @param constraint 出错规格
     * @return 出错信息字符串
     */
    private static String buildMessage(Constraint constraint, String brief) {
        Objects.requireNonNull(constraint, "检查约束编码");
        return String.format("%s -> %s", constraint.code(), brief != null ? brief : constraint.brief());
    }

    /**
     * 从指定的异常的级联层次上查找类型为BaseException异常，如果找到则返回找到的异常，否则返回<code>null</code>。
     *
     * @param ex 异常堆栈的最上层。
     * @return 找到的目标异常，或<code>null</code>，如果没有找到。
     */
    public static BaseException parse(Exception ex) {
        return parse(ex, BaseException.class);
    }

    public static <E extends Exception> E parse(Exception ex, Class<E> targetExceptionClass) {
        if (targetExceptionClass == null) {
            return null;
        }

        Throwable result = ex;
        while (result != null && !targetExceptionClass.isAssignableFrom(result.getClass())) {
            if (result.getCause() == null) {
                break;
            }
            result = result.getCause();
        }
        return targetExceptionClass.isAssignableFrom(result.getClass()) ? (E) result : null;
    }
}
