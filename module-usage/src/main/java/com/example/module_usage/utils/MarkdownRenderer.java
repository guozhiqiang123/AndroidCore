package com.example.module_usage.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.module_usage.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import ru.noties.markwon.Markwon;
import ru.noties.markwon.SpannableConfiguration;
import ru.noties.markwon.UrlProcessor;
import ru.noties.markwon.UrlProcessorRelativeToAbsolute;
import ru.noties.markwon.il.AsyncDrawableLoader;
import ru.noties.markwon.spans.AsyncDrawable;
import ru.noties.markwon.spans.SpannableTheme;
import ru.noties.markwon.syntax.Prism4jSyntaxHighlight;
import ru.noties.markwon.syntax.Prism4jTheme;
import ru.noties.markwon.syntax.Prism4jThemeDarkula;
import ru.noties.markwon.syntax.Prism4jThemeDefault;
import ru.noties.prism4j.Prism4j;

public class MarkdownRenderer {

    public interface MarkdownReadyListener {
        void onMarkdownReady(CharSequence markdown);
    }

    AsyncDrawable.Loader loader;


    Prism4j prism4j;

    Prism4jThemeDefault prism4jThemeDefault;

    Prism4jThemeDarkula prism4JThemeDarkula;

    private Future<?> task;
    private static MarkdownRenderer markdownRenderer;

    public static MarkdownRenderer instance() {
        if (markdownRenderer == null) {
            markdownRenderer = new MarkdownRenderer();
        }
        return markdownRenderer;
    }

    MarkdownRenderer() {
        loader = AsyncDrawableLoader.create();
        prism4j = new Prism4j(new GrammarLocatorDef());
        prism4jThemeDefault = new Prism4jThemeDefault();
        prism4JThemeDarkula = new Prism4jThemeDarkula();
    }

    public void render(
            @NonNull final Context context,
            final boolean isLightTheme,
            @Nullable final Uri uri,
            @NonNull final String markdown,
            @NonNull final MarkdownReadyListener listener) {

        Observable.create(new ObservableOnSubscribe<CharSequence>() {
            @Override
            public void subscribe(ObservableEmitter<CharSequence> emitter) throws Exception {
                final UrlProcessor urlProcessor;
                if (uri == null) {
                    urlProcessor = new UrlProcessorInitialReadme();
                } else {
                    urlProcessor = new UrlProcessorRelativeToAbsolute(uri.toString());
                }

                final Prism4jTheme prism4jTheme = isLightTheme
                        ? prism4jThemeDefault
                        : prism4JThemeDarkula;

                final int background = isLightTheme
                        ? prism4jTheme.background()
                        : 0x0Fffffff;

                final GifPlaceholder gifPlaceholder = new GifPlaceholder(
                        context.getResources().getDrawable(R.drawable.ic_play_circle_filled_18dp_white),
                        0x20000000
                );

                final SpannableConfiguration configuration = SpannableConfiguration.builder(context)
                        .asyncDrawableLoader(loader)
                        .urlProcessor(urlProcessor)
                        .syntaxHighlight(Prism4jSyntaxHighlight.create(prism4j, prism4jTheme))
                        .theme(SpannableTheme.builderWithDefaults(context)
                                .codeBackgroundColor(background)
                                .codeTextColor(prism4jTheme.textColor())
                                .build())
                        .factory(new GifAwareSpannableFactory(gifPlaceholder))
                        .build();
                final CharSequence text = Markwon.markdown(configuration, markdown);
                emitter.onNext(text);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<CharSequence>() {
                    @Override
                    public void onNext(CharSequence o) {
                        listener.onMarkdownReady(o);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

}
