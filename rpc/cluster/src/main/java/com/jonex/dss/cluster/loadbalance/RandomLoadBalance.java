package com.jonex.dss.cluster.loadbalance;

import com.jonex.dss.rpc.Provider;
import com.jonex.dss.rpc.Supplier;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <pre>
 *
 *  File: RandomLoadBalance.java
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
public class RandomLoadBalance extends AbstractLoadBalance {

    public final static String NAME = "random";

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    public RandomLoadBalance() {
        super(NAME);
    }

    @Override
    protected Provider doSelect(List<Provider> providerList, Supplier supplier) {
        int length = providerList.size();
        int totalWeight = 0;
        boolean sameWeight = true;

        int offset;
        int w;
        for(offset=0; offset < length; ++offset){
            w = super.getWeight(providerList.get(offset), supplier);
            totalWeight += w;
            if (sameWeight
                    && offset > 0
                    && w != super.getWeight(providerList.get(offset-1), supplier)){
                sameWeight = false;
            }
        }
        if (totalWeight > 0 && !sameWeight){
            offset = this.random.nextInt(totalWeight);
            for(w=0; w<length; w++){
                offset -= super.getWeight(providerList.get(w), supplier);
                //if weight greate than random
                if (offset < 0){
                    return providerList.get(w);
                }
            }
        }
        return providerList.get(random.nextInt(length));
    }
}
