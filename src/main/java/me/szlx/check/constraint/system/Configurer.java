package me.szlx.check.constraint.system;

import me.szlx.check.constraint.bundle.ConstraintBundle;
import me.szlx.check.constraint.container.ConstraintContainer;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * <p>配置全局出错管理容器。对象完成初始化之后，各属性的改变不在生效。</p>
 * <p>当 {@code constraintSystem != null} 时，则将忽略 {@code constraintContainer} 和 {@code invalidationHandler}。</p>
 * <p>当  {@code constraintSystem == null} 时，则将使用 {@code constraintContainer} 和 {@code invalidationHandler} 构造 {@code DefaultConstraintSystem} 实例对象。</p>
 */
public class Configurer {
    private ConstraintSystem constraintSystem;
    private ConstraintContainer constraintContainer;
    private InvalidationHandler invalidationHandler;
    private List<ConstraintBundle> bundleList;

    @PostConstruct
    public void configure() {
        ConstraintSystem system = getConstraintSystem();
        if (bundleList != null) {
            bundleList.forEach(bundle -> bundle.bindTo(system.getConstraintContainer()));
        }
        ConstraintSystem.Helper.unmount();
        ConstraintSystem.Helper.mount(system);
    }

    /**
     * 设置出错管理系统。当设置的出错管理系统不为 {@code null} 时，配置时将忽略属性 {@code constraintContainer} 和 {@code exceptionManager} 的值。
     *
     * @param constraintSystem 待设置的出错管理系统。
     */
    public void setConstraintSystem(ConstraintSystem constraintSystem) {
        this.constraintSystem = constraintSystem;
    }

    public void setConstraintContainer(ConstraintContainer constraintContainer) {
        this.constraintContainer = constraintContainer;
    }

    public void setInvalidationHandler(InvalidationHandler invalidationHandler) {
        this.invalidationHandler = invalidationHandler;
    }

    public void setBundleList(List<ConstraintBundle> bundleList) {
        this.bundleList = bundleList;
    }

    private ConstraintSystem getConstraintSystem() {
        if (constraintSystem == null) {
            constraintSystem = new DefaultConstraintSystem(constraintContainer, invalidationHandler);
        }
        return constraintSystem;
    }
}
