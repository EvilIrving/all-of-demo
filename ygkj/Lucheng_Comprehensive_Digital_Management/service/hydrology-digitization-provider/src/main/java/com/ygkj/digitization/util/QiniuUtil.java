package com.ygkj.digitization.util;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ygkj.com.util.PutRet;

import java.io.File;
import java.io.InputStream;

public class QiniuUtil {


    public static final String QINIU_ACCESS_KEY = "zbWy1VrivZg5PX98B4-O4G9A8NNw3OfMspMP88QA";
    public static final String QINIU_SECRET_KEY = "FLRsshUmGSynNFuTZTtLtuIvODKANEj1HqoM9jYk";
    private static final String BUCKET_NAME = "skstandard";
    public static final String QINIU_URL = "https://file.ygwjg.com/";
    private static final int FILE_PUT_SUCCESS = 200;

    private static Logger logger = LoggerFactory.getLogger(QiniuUtil.class);

    static Auth auth = Auth.create(QINIU_ACCESS_KEY, QINIU_SECRET_KEY);
    private static BucketManager bucketManager = new BucketManager(auth);

    // 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象
    static UploadManager uploadManager = new UploadManager();

    // 简单上传，使用默认策略
    public static String getUpToken0(){
        return auth.uploadToken(BUCKET_NAME);
    }

    // 覆盖上传
    public String getUpToken1(String key){
        return auth.uploadToken(BUCKET_NAME, key);
    }

    public static String upload(String filename) {
        try {
            logger.info("put qiniu filename=" + filename);
            String filetype = FileUtil.getFileExt(filename);
            //获取文件SHA1值
            String filekey = FileUtil.getSHA1Checksum(filename) + "."+ filetype;
            File file = new File(filename);

            //判断七牛是否已存在该文件
            FileInfo info=getFileInfo(filekey);
            if (info!=null) {
                if (file.exists()) {
                    file.delete();
                }
                return QINIU_URL + filekey;
            }

            //上传文件到七牛
            Response res = uploadManager.put(file, filekey, getUpToken0());
            PutRet ret = res.jsonToObject(PutRet.class);
            logger.info(res.toString());
            logger.info(res.bodyString());
            logger.info(QINIU_URL + ret.getKey());
            if (res.statusCode == FILE_PUT_SUCCESS) {
                if (file.exists()) {
                    file.delete();
                }
                return QINIU_URL + ret.getKey();
            }
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时简单状态信息
            logger.error(r.toString());
            try {
                // 响应的文本信息
                logger.error(r.bodyString());
            } catch (QiniuException e1) {
                return null;
            }
        } catch (Exception e) {
            // 响应的文本信息
            logger.error(e.getMessage());
            return null;
        }
        return null;
    }

    public static String upload( InputStream in,String filetype) {
        try {

            //获取文件SHA1值
            String filekey = FileUtil.getSHA1Checksum(in)+ "."+ filetype;	;

            //判断七牛是否已存在该文件
            FileInfo info=getFileInfo(filekey);
            if (info!=null) {
                return QINIU_URL + filekey;
            }
            byte[] buf=FileUtil.input2byte(in);
            in.close();
            //上传文件到七牛
            Response res = uploadManager.put(buf, filekey, getUpToken0());
            PutRet ret = res.jsonToObject(PutRet.class);
            logger.info(res.toString());
            logger.info(res.bodyString());
            logger.info(QINIU_URL + ret.getKey());
            if (res.statusCode == FILE_PUT_SUCCESS) {
                return QINIU_URL + ret.getKey();
            }
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时简单状态信息
            logger.error(r.toString());
            try {
                // 响应的文本信息
                logger.error(r.bodyString());
            } catch (QiniuException e1) {
                return null;
            }
        } catch (Exception e) {
            // 响应的文本信息
            logger.error(e.getMessage());
            return null;
        }
        return null;
    }

    public static String upload( byte[] data,String fileName) {


        try {
            if(data.length==0){
                return null;
            }
            //判断七牛是否已存在该文件
            FileInfo info=getFileInfo(fileName);
            if (info!=null) {
                return QINIU_URL + fileName;
            }
            //上传文件到七牛
            Response res = uploadManager.put(data, fileName, getUpToken0());
            PutRet ret = res.jsonToObject(PutRet.class);
            logger.info(res.toString());
            logger.info(res.bodyString());
            logger.info(QINIU_URL + ret.getKey());
            if (res.statusCode == FILE_PUT_SUCCESS) {
                return QINIU_URL + ret.getKey();
            }
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时简单状态信息
            logger.error(r.toString());
            try {
                // 响应的文本信息
                logger.error(r.bodyString());
            } catch (QiniuException e1) {
                return null;
            }
        } catch (Exception e) {
            // 响应的文本信息
            logger.error(e.getMessage());
            return null;
        }
        return null;
    }


    /**
     * 通过文件的HASH值获取七牛存储文件信息
     *
     * @param hashfilename
     * @return
     * @throws Exception
     */
    public static FileInfo getFileInfo(String hashfilename) {
        FileInfo info;
        try {
            info = bucketManager.stat(BUCKET_NAME, hashfilename);
            logger.info("---七牛文件已存在："+hashfilename);
            return info;
        } catch (QiniuException e) {
            logger.info("---七牛文件不存在"+hashfilename);
            Response r = e.response;
            logger.error(r.toString());
            return null;
        }

    }

    public static void delFile(String hashfilename){

        try {
            bucketManager.delete(BUCKET_NAME, hashfilename);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }

}
