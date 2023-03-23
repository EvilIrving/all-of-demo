//@ts-check
// eslint-disable-next-line
import { CreateElement } from 'vue';

/**
 * 对Card组件处理
 * @param {CreateElement} h Vue.$createElement 不在Vue文件内无法使用JSX直接不通过babel上原方法
 * @param {{component:import("vue").VNode|string, componentProp:import('vue').VNodeData , cardProp:import('vue').VNodeData}} param 配置项
 * @returns {import("vue").VNode} 处理过后的组件VNode
 */
export function factoryCardComponent(h, { component, componentProp, cardProp }) {
  const tagName = typeof component == 'string' ? component : component?.componentOptions?.tag;
  const directives = [{ name: 'drag' }];
  cardProp = { ...cardProp, directives };
  const renderComponent = h(tagName, { ...componentProp });
  return h('drag-card', { ...cardProp }, [renderComponent]);
}

/**
 * 对统计卡片组件处理
 * @param {CreateElement} h
 * @param {object} param 配置项
 * @param {import("vue").VNode|string} param.component
 * @param {import('vue').VNodeData} param.props
 */
export function factoryStatistics(h, { component, props }) {
  const tagName = typeof component == 'string' ? component : component?.componentOptions?.tag;
  return h(tagName, { ...props });
}

const Type = (function() {
  /**
   * @type {{[key:string]:(type:any)=>boolean}}
   */
  let Type = {};
  for (
    let i = 0, type;
    (type = ['Undefined', 'Null', 'Boolean', 'Number', 'String', 'Function', 'Array', 'Object'][i++]);

  ) {
    (function(type) {
      Type['is' + type] = function(obj) {
        return Object.prototype.toString.call(obj) === '[object ' + type + ']';
      };
    })(type);
  }
  return Type;
})();

/**
 * 通过组件标签获取组件实例
 * @param {Vue} vueInstance 父实例
 * @param {*} componentTag  标签
 * @returns
 */
export function getChildComponent(vueInstance, componentTag) {
  let component = null;
  let allComp = getAllChildComp(vueInstance);

  let i = allComp.findIndex(function(vm) {
    // @ts-ignore
    return vm.$options._componentTag === componentTag;
  });
  if (i !== -1) {
    component = allComp[i];
  }
  return component;
  /**
   * @param {Vue|Vue[]} instance
   * @returns {Vue[]}
   */
  function getAllChildComp(instance) {
    /** @type {Vue[]} */
    let allComp = [];
    /** @type {Vue[]} */
    let child = [];
    if (Type.isObject(instance)) {
      child = /** @type {Vue}*/ (instance).$children;
    } else if (Type.isArray(instance)) {
      child = /** @type {Vue[]}*/ (instance);
    }
    for (let i = 0; i < child.length; i++) {
      allComp.push(child[i]);
      if (child[i].$children.length > 0) {
        allComp = allComp.concat(getAllChildComp(child[i].$children));
      }
    }
    return allComp;
  }
}
