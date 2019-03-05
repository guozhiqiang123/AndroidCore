#自己开发的库的混淆

#被使用反射的类
-keep class com.gzq.lib_core.base.delegate.ActivityDetegate{*;}
-keep class com.gzq.lib_core.base.delegate.AppDelegate{*;}
-keep class com.gzq.lib_core.base.delegate.FragmentDetegate{*;}
-keep class com.gzq.lib_core.base.quality.QualityActivity{*;}
-keep class com.gzq.lib_core.base.quality.QualityBlockCanary{*;}
-keep class com.gzq.lib_core.base.quality.QualityFragment{*;}
-keep class com.gzq.lib_core.base.Box{*;}