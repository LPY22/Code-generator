### 需求分析：
在前面的本地项目开发中，绝大多数的复杂业务逻辑已经完成，接下来是实现项目的线上化
首先要对那些内容线上化，把一些本地的能力搬到线上：
1. 数据线上化
   * 元信息线上化：将元信息配置保存在数据库中。
   * 项目模板线上化，将静态文件和模板文件保存到存储服务上
   * 代码生成器线上化，将代码生成器产物包保存到存储服务上
   * ps:放在自己的服务器上，不好管理，不好迁移，不能读取加速，使用对象存储可以配置CDN的加速方式
2. 功能线上化
    * 在线查看生成器的信息
    * 在线使用生成器
    * 在线使用生成器制作工具

在线代码生成平台，支持用户在线制作，分享-》搜索，使用各类代码生成器，帮助开发者定制化开发提高效率
<br>具体要实现的需求：
+ 用户注册，登录
+ 管理员功能：用户管理，代码生成器管理（CURD）
+ 代码生成器搜索
+ 代码生成器详情查看
+ 代码生成器创建
+ 代码生成器下载
+ 代码生成器在线使用
+ 代码生成器在线制作

#### 二.线上化实现流程
对于一个复杂的需求或任务，要先分析实现流程，然后排期

1) 先完成库表设计，让数据库能支持存储代码生成器信息
2) 实现基本的用户注册登录，增删改查等功能，让用户能够浏览代码生成器信息
3) 实现文件的上传下载功能，让用户能够上传和下载代码生成器产物包
4) 实现在线使用代码生成器功能，让用户直接在线生成代码
5) 实现在线制作代码生成器功能，提高用户制作生成器效率
6) 项目优化，包括性能优化，存储优化 温柔乡，英雄冢

##### 数据库表设计
可以在github，gitlab上找一些别人的库表设计作为参考

   