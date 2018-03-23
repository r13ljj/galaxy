package com.jonex.dss.cluster.loadbalance;

import com.jonex.dss.common.network.URL;
import com.jonex.dss.rpc.Provider;
import com.jonex.dss.rpc.Supplier;

import java.util.List;

/**
 * <pre>
 *
 *  File: LoadBalance.java
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
public interface LoadBalance<T> {

    <T> Provider<T> select(List<Provider<T>> providerList, Supplier supplier);

}
