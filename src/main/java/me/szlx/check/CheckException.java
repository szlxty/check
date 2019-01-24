package me.szlx.check;


import me.szlx.check.constraint.Constraint;
import me.szlx.check.constraint.system.AbstractInvalidationHandler;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * 统一的检测不通过的运行时异常。
 */
public class CheckException extends RuntimeException {
    private final Constraint constraint;

    public CheckException(Supplier<String> codeOrBriefSupplier) {
        this(Objects.requireNonNull(codeOrBriefSupplier, "异常信息提供者不能为null").get());
    }

    public CheckException(String codeOrBrief) {
        this(Constraint.getOrCreate(Objects.requireNonNull(codeOrBrief, "异常信息不能为null")));
    }

    public CheckException(Supplier<String> codeOrBriefSupplier, Throwable cause) {
        this(Objects.requireNonNull(codeOrBriefSupplier, "异常信息提供者不能为null").get(), cause);
    }

    public CheckException(String codeOrBrief, Throwable cause) {
        this(Constraint.getOrCreate(Objects.requireNonNull(codeOrBrief, "异常信息不能为null")), cause);
    }

    /**
     * 根据指定的规则创建异常。
     *
     * @param constraint 规则.
     */
    public CheckException(Constraint constraint) {
        this(constraint, () -> constraint.brief(), null);
    }

    /**
     * 根据指定的规则和规则描述创建异常。在异常中，{@code brief} 指定的规则描述将取代 {@code constraint}中的规则描述。
     *
     * @param constraint 规则。
     * @param brief      规则描述。
     */
    public CheckException(Constraint constraint, String brief) {
        this(constraint, brief, null);
    }

    /**
     * 根据指定的规则和规则描述创建异常。在异常中，{@code brief} 指定的规则描述将取代 {@code constraint}中的规则描述。
     *
     * @param constraint    规则。
     * @param briefSupplier 规则描述提供者。
     */
    public CheckException(Constraint constraint, Supplier<String> briefSupplier) {
        this(constraint, briefSupplier, null);
    }

    /**
     * 根据指定的规则和异常创建异常，参数{@code cause}指定的异常将成为新异常因由异常。
     *
     * @param constraint 规则。
     * @param cause      出错原因。
     */
    public CheckException(Constraint constraint, Throwable cause) {
        this(constraint, () -> constraint.brief(), cause);
    }

    /**
     * 根据指定的规则、规则描述和异常创建异常，参数{@code cause}指定的异常将成为新异常因由异常。
     *
     * @param constraint 规则。
     * @param brief      规则描述。
     * @param cause      出错原因。
     */
    public CheckException(Constraint constraint, String brief, Throwable cause) {
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
    public CheckException(Constraint constraint, Supplier<String> briefSupplier, Throwable cause) {
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
        Objects.requireNonNull(constraint, "检查约束不能为null");
        return String.format("%s -> %s", constraint.code(), brief != null ? brief : constraint.brief());
    }

    /**
     * 从指定的异常的级联层次上查找类型为BaseException异常，如果找到则返回找到的异常，否则返回<code>null</code>。
     *
     * @param ex 异常堆栈的最上层。
     * @return 找到的目标异常，或<code>null</code>，如果没有找到。
     */
    public static CheckException parse(Exception ex) {
        return parse(ex, CheckException.class);
    }

    @SuppressWarnings("unchecked")
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

    public static class InvalidationHandler extends AbstractInvalidationHandler {
        @Override
        protected void doHandle(Constraint constraint, Throwable cause) {
            Objects.requireNonNull(constraint, "检查约束不能为null");
            throw new CheckException(constraint, cause);
        }
    }
}
