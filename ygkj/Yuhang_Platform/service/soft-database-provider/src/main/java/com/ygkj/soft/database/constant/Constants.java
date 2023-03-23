package com.ygkj.soft.database.constant;

/**
 * @author Huang.zh
 * @date 2020/9/1 17:30
 * @Description: 常量
 */
public class Constants {

    public static final String COMMA = ",";

    public static final String QUESTION_MARK = " ? ";

    public static final String AND = " AND ";
    public static final String EQUALS=" = ";
    public static final String PLACE_HOLDER = EQUALS+QUESTION_MARK;

    public static final String LEFT_PARENTHESIS="(";
    public static final String RIGHT_PARENTHESIS=")";

    public static final String VALUES="VALUES";

    public static final String SET=" SET ";

    public static final String WHERE=" WHERE ";
    public static final String SELECT=" SELECT * FROM ";

    public static final String SELECT_COUNT=" SELECT count(1) FROM ";

    public static final String UNION=" UNION ";

    public static final String NOT_DELETED = " 0 ";

    public static final String IS_NULL = " IS NULL ";

    public static final String IS_NOT_NULL = " IS NOT NULL ";

    public static final int PASS_STATUS = 1;

    public static final int REJECT_STATUS = 2;

    public static final String MULTIPLE_JDBC_TEMPLATE_PREFIX = "jdbcTemplate_";

    public static final String DEFAULT_CONNECTION_INIT_SQL = "SELECT 1";

    public static final String DEFAULT_DATA_BASE_NAME_KEY = "dataBaseName";

    public static final String DEFAULT_TABLE_NAME_KEY = "tableName";

    /**
     * 默认删除标记字段
     */
    public static final String DEFAULT_DELETE_COLUMN_KEY = " dts_cmf_deleted ";
    /**
     * 默认主键字段
     */
    public static final String DEFAULT_PRIMARY_KEY = " dts_cmf_id ";

    public static final String DEFAULT_DEPARTMENT_ID_KEY = " dts_cmf_dept ";
    /**
     * 默认数据版本字段
     */
    public static final String DEFAULT_DATA_VERSION_KEY = " dts_cmf_version ";

    /**
     * 默认创建者id
     */
    public static final String DEFAULT_CREATOR_ID_KEY = " dts_cmf_creator_id ";

    /**
     * 默认修改者id
     */
    public static final String DEFAULT_UPDATOR_ID_KEY = " dts_cmf_updator_id ";
    /**
     * 关联项后缀
     */
    public static final String ASSOCIATED_ITEM_SUFFIX = "_id";

    /**
     * 数源划分状态 未分配
     */
    public static final String UNDIVIDED_STATUS = "0";
    /**
     * 数源划分状态 已分配
     */
    public static final String DIVIDED_STATUS = "1";

    /**
     * 中间数据新增操作
     */
    public static final String OPERATION_ADD = "1";

    /**
     * 中间数据修改操作
     */
    public static final String OPERATION_EDIT = "2";

    /**
     * 中间数据删除操作
     */
    public static final String OPERATION_DELETE = "3";

}
