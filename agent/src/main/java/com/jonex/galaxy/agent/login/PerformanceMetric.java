package com.jonex.galaxy.agent.login;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * Created by xubai on 2018/10/15 下午5:15.
 */
public class PerformanceMetric implements ClassFileTransformer {

    private String methodName;

    public PerformanceMetric(String methodName) {
        this.methodName = methodName;
        System.out.println("PerformanceMetric constructor:"+methodName);
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        //Canonical: com/jonex/galaxy/agent/login/UserService
        String canonicalClassName = className.replaceAll("/", ".");
        if (!"com.jonex.galaxy.agent.login.UserService".equals(canonicalClassName)){
            return null;
        }
        System.out.println("111111111");
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = null;
        byte[] byteCode = null;
        try {
            //ctClass = classPool.get(className);
            ctClass = classPool.get(canonicalClassName);
        } catch (Exception e) {
            System.out.println("eeeeee1");
            classPool.insertClassPath(new LoaderClassPath(loader));
            try {
                ctClass = classPool.get(className);
            } catch (Exception e1) {
                System.out.println("eeeeee2");
                e1.printStackTrace();
            }
        }
        System.out.println("2222222 loader:"+loader+" className:"+className);
        if (ctClass != null){
            System.out.println("ctClass:"+ctClass);
            if (!ctClass.isInterface()){
                CtMethod[] methods = ctClass.getDeclaredMethods();
                if (methods != null && methods.length > 0){
                    System.out.println("3333333");
                    for (CtMethod method : methods) {
                        if (methodName.equals(method.getName())){
                            try {
                                method.addLocalVariable("time", CtClass.longType);
                                method.insertBefore("time = System.currentTimeMillis();");
                                method.insertAfter("time = System.currentTimeMillis() - time;");
                                method.insertAfter("System.out.println(\""+methodName+"函数耗时：\"+time+\"ms\");");
                            } catch (CannotCompileException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        System.out.println("444444444");
                        byteCode = ctClass.toBytecode();
                        return byteCode;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (CannotCompileException e) {
                        e.printStackTrace();
                    }
                }
            }
            ctClass.detach();
        }
        return null;
    }
}
