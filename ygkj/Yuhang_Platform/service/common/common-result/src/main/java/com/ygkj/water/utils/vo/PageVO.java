package com.ygkj.water.utils.vo;

import lombok.Data;

import java.util.List;

/**
* @ClassName:       PageVO
*                   分页VO
* @Author:          Huang.zh
* @CreateDate:      2019/9/19 13:25
* @UpdateUser:      Huang.zh
* @UpdateDate:      2019/9/19 13:25
* @Version:         0.0.1
*/
@Data
public class PageVO<T>  {
	/**
	 * serial_number
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 总记录数
	 */
	private Long totalRows;
	/**
	 * 总页数
	 */
	private Integer totalPages;
	/**
	 * 当前第几页
	 */
	private Integer pageNum;
	/**
	 * 每页记录数
	 */
	private Integer pageSize;
	/**
	 * 当前页记录数
	 */
	private Integer curPageSize;
	/**
	 * 数据列表
	 */
	private List<T> list;

}
