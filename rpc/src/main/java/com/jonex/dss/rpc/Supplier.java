package com.jonex.dss.rpc;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * <pre>
 *
 *  File: Supplier.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/3/23				lijunjun				Initial.
 *
 * </pre>
 */
public interface Supplier {

    String getMethodName();

    Method getMethod();

    Class<?>[] getParameterTypes();

    Object[] getParameters();

    Map<String, String> getAttachments();

    String getAttachment(String var1);

    String getAttachment(String var1, String var2);

    Provider<?> getProvider();

}
