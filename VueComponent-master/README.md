# 1.srcoolTab
### 支持左右滑动，当点击某个tab项，自动缓动居中

## Props
|       参数       | 描述                     |  类型  | 默认值 | 是否必传 |
| :--------------: | :----------------------- | :----: | :----: | :------: |
|     tabList      | /                        | Array  |   []   |    是    |
|    startIndex    | 起始tabItem的索引        | Number |   0    |    否    |
|     duration     | 动画持续时间，ms         | Number |  400   |    否    |
|    wrapWidth     | 单个tabItem项目的宽度，% | Number |   25   |    否    |
| wrapPaddingRight | 相邻tabItem的间距，px    | Number |   10   |    否    |

## Slots
| 默认值  | 参数                                                                                    | 是否必传 |
| :-----: | :-------------------------------------------------------------------------------------- | :------: |
| default | 1.item,单个tabItem项目值<br> 2.index,tab的索引<br>3.activiteIndex,点击的的tab项索引<br> |    是    |

## Methods
| 事件名称 |        触发条件         |      返回参数       |
| -------- | :---------------------: | :-----------------: |
| @click   | 点击某个tabItem项后触发 | 点击tabItem的索引值 |

# 2.Popup
### 弹框

## Props
|     参数      | 描述                                                      |   类型    |  默认值  | 是否必传 |
| :-----------: | :-------------------------------------------------------- | :-------: | :------: | :------: |
|     value     | v-mode绑定，控制弹框的显示与隐藏                          |  Boolean  |  false   |    是    |
| popupPosition | 弹框方向，可取值： "top","bottom","left","right","center" |  String   | "bottom" |    否    |
|  popupHeight  | 弹框高度,px                                               |  Number   |   260    |    否    |
|     close     | 关闭弹框函数                                              | Functioin |  ()=>{}  |    是    |

## Slots
|  name   | 参数     | 是否必传 |
| :-----: | :------- | :------: |
| default | 弹框内容 |    否    |

# 3.Swipe
### 轮播图

## Props
|       参数        | 描述                                                                                                   |      类型       |     默认值     | 是否必传 |
| :---------------: | :----------------------------------------------------------------------------------------------------- | :-------------: | :------------: | :------: |
|       list        | /                                                                                                      |      Array      |       []       |    是    |
|    startIndex     | 起始索引值                                                                                             |     Number      |       0        |    否    |
|    cirticalVal    | 滑动临界比,用来决定是否滑动到下/前一张还是在当前                                                       | Number，0-1之间 |      1/5       |    否    |
|     autoplay      | 是否开启自动轮播                                                                                       |     Boolean     |     false      |    否    |
|    continuous     | 是否无限轮播                                                                                           |     Boolean     |      true      |    否    |
|   autoPlayTime    | 自动轮播时间，ms                                                                                       |     Number      |      3000      |    否    |
|  showIndicators   | 是否显示指示器                                                                                         |     Boolean     |     false      |    否    |
| indicatorPosition | 指示器位置，可选值为：'top-left' 'top-center' 'top-right' 'bottom-left' 'bottom-center' 'bottom-right' |     String      | 'bottom-right' |    否    |
|  wrapperPadding   | swipeItem包裹容器内边距，px                                                                            |     Number      |       0        |    否    |


## Slots
|  name   | 说明          |        参数        | 是否必传 |
| :-----: | :------------ | :----------------: | :------: |
| default | swipeItem内容 | sItem，轮播图的url |    是    |

## Methods
| 事件名称 |     触发条件     | 返回参数 |
| -------- | :--------------: | :------: |
| @change  | 滑动下一张后触发 |  索引值  |


# 4.Picker
### 地区选择器。流畅好用地区，兼容PC和手机

## Props
|   参数    | 描述                 |   类型   | 默认值 | 是否必传 |
| :-------: | :------------------- | :------: | :----: | :------: |
|   value   | v-mode绑定的值地区值 |  Array   |   []   |    是    |
|   close   | 关闭地区选择器       | Function | ()=>{} |    是    |
|  visible  | 控制弹框显示与隐藏   | Boolean  | false  |    是    |
| showTitle | 是否展示标题         | Boolean  | false  |    否    |

## Methods
| 事件名称 |   触发条件   |    返回参数    |
| -------- | :----------: | :------------: |
| @confirm | 点击确认按钮 | 选择的地区数组 |


# 5.Preiview
### 图片预览组件，提供预览图缩放、单击退出预览图、双指流畅缩放、左右滑动切换预览图功能。

## Props
|     参数     | 描述                               |   类型   | 默认值 | 是否必传 |
| :----------: | :--------------------------------- | :------: | :----: | :------: |
|     list     | 预览图list                         |  Array   |   []   |    是    |
| previewState | 预览图状态，用于控制预览图是否开启 | Boolean  | false  |    否    |
|  startIndex  | 起始index                          |  Number  |   0    |    否    |
|  singleTap   | 单击事件                           | Function | ()=>{} |    否    |
| criticalVal  | 滑动临界值                         |  Number  |  1/3   |    否    |
| showCounter  | 是否显示计数器                     | Boolean  |  true  |    否    |
| maxScaleVal  | 最大缩放倍数                       |  Number  |   5    |    否    |
|   banScale   | 是否禁止缩放                       | Boolean  | false  |    否    |

## Methods
| 事件名称 |       触发条件       | 返回参数 |
| -------- | :------------------: | :------: |
| @change  | 切换到下一张预览图中 |  索引值  |

# 6.Steps
### 用于展示操作流程的各个环节，让用户了解当前的操作在整体流程中的位置。

## steps-Props
|      参数      | 描述                        |  类型  |    默认值     | 是否必传 |
| :------------: | :-------------------------- | :----: | :-----------: | :------: |
|     active     | 当前步骤                    | number |       0       |    否    |
|   direction    | 显示方向，可选值为 vertical | string | 'horizontal'  |    否    |
|  active-color  | 激活状态颜色                | stirng |   'primary'   |    否    |
| inactive-color | 未激活状态颜色              | string |    'weak'     |    否    |
|  active-icon   | 激活状态底部图标            | string | 'select_line' |    否    |
| inactive-icon  | 未激活状态底部图标          | string |       -       |    否    |
|  finish-icon   | 已完成步骤对应的底部图标    | string |       -       |    否    |

## steps-Methods
| 事件名称    |       触发条件       | 返回参数 |
| ----------- | :------------------: | :------: |
| @click-step | 切换到下一张预览图中 |  索引值  |

## step-slot
|     name      | 说明                           |
| :-----------: | :----------------------------- |
|  active-icon  | 自定义激活状态图标             |
| inactive-icon | 自定义未激活状态图标           |
|  finish-icon  | 自定义已完成步骤对应的底部图标 |

# 7.Icon
### 基于symbol方式引用的图标，可通过替换asstes/iconfont/index.js 更换图标库

## Props
| 参数  | 描述                         |  类型  | 默认值 | 是否必传 |
| :---: | :--------------------------- | :----: | :----: | :------: |
| name  | 图标名称，采用的是symbol方式 | string |   ""   |    是    |
| color | 图标颜色                     | string | 'weak' |    否    |
| size  | 图标大小                     | number |  '20'  |    否    |