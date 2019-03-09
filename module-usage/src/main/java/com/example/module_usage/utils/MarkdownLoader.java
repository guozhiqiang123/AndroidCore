package com.example.module_usage.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gzq.lib_core.base.Box;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class MarkdownLoader {
    private static MarkdownLoader markdownLoader;

    public interface OnMarkdownTextLoaded {
        void apply(String text);
    }

    public static MarkdownLoader instance() {
        if (markdownLoader == null) {
            markdownLoader = new MarkdownLoader();
        }
        return markdownLoader;
    }


    public void load(String nameMD, OnMarkdownTextLoaded loaded) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext(loadLocalMD(nameMD));
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        loaded.apply(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private String loadLocalMD(String nameMD) throws IOException {
        if (!nameMD.endsWith(".md")) {
            throw new IllegalArgumentException("请输入完整MarkDown文件的名称");
        }
        InputStream stream = Box.getApp().getAssets().open(nameMD);
        return readStream(stream);
    }

    private static String readStream(@Nullable InputStream inputStream) {
        String out = null;

        if (inputStream != null) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                final StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line)
                            .append('\n');
                }
                out = builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        // no op
                    }
                }
            }
        }

        return out;
    }

}
