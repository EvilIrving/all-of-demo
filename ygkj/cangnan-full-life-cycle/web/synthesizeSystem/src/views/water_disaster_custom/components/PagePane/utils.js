/**
 * 是否原生的PagePaneItem 即直接在default插槽上编写的VNode
 * @param {*} vnode
 * @returns {Boolean}
 */
const isNativeComponent = (vnode) => {
	return vnode.componentOptions?.Ctor.options.name === "PagePaneItem";
};
/**
 * 是否时自定义组件 允许一层嵌套的自定义组件
 * @param {*} vnode
 * @returns {Boolean}
 */
const isCustomComponent = (vnode) => {
	return vnode?.child && isNativeComponent(vnode.child._vnode);
};
/**
 * 是否时PagePanelItem
 * @param {*} vnode
 * @returns {Boolean}
 */
const isPagePaneItem = (vnode) => {
	return isNativeComponent(vnode) || isCustomComponent(vnode);
};
/**
 * 获取title
 * @param {*} title
 * @returns
 */
const getTitle = (title) => {
	if (typeof title == "string") return title;
	if (typeof title == "object") return title.text || "";
	return "";
};
/**
 * 生成a标签props
 * @param {*} to
 * @returns
 */
const genLinkProp = (to) => {
	let props = {};
	if (typeof to == "string" && isUrl(to)) props.attrs = { href: to };
	if (typeof to == "object" && to.url && isUrl(to.url)) {
		props.attrs = { href: to.url };
		if (to.target) props.attrs.target = to.target;
	}
	return props;
};
/**
 * @description 是否为url
 * @param {String} path path
 * @returns Boolean
 */
export function isUrl(path) {
	const reg = /(((^https?:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+(?::\d+)?|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)$/;
	return reg.test(path);
}
export { isNativeComponent, isCustomComponent, isPagePaneItem, getTitle, genLinkProp };
