/**
 * 获取一个字段在配置列表中的配置
 * @param {*} colName
 * @param {*} config
 * @returns {Boolean|Undefined}
 */
function getOneConfig(colName, config) {
	return config.find((item) => item.colName == colName);
}
/**
 * 一个字段是否是Dropdown类型
 * @returns
 */
export const isDropdown = (colName, config) => getOneConfig(colName, config)?.isDropDown == 0;
/**
 * 获取Dropdown的options
 * @returns {Array}
 */
export const getDropdownOptions = (colName, config) => {
	const coldropDownItems = JSON.parse(getOneConfig(colName, config)?.dropDownItems || "{}");
	return Object.keys(coldropDownItems).map((item) => ({
		label: coldropDownItems[item],
		value: item,
	}));
};

/**
 * 处理基础数据树状数据
 * @param {Object} root
 * @returns {Array}
 */
export function operateBaseData(root) {
	return root.children.map((item) => ({
		label: item.categoryName,
		count: item.count,
		value: item.id,
		children: item.children.map((item) => ({
			label: item.categoryName,
			count: item.count,
			value: item.id,
			children: item.children.map((childItem) => ({
				label: childItem.categoryName,
				count: childItem.count,
				value: childItem.id,
			})),
		})),
	}));
}
