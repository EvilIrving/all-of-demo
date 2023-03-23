package com.ygkj.consumer.auth.controller;

import com.ygkj.auth.VO.request.AchievementFilePageQueryVO;
import com.ygkj.auth.VO.request.SysAchievementAddVO;
import com.ygkj.auth.VO.request.SysAchievementUpdateVO;
import com.ygkj.auth.api.achievement.SysAchievementApi;
import com.ygkj.auth.api.achievement.model.SysAchievement;
import com.ygkj.consumer.auth.client.authentication.AuthenticationClient;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/15 17:24
 * @Description:
 */
@RestController
@RequestMapping("/sysAchievement")
public class SysAchievementController implements SysAchievementApi {

    @Autowired
    private AuthenticationClient authenticationClient;

    @GetMapping("")
    @Override
    public CommonResult selectAll(String name) {
        CommonResult<List<SysAchievement>> result = authenticationClient.achievementTree(name);
        // CommonResult<List<BusProject>> busProjects = wmsClient.selectAll();
        List<SysAchievement> achievements = result.getData();
        // List<BusProject> list = busProjects.getData();
        // String fixTopId = "0";
        // SysAchievement projectAchievement = new SysAchievement(fixTopId, "项目立项");
        // projectAchievement.setModify(false);
        // List<SysAchievement> children = new ArrayList<>();
        /*for (BusProject busProject : list) {
            List<String> ids = busProject.getBusProjectFiles().stream()
                    .filter(bpf -> StringUtils.isNotBlank(bpf.getFileName()) || StringUtils.isNotBlank(bpf.getFilePath()))
                    .map(BusProjectFile::getId).collect(Collectors.toList());
            List<SysFile> files = new ArrayList<>();
            for (BusProjectFile file : busProject.getBusProjectFiles()) {
                if (StringUtils.isNotBlank(file.getFileName()) || StringUtils.isNotBlank(file.getFilePath())) {
                    SysFile sysFile = new SysFile(file.getId(), file.getFileName(), file.getFilePath());
                    sysFile.setModify(false);
                    files.add(sysFile);
                }
            }
            String fileIds = String.join(",", ids);
            children.add(new SysAchievement(UUID.randomUUID().toString(), busProject.getProjectName(), fixTopId,
                    fileIds, files, new ArrayList<>(), false));
        }*/
        // projectAchievement.setChildren(children);
        // achievements.add(projectAchievement);
        return result;
    }

    @PostMapping("")
    @Override
    public CommonResult addAchievement(SysAchievementAddVO sysAchievementAddVO) {
        return authenticationClient.addAchievement(sysAchievementAddVO);
    }

    @PutMapping("")
    @Override
    public CommonResult updateAchievement(SysAchievementUpdateVO sysAchievementUpdateVO) {
        return authenticationClient.updateAchievement(sysAchievementUpdateVO);
    }

    @DeleteMapping("/{id}")
    @Override
    public CommonResult deleteSysAchievement(@PathVariable String id) {
        return authenticationClient.deleteSysAchievement(id);
    }

    @PostMapping("/files")
    @Override
    public CommonResult findFiles(AchievementFilePageQueryVO achievementFilePageQueryVO) {
        return authenticationClient.findFiles(achievementFilePageQueryVO);
    }


    @PostMapping("/statisticsTotalArchives")
    @Override
    public CommonResult statisticsTotalArchives() {
        return authenticationClient.statisticsTotalArchives();
    }
}
