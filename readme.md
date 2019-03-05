# 项目组件化、快速开发实践

2018年Android最火的是什么？当然属于组件化。组件化是个老生常谈的问题了，但是2018年却异常火爆，各种思想，框架，工具铺天盖地。趁着这波热度，我司的项目也逐渐过渡到组件化的开发。     
尝到了组件化开发的甜头，以后新的项目肯定也是朝这个方向规划的，所以这个组件化、可快速开发的基础框应运而生。    
组件化的优点很明显：①项目结构层次更加清晰；②方便多人协同开发；③方便单模块调试、测试     
废话说这么多就够了，咱们直接进入主题。   
## 核心功能
- 使用meta-data+反射的方式实现Application、Activity、Fragment生命周期的分发
- 使用建造者模式实现全局工具的统一配置（比如：网络baseUrl、UI适配设置、retrofit配置、数据库Room配置等）
- 使用ARetrofit作为应用的路由框架，简单易用，可完全取代原生用法
- 集成了LeakCanary、BlockCanary,自动监测内存泄漏、耗时代码；
- Retrofit更加内敛、更加方便使用的封装（支持配置Room进行缓存、支持简单的上传下载）
- 用户信息从登陆到退出程序的全局管理（一键更新，全局使用，处处可监听变化）
- 崩溃信息的捕获，方便脱机调试

## 项目结构
![](https://github.com/guozhiqiang123/AndroidCore/blob/master/img/structure_chart.png)
lib-core:集成了上面的核心功能，除特殊情况外，开发者不应该修改；   
lib-resource:
- 主要是放置公用的实体bean、代理接口、xml资源、图片资源、自定义的view、公用工具类等;
- 引用的第三方库或者是集成的SDK都应该放在这里；   
- app与module、module与module的通信是通过ARetrofit进行的（建议舍弃原生的startActivity和startActivityForResult）；
- app模块依赖其他module的时候应该使用addComponent，而不是implementation或者api(该功能对于渐进式改造老项目奇效)

