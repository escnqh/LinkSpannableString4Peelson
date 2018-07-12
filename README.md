# LinkSpannableString4Peelson

#### 需求定义
1. 文本中超链接跳转
    - H5网址
    - 图片
    - Activity
    - 等等
2. 自定义假数据解析

类似示例：

![类似](https://img-blog.csdn.net/20160707180336526?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

#### 评估时间
下周一（18日之前）完成第一版

22号之前做优化修改

#### 技术预演
@某个账号

https://www.baidu.com

#某个话题#



1. Linkify对~~文本~~进行处理（改成SpannableString）
2. 监听点击跳转
    - 坑）对textView要同时setMovementMethod()才有点击效果

#### 思路
1. 对传入文本进行正则匹配
2. 对匹配到的链接进行替换（加颜色、网址转换等）
3. 设置点击事件


#### 7月12日更新
针对富文本匹配方案的可扩展性进行了优化更新

扩展更多匹配原则应当实现BaseSpanMode接口，并且在初始化富文本工具时将实现类添加到SpanModeManager的list中
