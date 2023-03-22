---
title: TypeScript尝试入门
date: 2023-02-28
update: 2023-03-01
tags: 
- TypeScript
categories: 
- TypeScript
---

## 了解一下

TypeScript是什么：  `TypeScript`是`JavaScript`的超集，意味着它能做`JavaScript`所做的一切，但有一些附加功能。与JS的主要区别：

* 类型系统、type-checking
* 类型（自动）推导、auto-completion

学习TS的原因：

* 提升代码健壮性
* 面向接口编程（代码自解释，并行开发）
* 静态检查可以提高开发效率
* 减少开发时（人工推导带来的）认知负荷

## 运行 TypeScript

### 命令行工具

* `tsc`，编译器
  * `npm i -g TypeScript`
  * 查看版本：`tsc -v`  
  * 实时看编译结果：`tsc index.ts -w --outfile index.js`  
    > `-w` 自动编译你的代码
    > `--outfile file-name.js` 指定输出文件的名称
* [`ts-node`](https://typestrong.org/ts-node/), 运行时解析 TS ，执行Node API
  * `npm i -g ts-node`
* [`parceljs`](https://v2.parceljs.cn/docs/), 零配置的构建工具
  * `npm install -g parcel`
* `tsc init` 初始化 [`tsconfig.json`](https://www.typescriptlang.org/tsconfig)
  * 指定根文件，编译器选项

## 主要概念

### 接口--interface

使用接口定义一个对象

```ts
interface Animal {
    name: string;
    age: number;
}

// 接口继承
interface Bear extends Animal {
  honey: boolean
}

const bear: Bear = {
  name: "Winnie",
  honey: true,
}

// 直接在现有接口上添加新字段，类型别名是不能这样操作的
interface Animal {
    hasTail: boolean
}
```

#### 有类的接口

通过实现一个接口来告诉一个类，它必须包含某些属性和方法

```ts
interface HasFommat {
    format(): string;
}
class Users implements HasFommat {
    constructor(public username: string, protected password: number) { }

    format() {
        return this.username.toLocaleLowerCase()
    }
}

let user1: HasFommat
let user2: HasFommat

user1 = new Users('CAIN',4635121)
user2 = new Users('K',1314156)

user1.format()

//  实现HasFormatter的对象数组
let users : HasFommat[] = []
users.push(user1)
users.push(user2)
```

### 类型别名

```ts
type StringOrNumber = string | number;

type PersonObject = {
    name: string;
    id: StringOrNumber;
};
// 扩展一个类型 
type ChildrenObject = PersonObject & {
    lollypop: string
}
```

>TypeScript文档推荐使用接口来定义对象，直到你需要使用一个类型的功能。

> 使用接口而不是使用Class的优势是，它只被TypeScript使用，它不会被编译，编译后不会产生冗余代码

> 类本质上是一个对象工厂，接口是一个仅用于类型检查的结构。

### 基础类型

`JavaScript`中，原始值是指不属于对象且没有方法的数据，而在`TypeScript`中基元（Primitives）是不可变的（immutable）：它们不能被改变。重要的是，不要将基元本身与分配给基元值的变量相混淆。

声明一个变量后添加: type（称为 "类型注解 "或 "类型签名"），或多个类型：联合类型是一个可以被分配到多个类型的变量

```ts
// 添加: type
let id: number = 5;

// 联合类型来定义包含多种类型的数组
let age: number | string = '9'
let ids: (number | boolean | string)[] = ['1']

// 元组（Tuples）。元组是一个具有固定大小和已知数据类型的数组
let mamse: [number, boolean, string] = [3, true, '3']
```

>即便不指定类型，TypeScript也会自动推断变量的类型——类型推理

### 对象

```ts
// 对象必须有所有正确的属性和值类型
let person: {
    name: string;
    location: string;
    isProgrammer: boolean;
};
// 在使用的时候，赋值类型错误会报错，整体赋值时缺少某些属性未赋值也会报错：
// ERROR: should be a boolean
person.isProgrammer = 'Yes'; 

// ERROR: missing the isProgrammer property
person = {
  name: 'John',
  location: 'US',
}; 
```

定义对象时，通常使用`接口：interface`

```ts
interface Person {
  name: string;
  location: string;
  isProgrammer: boolean;
}
// 使用
let person1: Person = {}
let person2: Person = {}
```

### 函数

>同样可以使用接口定义

```ts
interface Speech {
    sayHI(name: string): string,
    sayBye: (name: string) => string
}

let sayStuff: Speech = {
    sayHI: function (name: string) : number {
        return `Hi, ${name}`
    },
    sayBye: (name: string) => `bye, ${name}`
}

console.log(sayStuff.sayBye('K'));  // Hi, K
console.log(sayStuff.sayHI('cain'));  // bye, K
```

定义函数参数的类型，以及函数的返回类型

```ts
// 使用function声明
function circle(diam: number): string {
    return 'The circumference is ' + Math.PI * diam;
}
// 或使用箭头函数
const circle2 = (diam: number,): string => {
    return 'The circumference is ' + Math.PI * diam;
};

// ts中，我们可以自行决定是否明确声明的是一个函数及函数的返回类型
// 在一个参数后面加一个问号，使其成为可选参数（在添加：Function时，编译无报错，不知为何）
const circle3: Function = (diam: number, b: number, c?: number | string): string => {
    return 'The circumference is ' + Math.PI * diam;
};
```

### 操作DOM

>前置概念：非空断言操作符`！`，我们可以明确地告诉编译器，一个表达式的值不是 "空 "或 "未定义"。

在`HTML`中，通过`queryselectorAll`等选择元素，然而比如通过`id`获取`img`元素后，该方法不能通过`id`就确定当前元素的类型，因此只能返回一个宽泛的类型`Element`，下一步操作图片的`src`属性就不可能了。

在TS中通过类型断言`as`指定具体类型，`console.dir()`打印元素，可以看到元素类型。

```ts
//获取h1元素
let title = document.querySelector("#title")
//获取img元素
let img = document.querySelector("#image") as HTMLImageElement
```

### 类

* 修饰符
  * `public` `private` `protected`
* 参数属性
  * `readonly`, 只允许出现在属性声明或索引签名或构造函数中
  * `readonly` 写在其他访问修饰符后面
* 抽象类
  * `abstract` 用于定义抽象类和其中的抽象方法
  * 抽象类是不允许被实例化
  * 抽象类中的抽象方法必须被子类实现 (required)

### 模块

在tsconfig.json文件中，改变以下选项以支持现代的导入和导出

```ts
  - "module": "CommonJS"
  + "module": "es2015"
```

在HTML文件引入时：

```ts
<script type="module" src="/public/script.js"></script>
```

>注意：总是作为一个JavaScript文件导入，即使在TypeScript文件中。

### 泛型

泛型（Generics）是指在定义函数、接口或类的时候，不预先指定具体的类型，而在使用的时候再指定类型的一种特性。设计泛型的关键目的是在成员之间提供有意义的约束，这些成员可以是：

* 类的实例成员
* 类的方法
* 函数参数
* 函数返回值

> 泛型就是用一个相同类型来关联两个或者更多的值。

### 枚举（Enums）

枚举（Enums）允许我们定义或声明相关值的集合，可以是**数字或字符串**，作为一组命名的常量。默认情况下，枚举（enums）是基于数字的。

```ts
enum ResourceTypes {
    Book = 1,
    // Up = 'Up',
    Author,
    Director,
    Person
}

console.log(ResourceTypes.Book);
```

### Type Narrowing

**变量可以从一个不太精确的类型转移到一个更精确的类型**。 这个过程称为类型缩小（type narrowing）

```ts
interface Vehicle {
    topSpeed: number
}

interface Train extends Vehicle {
    type: 'Train',
    carriages: number
}

interface Plane extends Vehicle {
    type: 'Plane',
    wingSpan: number
}

type PlaneOrTrain = Plane | Train

function getSpeedRatio(v: PlaneOrTrain) {
    if (v.type === 'Train') return v.topSpeed / v.carriages
    return v.topSpeed / v.wingSpan
}

let BigTrain: Train = {
    type: 'Train',
    carriages: 100,
    topSpeed: 200
}

console.log(getSpeedRatio(BigTrain));
```

## 资料

[TypeScript Tutorial-The Net Ninja Youtube Video](https://www.youtube.com/watch?v=2pZmKW9-I_k&list=PL4cUxeGkcC9gUgr39Q_yD6v-bSyMwKPUI)
[TypeScript 入门指南](https://rualc.com/cs/typescript-language-basic/#cong-js-qian-yi-dao-ts)：文章对学习TS做了归纳

* 不同版本的教程
  * [TypeScript Deep Dive 中文版](https://ts.xcatliu.com/introduction/what-is-typescript.html)：内容多，详细
  * [typescript-xcatliu版本](https://ts.xcatliu.com/introduction/what-is-typescript.html)
  * [typescript-yayujs翻译版](https://ts.yayujs.com/handbook/TheBasics.html)
