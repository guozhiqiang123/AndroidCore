package com.gzq.lib_resource.router;

import com.sjtu.yifei.annotation.Go;

public interface CommonRouterApi {
    @Go("/app/main/activity")
    boolean skipMainActivity();

    @Go("/login/register/login/activity")
    boolean skipLoginActivity();
    @Go("/markdown/recycleview/singlestyle")
    boolean skipRecycleViewSingleStyleActivity();
}
