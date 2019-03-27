# DiffLibrary

#### 项目介绍
使用DiffUtil增量刷新RecyclerView

#### 软件架构
软件架构说明


#### 使用说明

1. 编写适配器继承DiffBaseAdapter<T,H>,T为 item数据model  H 为 item Viewholder
2. 编写item的diff条件及构建payloads,继承BaseDiff<T> T为item数据model
3. 通过DiffUtil.calculate(DiffUtil.callback)计算刷新前和刷新后的数据差异
4. 调用DiffResult.dispatchTo(adapter) 方法刷新item


本库已去除使用DiffUtil过程中遇到的几乎所有坑（刷新数据错位等）,如需要设置多个HeadView、footView 请自行扩展。

后期不定期跟新内容
1 提供item数据变化后的几种常用动画
