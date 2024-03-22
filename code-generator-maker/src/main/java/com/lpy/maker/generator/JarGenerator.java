package com.lpy.maker.generator;

import java.io.*;

//使用Java内置的Process类执行Maven打包命令，并获取到命令的输入信息。需要注意的不同的
//操作系统，执行的命令代码不同。
public class JarGenerator {
    public static void doGenerate(String projectDir) throws IOException, InterruptedException {
        //清理之前的构建并打包
        //注意不同操作系统，执行的命令不同
        String winMavenCommand = "mvn.cmd clean package -DskipTests=true -Dfile.encoding=UTF-8";
        String otherMavenCommand = "mvn clean package -DskipTests=true -Dfile.encoding=UTF-8";
        String mavenCommand = winMavenCommand;

        //这里一定要拆分
        ProcessBuilder processBuilder = new ProcessBuilder((mavenCommand.split(" ")));
        processBuilder.directory(new File(projectDir));

        Process process = processBuilder.start();

        //读取命令的输出
        //在java中，当你创建一个Process对象来运行一个外部程序时，你实际上是在创建一个运行在单独进程中的程序实例
        //这个外部进程的标准输出和标准错误流式用来发送数据的。Java进程通过调用对应的Process对象的getInputStream
        //方法来读取。这里的input是对Java进程本身而言的，对于外部进程则是它的输出流
        InputStream inputStream = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine())!=null){
            System.out.println(line);
        }

        //等待命令执行完成
        int exitCode = process.waitFor();
        System.out.println("命令执行结束,退出码："+ exitCode);

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        doGenerate("D:\\桌面文件\\java_exercise\\Code-generator\\code-generator-maker\\generated\\acm_template_pro_generator");
    }
}
