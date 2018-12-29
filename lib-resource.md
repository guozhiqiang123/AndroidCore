# Android项目架构探索 

## lib-resource
作用：多module开发的时候，难免会有一些资源文件或者接口会在多个模块中共同使用。这些文件如果在各自的module中
copy一份，不仅在版本迭代中不易维护，更重要的是会造成包体积的增大，所以这时候就需要我们用一个模块来管理这些公
共资源。lib-resource不仅集中了公用的资源，还封装了MVP、MVVM等模式的基础类，以及一些我在开发中自定义的一些View,
收集了一些常用的工具类。使用者没必要全部依赖进自己的项目，将需要的部分copy到自己的模块即可。

### 结构图
![ddd](/img/20181229143433.png)     
- app:初始化一些第三方库
- mvp
  - base:基础封装
  - StateBaseActivity/StateBaseFragment:封装了快速切换状态页的基础类
- mvvm    
  - base:基础封装
  - binding:自定义事件
- state_page:自定义各种状态页（需要自己维护）
- utils:项目中常用工具类
- widget:自定义控件
- GlobalConfiguration:全局配置类（建议全局唯一）

### 公共引用
![aaa](/img/20181229144801.png)

## 参考和感谢    
[MVVMHabit](https://github.com/goldze/MVVMHabit)   
[LoadSir](https://github.com/KingJA/LoadSir)
