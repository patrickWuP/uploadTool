package com.wup.util;

import com.jcraft.jsch.*;
import com.wup.bean.OperateResult;

import java.io.File;
import java.util.Properties;

/**
 * @Description : 操作服务端文件工具类
 * @Project : imoocsecurity
 * @Program Name  : com.wup.util.SfttpUtil
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class SfttpUtil {
    private static Session session = null;
    private static ChannelSftp sftp = null;

    public static OperateResult initSftp(String host, int port, String userName, String password) {
        Channel channel = null;
        try {
            initSshdSession(host, port, userName, password);
            //参数sftp指明要打开的连接是sftp连接
            channel = session.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            if(sftp == null){
                return OperateResult.ErrorResult("ssh连接失败，请核实连接参数！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return OperateResult.ErrorResult("ssh连接异常，请核实连接参数！");
        }
        return OperateResult.SuccessResult();
    }

    private static void initSshdSession(String host, int port, String userName, String password) throws Exception{
        JSch jsch = new JSch();
        session = jsch.getSession(userName, host, port);
        if (password != null && !"".equals(password)) {
            session.setPassword(password);
        }
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");// do not verify host key
        session.setConfig(sshConfig);
        session.setTimeout(10000);
        session.setServerAliveInterval(1000 * 60 * 10);
        session.connect();
    }

    public static void upload(String srcFile,String dstFile) throws SftpException {
        try{
            File file = new File(dstFile);
            String parentPath = file.getParent().replaceAll("\\\\","/");
//            sftp.mkdir(parentPath);
            exec("mkdir -p " + parentPath);
            sftp.put(srcFile,dstFile);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void exec(String cmd){
        ChannelExec exec = null;
        try {
            //参数sftp指明要打开的连接是exec连接
            exec = (ChannelExec)session.openChannel("exec");
            exec.setCommand(cmd);
            exec.connect();
            exec.setErrStream(System.err);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(exec != null && exec.isConnected()){
                exec.disconnect();
            }
        }
    }
}
