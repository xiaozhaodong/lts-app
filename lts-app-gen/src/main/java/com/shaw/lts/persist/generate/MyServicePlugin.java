package com.shaw.lts.persist.generate;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * MyServicePlugin
 * 自动生成 Service
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/6/15 16:20
 **/
public class MyServicePlugin extends PluginAdapter {

    private static final String SERVICE_SUFFIX = "Service";
    private static final String SERVICE_IMPL_SUFFIX = "ServiceImpl";

    /**
     * 输出项目目录
     */
    private String targetDir;

    /**
     * service 输出包目录
     */
    private String serviceTargetPackage;

    /**
     * serviceImpl 输出包目录
     */
    private String serviceImplTargetPackage;

    /**
     * 基础父service 目录
     */
    private String superServiceTargetPackage;

    /**
     * 基础父serviceImpl 目录
     */
    private String superServiceImplTargetPackage;

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.targetDir = this.properties.getProperty("targetDir");
        this.serviceTargetPackage = this.properties.getProperty("serviceTargetPackage");
        this.serviceImplTargetPackage = this.properties.getProperty("serviceImplTargetPackage");
        this.superServiceTargetPackage = this.properties.getProperty("superServiceTargetPackage");
        this.superServiceImplTargetPackage = this.properties.getProperty("superServiceImplTargetPackage");
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> generatedFiles = new ArrayList<>();
        String entityName = introspectedTable.getTableConfiguration().getDomainObjectName();
        if (entityName == null) {
            entityName = convertToCamelCase(introspectedTable.getTableConfiguration().getTableName());
        }
        String remark = introspectedTable.getRemarks();
        String serviceName = entityName + SERVICE_SUFFIX;
        String serviceImplName = entityName + SERVICE_IMPL_SUFFIX;

        String servicePath = targetDir + "/" + serviceTargetPackage.replaceAll("\\.",
            "/") + "/" + serviceName + ".java";
        boolean result = isFileExists(servicePath);
        if (!result) {
            Interface serviceInterface = generateService(introspectedTable, entityName, remark,
                serviceName);
            generatedFiles.add(new GeneratedJavaFile(serviceInterface, targetDir, context.getJavaFormatter()));
        }
        String serviceImplPath = targetDir + "/" + serviceImplTargetPackage.replaceAll("\\.",
            "/") + "/" + serviceImplName + ".java";
        boolean exists = isFileExists(serviceImplPath);
        if (!exists) {
            TopLevelClass serviceImplClass = generateServiceImpl(introspectedTable, entityName,
                remark, serviceName, serviceImplName);
            generatedFiles.add(new GeneratedJavaFile(serviceImplClass, targetDir, context.getJavaFormatter()));
        }
        return generatedFiles;
    }

    private Interface generateService(IntrospectedTable introspectedTable, String entityName, String remark,
        String serviceName) {
        Interface serviceInterface = new Interface(serviceTargetPackage + "." + serviceName);
        serviceInterface.addImportedType(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
        serviceInterface.addImportedType(new FullyQualifiedJavaType(superServiceTargetPackage));
        serviceInterface.setVisibility(JavaVisibility.PUBLIC);
        serviceInterface.addJavaDocLine("/**");
        serviceInterface.addJavaDocLine(" * " + serviceName);
        serviceInterface.addJavaDocLine(" * " + remark + " Service");
        serviceInterface.addJavaDocLine(" * ");
        serviceInterface.addJavaDocLine(" * @author generate by xzd");
        serviceInterface.addJavaDocLine(" * @version 1.0.0");
        serviceInterface.addJavaDocLine(" * @date " + new Date());
        serviceInterface.addJavaDocLine("*/");
        String superInterface = superServiceTargetPackage.substring(superServiceTargetPackage.lastIndexOf(".") + 1);
        serviceInterface.addSuperInterface(new FullyQualifiedJavaType(superInterface + "<" + entityName + ">"));
        return serviceInterface;
    }

    private TopLevelClass generateServiceImpl(IntrospectedTable introspectedTable, String entityName, String remark,
        String serviceName, String serviceImplName) {
        TopLevelClass serviceImplClass = new TopLevelClass(serviceImplTargetPackage + "." + serviceImplName);
        serviceImplClass.addImportedType(new FullyQualifiedJavaType(serviceTargetPackage + "." + serviceName));
        serviceImplClass.addImportedType(new FullyQualifiedJavaType(superServiceImplTargetPackage));
        serviceImplClass.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Service"));
        serviceImplClass.addImportedType(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
        serviceImplClass.addAnnotation("@Service");
        serviceImplClass.setVisibility(JavaVisibility.PUBLIC);
        serviceImplClass.addJavaDocLine("/**");
        serviceImplClass.addJavaDocLine(" * " + serviceName);
        serviceImplClass.addJavaDocLine(" * " + remark + " Impl");
        serviceImplClass.addJavaDocLine(" * ");
        serviceImplClass.addJavaDocLine(" * @author generate by xzd");
        serviceImplClass.addJavaDocLine(" * @version 1.0.0");
        serviceImplClass.addJavaDocLine(" * @date " + new Date());
        serviceImplClass.addJavaDocLine("*/");
        String superImplClass = superServiceImplTargetPackage.substring(
            superServiceImplTargetPackage.lastIndexOf(".") + 1);
        serviceImplClass.setSuperClass(superImplClass + "<" + entityName + ">");
        serviceImplClass.addSuperInterface(new FullyQualifiedJavaType(serviceTargetPackage + "." + serviceName));
        return serviceImplClass;
    }

    private boolean isFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    private String convertToCamelCase(String input) {
        StringBuilder sb = new StringBuilder();
        boolean nextUpperCase = true;
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (currentChar == '_') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    sb.append(Character.toUpperCase(currentChar));
                    nextUpperCase = false;
                } else {
                    sb.append(Character.toLowerCase(currentChar));
                }
            }
        }
        return sb.toString();
    }
}
