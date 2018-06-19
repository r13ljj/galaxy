package com.jonex.galaxy.agent;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.jar.JarFile;

/**
 * java -javaagent:E:/workspace/galaxy/agent/target/agent-1.0-SNAPSHOT.jar=jonex GalaxyAgentTest
 *
 * Attach API 很简单，只有 2 个主要的类，都在 com.sun.tools.attach 包里面：
 * VirtualMachine 代表一个 Java 虚拟机，也就是程序需要监控的目标虚拟机，提供了 JVM 枚举，Attach 动作和 Detach 动作（Attach 动作的相反行为，从 JVM 上面解除一个代理）等等 ;
 * VirtualMachineDescriptor 则是一个描述虚拟机的容器类，配合 VirtualMachine 类完成各种功能。
 *
 */
public class GalaxyAgent {


    public static void agentmain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException {
        System.out.println("agentmain start, premain2:"+agentArgs);
        inst.addTransformer(new Transformer(), true);
        inst.retransformClasses(Shop.class);
        System.out.println("retransformClasses success");
    }

    public static void agentmain(String agentArgs){
        System.out.println("agentmain start, premain:"+agentArgs);
    }


    /*public static void premain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException, ClassNotFoundException {
        System.out.println("premain start, premain2:"+agentArgs);
        //inst.addTransformer(new Transformer(), true);
        ClassDefinition def = new ClassDefinition(Transformer.class, Transformer.getBytesFromFile(Transformer.classNumberReturns2));
        inst.redefineClasses(new ClassDefinition[] { def });
        System.out.println("redefineClasses success");
    }*/

    public static void premain(String agentArgs){
        System.out.println("premain start, premain:"+agentArgs);
    }


}
