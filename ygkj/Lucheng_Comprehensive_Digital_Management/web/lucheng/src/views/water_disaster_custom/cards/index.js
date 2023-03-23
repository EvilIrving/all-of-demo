import { upperFirst, lowerFirst, camelCase, kebabCase } from "lodash";
const requireComponent = require.context("./", false, /[A-Z]\w+\.(vue)$/);
const ComponentList = requireComponent.keys().reduce((component, componentPath) => {
	const componentName = upperFirst(
		camelCase(
			// 获取和目录深度无关的文件名
			componentPath
				.split("/")
				.pop()
				.replace(/\.\w+$/, "")
		)
	);
	component[componentName] = requireComponent(componentPath).default;
	return component;
}, {});
const ComponentList4kebabCase = requireComponent.keys().reduce((component, componentPath) => {
	const componentName = lowerFirst(
		kebabCase(
			// 获取和目录深度无关的文件名
			componentPath
				.split("/")
				.pop()
				.replace(/\.\w+$/, "")
		)
	);
	component[componentName] = requireComponent(componentPath).default;
	return component;
}, {});
// 约定式导出组件
export default ComponentList;
export { requireComponent, ComponentList4kebabCase };
