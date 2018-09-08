package com.wup.bean;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @Description : 操作相关配置信息
 * @Project : imoocsecurity
 * @Program Name  : com.wup.bean.OperateConfig
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class OperateConfig {
    //git本地项目路径
    private String gitProjectPath;
    //起始gitcommitid
    private String gitStartCommitId;
    //结束gitcommitid
    private String gitEndCommitId;
    //编译后的项目路径
    private String compileProjectPath;
    //服务端项目路径
    private String serverProjectPath;
    //本地文件保存路径
    private String localSaveFilePath;

    //服务端ip
    private String serverIp;
    //服务端端口
    private String serverPort;
    //服务端用户名
    private String serverUserName;
    //服务端密码
    private String serverPwd;


    public String getCompileProjectPath() {
        return compileProjectPath;
    }

    public void setCompileProjectPath(String compileProjectPath) {
        this.compileProjectPath = operateFilePath(compileProjectPath);
    }

    public String getServerProjectPath() {
        return serverProjectPath;
    }

    public void setServerProjectPath(String serverProjectPath) {
        this.serverProjectPath = operateFilePath(serverProjectPath);
    }

    public String getGitProjectPath() {
        return gitProjectPath;
    }

    public void setGitProjectPath(String gitProjectPath) {
        this.gitProjectPath = operateFilePath(gitProjectPath);
    }

    public String getGitStartCommitId() {
        return gitStartCommitId;
    }

    public void setGitStartCommitId(String gitStartCommitId) {
        this.gitStartCommitId = gitStartCommitId;
    }

    public String getGitEndCommitId() {
        return gitEndCommitId;
    }

    public void setGitEndCommitId(String gitEndCommitId) {
        this.gitEndCommitId = gitEndCommitId;
    }

    public String getLocalSaveFilePath() {
        return localSaveFilePath;
    }

    public void setLocalSaveFilePath(String localSaveFilePath) {
        this.localSaveFilePath = operateFilePath(localSaveFilePath);
    }

    //替换\为/,如果没有/将文件路径末尾+/
    private static String operateFilePath(String path){
        path = path.replaceAll("\\\\","/");
        if (!path.endsWith("/") && StringUtils.isNotBlank(path)){
            path = path + "/";
        }
        return path;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerUserName() {
        return serverUserName;
    }

    public void setServerUserName(String serverUserName) {
        this.serverUserName = serverUserName;
    }

    public String getServerPwd() {
        return serverPwd;
    }

    public void setServerPwd(String serverPwd) {
        this.serverPwd = serverPwd;
    }
}
