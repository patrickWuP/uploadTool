package com.wup.util;

import com.wup.bean.OperateConfig;
import com.wup.bean.OperateResult;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description : 操作文件执行工具类
 * @Project : imoocsecurity
 * @Program Name  : com.wup.util.OperateUtil
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class OperateUtil {

    //两个commitId文件差异列表
    private static List<String> diffFileList = null;
    //本地编译后项目中上传文件列表
    private static List<String> uploadFileList = null;
    //获取更新文件列表
    public static OperateResult getUpdateFile(OperateConfig operateConfig){
        diffFileList = new ArrayList<>();
        uploadFileList = new ArrayList<>();
        BufferedReader br = null;
        BufferedWriter bw = null;
        //校验信息是否正确
        OperateResult result = validateGetUpdateConfig(operateConfig);
        if (result.getStatus() == OperateResult.ERROR_STATUS){
            return result;
        }
        //获取更新的文件列表
        //获取两次commit id文件差异命令
        String command = "git -C " + operateConfig.getGitProjectPath() + " diff --name-status --no-commit-id " +
                operateConfig.getGitStartCommitId() + " " + operateConfig.getGitEndCommitId();
        //执行该命令，并获取其执行结果
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(command);
            //输出至本地文件
            //生成随机的文件名
            String fileName = DateUtil.getCurrentDate() + "-更新文件.txt";
            File file = new File(operateConfig.getLocalSaveFilePath() + fileName);
            br = new BufferedReader(new InputStreamReader(new BufferedInputStream(process.getInputStream())));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            String line = null;
            bw.write("================commit id 间文件差异如下======================\n");
            while ((line = br.readLine()) != null){
                String[] arr = line.trim().split("\\s+");
                if (arr.length != 2 || passFileCheck(arr[1])){
                    continue;
                }
                bw.write(line + "\n");
                diffFileList.add(arr[1]);
            }
            bw.write("================commit id 间文件差异如上======================\n");

            //本地将要上传的文件
            //判断两个commit id是否有差异文件
            if (diffFileList.size() == 0){
                return OperateResult.ErrorResult("两次commit id没有差异文件或id填写有误，请核实！");
            }
            if (StringUtils.isNotBlank(operateConfig.getCompileProjectPath())){
                bw.write("================本地上传的文件如下======================\n");
                for(String path:diffFileList){
                    //文件路径，class，html等文件
                    String filePath = operateConfig.getCompileProjectPath() + replacePath(path);
                    if (!new File(filePath).exists()){
                        return OperateResult.ErrorResult("该文件路径:" + filePath + "不存在，请核实！");
                    }
                    bw.write(filePath + "\n");
                    uploadFileList.add(filePath);
                    //处理class文件内部类问题，内部类无法做到验证其完全正确性，因为无法知道实际会生成几个内部类
                    if (filePath.endsWith(".class")){
                        int i = 1;
                        //内部类路径，使用$1.class判断是否有内部类
                        String innerFilePath = filePath.replace(".class","$1.class");
                        while (new File(innerFilePath).exists()){
                            bw.write(innerFilePath + "\n");
                            uploadFileList.add(innerFilePath);
                            i++;
                            String internalClass = "$" + i + ".class";
                            innerFilePath = filePath.replace(".class",internalClass);
                        }
                    }
                }
                bw.write("================本地上传的文件如上======================\n");
            }

            //已经上传至服务器，服务器端相关文件列表
            if (StringUtils.isNotBlank(operateConfig.getServerProjectPath()) && StringUtils.isNotBlank(operateConfig.getCompileProjectPath())){
                bw.write("================上传至服务器端的文件如下======================\n");
                for (String path:uploadFileList){
                    String serverFilePath = path.replace(operateConfig.getCompileProjectPath(),operateConfig.getServerProjectPath());
                    bw.write(serverFilePath + "\n");
                }
                bw.write("================上传至服务器端的文件如上======================\n");
            }

            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return OperateResult.ErrorResult("执行git命令异常!");
        }finally {
            try {
                br.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return OperateResult.SuccessResult();
    }


    public static OperateResult upLoadFile(OperateConfig config){
        //校验参数并初始化ssh连接
        OperateResult validateResult = validateUploadConfigAndInit(config);
        if (validateResult.getStatus() == OperateResult.ERROR_STATUS){
            return validateResult;
        }
        //获取更新文件操作
        OperateResult result = getUpdateFile(config);
        if (result.getStatus() == OperateResult.ERROR_STATUS){
            return result;
        }
        //执行上传操作
        String dstPath = "";
        try{
            for (String srcPath:uploadFileList){
                dstPath = srcPath.replace(config.getCompileProjectPath(),config.getServerProjectPath());
                SfttpUtil.upload(srcPath,dstPath);
                System.out.println(dstPath + ":文件上传成功!");
            }
        }catch (Exception e){
            e.printStackTrace();
            return OperateResult.ErrorResult(dstPath + ":文件上传异常!");
        }
        return OperateResult.SuccessResult();
    }

    /**
     * 检查文件是否为不进行处理的文件，主要针对一些不用提交的配置文件
     * @param path 文件路径
     * @return true 不进行处理，false 进行处理
     */
    private static boolean passFileCheck(String path){
        if (path.startsWith(".settings")){
            return true;
        }
        return false;
    }

    private static OperateResult validateUploadConfigAndInit(OperateConfig operateConfig){
        if (StringUtils.isBlank(operateConfig.getCompileProjectPath())){
            return OperateResult.ErrorResult("编译后的项目路径不能为空！");
        }
        if (!new File(operateConfig.getCompileProjectPath()).exists()){
            return OperateResult.ErrorResult("编译后的项目路径不存在！");
        }
        if (StringUtils.isBlank(operateConfig.getServerProjectPath())){
            return OperateResult.ErrorResult("服务端的项目路径不能为空！");
        }
        if (StringUtils.isBlank(operateConfig.getServerIp())){
            return OperateResult.ErrorResult("服务端ip不能为空");
        }
        if (StringUtils.isBlank(operateConfig.getServerPort())){
            return OperateResult.ErrorResult("服务端端口号不能为空");
        }
        if (StringUtils.isBlank(operateConfig.getServerUserName())){
            return OperateResult.ErrorResult("服务端用户名不能为空");
        }
        if (StringUtils.isBlank(operateConfig.getServerPwd())){
            return OperateResult.ErrorResult("服务端密码不能为空");
        }
        //初始化ssh连接判断连接是否正常
        return SfttpUtil.initSftp(operateConfig.getServerIp(),Integer.valueOf(operateConfig.getServerPort()),operateConfig
                .getServerUserName(),operateConfig.getServerPwd());
    }
    /**
     * 获取文件更新，判断commitId和本地路径是否有值
     * @param operateConfig  操作相关配置信息
     * @return 操作结果
     */
    private static OperateResult validateGetUpdateConfig(OperateConfig operateConfig){
        if (StringUtils.isBlank(operateConfig.getGitProjectPath())){
            return OperateResult.ErrorResult("git本地项目路径不能为空！");
        }
        if (!new File(operateConfig.getGitProjectPath()).exists()){
            return OperateResult.ErrorResult("git本地项目路径不存在！");
        }
        if (StringUtils.isBlank(operateConfig.getGitStartCommitId())){
            return OperateResult.ErrorResult("起始commit id不能为空！");
        }
        if (StringUtils.isBlank(operateConfig.getGitEndCommitId())){
            return OperateResult.ErrorResult("结束commit id不能为空！");
        }
        if (StringUtils.isBlank(operateConfig.getLocalSaveFilePath())){
            return OperateResult.ErrorResult("本地文件路径不能为空！");
        }
        File file = new File(operateConfig.getLocalSaveFilePath());
        if (!file.exists()){
            return OperateResult.ErrorResult("本地文件路径不存在！");
        }
        return OperateResult.SuccessResult();
    }

    /**
     * 对路径进行替换操作
     * @param path 项目路径
     * @return 返回路径
     */
    private static String replacePath(String path){
        path = path.replaceAll("src/main/java|src/main/resources|src/java|src/source","WEB-INF/classes");
        path = path.replaceAll("src/main/webapp/|WebRoot/","");
        if (path.endsWith(".java")){
            path = path.replace(".java",".class");
        }
        return path;
    }
}
