在软件工程综合实习中，我们需要做到
- 使用Scrum敏捷开发
- 确定小组成员角色分配
- 使用TAPD(Tencent Agile Product Development)腾讯敏捷产品研发平台(www.tapd.cn)，完成全生命周期管理
- 使用gitee作为代码托管平台
- 使用TAPD布置任务，讨论和总结每天的任务
- 项目分成三次迭代，分别为
  - 迭代1：项目整体规划
  - 迭代2：基本功能开发
  - 迭代3：进阶功能开发
-在android环境下开发此平台
- 使用Jenkins集成工作环境，要求
  - 使用jmeter测试
  - 使用SonarQube进行代码质量分析

## 我们想做的项目

在线绘谱平台。提供各种乐器的乐谱，如口琴、吉他、钢琴等等，并提供下载功能
基本功能：
**支持多种曲谱类型**：包括吉他谱，钢琴谱，简谱等等。

**一键下载**：喜欢的乐谱一键下载。

**每日推荐**：每天自动更新推荐乐谱。

**演奏模式**：自己演奏喜爱的乐谱

**分享乐谱**：将曲子分享给好友（qq或微信）。

**求谱系统**：搜索不到乐谱时，可以发帖子向谱友求助。

**收藏功能**：将乐谱保存到我的收藏

### 实现
* MVP架构
* iOS与Android的混合UI风格
* 加载图片 ：使用Glide加载图片
* 数据库：ormlite数据库框架
*服务器： Bmob云服务器

- 测试 (Test)
  - Junit
  - Jmeter
- 代码质量检测 
  - SonarQube

项目成员工作分配见于：https://www.tapd.cn/60323303/prong/stories/stories_list



