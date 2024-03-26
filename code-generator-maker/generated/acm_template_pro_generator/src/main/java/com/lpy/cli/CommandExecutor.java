package com.lpy.cli;

import com.lpy.cli.command.ConfigCommand;
import com.lpy.cli.command.GenerateCommand;
import com.lpy.cli.command.ListCommand;
import picocli.CommandLine;

//命令模式中的遥控器，里面集成了很多命令可以执行
@CommandLine.Command(name = "acm_template_pro_generator", mixinStandardHelpOptions = true)
public class CommandExecutor implements Runnable{
    private final CommandLine commandLine;

    {
        commandLine = new CommandLine(this).addSubcommand(new GenerateCommand()).addSubcommand(new ConfigCommand())
                .addSubcommand(new ListCommand());
    }
    @Override
    public void run() {
        //不输入子命令时，给出提示
        System.out.println("请输入具体命令，或者--help查看命令提示");
    }

    /**
     * 执行客户端（终端）输入的命令（命令行）
     *
     * @param args
     * @return
     */
    public Integer doExcute(String[] args){
        return commandLine.execute(args);
    }
}
