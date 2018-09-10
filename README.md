# swing
文件上传工具说明：<br/>
将源码git clone https://github.com/patrickWuP/uploadTool.git,通过mvn install编译成功后，确保target中生成swing-1.1.0-SNAPSHOT.jar即可。<br/>
1.环境要求：确保本地装有git，jdk，开发时jdk版本为1.8，最好使用1.8进行启动，其他版本没有做兼容测试。<br/>
2.使用java -jar启动即可。<br/>
3.使用说明，【获取更新文件】必填字段：开始commit id、结束commit id、git源项目路径、本地保存文件路径；<br/>
输入本地编译后项目路径会返回与其相关文件信息，输入服务器端项目路径会返回与其相关文件信息，不输入则不生成。<br/>
【上传更新文件】所有字段都必填，同时生成一份更新文件列表文档在本地保存路径。	<br/>
4.常见问题：项目目录结构暂不能有中文<br/>
	   做了参数一般性校验，参数格式未做校验，请注意参数格式问题<br/>
	   lib/config.txt保存了配置信息，包括ssh连接参数，传播该工具时，请记得删除改文件信息，避免敏感数据泄露<br/>
5.本项目参考项目如下：https://github.com/surpassE/java-sshd<br/>
  本项目路径如下：https://github.com/patrickWuP/uploadTool

如有任何问题请@wupeng6684@gmail.com，谢谢
