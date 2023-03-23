import upperFirst from "lodash/upperFirst";
import camelCase from "lodash/camelCase";
const requireStatComponent = require.context("./", false, /[A-Z]\w+\.(vue)$/);
const StatComponentList = requireStatComponent.keys().reduce((component, componentPath) => {
	const componentName = upperFirst(
		camelCase(
			// 获取和目录深度无关的文件名
			componentPath
				.split("/")
				.pop()
				.replace(/\.\w+$/, "")
		)
	);
	component[componentName] = requireStatComponent(componentPath).default;
	return component;
}, {});
// 约定式导出组件
export default StatComponentList;
export { requireStatComponent };
