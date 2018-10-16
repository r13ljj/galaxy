package com.jonex.galaxy.agent;

import java.util.Random;

/**
 * <pre>
 *
 *  File: Shop.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/6/13				lijunjun				Initial.
 *
 * </pre>
 */
public class Shop {

    private static final Random RANDOM = new Random();

    public double getProductPrice(String product){
        //return RANDOM.nextDouble() * product.charAt(0) * (product.length() > 1 ? product.charAt(1) : 1d);
        return 999.8d;
    }

}
