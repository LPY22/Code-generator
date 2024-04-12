package com.lpy.maker.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.lpy.maker.meta.Meta;
import com.lpy.maker.meta.enums.FileGenerateTypeEnum;
import com.lpy.maker.meta.enums.FileTypeEnum;
import com.lpy.maker.template.enums.FileFilterRangeEnum;
import com.lpy.maker.template.enums.FileFilterRuleEnum;
import com.lpy.maker.template.model.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class TemplateMaker {

    public static long makeTemplate(TemplateMakerConfig templateMakerConfig){
        Meta meta = templateMakerConfig.getMeta();
        Long id = templateMakerConfig.getId();
        TemplateMakerFileConfig fileConfig = templateMakerConfig.getFileConfig();
        TemplateMakerModelFileConfig modelConfig = templateMakerConfig.getModelConfig();
        String originProjectPath = templateMakerConfig.getOriginProjectPath();
        TemplateMakerOutputConfig templateMakerOutputConfig = templateMakerConfig.getTemplateMakerOutputConfig();

        return makeTemplate(id,meta,originProjectPath,fileConfig,modelConfig,templateMakerOutputConfig);
    }
    public static long makeTemplate(Long id, Meta newMeta, String originProjectPath, TemplateMakerFileConfig templateMakerFileConfig, TemplateMakerModelFileConfig templateMakerModelFileConfig, TemplateMakerOutputConfig templateMakerOutputConfig)
    {
        //没有则生成id
        if (id == null) {
            id = IdUtil.getSnowflakeNextId();
        }
        // todo 业务逻辑。。非首次制作1.不需要复制原始文件；2.可以在已有模板的基础上再次挖坑 3，不用重复输入已有元信息，而是覆盖和追加元信息配置。
        //复制目录
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = projectPath + File.separator + ".temp";
        String templatePath = tempDirPath + File.separator + id;

        //是否是首次制作模板，是则把源项目复制过来
        if (!FileUtil.exist(templatePath)) {
            FileUtil.mkdir(templatePath);
            FileUtil.copy(originProjectPath, templatePath, true);
        }

        //输入信息
        //输入文件信息
//        String sourceRootPath = templatePath + File.separator + FileUtil.getLastPathEle(Paths.get(originProjectPath)).toString();
        String sourceRootPath = FileUtil.loopFiles(new File(templatePath),1,null)
                .stream().filter(File::isDirectory).findFirst().orElseThrow(RuntimeException::new)
                .getAbsolutePath();//方便之后的配置修改中不用再写originProjectPath


        //对路径转移（win)
        sourceRootPath = sourceRootPath.replaceAll("\\\\", "/");
//        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoconfigList = templateMakerFileConfig.getFiles();
        List<Meta.FileConfig.FileInfo> newFileInfoList = makeFileTemplates(templateMakerFileConfig, templateMakerModelFileConfig, sourceRootPath);
        List<Meta.ModelConfig.ModelInfo> newModelInfoList = getModelInfoList(templateMakerModelFileConfig);


        //三：生成配置文件
        String metaOutputPath = templatePath + File.separator + "meta.json";
        //已有meta 文件则说明不是第一次制作
        if (FileUtil.exist(metaOutputPath)) {
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);//先用JSONUtil.toBean把配置文件里面的值赋过来 如果是类与类之间，可以使用BeanUtil.copyProperties(this,that)
//            System.out.println(oldMeta.getModelConfig());
            BeanUtil.copyProperties(newMeta,oldMeta, CopyOptions.create().ignoreNullValue());
//            System.out.println(oldMeta.getModelConfig());
            newMeta = oldMeta;//把newMeta上的非空值赋给oldMeta 然后在让newMeta = oldMeta 方便和第一次制作一起处理逻辑

            //1.追加配置参数
            List<Meta.FileConfig.FileInfo> fileInfoList = newMeta.getFileConfig().getFiles();
            fileInfoList.addAll(newFileInfoList);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = newMeta.getModelConfig().getModels();
            modelInfoList.addAll(newModelInfoList);
            //1.5 配置去重
            newMeta.getFileConfig().setFiles(distinctFiles(fileInfoList));
            newMeta.getModelConfig().setModels((distinctModels(modelInfoList)));
//            //2.更新元信息文件
//            FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(oldMeta), metaOutputPath);
        } else {
            //1.构造配置参数
            Meta.FileConfig fileConfig = new Meta.FileConfig();
            newMeta.setFileConfig(fileConfig);
            fileConfig.setSourceRootPath(sourceRootPath);
            List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
            fileConfig.setFiles(fileInfoList);

            fileInfoList.addAll(newFileInfoList);

            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            newMeta.setModelConfig(modelConfig);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
            modelConfig.setModels(modelInfoList);
            modelInfoList.addAll(newModelInfoList);

        }
        //2.输出配置逻辑
        if(templateMakerOutputConfig != null){
            //文件外层和分组去重
            if(templateMakerOutputConfig.isRemoveGroupFilesFromRoot()){
                List<Meta.FileConfig.FileInfo> fileInfoList = newMeta.getFileConfig().getFiles();
                newMeta.getFileConfig().setFiles(TemplateMakerUtils.removeGroupFilesFromRoot(fileInfoList));
            }
        }

        //3.输出元信息文件
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), metaOutputPath);
        return id;
    }

    private static List<Meta.ModelConfig.ModelInfo> getModelInfoList(TemplateMakerModelFileConfig templateMakerModelFileConfig) {
        //- 本次新增的模型配置列表
        List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>();
        if(templateMakerModelFileConfig == null){
            return newModelInfoList;
        }
        //处理模型信息
        List<TemplateMakerModelFileConfig.ModelInfoConfig> models = templateMakerModelFileConfig.getModels();
        if(CollUtil.isEmpty(models)){
            return newModelInfoList;
        }
        //转换为配置接受的ModelInfo对象
        List<Meta.ModelConfig.ModelInfo> inputModelInfoList = models.stream().map(modelInfoConfig -> {
            Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
            BeanUtil.copyProperties(modelInfoConfig, modelInfo);
            return modelInfo;
        }).collect(Collectors.toList());

        //如果是模型组 那就再套一层
        TemplateMakerModelFileConfig.ModelGroupConfig modelGroupConfig = templateMakerModelFileConfig.getModelGroupConfig();
        if(modelGroupConfig != null){
            //复制变量
            Meta.ModelConfig.ModelInfo groupModelInfo = new Meta.ModelConfig.ModelInfo();
            BeanUtil.copyProperties(modelGroupConfig,groupModelInfo);

//            String condition = modelGroupConfig.getCondition();
//            String groupKey = modelGroupConfig.getGroupKey();
//            String groupName = modelGroupConfig.getGroupName();
//            Meta.ModelConfig.ModelInfo groupModelInfo = new Meta.ModelConfig.ModelInfo();
//            groupModelInfo.setCondition(condition);
//            groupModelInfo.setGroupKey(groupKey);
//            groupModelInfo.setGroupName(groupName);

            //模型放到一个分组
            groupModelInfo.setModels(inputModelInfoList);
            newModelInfoList.add(groupModelInfo);
        }else {
            //不是一组，直接全部添加
            newModelInfoList.addAll(inputModelInfoList);
        }
        return newModelInfoList;
    }

    private static List<Meta.FileConfig.FileInfo> makeFileTemplates(TemplateMakerFileConfig templateMakerFileConfig, TemplateMakerModelFileConfig templateMakerModelFileConfig, String sourceRootPath) {
        //掉用makeFileTemplate函数生成模板文件
        //遍历输入文件
        List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>();
        //非空校验
        if(templateMakerFileConfig == null){
            return newFileInfoList;
        }
        List<TemplateMakerFileConfig.FileInfoConfig> fileConfigInfoList = templateMakerFileConfig.getFiles();
        if(CollUtil.isEmpty(fileConfigInfoList)){
            return newFileInfoList;
        }
        for( TemplateMakerFileConfig.FileInfoConfig fileInfoConfig: fileConfigInfoList){
            String inputFilePath = fileInfoConfig.getPath();
            //如果配置参数填的相对地址-》改为绝对地址
            if(!inputFilePath.startsWith(sourceRootPath)){
                if(!inputFilePath.startsWith("/")){
                    inputFilePath = sourceRootPath +"/"+inputFilePath;
                }
                else inputFilePath = sourceRootPath +inputFilePath;
            }

            //获取过滤后的文件列表，如果是目录的话doFilter已经循环遍历过了
            List<File> fileList = FileFilter.doFilter(inputFilePath, fileInfoConfig.getFileFilterConfigs());
            //不处理已经生成的FTL模板文件
            fileList = fileList.stream().filter(file -> !file.getAbsolutePath().endsWith(".ftl")).collect(Collectors.toList());
            for (File file :
                    fileList) {
                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(templateMakerModelFileConfig, sourceRootPath, file ,fileInfoConfig);
                newFileInfoList.add(fileInfo);
            }
        }
        //如果是文件组
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = templateMakerFileConfig.getFileGroupConfig();
        if(fileGroupConfig!=null){
            String condition = fileGroupConfig.getCondition();
            String groupKey = fileGroupConfig.getGroupKey();
            String groupName = fileGroupConfig.getGroupName();

            //新增分组配置
            Meta.FileConfig.FileInfo groupFileInfo = new Meta.FileConfig.FileInfo();
            groupFileInfo.setType(FileTypeEnum.GROUP.getValue());
            groupFileInfo.setCondition(condition);
            groupFileInfo.setGroupName(groupName);
            groupFileInfo.setGroupKey(groupKey);
            //文件全放在一个分组内
            groupFileInfo.setFiles(newFileInfoList);
            newFileInfoList = new ArrayList<>();
            newFileInfoList.add(groupFileInfo);
        }
        return newFileInfoList;
    }

    //把生成单个文件模板方法抽出来
    private static Meta.FileConfig.FileInfo makeFileTemplate(TemplateMakerModelFileConfig templateMakerModelFileConfig,  String sourceRootPath, File inputFile, TemplateMakerFileConfig.FileInfoConfig fileInfoConfig){
        //要挖坑文件的绝对路径（用于制作模板）
        // 注意win系统需要对路径进行转义
        String fileInputAbsolutePath = inputFile.getAbsolutePath().replaceAll("\\\\","/");
        String fileOutputAbsolutePath = fileInputAbsolutePath+ ".ftl";

        //文件输入输出相对路径（用于生成配置） 配置用于生成项目替换后的代码，要用相对路径
        String fileInputPath = fileInputAbsolutePath.replace(sourceRootPath + "/","");
        String fileOutputPath = fileInputPath + ".ftl";

        //使用字符串替换，生成模板文件
        String fileContent;
        //如果有模板文件，说明不是第一次
        boolean hasTemplateFile = FileUtil.exist(fileOutputAbsolutePath);
        if(hasTemplateFile){
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        }else fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);

        //支持多个模型：对同一个文件的内容，遍历模型进行多轮替换
        TemplateMakerModelFileConfig.ModelGroupConfig modelGroupConfig = templateMakerModelFileConfig.getModelGroupConfig();
        String newFileContent = fileContent;
        String replacement;
        for(TemplateMakerModelFileConfig.ModelInfoConfig modelInfoConfig:templateMakerModelFileConfig.getModels()){
            //不是分组
            if(modelGroupConfig == null){
                replacement = String.format("${%s}", modelInfoConfig.getFieldName());
            }else{//是分组
                String groupKey = modelGroupConfig.getGroupKey();
                //注意要多一层
                replacement = String.format("$(%s.%s)",groupKey,modelInfoConfig.getFieldName());
            }
            //多次替换
            newFileContent = StrUtil.replace(newFileContent,modelInfoConfig.getReplaceText(),replacement);
        }


        //文件配置信息
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        //注意文件的输入路径要和输出路径反转 应为在用生成器生成项目的时候，应该是模板文件.ftl作为输入
        fileInfo.setInputPath(fileOutputPath);
        fileInfo.setOutputPath(fileInputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setCondition(fileInfoConfig.getCondition());
        //先默认设为动态类型
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
        //检查替换了${%s}之后的内容是否和之前一样来判断是否是动态文件
        boolean contentEquals = newFileContent.equals(fileContent);
        if(!hasTemplateFile){
            if(contentEquals) {
                //输出路径 = 输入路径
                fileInfo.setInputPath(fileInputPath);
                fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
            }else{
                //沒有模板文件，需要挖坑，生成模板文件
                FileUtil.writeUtf8String(newFileContent,fileOutputAbsolutePath);
            }
        }else if(!contentEquals){
            //有模板文件，但是挖了新坑，生成模板文件
            FileUtil.writeUtf8String(newFileContent,fileOutputAbsolutePath);

        }
        return fileInfo;
    }

    public static void main(String[] args) {
       Meta meta = new Meta();
       meta.setName("acm-template-generator");
       meta.setDescription("ACM 示例模板生成器");

        String projectPath = System.getProperty("user.dir");
//        String originProjectPath = new File(projectPath).getParent() + File.separator + "code-demo-projects/acm-template";
        String originProjectPath = new File(projectPath).getParent() + File.separator + "code-demo-projects/springboot-init";
//        String inputFilePath = "src/com/lpy/acm/MainTemplate.java";
        String inputFilePath1 = "src/main/java/com/yupi/springbootinit/common";
//        String inputFilePath2 = "src/main/java/com/yupi/springbootinit/controller";
        String inputFilePath2 = "src/main/resources/application.yml";

        //模型参数配置
        TemplateMakerModelFileConfig templateMakerModelFileConfig = new TemplateMakerModelFileConfig();

        //模型组参数配置
        TemplateMakerModelFileConfig.ModelGroupConfig modelGroupConfig = new TemplateMakerModelFileConfig.ModelGroupConfig();
        modelGroupConfig.setGroupName("数据库配置");
        modelGroupConfig.setGroupKey("mysql");
        templateMakerModelFileConfig.setModelGroupConfig(modelGroupConfig);

        //模型配置
        TemplateMakerModelFileConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelFileConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("url");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDefaultValue("jdbc:mysql://localhost:3306/my_db");
        modelInfoConfig1.setReplaceText("jdbc:mysql://localhost:3306/my_db");

        TemplateMakerModelFileConfig.ModelInfoConfig modelInfoConfig2 = new TemplateMakerModelFileConfig.ModelInfoConfig();
        modelInfoConfig2.setFieldName("username");
        modelInfoConfig2.setType("String");
        modelInfoConfig2.setDefaultValue("root");
        modelInfoConfig2.setReplaceText("root");

        List<TemplateMakerModelFileConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1,modelInfoConfig2);
        templateMakerModelFileConfig.setModels(modelInfoConfigList);

        List<String> inputFilePathList = Arrays.asList(inputFilePath1, inputFilePath2);
        //模型参数信息（首次
//        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
//        modelInfo.setFieldName("outputText");
//        modelInfo.setType("String");
//        modelInfo.setDefaultValue("sum=  ");
//        modelInfo.setDescription("输出信息");

        //模型参数信息（二次
        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("className");
        modelInfo.setType("String");

        //替换变量（首次
//        String searchStr = "Sum: ";
        //替换变量（二次
        String searchStr = "BaseResponse" ;

        //添加文件过滤操作(没有抽象成函数
        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(inputFilePath1);
        List<FileFilterConfig> fileFilterConfigList = new ArrayList<>();
        //设置过滤器
        FileFilterConfig fileFilterConfig = FileFilterConfig.builder().range(FileFilterRangeEnum.FILE_NAME.getValue())
                .rule(FileFilterRuleEnum.CONTAINS.getValue())
                .value("Base").build();
        fileFilterConfigList.add(fileFilterConfig);
        fileInfoConfig1.setFileFilterConfigs(fileFilterConfigList);
        //设置分组配置
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = new TemplateMakerFileConfig.FileGroupConfig();
        fileGroupConfig.setCondition("outputText");
        fileGroupConfig.setGroupKey("test");
        fileGroupConfig.setGroupName("测试分组");
        templateMakerFileConfig.setFileGroupConfig(fileGroupConfig);


        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig2 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig2.setPath(inputFilePath2);
        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig1,fileInfoConfig2));
        TemplateMakerOutputConfig templateMakerOutputConfig = new TemplateMakerOutputConfig();

        long id = makeTemplate(null,meta,originProjectPath,templateMakerFileConfig,templateMakerModelFileConfig,templateMakerOutputConfig);
        System.out.println(id);


    }
    // 模型去重方法
    private static List<Meta.ModelConfig.ModelInfo> distinctModels(List<Meta.ModelConfig.ModelInfo> modelInfoList) {
        //1.有分组的，以组为单位划分
        Map<String, List<Meta.ModelConfig.ModelInfo>> groupKeyModelInfoListMap = modelInfoList.stream()
                .filter(modelInfo -> StrUtil.isNotBlank(modelInfo.getGroupKey()))
                .collect(Collectors.groupingBy(Meta.ModelConfig.ModelInfo::getGroupKey));
        //2.同组的模型配置合并
        //保存每个组对应的合并后的对象 map //这里默认了最新的配置放在了列表最后，事实也是这样modelInfoList.addAll(newModelInfoList)
        Map<String,Meta.ModelConfig.ModelInfo> groupKeyMergedModelInfoMap = new HashMap<>();
        for(Map.Entry<String,List<Meta.ModelConfig.ModelInfo>> entry: groupKeyModelInfoListMap.entrySet()){
            List<Meta.ModelConfig.ModelInfo> tempModelInfoList = entry.getValue();
            ArrayList<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>(tempModelInfoList.stream()
                    .flatMap(ModelInfo -> ModelInfo.getModels().stream())
                    .collect(Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r))
                    .values());

            //使用新的group配置
            Meta.ModelConfig.ModelInfo newModelInfo = CollUtil.getLast(tempModelInfoList);
            newModelInfo.setModels(newModelInfoList);
            String groupKey = entry.getKey();
            groupKeyMergedModelInfoMap.put(groupKey,newModelInfo);
        }
        //3.将模型分组结果添加到结果列表
        List<Meta.ModelConfig.ModelInfo> resultList = new ArrayList<>(groupKeyMergedModelInfoMap.values());

        //4.将未分组的模型配置添加到结果列表
        List<Meta.ModelConfig.ModelInfo> noGroupModelInfoList = modelInfoList.stream().filter(modelInfo -> StrUtil.isBlank(modelInfo.getGroupKey()))
                .collect(Collectors.toList());
        resultList.addAll(new ArrayList<>(noGroupModelInfoList.stream()
                .collect(Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName,o->o,(e,r)->r)).values()));
        return resultList;

    }

    private static List<Meta.FileConfig.FileInfo> distinctFiles(List<Meta.FileConfig.FileInfo> fileInfoList){
        //策略：同分组内文件merge , 不同分组保留
        //1. 有分组的，以组为单位划分
        Map<String, List<Meta.FileConfig.FileInfo>> groupKeyFileInfoListMap = fileInfoList.stream().filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(Collectors.groupingBy(Meta.FileConfig.FileInfo::getGroupKey));

        //2. 同组内文件配置合并
        //保存每个组对应的合并后的对象 Map
        Map<String,Meta.FileConfig.FileInfo> groupKeyMergedFileInfoMap = new HashMap<>();
        for(Map.Entry<String,List<Meta.FileConfig.FileInfo>> entry : groupKeyFileInfoListMap.entrySet()){
            List<Meta.FileConfig.FileInfo> tempFileInfoList = entry.getValue();
            ArrayList<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(tempFileInfoList.stream()
                    .flatMap(fileInfo -> fileInfo.getFiles().stream())//flatMap将嵌套结构按顺序展平成一个流，注意括号内lambda表达式要以.stream()结尾
                    .collect(Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e, r) -> r))
                    .values());

            //使用新的group配置
            Meta.FileConfig.FileInfo newFileInfo = CollUtil.getLast(tempFileInfoList);
            newFileInfo.setFiles(newFileInfoList);
            String groupKey = entry.getKey();
            groupKeyMergedFileInfoMap.put(groupKey,newFileInfo);
        }

        //3.将文件分组添加到结果列表
        List<Meta.FileConfig.FileInfo> resultList = new ArrayList<>(groupKeyMergedFileInfoMap.values());

        //4.将未分组的文件添加到结果列表
        //用之前写的代码吧
        //Stream的collect方法是一个中端操作，将流中的元素收集到一个结果中。它可以使用多种搜集器Collector来定义如何
        //收集流中的元素，可以接收收集器例如Collectors.toList() Collectors.toSet() Collectors.joining() Collectors.toMap()
        //还可以接受累加器 Accumulator 累加器函数接受两个参数，当前元素和已经搜集的部分结果，累加器函数定义了如何将流中的元素合并到结果中。
        //还可以接受组合器Combiner 组合器函数接受两个部分结果，并返回一个新的结果。组合器通常用于并行流操作，以合并不同部分的结果。
        List<Meta.FileConfig.FileInfo> noGroupFileInfoList = fileInfoList.stream()
                .filter(fileInfo -> StrUtil.isBlank(fileInfo.getGroupKey()))
                .collect(Collectors.toList());
        resultList.addAll(new ArrayList<>(noGroupFileInfoList.stream()
                .collect(Collectors.toMap(Meta.FileConfig.FileInfo::getOutputPath,o->o,(e,r)->r)).values()));
        return resultList;

    }
}