package me.szlxty.check.system;

import me.szlxty.check.Constraint;

public interface InvalidationHandler {
    /**
     * 根据指定的信息创建异常。
     *
     * @param constraint 约束，不能为null。
     * @return 新创建的异常。
     */
    void handle(Constraint constraint);

    /**
     * 根据指定的信息创建异常。当brief不为null并且与error的描述信息存在差异时，将使用根据它们创建的新约束创建异常。
     *
     * @param constraint 约束，不能为null。
     * @param brief      约束描述。
     * @return 新创建的异常。
     */
    void handle(Constraint constraint, String brief);

    /**
     * 根据指定的信息创建异常。
     *
     * @param constraint 约束，不能为null。
     * @param cause      出错原因。
     * @return 新创建的异常。
     */
    void handle(Constraint constraint, Throwable cause);

    /**
     * 根据指定的信息创建异常。当brief不为null并且与error的描述信息存在差异时，将使用根据它们创建的新约束创建异常。
     *
     * @param constraint 约束，不能为null。
     * @param brief      约束描述。
     * @param cause      出错原因。
     * @return 新创建的异常。
     */
    void handle(Constraint constraint, String brief, Throwable cause);
}
