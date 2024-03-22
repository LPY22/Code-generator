package com.lpy.maker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    //这里用作命令行项目的全局调用入口
    public static void main(String[] args) {
//直接调用工具完成整个目录的复制生成
/*        //获取整个项目的根路径
        String projectPath = System.getProperty("user.dir");
        File parentFile = new File(projectPath).getParentFile();
        //inputpath ACM示例模版目录
        String inputPath = new File(parentFile,"code-demo-projects/acm-template").getAbsolutePath();
        //outputpath 直接输出到项目模版的根目录
        String outputPath = projectPath;
        StaticGenerator.copyFilesByHutool(inputPath,outputPath);*/

        //手动设置args,用于测试
//        args = new String[]{"generate","-l","-o"};
//        args = new String[]{"config"};
//        args = new String[]{"list"};
        List<String> completeArgs = new ArrayList<>(Arrays.asList(args));
//        if(completeArgs.stream().anyMatch(s->"generate".equals(s)))
//        {
//            Field[] fields = ReflectUtil.getFields(GenerateCommand.class);
//            for (Field field : fields) {
//                //检查字段上是否有@CommandLine.Option注解
//                if (field.isAnnotationPresent(CommandLine.Option.class)) {
//                    CommandLine.Option option = field.getAnnotation(CommandLine.Option.class);
//                    //检查选项的required属性是否为true
//                    if (option.required()) {
//                        String[] optionName = option.names();
//                        boolean exists = completeArgs.stream().anyMatch(s -> Arrays.asList(optionName).contains(s));
//                        if (!exists) completeArgs.add(optionName[0]);
//                    }
//                }
//            }
//        }
//        CommandExecutor commandExecutor = new CommandExecutor();
//        commandExecutor.doExcute(completeArgs.toArray(new String[0]));
    }
}