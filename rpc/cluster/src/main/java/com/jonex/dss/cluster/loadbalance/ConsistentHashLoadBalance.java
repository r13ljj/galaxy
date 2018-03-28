package com.jonex.dss.cluster.loadbalance;

import com.jonex.dss.rpc.Provider;
import com.jonex.dss.rpc.Supplier;

import java.util.List;

/**
 * Created by xubai on 2018/03/26 上午1:08.
 */
public class ConsistentHashLoadBalance extends AbstractLoadBalance {

    @Override
    protected Provider doSelect(List list, Supplier supplier) {
        return null;
    }
}
