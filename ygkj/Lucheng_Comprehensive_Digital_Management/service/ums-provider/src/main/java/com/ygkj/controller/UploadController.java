package com.ygkj.controller;

import com.ygkj.auth.api.file.model.SysFile;
import com.ygkj.constants.Constant;
import com.ygkj.exception.code.BaseResponseCode;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.service.SysFileService;
import com.ygkj.utils.DataResult;
import com.ygkj.utils.FileUtil;
import com.ygkj.utils.HttpContextUtils;
import com.ygkj.utils.QiniuUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author Huang.zh
 * @Description 文件上传工具类
 * @Date 2020/9/2 10:43
 */
@RestController
@RequestMapping(value = "/upload")
public class UploadController {

    @Autowired
    private SysFileService sysFileService;

    @Value("${sca.file.upload.mode}")
    private String mode;

    @PostMapping("")
    public Object upload(@RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile) {
        Map<String, Object> result = new HashMap<>();
        String filename = uploadFile.getOriginalFilename();// 获取file名称
        String fileName[] = filename.split("\\.");
        String type = fileName[fileName.length - 1];
        if (!allowedType(type)){
            return DataResult.getResult(BaseResponseCode.OPERATION_ERRO, "文件格式不正确，请重新上传~");
        }
        switch (mode) {
            case "qiniu":
                result = uploadQiNiu(uploadFile);
                break;
            case "local":
                result = uploadLocal(uploadFile);
                break;
            default:
                break;
        }
        if ((boolean) result.get("success")) {
            String token = HttpContextUtils.getHttpServletRequest().getHeader(Constant.ACCESS_TOKEN);
            String userId = null;
            if (!StringUtils.isEmpty(token)) {
                userId = JwtTokenUtil.getUserId(token);
            }
            SysFile sysFile = new SysFile(UUID.randomUUID().toString(), String.valueOf(result.get("originalName")), String.valueOf(result.get("filePath")),
                    String.valueOf(result.get("ext")), Double.valueOf(String.valueOf(result.get("fileSize"))), userId, new Date());

            try {
                result.put("id", sysFile.getId());
                sysFileService.saveFile(sysFile);
                return DataResult.getResult(BaseResponseCode.SUCCESS, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return DataResult.getResult(BaseResponseCode.OPERATION_ERRO, "");
    }

    @PostMapping("/uploadBase64")
    public Object base64UpLoad(@RequestParam("base64Data") String base64Data, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        try {
            String dataPrix = "";
            String data = "";
            if (base64Data == null || "".equals(base64Data)) {
                throw new Exception("上传失败，上传图片数据为空");
            } else {
                String[] d = base64Data.split("base64,");
                if (d != null && d.length == 2) {
                    dataPrix = d[0];
                    data = d[1];
                } else {
                    throw new Exception("上传失败，数据不合法");
                }
            }
            String suffix = "";
            if ("data:image/jpeg;".equalsIgnoreCase(dataPrix)) {//data:image/jpeg;base64,base64编码的jpeg图片数据
                suffix = ".jpg";
            } else if ("data:image/x-icon;".equalsIgnoreCase(dataPrix)) {//data:image/x-icon;base64,base64编码的icon图片数据
                suffix = ".ico";
            } else if ("data:image/gif;".equalsIgnoreCase(dataPrix)) {//data:image/gif;base64,base64编码的gif图片数据
                suffix = ".gif";
            } else if ("data:image/png;".equalsIgnoreCase(dataPrix)) {//data:image/png;base64,base64编码的png图片数据
                suffix = ".png";
            } else if ("data:video/mp4;".equalsIgnoreCase(dataPrix)) {//data:image/png;base64,base64编码的png图片数据
                suffix = ".mp4";
            } else {
                throw new Exception("上传图片格式不合法");
            }
            String tempFileName = UUID.randomUUID().toString() + suffix;//因为BASE64Decoder的jar问题，此处使用spring框架提供的工具包
            byte[] bs = Base64Utils.decodeFromString(data);
            try {//使用apache提供的工具类操作流
                System.out.println(request.getServletContext().getRealPath("/upload"));
                FileUtils.writeByteArrayToFile(new File(request.getServletContext().getRealPath("/upload"), tempFileName), bs);

                String filePath = QiniuUtil.upload(bs, tempFileName);

                String token = HttpContextUtils.getHttpServletRequest().getHeader(Constant.ACCESS_TOKEN);
                String userId = JwtTokenUtil.getUserId(token);
                SysFile sysFile = new SysFile(UUID.randomUUID().toString(), filePath, filePath, suffix, null, userId, new Date());
                result.put("id", sysFile.getId());
                sysFileService.saveFile(sysFile);
            } catch (Exception ee) {
                return DataResult.getResult(BaseResponseCode.OPERATION_ERRO, "");
            }
            return DataResult.getResult(BaseResponseCode.SUCCESS, result);
        } catch (Exception e) {
            return DataResult.getResult(BaseResponseCode.OPERATION_ERRO, "");
        }
    }

    private Map<String, Object> uploadQiNiu(MultipartFile uploadFile) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", false);
        try {
            if (uploadFile != null) {
                String file_name = uploadFile.getOriginalFilename();
                String file_names[] = file_name.split("\\.");
                String filePath = QiniuUtil.upload(
                        FileUtil.input2byte(uploadFile.getInputStream()),
                        FileUtil.getSHA1Checksum(uploadFile.getInputStream())
                                + "." + file_names[file_names.length - 1]);
                result.put("success", true);
                result.put("filePath", filePath);
                result.put("ext", file_names[file_names.length - 1]);
                result.put("originalName", uploadFile.getOriginalFilename());
                result.put("fileSize", fileSize(uploadFile));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @Author Huang.zh
     * @Description 本地上传方法
     * @Date 2020/9/2 13:47
     */
    private Map<String, Object> uploadLocal(MultipartFile uploadFile) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", false);

        InputStream inStream = null; // 创建字节输入流
        BufferedInputStream bInStream = null; // 创建缓冲输入字节流
        OutputStream outStream = null; // 创建字节输出流
        BufferedOutputStream bOutStream = null; // 创建缓冲字节输出流
        try {

            // 文件保存路径
            String filePath = "E://uploadImage//"; // 映射的地址
            String filename = uploadFile.getOriginalFilename();// 获取file名称


            String fileName[] = filename.split("\\.");
            String path = UUID.randomUUID() + "." + fileName[1];

            File targetFile = new File(filePath);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }

            inStream = uploadFile.getInputStream();// 创建文件输入流，指向路径可以是：FilePath1，也可以是file1
            bInStream = new BufferedInputStream(inStream); // 用缓冲输入流装饰文件输入流
            outStream = new FileOutputStream(filePath + path); // 创建文件输出流，指向目标路径
            bOutStream = new BufferedOutputStream(outStream); // 用缓冲输出流来包装文件输出流

            int i = 0;
            while ((i = bInStream.read()) != -1) {
                bOutStream.write(i); // 对接流，输出文件
            }

            bOutStream.close();
            bInStream.close();
            result.put("success", true);
            result.put("filePath", path);
            result.put("originalName", filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private float fileSize(MultipartFile uploadFile) {
        float fiseSize;
        long oriSize = uploadFile.getSize();
        fiseSize = oriSize;
        DecimalFormat df = new DecimalFormat("#.00");
        String fmt = df.format(fiseSize / 1024);
        return Float.parseFloat(fmt);
    }

    public Boolean allowedType(String type){
        String[] types = {"JPG","PNG","JPEG","GIF","PDF","DOC","DOCX","XLS","XLSX","MP4","AVI","RMVB"};
        for (String s : types) {
            if (s.equalsIgnoreCase(type)){
                return true;
            }
        }
        return false;
    }
}
