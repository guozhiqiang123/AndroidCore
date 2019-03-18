package com.gzq.lib_resource.router;

import com.sjtu.yifei.annotation.Extra;
import com.sjtu.yifei.annotation.Go;
import com.sjtu.yifei.route.ActivityCallback;

public interface CommonRouterApi {
    @Go("/app/main/activity")
    boolean skipMainActivity();

    @Go("/login/register/login/activity")
    boolean skipLoginActivity();

    @Go("/login/register/login/activity")
    boolean skipLoginActivity(@Extra ActivityCallback callback);

    @Go("/markdown/recycleview/singlestyle")
    boolean skipRecycleViewSingleStyleActivity();
}
