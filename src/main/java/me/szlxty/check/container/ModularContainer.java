package me.szlxty.check.container;

import me.szlxty.check.Checkers;
import me.szlxty.check.Constraint;
import me.szlxty.check.bundle.ConstraintBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * <p>通过约束编码的分节处理，为约束管理提供模块化支持。</p>
 * <p>若约束在当前模块不存在时，则尝试在父级模块中寻找，并依次类推，直到找到为止。</p>
 */
public class ModularContainer implements ConstraintContainer {
    private static Logger logger = LoggerFactory.getLogger(ModularContainer.class);
    private final ConstraintContainer constraintContainer;

    public ModularContainer(ConstraintContainer constraintContainer) {
        Objects.requireNonNull(constraintContainer, "初始化ModularContainer时，指定的约束容器不能为null");

        boolean isModularContainer = ModularContainer.class.isAssignableFrom(constraintContainer.getClass());
        Checkers.invalidIf(isModularContainer, "初始化ModularContainer时，指定的约束容器已经是ModularContainer了");

        this.constraintContainer = constraintContainer;
    }

    @Override
    public void install(ConstraintBundle constraintBundle) {
        constraintContainer.install(constraintBundle);
    }

    @Override
    public void install(Constraint... constraints) {
        constraintContainer.install(constraints);
    }

    /**
     * <p>若约束在当前模块不存在时，则尝试在父级模块中寻找，并依次类推，直到找到为止。如果最终还是没有找到，则返回{@code null}</p>
     *
     * @param constraintCodeOrBrief 待查找的约束代码。
     * @param <T>                   待返回的约束类型。
     * @return 类型为<T>的约束实例，如果找到，否则返回<code>null</code>。
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Constraint> T find(String constraintCodeOrBrief) {
        Objects.requireNonNull(constraintCodeOrBrief, "检查约束代码或描述不能为null");

        String theCode = constraintCodeOrBrief;
        Constraint result = null;
        while (result == null && theCode != null) {
            result = constraintContainer.find(theCode);
            if (result == null) {
                logger.debug("当前约束名称空间未找到约束[{}]", theCode);
            }
            String oldCode = theCode;
            theCode = trimErrorCode(theCode);
            if (result == null) {
                if (theCode != null) {
                    logger.debug("尝试在其父约束名称空间寻找[{}]", theCode);
                } else {
                    logger.debug("未找到约束[{}]", oldCode);
                }
            }
        }
        return (T) result;
    }

    // a.b.c.X  >>>  a.b.X  >>>  a.X  >>>  X  >>>  null
    private String trimErrorCode(String errorCode) {
        String delimiter = ".";
        int realCodeIndex = errorCode.lastIndexOf(delimiter);
        int trimIndex = errorCode.lastIndexOf(delimiter, realCodeIndex - 1);

        if (realCodeIndex == -1) {
            return null;
        } else if (trimIndex == -1) {
            return errorCode.substring(realCodeIndex + delimiter.length());
        } else {
            return errorCode.substring(0, trimIndex) + errorCode.substring(realCodeIndex);
        }
    }
}
