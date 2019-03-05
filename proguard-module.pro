#项目中一些不需要混淆的实体和其他

#实体类
-keep class com.gzq.lib_resource.bean.**{*;}
-keep class com.gzq.lib_resource.api.**{*;}
-keep class com.gcml.auth.face2.model.entity.**{*;}
-keep class com.gcml.module_guardianship.bean.**{*;}
-keep class com.gcml.module_health_manager.bean.**{*;}
-keep class com.gcml.module_login_and_register.bean.**{*;}
-keep class com.gcml.module_mine.bean.**{*;}
-keep class com.gcml.module_shouhuan.bean.**{*;}


#保持被反射类不被混淆
-keep class com.gzq.lib_resource.app.AppStore{*;}
-keep class com.gzq.lib_resource.GlobalConfiguration{*;}