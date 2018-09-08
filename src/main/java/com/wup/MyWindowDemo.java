package com.wup;

import com.wup.bean.OperateConfig;
import com.wup.bean.OperateResult;
import com.wup.util.ConfigUtil;
import com.wup.util.OperateUtil;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @Description : 输入参数窗体
 * @Project : imoocsecurity
 * @Program Name  : com.wup.MyWindowDemo
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class MyWindowDemo {
    //展示的弹出展示的整体布局
    private Frame f;
    //本地git项目路径
    private TextField gitProjectPathTf;
    //起始git commit id
    private TextField startCommitIdTf;
    //结束git commit id
    private TextField endCommitIdTf;
    //本地编译后的项目路径
    private TextField compileProjectPathTf;
    //服务端项目路径
    private TextField serverProjectPathTf;
    //本地保存文件更新信息
    private TextField localFileTf;

    /**
     * ssh连接参数
     */
    //服务端ip
    private TextField serverIpTf;
    //服务端端口
    private TextField serverPortTf;
    //服务端用户名
    private TextField serverUserNameTf;
    //服务端密码
    private TextField serverPwdTf;

    //获取更新文件列表按钮
    private Button getUpdateFileBtn;
    //上传更新文件按钮
    private Button uploadFileBtn;

    private Label tipLab;
    private Dialog tipdialog;
    private Button okBut;

    //初始化操作配置信息
    private OperateConfig operateConfig = null;

    public MyWindowDemo(){
        init();
    }

    private void init(){
        f = new Frame("My Window");
        f.setBounds(300,200,600,400);
        f.setLayout(new FlowLayout());
        //本地git项目路径
        Label labProjectPath = new Label("本地git源项目路径");
        gitProjectPathTf = new TextField(60);
        f.add(labProjectPath);
        f.add(gitProjectPathTf);
        //起始git commit id 设置
        Label labStart = new Label("起始git commit id");
        startCommitIdTf = new TextField(60);
        f.add(labStart);
        f.add(startCommitIdTf);
        //结束git commit id 设置
        Label labEnd = new Label("结束git commit id");
        endCommitIdTf = new TextField(60);
        f.add(labEnd);
        f.add(endCommitIdTf);
        //本地编译后的项目地址
        Label labCompileProject = new Label("编译后的项目路径");
        compileProjectPathTf = new TextField(60);
        f.add(labCompileProject);
        f.add(compileProjectPathTf);
        //服务端项目路径
        Label labServerProject = new Label("服务端的项目路径");
        serverProjectPathTf = new TextField(60);
        f.add(labServerProject);
        f.add(serverProjectPathTf);
        //本地保存更新文件信息
        Label labLocalFilePath = new Label("保存更新文件路径");
        localFileTf = new TextField(60);
        f.add(labLocalFilePath);
        f.add(localFileTf);
        /**
         * ssh配置参数
         */
        Label sshConfig = new Label("===============================以下为ssh配置参数===============================");
        f.add(sshConfig);
        //服务端ip
        Label serverIp = new Label("linux服务端ip      ");
        serverIpTf = new TextField(60);
        f.add(serverIp);
        f.add(serverIpTf);
        //服务端端口号
        Label serverPort = new Label("linux服务端端口  ");
        serverPortTf = new TextField(60);
        f.add(serverPort);
        f.add(serverPortTf);
        //服务端用户名
        Label serverUserName = new Label("服务端用户名      ");
        serverUserNameTf = new TextField(60);
        f.add(serverUserName);
        f.add(serverUserNameTf);
        //服务端密码
        Label serverPwd = new Label("服务端密码         ");
        serverPwdTf = new TextField(60);
        f.add(serverPwd);
        f.add(serverPwdTf);

        //获取更新文件列表按钮设置
        getUpdateFileBtn = new Button("获取更新文件列表");
        f.add(getUpdateFileBtn);
        //上传更新文件列表按钮
        uploadFileBtn = new Button("上传更新文件列表");
        f.add(uploadFileBtn);
        //弹出提示框设置
        tipdialog = new Dialog(f,"提示信息",true);
        tipdialog.setBounds(400,200,350,130);
        tipdialog.setLayout(new FlowLayout());
        tipLab = new Label("");
        okBut = new Button("确定");
        tipdialog.add(tipLab);
        tipdialog.add(okBut);

        myEvent();
        //加载已保存的配置信息
        getSavedConfig();

        f.setVisible(true);
    }

    /**
     * 加载已保存的配置信息
     */
    private void getSavedConfig(){
        //初始化操作配置信息并赋值
        operateConfig = ConfigUtil.getOperateConfig();
        //本地git项目路径
        gitProjectPathTf.setText(operateConfig.getGitProjectPath());
        //起始git commit id
        startCommitIdTf.setText(operateConfig.getGitStartCommitId());
        //结束git commit id
        endCommitIdTf.setText(operateConfig.getGitEndCommitId());
        //本地编译后的项目路径
        compileProjectPathTf.setText(operateConfig.getCompileProjectPath());
        //服务端项目路径
        serverProjectPathTf.setText(operateConfig.getServerProjectPath());
        //本地保存文件更新信息
        localFileTf.setText(operateConfig.getLocalSaveFilePath());
        //服务端ip
        serverIpTf.setText(operateConfig.getServerIp());
        //服务端端口
        serverPortTf.setText(operateConfig.getServerPort());
        //服务端用户名
        serverUserNameTf.setText(operateConfig.getServerUserName());
        //服务端密码
        serverPwdTf.setText(operateConfig.getServerPwd());
    }

    /**
     * 设置界面操作事件
     */
    private void myEvent(){
        //提示信息框的确认按钮
        okBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tipdialog.setVisible(false);
            }
        });
        //提示信息框的默认关闭操作
        tipdialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                tipdialog.setVisible(false);
            }
        });
        //整体弹出样式关闭
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //获取更新文件列表按钮
        getUpdateFileBtn.addActionListener(e -> {
            //初始化配置信息
            initOperateConfig();
            //进行获取更新文件列表操作处理
            OperateResult result = OperateUtil.getUpdateFile(operateConfig);
            //对结果进行处理
            resultHandler(result);
        });

        //上传更新文件列表按钮操作
        uploadFileBtn.addActionListener(e -> {
            //初始化配置信息
            initOperateConfig();
            //执行上传操作
            OperateResult result = OperateUtil.upLoadFile(operateConfig);
            //对结果进行处理
            resultHandler(result);
        });
    }

    /**
     * 对操作结果进行处理
     * @param result 操作结果
     */
    private void resultHandler(OperateResult result){
        //执行失败弹出提示信息
        if (result.getStatus() == OperateResult.ERROR_STATUS){
            showTip(result.getMsg());
        }else {
            showTip("操作成功!");
            ConfigUtil.saveOperateConfig(operateConfig);
        }
    }
    /**
     * 弹出提示信息
     * @param text 提示信息内容
     */
    private void showTip(String text){
        if (StringUtils.isNotBlank(text)){
            tipLab.setText(text);
            tipdialog.setVisible(true);
        }
    }

    /**
     * 进行操作时，对操作配置信息进行赋值
     */
    private void initOperateConfig(){
        operateConfig.setGitProjectPath(gitProjectPathTf.getText());
        operateConfig.setGitStartCommitId(startCommitIdTf.getText());
        operateConfig.setGitEndCommitId(endCommitIdTf.getText());
        operateConfig.setCompileProjectPath(compileProjectPathTf.getText());
        operateConfig.setServerProjectPath(serverProjectPathTf.getText());
        operateConfig.setLocalSaveFilePath(localFileTf.getText());
        operateConfig.setServerIp(serverIpTf.getText());
        operateConfig.setServerPort(serverPortTf.getText());
        operateConfig.setServerUserName(serverUserNameTf.getText());
        operateConfig.setServerPwd(serverPwdTf.getText());
    }
}
