
> **静态文件** 生成代码时完全不用修改内容可以直接复制的文件，比如可能某些md文件
#### 第一阶段实现流程：
1. 生成静态文件，通过Main方法运行
2. 生成动态文件，通过Main方法运行
3. 同时生成静态和动态文件，通过Main方法运行，得到完整代码生成
4. 开发命令行工具，接受用户的输入并生成完整代码
5. 将工具封装为jar包和脚本，供用户调用
>FreeMarker **模板引擎**
> 一种用于生成动态内容的类库，通过将预定义的模板和特定数据合并来生成最终的输出。
> 模版引擎还具有一些安全特性，防止跨站脚本攻击等。
> 现在的模版引擎技术有：Java Thymeleaf,FreeMarker 前端：Mustache

### FreeMarker常用语法
#### 1.插值${xxx}
```
1   表达式：${100+money}
```
#### 2.分支和判空
```
<#if user == "鱼皮">
    我是鱼皮
<#else>
    我是猪皮
</#if>

<#if user??>
    存在用户
<#else>
    用户不存在
</#if>
```
#### 3.默认值
FreeMarker对变量的空值校验很严格，如果模版中有对象为空，FreeMarker将报错并中断生成。
>使用！默认值 的语法设置默认值
```java
${user!"用户为空"}
```
#### 4.循环
```java
<#list users as user>
    ${user}
</#list>
```
#### 5.宏定义 与C语言中类似（在编译之前，用一个预定义的模版代码片段进行替换）
类似于前端组件复用的思想，在FreeMarker中，使用`macro`指令来定义宏
例如定义一个宏来输出特定格式的用户昵称<br><#xxx >称为FTL指令
```java
<#macro card userName>
------
${userName}
------
</#macro>
```
card是宏的名称，userName是宏接受的参数。
宏设置了以后 用@xxx 语法来调用
```java
<@card userName="鱼皮"/>
<@card userName="二黑"/>
```
#### 6.内建函数
内建函数是为了提高开发者处理参数效率而提供的语法糖，可以通过`?`来调用
例如str转大写
```java
${userName?upper_case}
```
输出列表的长度
```java
${myList?size}
```
在循环中输出元素的下标
```java
<#list users as user>
    ${user.name?index}
</#list>
```

### 第三节
基于命令行继续开发本地代码生成器
1. Java 命令行开发方案
2. Picocli 命令行框架入门
3. 命令模式的理解
4. Picocli 命令行代码生成器开发

> **命令行程序** CLI(Command Line Interface)<br> 
> 指通过终端窗口接受用户输入的纯文本命令，并执行相应的任务

命令的结构由三个构成
1. 命令command 
2. 选项Option 对命令进行一些设置 有两种一个破折号是短选项 两个破折号是长选项 -cvf --out
其中-cvf是组合选项 -cvf=file1.txt 等于 -c -v -f=file1.txt
3. 参数parameters 就是直接跟的参数，前面没有option 
4. 命令行程序的几个优点：
    + 不依赖于特点的图像界面
    + 可以在操作系统自带的终端环境运行，这样也内置了一些快捷操作(比如查看历史命令，上下文切换命令)

但自己通过java的scanner库来写也太麻烦了，等于平地起高楼
鱼皮哥调研了几种经典的Java命令行开发相关库，分为三类
1. 命令行工具开发框架 Picocli 
2. 控制台输入处理库 Jline
3. 命令行解析库 支持对命令行进行解析取值,写注解就行 JCommander

用命令行实现交互式输入，梳理了四种模式
现在采用的是打开交互式输入。然后如果有必须要输入的option,用反射读取
实现runable或者callable接口的类getdeclaredfield 然后得到fileds 遍历字段用getannotationPresent验空。
然后用getAnnotaion(CommandLine.option.class) 得到字段的注解，在读option.name加入到args中
####子命令
子命令指命令中又包含一组命令，相当于分组嵌套，例如在git,docker这种较复杂的命令行程序中使用
1. 声明式：通过在注解@Command中给subcommands属性设置值来添加子命令

```java
import picocli.CommandLine;

@CommandLine.Command(subcommands = {
        GitStatus.class,
        GitCommit.class
})
public class git{}
```

2. 编程式：在创建commandLine对象时，调用addSubcommand来绑定子命令
```java
new CommandLine(new Git()).addSubcommand("status",new GitStatus()).addSubCommit("commit",new Gitcommit())
```
ps:runable 和 callable 的区别 callable 可以有返回值并且可以抛出异常
### 四.用Picocli来完成命令行代码生成器开发
要支持三种子命令
* generate子命令：生成文件
* list子命令： 查看要生成的原始文件列表信息
* config子命令： 查看允许用户传入的动态参数信息
要求能同时支持通过完整命令和交互式输入的方式来设置动态参数
* 开发步骤（六步）
  * 创建命令执行器（主命令）
  * 分别实现每种子命令
  * 提供项目的全局调用入口
  * 构建程序jar包
  * 测试使用
  * 简化使用（封装脚本）

好，已经完成了各个子命令（实现了接口），然后遥控器CommandExecutor(解析指令调用命令)
然后呢generator是设备 Main是客户端的输入
也就是前三步
#### 构建jar包
不能每次在Main方法里面改参数，将代码生成器打成jar包
1. 修改Main.java主类，只通过传入的args执行
2. 使用Maven打包构建，在pom.xml文件中引入maven-assembly-plugin插件，可以将依赖；库一起打入jar包，并
指定Mainclass路径为com.lpy.Main
```java
<build>
    <plugins>
        <plugin>
            <configuration>
                <archive>
                    <manifest>
                        <mainClass>com.lpy.Main</mainClass>
```

