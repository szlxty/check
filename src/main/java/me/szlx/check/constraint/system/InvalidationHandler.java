package me.szlx.check.constraint.system;

import me.szlx.check.constraint.Constraint;

public interface InvalidationHandler {
    /**
     * 根据指定的信息创建异常。
     *
     * @param constraint 约束，不能为null。
     */
    void handle(Constraint constraint);

    /**
     * 根据指定的信息创建异常。当brief不为null并且与error的描述信息存在差异时，将使用根据它们创建的新约束创建异常。
     *
     * @param constraint 约束，不能为null。
     * @param brief      约束描述。
     */
    void handle(Constraint constraint, String brief);

    /**
     * 根据指定的信息创建异常。
     *
     * @param constraint 约束，不能为null。
     * @param cause      出错原因。
     */
    void handle(Constraint constraint, Throwable cause);

    /**
     * 根据指定的信息创建异常。当brief不为null并且与error的描述信息存在差异时，将使用根据它们创建的新约束创建异常。
     *
     * @param constraint 约束，不能为null。
     * @param brief      约束描述。
     * @param cause      出错原因。
     */
    void handle(Constraint constraint, String brief, Throwable cause);
}
