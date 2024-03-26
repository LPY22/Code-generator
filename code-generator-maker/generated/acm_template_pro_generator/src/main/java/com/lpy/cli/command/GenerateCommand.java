package com.lpy.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.lpy.generator.file.FileGenerator;
import com.lpy.model.DataModel;
import lombok.Data;
import picocli.CommandLine;

import java.util.concurrent.Callable;
@CommandLine.Command(name ="generate",description = "生成代码",mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Callable<Integer> {
    @CommandLine.Option(names = {"-l","--loop"}, arity = "0..1", description = "是否生成循环",interactive = true,required = true,echo = true)
    private boolean loop = false;
    @CommandLine.Option(names = {"-a","--author"}, arity = "0..1", description = "作者注释",interactive = true,required = true,echo = true)
    private String author = "lpy";
    @CommandLine.Option(names = {"-o","--outputText"}, arity = "0..1", description = "输出信息",interactive = true,required = true,echo = true)
    private String outputText = "sum = ";
    @Override
    public Integer call() throws Exception {
        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
        System.out.println("配置信息: "+ dataModel);
        FileGenerator.doGenerate(dataModel);
        return 0;
    }
}