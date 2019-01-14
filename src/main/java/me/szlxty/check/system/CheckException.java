package me.szlxty.check.system;

import me.szlxty.check.Constraint;
import me.szlxty.core.BaseException;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * 监测触发的异常
 */
public class CheckException extends BaseException {

    public CheckException(Supplier<String> codeOrBriefSupplier) {
        super(codeOrBriefSupplier);
    }

    public CheckException(String codeOrBrief) {
        super(codeOrBrief);
    }

    public CheckException(Supplier<String> codeOrBriefSupplier, Throwable cause) {
        super(codeOrBriefSupplier, cause);
    }

    public CheckException(String codeOrBrief, Throwable cause) {
        super(codeOrBrief, cause);
    }

    public CheckException(Constraint constraint) {
        super(constraint);
    }

    public CheckException(Constraint constraint, String brief) {
        super(constraint, brief);
    }

    public CheckException(Constraint constraint, Supplier<String> briefSupplier) {
        super(constraint, briefSupplier);
    }

    public CheckException(Constraint constraint, Throwable cause) {
        super(constraint, cause);
    }

    public CheckException(Constraint constraint, String brief, Throwable cause) {
        super(constraint, brief, cause);
    }

    public CheckException(Constraint constraint, Supplier<String> briefSupplier, Throwable cause) {
        super(constraint, briefSupplier, cause);
    }

    public static class InvalidationHandler extends AbstractInvalidationHandler {
        @Override
        protected void doHandle(Constraint constraint, Throwable cause) {
            Objects.requireNonNull(constraint, "检查的约束不能为null");
            throw new CheckException(constraint, cause);
        }
    }
}
