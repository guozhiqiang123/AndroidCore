package com.example.module_usage;

import android.content.Intent;
import android.os.Bundle;

import com.example.module_usage.databinding.ActivityRecycleviewSinglestyleBinding;
import com.example.module_usage.utils.MarkdownLoader;
import com.example.module_usage.utils.MarkdownRenderer;
import com.gzq.lib_resource.mvvm.base.BaseActivity;
import com.sjtu.yifei.annotation.Route;

import ru.noties.markwon.Markwon;

@Route(path = "/markdown/recycleview/singlestyle")
public class RecycleViewSingleStyleActivity extends BaseActivity<ActivityRecycleviewSinglestyleBinding, RecycleViewSingleModel> {
    @Override
    public void initParam(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_recycleview_singlestyle;
    }

    @Override
    public RecycleViewSingleModel setViewModel(ActivityRecycleviewSinglestyleBinding binding) {
        RecycleViewSingleModel vm = new RecycleViewSingleModel();
        binding.setVm(vm);
        return vm;
    }

    @Override
    public void setOtherModel(ActivityRecycleviewSinglestyleBinding binding) {
        MarkdownLoader.instance()
                .load("RecycleView-SingleStyle.md", new MarkdownLoader.OnMarkdownTextLoaded() {
                    @Override
                    public void apply(String text) {
                        MarkdownRenderer.instance().render(RecycleViewSingleStyleActivity.this, true, null, text, new MarkdownRenderer.MarkdownReadyListener() {
                            @Override
                            public void onMarkdownReady(CharSequence markdown) {git
                                Markwon.setText(binding.content, markdown);
                            }
                        });
                    }
                });
    }
}
