package com.jonex.dss.cluster.loadbalance;

import com.jonex.dss.common.Named;
import com.jonex.dss.rpc.Provider;
import com.jonex.dss.rpc.Supplier;

import java.util.List;

/**
 * <pre>
 *
 *  File: AbstractLoadBalance.java
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
public abstract class AbstractLoadBalance<T> extends Named implements LoadBalance<T>{

    public AbstractLoadBalance() {
        super("");
    }

    public AbstractLoadBalance(String str) {
        super(str);
    }


    @Override
    public <T> Provider<T> select(List<Provider<T>> providerList, Supplier supplier) {
        if (providerList != null && providerList.size() != 0) {
            return (providerList.size()==1) ? providerList.get(0) : this.doSelect(providerList, supplier);
        }
        return null;
    }

    protected abstract <T> Provider<T> doSelect(List<Provider<T>> providerList, Supplier supplier);

    protected int getWeight(Provider provider, Supplier supplier){

        return 0;
    }

}
