package com.gzq.androidcore.router;

import com.sjtu.yifei.annotation.Go;

public interface AppRouterApi {
    @Go("/usage/recycleview/morestyle")
    boolean skipRecycleViewMoreStyleActivity();

    @Go("/usage/xaop/activity")
    boolean skipUsageXAOPActivity();
}
