package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class IFileServiceImpl implements IFileService {

  private Logger logger = LoggerFactory.getLogger(IFileServiceImpl.class);

  public String upload(MultipartFile file, String path){

    String fileName = file.getOriginalFilename();
    String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
    String newFileName = UUID.randomUUID()+"."+fileExtensionName;
    logger.info("开始上传文件，文件名：{}, 上传路径 ：{}, 新文件名：{}",fileName,path,newFileName);

    File fileDir = new File(path);
    if(!fileDir.exists()){
      fileDir.setWritable(true);
      fileDir.mkdirs();
    }

    File targetFile = new File(path,newFileName);

    try {
      //文件上传成功
      file.transferTo(targetFile);

      // targetFile上传到ftp服务器
      FtpUtil.uploadFile(Lists.newArrayList(targetFile));

      // 上传完后，删除upload下文件
      targetFile.delete();
    } catch (IOException e) {
      logger.error("文件上传失败",e);
      return null;
    }
    return targetFile.getName();
  }


}
