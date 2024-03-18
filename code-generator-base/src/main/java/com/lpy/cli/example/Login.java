package com.lpy.cli.example;

import picocli.CommandLine;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class Login implements Callable<Integer> {
    @CommandLine.Option(names = {"-u","--user"},description = "User name")
    String user;
    @CommandLine.Option(names = {"-p", "--password"},description = "Passphrase",arity = "0..1", interactive = true, required = true)
//   Option的echo参数用于是否显示用户的输入(用jar包方式运行程序是不会显示的） prompt参数指定引导用户输入的提示语
//    interactive = true 代表这个选项变为交互式选项，交互式选项在默认情况下不能在命令中指定参数的 ，只能通过交互式输入
//     例如 -p xxx不行 给option添加arity属性 arity = 0..1 这样既可以直接在命令中输入参数，也可以使用交互式
//   如果设置交互式属性interactive,但用户没有输入-p，那么会被设为null.好的实践是如果用户没有输入自动给他补上
    String password;
    @Override
    public Integer call() throws Exception {
        System.out.println("password = " + password);
        return 0;
    }
    public static void main(String[] args){
        List<String> new_args = new ArrayList<>(Arrays.asList(args));
        Login login = new Login();
        //通过反射读取Login类中所有字段
        Field[] fields = login.getClass().getDeclaredFields();
        for(Field field:fields){
            //检查字段上是否有@CommandLine.Option注解
            if(field.isAnnotationPresent(CommandLine.Option.class)){
                CommandLine.Option option = field.getAnnotation(CommandLine.Option.class);
                //检查选项的required属性是否为true
                if(option.required()){
                    String optionName = option.names()[0];
                    new_args.add(optionName);
                }
            }
        }
        System.out.println(new_args);
        new CommandLine(new Login()).execute(new_args.toArray(new String[new_args.size()]));
    }
}
