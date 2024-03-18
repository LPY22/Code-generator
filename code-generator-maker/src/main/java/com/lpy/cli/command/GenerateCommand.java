package com.lpy.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.lpy.generator.MainGenerator;
import com.lpy.model.MainTemplateConfig;
import lombok.Data;
import picocli.CommandLine;

import java.util.concurrent.Callable;
@CommandLine.Command(name ="generate",description = "生成目标项目",mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Callable<Integer> {//arity = "0..1"既可以在命令行直接输入 也可以用交互式
    @CommandLine.Option(names = {"-l","--loop"}, arity = "0..1", description = "是否循环读入多组数据",interactive = true,required = true,echo = true)
    private boolean loop;
    @CommandLine.Option(names = {"-a","--author"}, arity = "0..1", description = "作者",interactive = true,required = true,echo = true)
    private String author = "lpy";
    @CommandLine.Option(names = {"-o","--outputText"}, arity = "0..1", description = "输出文本",interactive = true,required = true,echo = true)
    private String outputText = "sum=";
    @Override
    public Integer call() throws Exception {
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        BeanUtil.copyProperties(this,mainTemplateConfig);
        System.out.println("配置信息: "+ mainTemplateConfig);
        MainGenerator.doGenerate(mainTemplateConfig);
        return 0;
    }
}
