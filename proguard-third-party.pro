#第三方库的混淆

# >>>>>>>>>>>>>>>>>>>>>>>>  Facebook Fresco  Start >>>>>>>>>>>>>>>>>>>>>>>>
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**

-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
# >>>>>>>>>>>>>>>>>>>>>>>>  Facebook Fresco  End >>>>>>>>>>>>>>>>>>>>>>>>



# >>>>>>>>>>>>>>>>>>>>>>>>  Glide  Start >>>>>>>>>>>>>>>>>>>>>>>>
# Glide specific rules #
# https://github.com/bumptech/glide
-dontwarn  com.bumptech.**
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
# >>>>>>>>>>>>>>>>>>>>>>>>  Glide  End >>>>>>>>>>>>>>>>>>>>>>>>


# >>>>>>>>>>>>>>>>>>>>>>>>  Picasso  Start >>>>>>>>>>>>>>>>>>>>>>>>
## Square Picasso specific rules ##
## https://square.github.io/picasso/ ##
-dontwarn com.squareup.okhttp.**
# >>>>>>>>>>>>>>>>>>>>>>>>  Picasso  End >>>>>>>>>>>>>>>>>>>>>>>>


# >>>>>>>>>>>>>>>>>>>>>>>>  Jackson  Start >>>>>>>>>>>>>>>>>>>>>>>>
-dontwarn com.fasterxml.jackson.databind.**
-keep class com.fasterxml.jackson.core.** {*;}
-keep interface com.fasterxml.jackson.core { *; }
-keep public class * extends com.fasterxml.jackson.core.**
-keep class com.fasterxml.jackson.databind.introspect.VisibilityChecker$Std.<clinit>
-keep class com.fasterxml.jackson.databind.ObjectMapper.<clinit>
-keep class com.fasterxml.jackson.databind.** {*;}
-keep class com.fasterxml.jackson.databind.introspect.VisibilityChecker$*{*;}
-keep interface com.fasterxml.jackson.databind { *; }
-keep public class * extends com.fasterxml.jackson.databind.**
# >>>>>>>>>>>>>>>>>>>>>>>>  Jackson  End >>>>>>>>>>>>>>>>>>>>>>>>


# >>>>>>>>>>>>>>>>>>>>>>>>  GSON  Start >>>>>>>>>>>>>>>>>>>>>>>>
## GSON 2.2.4 specific rules ##
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

-keepattributes EnclosingMethod

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.**{*;}
-keep class com.google.gson.examples.android.model.** { *; }
# >>>>>>>>>>>>>>>>>>>>>>>>  GSON  End >>>>>>>>>>>>>>>>>>>>>>>>

# >>>>>>>>>>>>>>>>>>>>>>>>  zxing  Start >>>>>>>>>>>>>>>>>>>>>>>>
-dontwarn com.google.zxing.**
-dontwarn com.google.zxing.client.android.**
-keep  class com.google.zxing.**{*;}
# >>>>>>>>>>>>>>>>>>>>>>>>  zxing  End >>>>>>>>>>>>>>>>>>>>>>>>



# >>>>>>>>>>>>>>>>>>>>>>>>  EventBus 2  Start >>>>>>>>>>>>>>>>>>>>>>>>
## GreenRobot EventBus specific rules ##
# https://github.com/greenrobot/EventBus/blob/master/HOWTO.md#proguard-configuration

-keepclassmembers class ** {
    public void onEvent*(***);
}

# Only required if you use AsyncExecutor
-keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
    public <init>(java.lang.Throwable);
}

# Don't warn for missing support classes
-dontwarn de.greenrobot.event.util.*$Support
-dontwarn de.greenrobot.event.util.*$SupportManagerFragment
# >>>>>>>>>>>>>>>>>>>>>>>>  EventBus 2  End >>>>>>>>>>>>>>>>>>>>>>>>



# >>>>>>>>>>>>>>>>>>>>>>>>  EventBus 3  Start >>>>>>>>>>>>>>>>>>>>>>>>
## New rules for EventBus 3.0.x ##
# http://greenrobot.org/eventbus/documentation/proguard/

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
# >>>>>>>>>>>>>>>>>>>>>>>>  EventBus 3  End >>>>>>>>>>>>>>>>>>>>>>>>


# >>>>>>>>>>>>>>>>>>>>>>>>  Rxjava  Start >>>>>>>>>>>>>>>>>>>>>>>>
-dontwarn sun.misc.**
-dontwarn org.apache.http.**

-keep class rx.**.

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
# >>>>>>>>>>>>>>>>>>>>>>>>  Rxjava  End >>>>>>>>>>>>>>>>>>>>>>>>


# >>>>>>>>>>>>>>>>>>>>>>>>  OkHttp  Start >>>>>>>>>>>>>>>>>>>>>>>>
# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

# OkHttp3
-dontwarn okhttp3.**
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**
-keep class okio.** {*;}
# >>>>>>>>>>>>>>>>>>>>>>>>  OkHttp  End >>>>>>>>>>>>>>>>>>>>>>>>


# >>>>>>>>>>>>>>>>>>>>>>>>  Retrofit2  Start >>>>>>>>>>>>>>>>>>>>>>>>
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-dontwarn retrofit2.adapter.**

# Retrofit 2.X
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# >>>>>>>>>>>>>>>>>>>>>>>>  Retrofit2  End >>>>>>>>>>>>>>>>>>>>>>>>


# >>>>>>>>>>>>>>>>>>>>>>>>  Ormlite  Start >>>>>>>>>>>>>>>>>>>>>>>>
-keep class com.j256.ormlite.** {*;}
# >>>>>>>>>>>>>>>>>>>>>>>>  Ormlite  End >>>>>>>>>>>>>>>>>>>>>>>>


# >>>>>>>>>>>>>>>>>>>>>>>>  Butterknife  Start >>>>>>>>>>>>>>>>>>>>>>>>
# ButterKnife 7
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
# >>>>>>>>>>>>>>>>>>>>>>>>  Butterknife  End >>>>>>>>>>>>>>>>>>>>>>>>



# >>>>>>>>>>>>>>>>>>>>>>>>  热修复框架rocoofix  Start >>>>>>>>>>>>>>>>>>>>>>>>
-keep class com.dodola.rocoofix.** {*;}
-keep class com.lody.legend.** {*;}
# >>>>>>>>>>>>>>>>>>>>>>>>  热修复框架rocoofix  End >>>>>>>>>>>>>>>>>>>>>>>>


# >>>>>>>>>>>>>>>>>>>>>>>>  数据库加密不混淆  Start >>>>>>>>>>>>>>>>>>>>>>>>
-dontwarn com.google.common.**
-keep  class net.sqlcipher.** {*;}
-keep  class net.sqlcipher.database.** {*;}
# >>>>>>>>>>>>>>>>>>>>>>>>  数据库加密不混淆  End >>>>>>>>>>>>>>>>>>>>>>>>



# >>>>>>>>>>>>>>>>>>>>>>>> 七牛  Start >>>>>>>>>>>>>>>>>>>>>>>>
-keep class com.qiniu.** {*;}
-dontwarn com.qiniu.**
# >>>>>>>>>>>>>>>>>>>>>>>> 七牛  End >>>>>>>>>>>>>>>>>>>>>>>>


# >>>>>>>>>>>>>>>>>>>>>>>> UMeng大数据分析sdk混淆配置  Start >>>>>>>>>>>>>>>>>>>>>>>>
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-dontwarn org.apache.http.**

-keep class org.apache.http.** { *;}

-keep class com.umeng.analytics.** {*;}
-dontwarn com.umeng.analytics.**

# >>>>>>>>>>>>>>>>>>>>>>>> UMeng大数据分析sdk混淆配置  End >>>>>>>>>>>>>>>>>>>>>>>>


# >>>>>>>>>>>>>>>>>>>>>>>> 高德相关混淆文件  Start >>>>>>>>>>>>>>>>>>>>>>>>
-keep class com.amap.api.** {*;}
-keep class com.autonavi.** {*;}
-keep class com.a.a.** {*;}
-keep class com.loc.** {*;}
-dontwarn com.amap.api.**
-dontwarn com.autonavi.**
-dontwarn com.a.a.**
-dontwarn com.loc.**
# >>>>>>>>>>>>>>>>>>>>>>>> 高德相关混淆文件  End >>>>>>>>>>>>>>>>>>>>>>>>


# >>>>>>>>>>>>>>>>>>>>>>>> 百度定位  End >>>>>>>>>>>>>>>>>>>>>>>>
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**
# >>>>>>>>>>>>>>>>>>>>>>>> 百度定位  End >>>>>>>>>>>>>>>>>>>>>>>>


# >>>>>>>>>>>>>>>>>>>>>>>> pinyin4j  Start >>>>>>>>>>>>>>>>>>>>>>>>
-dontwarn net.soureceforge.pinyin4j.**
-dontwarn demo.**
-keep class net.sourceforge.pinyin4j.** { *;}
-keep class demo.** { *;}
-keep class com.hp.** { *;}
# >>>>>>>>>>>>>>>>>>>>>>>> pinyin4j  End >>>>>>>>>>>>>>>>>>>>>>>>

# >>>>>>>>>>>>>>>>>>>>>>>> httpclient  Start >>>>>>>>>>>>>>>>>>>>>>>>
-dontwarn android.net.compatibility.**
-dontwarn android.net.http.**
-dontwarn com.android.internal.http.multipart.**
-dontwarn org.apache.commons.**
-dontwarn org.apache.http.**
-dontwarn org.apache.http.protocol.**
-keep class android.net.compatibility.**{*;}
-keep class android.net.http.**{*;}
-keep class com.android.internal.http.multipart.**{*;}
-keep class org.apache.commons.**{*;}
-keep class org.apache.org.**{*;}
-keep class org.apache.harmony.**{*;}
# >>>>>>>>>>>>>>>>>>>>>>>> pinyin4j  End >>>>>>>>>>>>>>>>>>>>>>>>


-dontwarn net.sf.json.**
-keep class net.sf.json.** {*;}

# 动画库
-keep class com.nineoldandroids.** {*;}


#ARetrofit
-keep class * implements com.sjtu.yifei.annotation.InjectContract{*;}

#XAOP
-keep @com.xuexiang.xaop.annotation.* class * {*;}
-keep class * {
    @com.xuexiang.xaop.annotation.* <fields>;
}
-keepclassmembers class * {
    @com.xuexiang.xaop.annotation.* <methods>;
}