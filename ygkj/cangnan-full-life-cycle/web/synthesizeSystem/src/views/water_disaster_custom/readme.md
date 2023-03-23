# README

## 卡片组件命名和配置 key

> 文件名以单词首字母大写的 camelCase 配置中 prop 的 key 使用 kebab-Case

```javascript
cardMap(){
   // 示例
  return {
      ...{
      实时台风: {
        component: <real-typhoon-card /,
        props: { typhoonList: this.typhoonData, key："real-typhoon-card" },
        cardProps: { isHigh: true },
      }
    }
  }
}

```

## 卡片组件开发

### 结构注意

```html
<!-- html结构 顶级用对应组件key为类名的div包裹 -->
<div class="real-typhoon-card">
	<!-- $slots.default可以使用 ./components/PagePane 或是 ./components/PanelItem.vue 建议前者 -->
</div>
```

### 组件使用

#### **PagePane**

> 此组件插槽可以接收 PagePaneItem 或者直接编写元素 如果使用 PagePaneItem 则会忽略 title 属性 并使用(v-model\value)绑定当前页面名称

**属性**

| 属性名           | 类型            | 描述                                                                    | 默认值 | 类型描述                               |
| ---------------- | --------------- | ----------------------------------------------------------------------- | ------ | -------------------------------------- |
| (v-model\|value) | String          | 绑定的页面名称                                                          | -      | 与插槽中 pagePaneItem 的 name 属性一致 |
| title            | [String,Object] | 标题名称                                                                | 标题   | String \| {text:String,icon?:String}   |
| to               | [String,Object] | 标题点击跳转链接，为 Object 时 Url 为跳转链接 target 为 a 标签的 target | -      | String \| {url:String,target?:String}  |
| inSimulation     | Boolean         | 是否演练中                                                              | false  | -                                      |

**事件**

| 事件    | 描述           | 默认值 | 参数          |
| ------- | -------------- | ------ | ------------- |
| onTitle | 标题点击事件   | -      | -             |
| change  | 多页面切页事件 | -      | (name:string) |

**插槽**

| name   | 说明           | 插槽作用域值（slot-scope） |
| ------ | -------------- | -------------------------- |
| -      | 默认插槽       | -                          |
| append | 标题名称后插槽 | -                          |

#### **PagePaneItem**

**属性**

| 属性名 | 类型   | 描述                       | 默认值 | 类型描述 |
| ------ | ------ | -------------------------- | ------ | -------- |
| name   | String | 名称 ,用于判断是否是当前页 | -      | -        |
| label  | String | 用于显示的标题名称         | 标题   | String   |

## 卡片组件导出属性注意

大屏组件导出时 若有落点以及图例则需要包含 layerName 属性和 legendName 属性供关闭卡片时使用 如下

```javascript
export default {
	name: "RainForecastCard", // name 属性最好别复制了忘改
	//layerName 与 legendName 允许接受string或string[]
	layerName: "maxPointLayer",
	legendName: ["实时降雨量", "预报降雨量"],
	...props,
};
```

## 配置组件注意

### 卡片组件配置

> 以中文名为键 值为配置对象 下面使用 ts 进行描述

```typescript

interface CardProps{
  isHigh?:Boolean,
  /* ...注入到DragCard中的其他属性 */
}

type ComponentConfig{
  component: [VNode,String],//VNode类型即 <CustomComponent /> jsx 组件 String即组件标签名"CustomComponent" 或 "custom-component"
  props:Object,
  cardProps?: CardProps
}
```

```javascript
cardMap(){
   // 示例
  return {
      ...{
      实时台风: {
        component: <real-typhoon-card /,
        props: { typhoonList: this.typhoonData },
        cardProps: { isHigh: true },
      }
    }
  }
}

```

## 统计卡片配置

> 统计卡片按中文名键 值为 VNode 或 String\<tagName>即可

```javascript
statMap(){
  return {
        ....{
        实时降雨: <real-rain-count />
        }
      }
}

```
