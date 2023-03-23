package com.ygkj.digitization.util;

import com.aspose.words.Document;
import com.ygkj.digitization.feign.NoticeClient;
import com.ygkj.digitization.mapper.WarnUserMapper;
import com.ygkj.digitization.vo.response.WarnUserRespVo;
import com.ygkj.project.model.SysUser;
import com.ygkj.utils.StringUtils;
import com.ygkj.warning.model.SysSendMessageLog;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
public class CommonUtil {

    /**
     * 上传pdf文件到七牛，得到七牛的地址
     * @param sendUnit
     * @param lsName
     * @param curTime
     * @param year
     * @param time
     * @return
     * @throws Exception
     */
    public static String uploadPdfToqiNiu(String sendUnit,String lsName,String curTime,
                                   String year,String time,String deatilInfo,String type) throws Exception {
        String filePath = "";
        if (AsposeUtils.checkLicense()) {
            String path = "";
            if ("1".equals(type) || "2".equals(type)) {
                path = "aspose" + File.separator + "warnRuleWater.docx";
            } else {
                path = "aspose" + File.separator + "warnRuleOther.docx";
            }
            InputStream docTempInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            Document doc = new Document(docTempInputStream);
            //设置表格中的固定内
            doc.getRange().replace("$sendUtil$", sendUnit, true, false);//发送单位
            doc.getRange().replace("$lsName$", lsName, true, false);//签发人
            doc.getRange().replace("$curTime$", curTime, true, false);
            doc.getRange().replace("$year$", year, true, false);
            doc.getRange().replace("$time$", time, true, false);
            doc.getRange().replace("$deatilInfo$", deatilInfo, true, false);

            String pathPdf = "aspose" + File.separator + System.currentTimeMillis() + ".pdf";
            doc.save(pathPdf);
            byte[] bytes = fileToByteArray(pathPdf);
            //上传到七牛
            filePath = QiniuUtil.upload(bytes, UUID.randomUUID().toString()+ ".pdf");
        }
        return filePath;
    }

    public static byte[] fileToByteArray(String filePath) {
        //创建源与目的地
        File src = new File(filePath);//获得文件的源头，从哪开始传入(源)
        byte[] dest = null;//最后在内存中形成的字节数组(目的地)
        //选择流
        InputStream is = null;//此流是文件到程序的输入流
        ByteArrayOutputStream baos = null;//此流是程序到新文件的输出流
        //操作(输入操作)
        try {
            is = new FileInputStream(src);//文件输入流
            baos = new ByteArrayOutputStream();//字节输出流，不需要指定文件，内存中存在
            byte[] flush = new byte[1024 * 10];//设置缓冲，这样便于传输，大大提高传输效率
            int len = -1;//设置每次传输的个数,若没有缓冲的数据大，则返回剩下的数据，没有数据返回-1
            while ((len = is.read(flush)) != -1) {
                baos.write(flush, 0, len);//每次读取len长度数据后，将其写出
            }
            baos.flush();//刷新管道数据
            dest = baos.toByteArray();//最终获得的字节数组
            return dest;//返回baos在内存中所形成的字节数组
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //释放资源,文件需要关闭,字节数组流无需关闭
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * 发送预警短信信息
     * @param peopleIds
     * @param sendInfoDeatilInfo
     */
    public static void sendSms(String peopleIds, String sendInfoDeatilInfo, String type,
                               WarnUserMapper warnUserMapper, NoticeClient noticeClient) {
        List<SysSendMessageLog> list = new ArrayList<>();
        if (!StringUtils.isEmpty(peopleIds)) {
            SysSendMessageLog messageLog = null;
            List<WarnUserRespVo> userList = warnUserMapper.findUserList(peopleIds);
            for (WarnUserRespVo sysUser : userList) {
                messageLog = new SysSendMessageLog();
                messageLog.setSendType("短信");
                messageLog.setContent(sendInfoDeatilInfo);
                messageLog.setPhone(sysUser.getPhone());
                messageLog.setDestinationUserName(sysUser.getUserName());
                messageLog.setDestinationUserId(sysUser.getId());
                messageLog.setFromUserName("厉海斌");
                messageLog.setFromUserId("0cbda2f0d5e043b3a4e5c07b0e8bf1ea");
                messageLog.setType(type);
                messageLog.setSendTime(new Date());
                messageLog.setNeedHandle(false);
                messageLog.setSystem(SysSendMessageLog.SystemType.HYDROLOGY_DIGITIZATION.type());
                messageLog.setMessageType(SysSendMessageLog.messageType.WARNING.type());
                list.add(messageLog);
            }
            log.info("数据的条数:{}",list.size());
            try {
                noticeClient.sendMessage(list);
            } catch (Exception e) {}
        }
    }
}
