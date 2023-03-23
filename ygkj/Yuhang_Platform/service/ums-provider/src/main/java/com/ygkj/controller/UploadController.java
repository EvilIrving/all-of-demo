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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
public class UploadController{

	@Autowired
	private SysFileService sysFileService;

	@Value("${sca.file.upload.mode}")
	private String mode;
	
	@PostMapping("")
	public Object upload(@RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile) {
		Map<String, Object> result = new HashMap<>();
		switch (mode){
			case "qiniu":
				result = uploadQiNiu(uploadFile);
				break;
			case "local":
				result = uploadLocal(uploadFile);
				break;
			default:
				break;
		}
		if ((boolean)result.get("success")){
			String token = HttpContextUtils.getHttpServletRequest().getHeader(Constant.ACCESS_TOKEN);
			String userId = JwtTokenUtil.getUserId(token);
			SysFile sysFile = new SysFile(UUID.randomUUID().toString(), String.valueOf(result.get("originalName")), String.valueOf(result.get("filePath")),
					String.valueOf(result.get("ext")), Double.valueOf(String.valueOf(result.get("fileSize"))), userId, new Date());
			try {
				result.put("id",sysFile.getId());
				sysFile.setFileCode(sysFile.getId());
				sysFileService.saveFile(sysFile);
				return DataResult.getResult(BaseResponseCode.SUCCESS,result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return DataResult.getResult(BaseResponseCode.OPERATION_ERRO,"");
	}

	private Map<String, Object> uploadQiNiu(MultipartFile uploadFile){
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


			String fileName[] =filename.split("\\.");
			String path = UUID.randomUUID() +"."+fileName[1];

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
			result.put("filePath",path);
			result.put("originalName", filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private float fileSize(MultipartFile uploadFile){
		float fiseSize;
		long oriSize = uploadFile.getSize();
		fiseSize = oriSize;
		DecimalFormat df = new DecimalFormat("#.00");
		String fmt = df.format(fiseSize/1024);
		return Float.parseFloat(fmt);
	}
}
