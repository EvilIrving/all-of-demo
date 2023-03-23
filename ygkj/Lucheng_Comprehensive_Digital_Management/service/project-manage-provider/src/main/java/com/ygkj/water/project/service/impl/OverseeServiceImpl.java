package com.ygkj.water.project.service.impl;

import com.ygkj.auth.VO.request.LoginReqVO;
import com.ygkj.auth.VO.response.LoginRespVO;
import com.ygkj.gragh.model.SysUser;
import com.ygkj.project.model.ProjectOversee;
import com.ygkj.project.model.ProjectOverseeWork;
import com.ygkj.project.model.SysFile;
import com.ygkj.project.vo.request.OverseeCodeReqVo;
import com.ygkj.project.vo.request.OverseeReqVo;
import com.ygkj.project.vo.request.ProjectOverseeReqVo;
import com.ygkj.project.vo.response.ProjectOverseeResVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.warning.model.MsgSendRecord;
import com.ygkj.warning.vo.request.DingWorkReqVo;
import com.ygkj.water.project.feign.SendFeignClient;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.mapper.OverseeMapper;
import com.ygkj.water.project.mapper.SysFileMapper;
import com.ygkj.water.project.mapper.UserMapper;
import com.ygkj.water.project.service.OverseeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("overseeService")
@Slf4j
public class OverseeServiceImpl implements OverseeService {

    @Resource
    private OverseeMapper overseeMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private SysFileMapper sysFileMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SendFeignClient sendFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private GenerateDistributionIDService idService;

    @Value("${oversee.template.msg}")
    private String msgTemplate;

    @Value("${oversee.template.url}")
    private String urlTemplate;

    @Value("${oversee.template.codemsg}")
    private String codeTemplate;

    @Value("${oversee.work.url}")
    private String workUrl;

    @Value("${oversee.work.mobileurl}")
    private String workMobileUrl;

    private void sendMsg(OverseeReqVo reqVo, List<ProjectOverseeWork> list) {
        String userId = reqVo.getId();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(projectOverseeWork -> {
                String msg = "";
                try {
                    String url = String.format(urlTemplate, reqVo.getId(), projectOverseeWork.getWorkerPhone());
                    msg = String.format(msgTemplate,
                            reqVo.getProjectName(),
                            formatTime(reqVo.getCreateTime()),
                            reqVo.getProblemDescribe(),
                            formatTime(reqVo.getDeadline()),
                            url);
                    MsgSendRecord msgSendRecord = new MsgSendRecord();
                    msgSendRecord.setReceivePhone(projectOverseeWork.getWorkerPhone());
                    msgSendRecord.setSendType(1);
                    msgSendRecord.setContext(msg);
                    msgSendRecord.setBusinessType(1);
                    msgSendRecord.setSendId(userId);
                    sendFeignClient.send(msgSendRecord);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private String formatTime(Date date) {
        return sdf.format(date);
    }

    /**
     * 删除督办
     *
     * @param id
     */
    @Override
    public void del(String id) {
        overseeMapper.deleteById(id);
    }

    /**
     * 查询督办
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<ProjectOverseeResVo> page(ProjectOverseeReqVo reqVo) {
        reqVo.page();
        List<ProjectOverseeResVo> list = overseeMapper.page(reqVo);
        if (!CollectionUtils.isEmpty(list)) {
            Date now = new Date();
            List<ProjectOverseeWork> workList = overseeMapper.listWorkerByIds(
                    list.stream().map(ProjectOverseeResVo::getId).collect(Collectors.toList()));
            Map<String, List<ProjectOverseeWork>> workMap = workList.stream().collect(Collectors.groupingBy(ProjectOverseeWork::getOverseeId));
            List<String> fileIdList = new ArrayList<>();
            for (ProjectOverseeResVo resVo : list) {
                addAllFileId(resVo.getWorkFile(), fileIdList);
                addAllFileId(resVo.getProblemFile(), fileIdList);
            }
            Map<String, SysFile> fm = new HashMap<>();
            if (!CollectionUtils.isEmpty(fileIdList)) {
                List<SysFile> sysFileList = sysFileMapper.listFile(fileIdList);
                fm = sysFileList.stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
            }
            Map<String, SysFile> fileMap = fm;

            String userId = reqVo.getUserId();
            list.forEach(projectOverseeResVo -> {
                String id = projectOverseeResVo.getId();
                projectOverseeResVo.setStatus(
                        StringUtils.isEmpty(projectOverseeResVo.getWorkDescribe())
                                ? ObjectUtils.isNotEmpty(projectOverseeResVo.getDeadline())
                                ? now.before(projectOverseeResVo.getDeadline()) ? 0 : -1
                                : 0
                                : 1
                );
                if (workMap.containsKey(id)) {
                    List<ProjectOverseeWork> overseeWorkList = workMap.get(id);
                    List<ProjectOverseeWork> outList = new ArrayList<>();
                    List<ProjectOverseeWork> inList = new ArrayList<>();
                    for (ProjectOverseeWork projectOverseeWork : overseeWorkList) {
                        Integer type = projectOverseeWork.getType();
                        if (type == 1) {
                            if (projectOverseeWork.getWorkerId().equals(userId) && projectOverseeResVo.getStatus() == 0) {
                                projectOverseeResVo.setUpdateFlag(true);
                            }
                            inList.add(projectOverseeWork);
                        } else {
                            outList.add(projectOverseeWork);
                        }
                    }
                    projectOverseeResVo.setOutsideList(outList);
                    projectOverseeResVo.setInsideList(inList);
                }

                //附件信息
                List<SysFile> pl = getFileList(fileMap, projectOverseeResVo.getProblemFile());
                projectOverseeResVo.setProblemFileList(pl);
                List<SysFile> wl = getFileList(fileMap, projectOverseeResVo.getWorkFile());
                projectOverseeResVo.setWorkFileList(wl);
            });

        }
        return PageUtils.getPageVO(list);
    }

    private List<SysFile> getFileList(Map<String, SysFile> fileMap, String fileIds) {
        List<SysFile> list = new ArrayList<>();
        if (!StringUtils.isEmpty(fileIds)) {
            String[] fileIdArr = fileIds.split(",");
            for (String id : fileIdArr) {
                if (fileMap.containsKey(id)) {
                    list.add(fileMap.get(id));
                }
            }
        }
        return list;
    }

    private void addAllFileId(String fileIds, List<String> fileIdList) {
        if (!StringUtils.isEmpty(fileIds)) {
            fileIdList.addAll(Arrays.asList(fileIds.split(",")));
        }
    }

    /**
     * 新增
     *
     * @param reqVo
     */
    @Override
    public void save(OverseeReqVo reqVo) {
        String id = idService.nextID();
        reqVo.setId(id);
        reqVo.setCreateTime(new Date());
        ProjectOversee projectOversee = new ProjectOversee();
        BeanUtils.copyProperties(reqVo, projectOversee);
        overseeMapper.insert(projectOversee);

        //保存人员信息
        List<ProjectOverseeWork> list = new ArrayList<>();
        addAllWorker(list, reqVo.getOutsideList(), 2, id);
        addAllWorker(list, reqVo.getInsideList(), 1, id);
        //准备待办
        List<DingWorkReqVo> workReqVoList = assembleWork(reqVo);
        if (!CollectionUtils.isEmpty(list)) {
            overseeMapper.batchInsertWorker(list);
        }

        //外部人员发送短信
        sendMsg(reqVo, reqVo.getOutsideList());
        //外部人员发送待办
        sendWord(workReqVoList);
    }

    private void sendWord(List<DingWorkReqVo> workReqVoList) {
        if (!CollectionUtils.isEmpty(workReqVoList)) {
            workReqVoList.forEach(d -> sendFeignClient.addWork(d));
        }
    }

    private List<DingWorkReqVo> assembleWork(OverseeReqVo reqVo) {
        List<ProjectOverseeWork> insideList = reqVo.getInsideList();
        List<DingWorkReqVo> workReqVoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(insideList)) {
            String startTime = sdf.format(reqVo.getCreateTime());
            String endTime = sdf.format(reqVo.getDeadline());
            String overseeId = reqVo.getId();
            for (ProjectOverseeWork projectOverseeWork : insideList) {
                DingWorkReqVo dingWorkReqVo = new DingWorkReqVo();
                String reqId = idService.nextID();
                dingWorkReqVo.setReqId(reqId);
                dingWorkReqVo.setContent(reqVo.getProblemDescribe());
                dingWorkReqVo.setTitle("海塘督办");
                dingWorkReqVo.setUserCode(projectOverseeWork.getWorkerId());
                dingWorkReqVo.setStartTime(startTime);
                dingWorkReqVo.setEndTime(endTime);
                dingWorkReqVo.setWorkType("1");
                dingWorkReqVo.setWorkUrl(String.format(workUrl, projectOverseeWork.getId(), overseeId));
                dingWorkReqVo.setMobileWorkUrl(String.format(workMobileUrl, projectOverseeWork.getId(), overseeId));
                projectOverseeWork.setReqId(reqId);
                workReqVoList.add(dingWorkReqVo);
            }
        }
        return workReqVoList;
    }

    private void addAllWorker(List<ProjectOverseeWork> list, List<ProjectOverseeWork> subList,
                              final int type, final String overseeId) {
        if (!CollectionUtils.isEmpty(subList)) {
            subList.forEach(projectOverseeWork -> {
                projectOverseeWork.setId(idService.nextID());
                projectOverseeWork.setOverseeId(overseeId);
                projectOverseeWork.setType(type);
            });
            list.addAll(subList);
        }
    }

    /**
     * 整改
     *
     * @param oversee
     */
    @Override
    public void update(ProjectOversee oversee) throws Exception {
        ProjectOversee projectOversee = overseeMapper.selectById(oversee.getId());
        isOverdue(projectOversee);
//        if(!StringUtils.isEmpty(projectOversee.getReportPhone())){
//            throw new Exception("该督办已处理,请勿重复处理");
//        }
        //只有填了描述才算做了处理
        if (!StringUtils.isEmpty(oversee.getWorkDescribe())) {

            if (StringUtils.isEmpty(oversee.getReportPhone())) {
                //手机号为空说明是内部填报,通过人员id查询手机号
                oversee.setReportPhone(userMapper.load(oversee.getCreateId()).get(0).getPhone());
                oversee.setCreateId(null);
                //收先查出对应的待办id
                ProjectOverseeWork projectOverseeWork = new ProjectOverseeWork();
                projectOverseeWork.setOverseeId(oversee.getId());
                projectOverseeWork.setType(1);
                //完成所有的待办
                List<ProjectOverseeWork> projectOverseeWorkList = overseeMapper.selectWork(projectOverseeWork);
                if(!CollectionUtils.isEmpty(projectOverseeWorkList)){
                    projectOverseeWorkList.forEach(work->{
                        //内部人员还要完成待办
                        sendFeignClient.endWork(work.getReqId());
                    });
                }
            } else {
                //外部填报需要检验验证码
                String key = getVerificationKey(oversee.getId(), oversee.getReportPhone());
                String code = (String) redisTemplate.opsForValue().get(key);
                if (StringUtils.isEmpty(code) || !code.equals(oversee.getVerificationCode())) {
                    throw new Exception("验证码错误");
                }
                //清除验证码
                redisTemplate.delete(key);
            }

            if (oversee.getWorkTime() == null) {
                oversee.setWorkTime(new Date());
            }
            overseeMapper.updateById(oversee);
        }
    }

    private void isOverdue(ProjectOversee projectOversee) throws Exception {
        if (projectOversee.getDeadline().before(new Date())) {
            throw new Exception("该督办已过期");
        }
    }

    /**
     * 发送验证码
     *
     * @param reqVo
     * @return
     */
    @Override
    public void sendVerificationCode(OverseeCodeReqVo reqVo) throws Exception {
        ProjectOverseeWork projectOverseeWork = new ProjectOverseeWork();
        projectOverseeWork.setOverseeId(reqVo.getId());
        projectOverseeWork.setWorkerPhone(reqVo.getPhone());
        if (StringUtils.isEmpty(reqVo.getPhone())) {
            projectOverseeWork.setWorkerPhone("woshijiashoujihao");
        }
        //查询该手机号是否是该督办的整改人
        List<ProjectOverseeWork> projectOverseeWorkList = overseeMapper.selectWork(projectOverseeWork);
        if (CollectionUtils.isEmpty(projectOverseeWorkList)) {
            throw new Exception("该手机号不在整改人员列表");
        }

        //生成一个随机数
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        String verificationCode = sb.toString();
        String msg = String.format(codeTemplate, verificationCode);
        projectOverseeWork = projectOverseeWorkList.get(0);
        String name = projectOverseeWork.getWorkerName();
        String phone = projectOverseeWork.getWorkerPhone();
        MsgSendRecord msgSendRecord = new MsgSendRecord();
        msgSendRecord.setReceivePhone(phone);
        msgSendRecord.setSendPhone(phone);
        msgSendRecord.setReceiveName(name);
        msgSendRecord.setSendName(name);
        msgSendRecord.setSendType(1);
        msgSendRecord.setContext(msg);
        msgSendRecord.setBusinessType(3);
        sendFeignClient.send(msgSendRecord);
        //将验证码加入redis
        redisTemplate.opsForValue().set(getVerificationKey(reqVo.getId(), reqVo.getPhone()), verificationCode, 10, TimeUnit.MINUTES);
    }

    /**
     * 督办登陆
     *
     * @param workId
     * @return
     */
    @Override
    public LoginRespVO loginByOverseeWorkId(String workId) throws Exception {
        //根据workId查询人员信息
        ProjectOverseeWork projectOverseeWork = new ProjectOverseeWork();
        projectOverseeWork.setId(workId);
        List<ProjectOverseeWork> projectOverseeWorkList = overseeMapper.selectWork(projectOverseeWork);
        projectOverseeWork = projectOverseeWorkList.get(0);
        //查询是否超期
        ProjectOversee projectOversee = overseeMapper.selectById(projectOverseeWork.getOverseeId());
        isOverdue(projectOversee);
        //查询用户信息
        SysUser sysUser = userFeignClient.detailInfo(projectOverseeWork.getWorkerId());
        //用该用户进行登陆
        LoginReqVO loginReqVO = new LoginReqVO();
        loginReqVO.setPassword(sysUser.getPassword());
        loginReqVO.setUsername(sysUser.getPhone());

        return userFeignClient.login(loginReqVO);
    }

    private String getVerificationKey(String id, String phone) {
        return new StringBuilder("oversee:")
                .append(id).append("_").append(phone)
                .toString();
    }

}
