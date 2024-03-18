package com.lpy.cli.example;

import picocli.CommandLine;
//mixinStandardhelpOption 就是根据你创建的类 自动给你提供--help 但一般你要写description
@CommandLine.Command(name = "main",mixinStandardHelpOptions = true)
public class SubCommandExample implements Runnable {

    @Override
    public void run() {
        System.out.println("执行主命令");
    }
    @CommandLine.Command(name = "add",description = "添加",mixinStandardHelpOptions = true)
    static class AddCommand implements Runnable{

        @Override
        public void run() {
            System.out.println("执行添加命令");
        }
    }
    @CommandLine.Command(name = "delete",mixinStandardHelpOptions = true)
    static class DeleteCommand implements Runnable{

        @Override
        public void run() {
            System.out.println("执行删除命令");
        }
    }
    @CommandLine.Command(name = "query",mixinStandardHelpOptions = true)
    static class QueryCommand implements Runnable{

        @Override
        public void run() {
            System.out.println("执行查询命令");
        }
    }

    public static void main(String[] args) {
        //执行主命令
        String[] myArgs = new String[] { "-h" };//后面加个花括号表示一个空数组
        int exitCode = new CommandLine(new SubCommandExample()).addSubcommand(new AddCommand()).addSubcommand(
                new DeleteCommand()).addSubcommand(new QueryCommand()).execute(myArgs);
        System.exit(exitCode);


    }
}
