package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FtpUtil {

  private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);

  private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");

  private static String ftpUser = PropertiesUtil.getProperty("ftp.user");

  private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");

  private String ip;

  private int port;

  private String user;

  private String pwd;

  private FTPClient ftpClient;

  public static boolean uploadFile(List<File> fileList){
    FtpUtil ftpUtil = new FtpUtil(ftpIp,21,ftpUser,ftpPass);
    logger.info("开始连接服务器");
    boolean result = ftpUtil.uploadFile("img",fileList);
    logger.info("开始连接服务器,结束上传，上传结果{}",result);
    return result;
  }

  private boolean uploadFile(String remotePath, List<File> fileList){
    boolean upload = true;
    FileInputStream fis = null;
    //连接ftp服务器
    if(connectionServer(this.ip,this.user,this.pwd)){
      try {
        ftpClient.changeWorkingDirectory(remotePath);
        ftpClient.setBufferSize(1024);
        ftpClient.setControlEncoding("UTF-8");
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
        for(File file : fileList){
          fis = new FileInputStream(file);
          ftpClient.storeFile(file.getName(),fis);
        }
      } catch (IOException e) {
        logger.error("上传文件异常",e);
      }finally {
        try {
          fis.close();
          ftpClient.disconnect();
        } catch (IOException e) {
          upload = false;
         logger.error("关闭流失败",e);
        }
      }
    }
    return upload;
  }
  private boolean connectionServer(String ip, String user, String pwd){

    boolean isSuccess = false;
    ftpClient = new FTPClient();
    try {
      ftpClient.connect(ip);
      isSuccess = ftpClient.login(user,pwd);
    } catch (IOException e) {
      logger.error("连接ftp服务器失败",e);
    }
    return isSuccess;
  }

  public FtpUtil(final String ip, final int port, final String user,
          final String pwd) {
    this.ip = ip;
    this.port = port;
    this.user = user;
    this.pwd = pwd;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(final String ip) {
    this.ip = ip;
  }

  public int getPort() {
    return port;
  }

  public void setPort(final int port) {
    this.port = port;
  }

  public String getUser() {
    return user;
  }

  public void setUser(final String user) {
    this.user = user;
  }

  public String getPwd() {
    return pwd;
  }

  public void setPwd(final String pwd) {
    this.pwd = pwd;
  }
}
