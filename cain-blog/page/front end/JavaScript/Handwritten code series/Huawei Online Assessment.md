---
title: 牛客网题目
date: 2022-06-17 09:27:47
updated: 2022-06-17
tags:
- JavaScript
- 刷题
- 算法
categories:
- 算法
---

## 牛客手写方法

### 事件委托

1. 给"ul"标签添加点击事件
2. 当点击某"li"标签时，该标签内容拼接"."符号。如：某"li"标签被点击时，该标签内容为".."

```javascript

document.querySelector('ul')
  .onclick = e => {
    e = e || window.event;
    e.target.nodeName.toLowerCase() == 'li'
      ? e.target.innerText += '.'
      : ''
  }


```

### 快速排序

- 挑选基准值：从数列中挑出一个元素，称为“基准”（pivot），
- 分割：重新排序数列，所有比基准值小的元素摆放在基准前面，所有比基准值大的元素摆在基准后面（与基准值相等的数可以到任何一边）。在这个分割结束之后，对基准值的排序就已经完成，
- 递归排序子序列：递归地将小于基准值元素的子序列和大于基准值元素的子序列排序。

```javascript



```

### 全排列

要求以数组的形式返回字符串参数的所有排列组合。
注意：

1. 字符串参数中的字符无重复且仅包含小写字母
2. 返回的排列组合数组不区分顺序

```javascript

// 创建返回的结果数组
// 通过字符串参数创建等长的"被使用"数组用于递归过程中记录字符顺序
// 创建回溯函数，通过该函数内部递归调用
// 在回溯函数中，当临时数组的长度等于字符串参数长度时可以返回本次循环结果
// 进入字符串参数长度的循环体中，将该次字符保存在临时数组中
// 将该次字符的"被使用"数组位修改为true，表示该字符在本次之前的递归过程中已被记录使用，跳过即可
// 递归调用回溯函数
// 退栈时设置该字符"被使用"数组为false，删除临时数组最后一位
// 返回结果

const _permute = string => {
    const result = []
    const used = Array.from({length: string.length}, () => false)
    const _backTrack = (candidate, memo = []) => {
        if(memo.length === string.length) {
            result.push(memo.slice().join(''))
            return
        }
        for(let i=0 ; i<candidate.length ; i++) {
            if(used[i]) continue
            memo.push(candidate[i])
            used[i] = true
            _backTrack(candidate, memo)
            used[i] = false
            memo.pop()
        }
    }
    _backTrack(string)
    return result
}
```

### instanceof

实现一个仿instanceof功能的"_instanceof"函数，该函数可以判断首参是否在第二个Fn构造函数的原型链上，核心步骤有：

获取首个对象参数的原型对象
获取Fn函数的原型对象
进入死循环，当两个参数的原型对象相等时返回true
当两个参数的原型对象不相等时获取首个对象参数原型的原型并且循环该步骤直到null时返回false

```javascript

// 创建返回的结果数组
// 通过字符串参数创建等长的"被使用"数组用于递归过程中记录字符顺序
// 创建回溯函数，通过该函数内部递归调用
// 在回溯函数中，当临时数组的长度等于字符串参数长度时可以返回本次循环结果
// 进入字符串参数长度的循环体中，将该次字符保存在临时数组中
// 将该次字符的"被使用"数组位修改为true，表示该字符在本次之前的递归过程中已被记录使用，跳过即可
// 递归调用回溯函数
// 退栈时设置该字符"被使用"数组为false，删除临时数组最后一位
// 返回结果

const _instanceof = (target, Fn) => {
    let proto = target.__proto__
    let prototype = Fn.prototype
    while(true) {
        if(proto === Fn.prototype) return true
        if(proto === null) return false
        proto = proto.__proto__
    }
}
```

### Array.map

实现一个仿Array.map功能的"Array._map"函数，该函数创建一个新数组，该新数组的结果是数组中的每个元素都调用一次函数参数后的返回值。

```javascript

// 判断参数是否为函数，如果不是则直接返回
// 创建一个空数组用于承载新的内容
// 循环遍历数组中的每个值，分别调用函数参数，将返回值添加进空数组中
// 返回新的数组

Array.prototype._map = function(Fn) {
    if (typeof Fn !== 'function') return
    const array = this
    const newArray = new Array(array.length)
    for (let i=0; i<array.length; i++) {
        let result = Fn.call(arguments[1], array[i], i, array)
        newArray[i] = result
    }
    return newArray
}
```

### Array.filter

仿Array.filter功能的"Array._filter"函数，该函数创建一个新数组，该数组包含通过函数参数条件的所有元素。

```javascript

// 判断参数是否为函数，如果不是则直接返回
// 创建一个空数组用于承载新的内容
// 循环遍历数组中的每个值，分别调用函数参数，将满足判断条件的元素添加进空数组中
// 返回新的数组

Array.prototype._filter = function(Fn) {
    if (typeof Fn !== 'function') return
    const array = this
    const newArray = []
    for (let i=0; i<array.length; i++) {
        const result = Fn.call(arguments[1], array[i], i, array)
        result && newArray.push(array[i])
    }
    return newArray
}
```

### Array.reduce

实现一个仿Array.reduce功能的"Array._reduce"函数，并且需要将”_reduce“函数挂载在Array的原型对象上。根据Array.reduce的特点有：

接收一个函数作为累加器，数组中的每个值（从左到右）开始缩减，最终计算为一个值
可以接收一个初始值，当没有初始值时，默认初始值为数组中的第一项

```javascript

// 在Array的原型对象上添加”_reduce“函数
// ”_reduce“函数第一个参数为回调函数，第二个参数为初始值
// 进入数组长度的循环体中
// 当初始值为空时，首个被加数为数组的第一项
// 当初始值不为空时，首个被加数为初始值

Array.prototype._reduce = function(fn, prev) {
    for(let i=0 ; i<this.length ; i++) {
        if(prev === undefined) {
            prev = fn(this[i], this[i+1], i+1, this)
                ++i
        } else {
            prev = fn(prev, this[i], i, this)
        }
    }
    return prev
}

Array.prototype.selfReduce = function (callback, initValue) {
    // 判断是否是数组调用
    if (!Array.isArray(this)) throw new TypeError('not a array')
    // 数组为空 且无初始值
    if (this.length === 0 && !initValue) throw new TypeError('Reduce of empty array with no initial value')

    let arr = this, res = null
    // 若有初始值则赋值，无则取数组第一个值
    initValue ? res = initValue : res = arr[0]

    for (let i = initValue ? 0 : 1; i < arr.length; i++) {
        res = callback(res, arr[i], i, arr) // 覆盖上次计算结果
    }
    return res
}
```

### _Object.create

实现一个仿Object.create功能的"_objectCreate"函数，该函数创建一个新对象，使用现有的对象来提供新创建的对象的proto

```javascript

// 创建一个临时函数
// 将该临时函数的原型指向对象参数
// 返回该临时对象的实例

const _objectCreate = proto => {
    if(typeof proto !== 'object' || proto === null) return
    const fn = function() {}
    fn.prototype = proto
    return new fn()
}
```

### Function.call

实现一个仿Function.call功能的"Function._call"函数，该函数会临时修改内部this的指向并返回结果。

```javascript

// 参数默认为window
// 获取调用该方法的对象，将this赋给对象参数，可以任意命名
// 通过该对象参数临时调用函数并返回结果
// 最后删除对象参数的临时函数属性

Function.prototype._call = function(target = window) {
    target['fn'] = this
    const result = target['fn']([...arguments].shift())
    delete target['fn']
    return result
}
```

### Function.bind

实现一个仿Function.bind功能的"Function._bind"函数，该函数会返回一个新的函数且该新函数内部通过apply修改了函数内部this指向

```javascript

// 创建一个新this用来保存旧的this对象
// 返回一个匿名函数，该匿名函数返回通过apply修改了指针指向的函数运算结果

Function.prototype._bind = function(target, ...arguments1) {
    const _this = this
    return function(...arguments2) {
        return _this.apply(target,arguments1.concat(arguments2))
    }
}
```

### 实现new操作符

实现一个仿new功能的新"_new"函数，该函数会返回一个对象，该对象的构造函数为函数参数、原型对象为函数参数的原型

```javascript

// 创建一个新对象
// 获取函数参数
// 将新对象的原型对象和函数参数的原型连接起来
// 将新对象和参数传给构造器执行
// 如果构造器返回的不是对象，那么就返回第一个新对象

const _new = function() {
    const object1 = {}
    const Fn = [...arguments].shift()
    object1.__proto__ = Fn.prototype
    const object2 = Fn.apply(object1, arguments)
    return object2 instanceof Object ? object2 : object1
}
```

### Object.freeze

实现一个仿Object.freeze功能的"_objectFreeze"函数，该函数可以冻结一个对象，一个被冻结的对象不能被修改、不能添加新的属性、不能删除已有属性

```javascript

// 进入对象参数的遍历体中
// 判断当前对象参数是否为普通对象或数组
// 如果是普通对象或数组，则递归调用该函数，函数参数为当前遍历项
// 如果不是，则直接设置该参数的可写性为false
// 最后通过Object.seal函数封闭该对象，阻止添加新属性并将所有现有属性标记为不可配置

const _objectFreeze = object => {
    for(prop in object) {
        const type = Object.prototype.toString.call(object[prop])
        if(type === '[object Object]' || type === '[object Array]') {
            _objectFreeze(object[prop])
        } else {
            Object.defineProperty(object, prop, {
                writable: false
            })
        }
    }
    Object.seal(object)
}
```

### 浅拷贝

实现一个对象参数的浅拷贝并返回拷贝之后的新对象，因为可能包含函数、正则、日期、ES6新对象，所以需要对这些对象类型进行特殊判断

```javascript

// 如果对象参数的数据类型不为"object"或为"null"，则直接返回该参数
// 如果是"object"，就获取该参数的构造函数名，通过正则表达式判断该对象是否为函数、正则、日期、ES6新对象等，如果返回true，则直接返回该参数
// 当以上条件判断之后函数依然没有结束时，此时通过数组的原型方法判断该参数为普通对象或数组并创建相应数据类型的新变量
// 进入遍历体，将对象参数的每一项赋值给新变量
// 最终返回该新变量

const _shallowClone = target => {
    if(typeof target === 'object' && target !== null) {
        const constructor = target.constructor
        if(/^(Function|RegExp|Date|Map|Set)$/i.test(constructor.name)) return target
        const cloneTarget = Array.isArray(target) ? [] : {}
        for(prop in target) {
            if(target.hasOwnProperty(prop)) {
                cloneTarget[prop] = target[prop]
            }
        }
        return cloneTarget
    } else {
        return target
    }
}
```

### 简易深拷贝

实现对象参数的深拷贝并返回拷贝之后的新对象，因为参数对象和参数对象的每个数据项的数据类型范围仅在数组、普通对象（{}）、基本数据类型中且无需考虑循环引用问题，所以不需要做过多的数据类型判断

```javascript

// 如果对象参数的数据类型不为“object”或为“null”，则直接返回该参数
// 根据该参数的数据类型是否为数组创建新对象
// 遍历该对象参数，将每一项递归调用该函数本身的返回值赋给新对象

const _sampleDeepClone = target => {
    if(typeof target === 'object' && target !== null) {
        const cloneTarget = Array.isArray(target) ? [] : {}
        for(prop in target) {
            if(target.hasOwnProperty(prop)) {
                cloneTarget[prop] = _sampleDeepClone(target[prop])
            }
        }
        return cloneTarget
    } else {
        return target
    }
}
```

### 深拷贝

实现对象参数的深拷贝并返回拷贝之后的新对象，因为需要考虑参数对象和参数对象的每个数据项的数据类型可能包括函数、正则、日期、ES6新对象且必须考虑循环引用问题，所以需要引入ES6新对象Map并且详细的判断数据类型

```javascript

// 首先判断对象参数是否为“null”，是则返回“null”
// 判断对象参数数据类型是否为“object”，不是则返回该参数
// 获取到对象参数的构造函数名，判断是否为函数、正则、日期、ES6新对象其中之一，如果是则直接返回通过该参数对象对应的构造函数生成的新实例对象
// 当以上条件判断之后函数依然没有结束时继续进行以下操作
// 在Map对象中获取当前参数对象，如果能获取到，则说明这里为循环引用并返回Map对象中该参数对象的值
// 如果在Map对象中没有获取到对应的值，则保存该参数对象到Map中，作为标记
// 根据该参数的数据类型是否为数组创建新对象
// 遍历该对象参数，将每一项递归调用该函数本身的返回值赋给新对象

const _completeDeepClone = (target, map = new Map()) => {
    if(target === null) return target
    if(typeof target !== 'object') return target
    const constructor = target.constructor
    if(/^(Function|RegExp|Date|Map|Set)$/i.test(constructor.name)) return new constructor(target)
    if(map.get(target)) return map.get(target)
    map.set(target, true)
    const cloneTarget = Array.isArray(target) ? [] : {}
    for(prop in target) {
        if(target.hasOwnProperty(prop)) {
            cloneTarget[prop] = _completeDeepClone(target[prop], map)
        }
    }
    return cloneTarget
}
```

### 寄生组合式继承

通过寄生组合式继承使"Chinese"构造函数继承于"Human"构造函数。寄生组合式继承，即通过借用构造函数来继承属性，通过原型链的形式来继承方法，只调用了一次父类构造函数，效率高，也避免了在子类的原型对象上创建不必要的、多余的属性，原型链也不会被改变

```javascript

// 在"Human"构造函数的原型上添加"getName"函数
// 在”Chinese“构造函数中通过call函数借助”Human“的构造器来获得通用属性
// Object.create函数返回一个对象，该对象的__proto__属性为对象参数的原型。此时将”Chinese“构造函数的原型和通过Object.create返回的实例对象联系起来
// 最后修复"Chinese"构造函数的原型链，即自身的"constructor"属性需要指向自身
// 在”Chinese“构造函数的原型上添加”getAge“函数

function Human(name) {
    this.name = name
    this.kingdom = 'animal'
    this.color = ['yellow', 'white', 'brown', 'black']
}
Human.prototype.getName = function() {
    return this.name
}
function Chinese(name,age) {
    Human.call(this,name)
    this.age = age
    this.color = 'yellow'
}
Chinese.prototype = Object.create(Human.prototype)
Chinese.prototype.constructor = Chinese
Chinese.prototype.getAge = function() {
    return this.age
}
```

### 发布订阅模式

完成"EventEmitter"类实现发布订阅模式，考虑到同一名称事件可能有多个不同的执行函数，所以在构造函数中需要以对象的结构存放事件

```javascript

// 构造函数中创建”events“对象变量用于存放所有的事件
// 添加”on“函数，用于订阅事件。当总事件中不存在此事件时创建新的事件数组，当存在时将”fn“函数添加在该事件对应数组中
// 添加”emit“函数，用于发布事件，遍历该事件下的函数数组并全部执行

class EventEmitter {
    constructor() {
        this.events = {}
    }
    on(event, fn) {
        if(!this.events[event]) {
            this.events[event] = [fn]
        } else {
            this.events[event].push(fn)
        }
    }
    emit(event) {
        if(this.events[event]) {
            this.events[event].forEach(callback => callback())
        }
    }
}
```

### 观察者模式

完成"Observer"、"Observerd"类实现观察者模式。

```javascript

// 被观察者构造函数声明三个属性分别为"name"用于保存被观察者姓名、"state"用于保存被观察者状态、"observers"用于保存观察者们
// 被观察者创建"setObserver"函数，该函数通过数组的push函数将观察者参数传入"observers"数组中
// 被观察者创建"setState"函数，该函数首先通过参数修改被观察者的"state"属性，然后通过遍历"observers"数组分别调用各个观察者的"update"函数并且将该被观察者作为参数传入
// 观察者创建"update"函数，用于打印信息

class Observerd {
    constructor(name) {
        this.name = name
        this.state = '走路'
        this.observers = []
    }
    setObserver(observer) {
        this.observers.push(observer)
    }
    setState(state) {
        this.state = state
        this.observers.forEach(observer => observer.update(this))
    }
}
class Observer {
    constructor() {
         
    }
    update(observerd) {
        console.log(observerd.name + '正在' + observerd.state)
    }
}
```

## 华为机试题

### day1

- 计算字符串最后一个单词的长度，单词以空格隔开，字符串长度小于5000。（注：字符串末尾不以空格为结尾）

```JavaScript
// 方法一
var unit = readline()
var arr = unit.split(' ')
if(unit && arr.length < 5000 ){
    console.log(arr[arr.length - 1].length)
}
// 方法二
var unit = readline()
var i = unit.length - 1;
while (i > -1) {
        if (unit[i] === ' ') break;
        i -= 1;
    }
console.log(unit.length - 1 - i)
```

- 接受一个由字母、数字和空格组成的字符串，和一个字符，然后输出输入字符串中该字符的出现次数。（不区分大小写字母）

```JavaScript
// 方法一
var str = readline().toLowerCase()
var key = readline().toLowerCase() 

console.log(str.split(key).length -1)
// 方法二
console.log(str.match(new RegExp(`${key}`,'ig') || []).length)
//方法三
var num = 0
str.split('').forEach((i)=>{
    i==key ? num++ : ''
})
console.log(num)
```

- 生成N个1到500之间的随机整数。请你删去其中重复的数字，即相同的数字只保留一个，把其余相同的数去掉，然后再把这些数从小到大排序，按照排好的顺序输出。

```JavaScript
// 方法一
let n = parseInt(readline());
let arr = [...Array(n)].map(() => readline());
Array.from(new Set(arr)).sort((a, b) => a - b).map((e) => console.log(e));

//方法二
const num = readline()
const N = parseInt(num)
let arry = []
for(let i=0;i<N;i++){
    let val = readline()
    let index = parseInt(val)
    arry[index] = 1
}
for (let i=1;i<arry.length;i++){
    if(arry[i]){
        console.log(i)
    }
}

```

### day2

- 输入一个字符串，请按长度为8拆分每个输入字符串并进行输出；长度不是8整数倍的字符串请在后面补数字0，空字符串不处理。

```JavaScript
// 方法一

```

- 接受一个由字母、数字和空格组成的字符串，和一个字符，然后输出输入字符串中该字符的出现次数。（不区分大小写字母）

```JavaScript
// 方法一

```

- 接受一个由字母、数字和空格组成的字符串，和一个字符，然后输出输入字符串中该字符的出现次数。（不区分大小写字母）

```JavaScript
// 方法一

```

- 接受一个由字母、数字和空格组成的字符串，和一个字符，然后输出输入字符串中该字符的出现次数。（不区分大小写字母）

```JavaScript
// 方法一

```

- 接受一个由字母、数字和空格组成的字符串，和一个字符，然后输出输入字符串中该字符的出现次数。（不区分大小写字母）

```JavaScript
// 方法一

```

- 接受一个由字母、数字和空格组成的字符串，和一个字符，然后输出输入字符串中该字符的出现次数。（不区分大小写字母）

```JavaScript
// 方法一

```
