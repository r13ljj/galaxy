package com.jonex.dss.cluster.loadbalance;

import com.jonex.dss.rpc.Provider;
import com.jonex.dss.rpc.Supplier;

import java.util.List;

/**
 * Created by xubai on 2018/03/26 上午1:06.
 */
public class RoundRobinLoadBalance extends AbstractLoadBalance{

    @Override
    protected Provider doSelect(List<Provider> providerList, Supplier supplier) {
        return null;
    }
}
