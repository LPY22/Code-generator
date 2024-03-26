# ${name}

> ${description}
>
> 作者：${author}
>

可以通过命令行交互式输入的方式动态生成想要的项目代码

## 使用说明

执行项目根目录下的脚本文件

```
generator<命令> <选项参数>
```

示例命令：

```
generator generate <#list modelConfig.models as modelInfo> -${modelInfo.abbr} </#list>
```

## 参数说明
<#list modelConfig.models as modelInfo>
${modelInfo?index+1}) ${modelInfo.fieldName}

类型: ${modelInfo.type}
描述: ${modelInfo.description}
默认值: ${modelInfo.defaultValue?c}
缩写: ${modelInfo.abbr}
</#list>

🌟 Liked this project? Give it a star! 🌟

Thank you for checking out our project! If you found it useful or interesting, please consider giving it a star ⭐️ to show your support. Your feedback and contributions are highly appreciated! Let's make this project even better together.

🚀 Don't forget to follow us on GitHub for more updates and projects!