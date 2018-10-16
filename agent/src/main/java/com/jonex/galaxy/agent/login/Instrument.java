package com.jonex.galaxy.agent.login;

import java.lang.instrument.Instrumentation;

/**
 * Created by xubai on 2018/10/15 下午5:11.
 */
public class Instrument {

    public static void premain(String agentArgs, Instrumentation inst){
        if (agentArgs != null){
            inst.addTransformer(new PerformanceMetric(agentArgs));
        }else {
            System.out.println("Usage: java -javaagent:Instrument.jar=[class:method]");
            System.exit(0);
        }
    }

}
