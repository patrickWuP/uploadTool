package com.wup.util;

import com.alibaba.fastjson.JSON;
import com.wup.bean.OperateConfig;

import java.io.*;

/**
 * @Description : TODO wupeng 类描述
 * @Project : imoocsecurity
 * @Program Name  : com.wup.util.ConfigUtil
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class ConfigUtil {

    /**
     * C:\project\swing\target\swing-1.1.0-SNAPSHOT.jar!\config.txt (系统找不到指定的路径。)
     * 放弃该方式读取配置信息
     * private static final String ROOT_PATH = ConfigUtil.class.getClass().getResource("/config.txt").getPath().replaceFirst("file:","");
     */
    private static final String ROOT_PATH;

    static {
         //将config.txt文件放在外部的lib中,没有则创建
        String path = ConfigUtil.class.getClass().getResource("/config.txt").getPath().replaceFirst("file:","");
        //打包成jar包路径获取逻辑
        if (path.indexOf("swing-1.1.0-SNAPSHOT.jar") != -1){
            ROOT_PATH = path.substring(0,path.indexOf("swing-1.1.0-SNAPSHOT.jar")) + "lib/config.txt";
        }else {
            //本地调试配置文件路径
            ROOT_PATH = path.substring(0,path.indexOf("classes")) + "lib/config.txt";
        }
        File file = new File(ROOT_PATH);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 操作成功后，保存当前操作配置信息至config.txt
     */
    public static void saveOperateConfig(OperateConfig operateConfig){
        String config = JSON.toJSONString(operateConfig);
        File file = new File(ROOT_PATH);
        Writer writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(config);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化操作配置信息
     * @return OperateConfig
     */
    public static OperateConfig getOperateConfig(){
        OperateConfig operateConfig = null;
        File file = new File(ROOT_PATH);
        try(InputStream inputStream = new FileInputStream(file)){
            byte[] contentByte = new byte[inputStream.available()];
            inputStream.read(contentByte);
            String config = new String(contentByte);
            operateConfig = JSON.parseObject(config,OperateConfig.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return operateConfig == null ? new OperateConfig() : operateConfig;
    }

    public static void main(String[] args) {
//        OperateConfig operateConfig = new OperateConfig();
//        operateConfig.setGitStartCommitId("123424");
//        operateConfig.setGitEndCommitId("2342343");
//        saveOperateConfig(operateConfig);
        getOperateConfig();
//        System.out.println(System.getProperty("java.class.path"));
        String url = "C:/project/swing/target/swing-1.1.0-SNAPSHOT.jar!/config.txt";
        url = url.substring(0,url.indexOf("swing-1.1.0-SNAPSHOT.jar")) + "lib/config.txt";
        System.out.println(url);
    }
}
