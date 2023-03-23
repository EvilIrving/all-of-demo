package com.ygkj.vo.req;

import com.ygkj.gragh.model.PageEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wgf
 * @date 2021/11/23 16:57
 * @Description: 物业化用户列表
 */
@Data
public class WyhUserVo extends PageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String username;

    private String phone;

    private String deptId;

    private String post;
}
