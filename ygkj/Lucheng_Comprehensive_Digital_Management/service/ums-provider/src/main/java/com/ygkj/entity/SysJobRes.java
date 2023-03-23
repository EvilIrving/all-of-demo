package com.ygkj.entity;

/**
 * <p>
 * 资源角色绑定关系表
 * </p>
 *
 * @author wgf
 * @since 2021-01-07
 */

public class SysJobRes{

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 角色id
     */
    private String jobId;

    /**
     * 资源id
     */
    private String resId;

    /**
     * 1.登陆后菜单，2内部查看菜单
     */
    private Integer flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "SysJobRes{" +
                "id='" + id + '\'' +
                ", jobId='" + jobId + '\'' +
                ", resId='" + resId + '\'' +
                ", flag=" + flag +
                '}';
    }
}
