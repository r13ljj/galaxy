package com.jonex.dss.common;

/**
 * <pre>
 *
 *  File: Named.java
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
public abstract class Named {

    private String name;

    public Named(String str) {
        this.name = str;
    }

    public String getName(){
        return name;
    }
}
