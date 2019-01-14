package me.szlxty.check.bundle;

import me.szlxty.check.Constraint;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 扫描路径下的约束类，并自动装载。
 */
public class ImplementBundle extends AbstractBundle {
    private Logger logger = LoggerFactory.getLogger(ImplementBundle.class);
    private String delimiter = "[\\s,]+";
    private String scanPackage;

    public static ConstraintBundle from(String constraintDefinitionPackage) {
        ImplementBundle constraintBundle = new ImplementBundle();
        constraintBundle.setScanPackage(constraintDefinitionPackage);
        return constraintBundle;
    }

    @Override
    public List<Constraint> getConstraints() {
        List<Constraint> constraintList = new ArrayList<>();
        for (Class<? extends Constraint> klass : getAllConstraintClass()) {
            if (klass.isEnum()) {
                constraintList.addAll(Arrays.asList(klass.getEnumConstants()));
                logger.debug("搜集到约束描述类：{}", klass.getCanonicalName());
            } else {
                try {
                    Constraint theConstraint = klass.newInstance();
                    constraintList.add(theConstraint);
                    logger.debug("搜集到约束描述类：{}", klass.getCanonicalName());
                } catch (Exception e) {
                }
            }
        }
        return constraintList;
    }

    /**
     * 设置扫描的包路径，可以一次性设置多个包路径，多个包路径的分隔以{@link #delimiter}的正则表达式分隔。
     *
     * @param scanPackage 扫描的包路径。
     */
    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    private Set<Class<? extends Constraint>> getAllConstraintClass() {
        Set<String> packageNameSet = getPackages();
        Set<Class<? extends Constraint>> constraintClassSet = new HashSet<>();
        if (packageNameSet.contains("")) {
            constraintClassSet.addAll(getClasses(""));
        } else {
            for (String packageName : packageNameSet) {
                constraintClassSet.addAll(getClasses(packageName));
            }
        }
        return constraintClassSet;
    }

    private Set<String> getPackages() {
        if (scanPackage == null) {
            return Collections.singleton("");
        }
        String[] packages = Pattern.compile(delimiter).split(scanPackage);
        List<String> packageList = Arrays.asList(packages);
        return packageList.contains("") ? Collections.singleton("") : new HashSet<>(packageList);
    }

    @SuppressWarnings("unchecked")
    private static List<Class<? extends Constraint>> getClasses(final String pkg) {
        try (ScanResult scanResult = new ClassGraph()
                .verbose()                   // Log to stderr
                .enableAllInfo()             // Scan classes, methods, fields, annotations
                .whitelistPackages(pkg)      // Scan com.xyz and subpackages (omit to scan all packages)
                .scan()) {
            return scanResult.getClassesImplementing(Constraint.class.getCanonicalName())
                    .stream()
                    .map(classInfo -> classInfo.loadClass(Constraint.class, true))
                    .collect(Collectors.toList());
        }
    }

    /**
     * 获取多个扫描包路径的分隔符(正则表达式)。默认值为{@code [\s,]}
     *
     * @return 扫描包路径的分隔符(正则表达式)。
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * 设置多个扫描包路径的分隔符(正则表达式)。
     *
     * @param delimiter 待设置的多个扫描包路径的分隔符(正则表达式)。
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
