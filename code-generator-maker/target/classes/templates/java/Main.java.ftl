package ${basePackage};

import cn.hutool.core.util.ReflectUtil;
import ${basePackage}.cli.CommandExecutor;
import ${basePackage}.cli.command.GenerateCommand;
import picocli.CommandLine;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    //这里用作命令行项目的全局调用入口
    public static void main(String[] args) {
        List<String> completeArgs = new ArrayList<>(Arrays.asList(args));
        if(completeArgs.stream().anyMatch(s->"generate".equals(s)))
        {
            Field[] fields = ReflectUtil.getFields(GenerateCommand.class);
            for (Field field : fields) {
                //检查字段上是否有@CommandLine.Option注解
                if (field.isAnnotationPresent(CommandLine.Option.class)) {
                    CommandLine.Option option = field.getAnnotation(CommandLine.Option.class);
                    //检查选项的required属性是否为true
                    if (option.required()) {
                        String[] optionName = option.names();
                        boolean exists = completeArgs.stream().anyMatch(s -> Arrays.asList(optionName).contains(s));
                        if (!exists) completeArgs.add(optionName[0]);
                    }
                }
            }
        }
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.doExcute(completeArgs.toArray(new String[0]));
    }
}