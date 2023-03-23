package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.model.SysPermission;
import com.ygkj.model.SysRolePermission;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@DS("main_db")
public interface MainDbMapper {

    @Select("SELECT DISTINCT(c.scene_id) FROM lc_basic_db.sys_user_role a\n" +
            "LEFT JOIN lc_basic_db.sys_role_permission b on a.role_id = b.role_id\n" +
            "LEFT JOIN lc_basic_db.sys_permission c ON c.id = b.permission_id\n" +
            "WHERE c.scene_id IS NOT NULL AND a.user_id = #{userId}")
    List<String> userUnits(String userId);

    @Select("SELECT DISTINCT(role_id) FROM lc_basic_db.sys_user_role WHERE user_id = #{userId}")
    List<String> userRoleIds(String userId);

    @Insert("INSERT INTO lc_basic_db.sys_permission (id,name,pid,scene_id,type) VALUES(#{id},#{name},#{pid},#{sceneId},#{type})")
    int insertScenePermission(SysPermission permission);

    int batchInsertPermissionAndRole(List<SysRolePermission> list);
}
