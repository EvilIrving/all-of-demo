---
title: 递归与堆栈
date: 2020-03-18
updated: 2020-03-19
tags:
- JavaScript
- 递归与堆栈
categories:
- JavaScript
---

## 递归深度

最大的嵌套调用次数（包括首次），也等于堆栈中上下文的最大数量，最大递归深度受限于 JavaScript 引擎。引擎在最大迭代深度为 10000 及以下时是可靠的。相比来说，循环算法更节省内存。虽然任何递归都可以用循环来重写，并且通常循环变体更有效，但递归可以使代码更短，更易于理解和维护

## 执行上下文

一个内部数据结构，它包含有关函数执行时的详细细节：当前控制流所在的位置，当前的变量，this 的值，以及其它的一些内部细节。

一个函数调用仅具有一个与其相关联的执行上下文。当一个函数进行嵌套调用时，将发生以下的事：
当前函数被暂停；
与它关联的执行上下文被一个叫做 执行上下文堆栈 的特殊数据结构保存；
执行嵌套调用；
嵌套调用结束后，从堆栈中恢复之前的执行上下文，并从停止的位置恢复外部函数。

## 链表

链表元素 是一个使用以下元素通过递归定义的对象

* value。
* next 属性引用下一个 链表元素 或者代表末尾的 null。  
  
## 测试任务

* 编写一个函数 sumTo(n) 计算 1 + 2 + ... + n 的和
* 编写一个函数 factorial(n) 使用递归调用计算 n!
* 斐波那契数 序列有这样的公式： Fn = Fn-1 + Fn-2。编写一个函数 fib(n) 返回第 n 个斐波那契数。
* 编写一个可以逐个输出链表元素的函数 printList(list)及反向输出单链表函数reversePrintList(list)。两种解法：循环和递归。

## 参考答案

```javascript
// 循环
function sumTo(n) {
  let sum = 0;
  for (let i = 1; i <= n; i++) {
    sum += i;
  }
  return sum;
}
// 递归
function sumTo(n) {
  if (n == 1) return 1;
  return n + sumTo(n - 1);
}
// 公式 sumTo(n) = n*(n+1)/2
function sumTo(n) {
  return n * (n + 1) / 2;
}

// 递归
function factorial(n) {
  return (n != 1) ? n * factorial(n - 1) : 1;
}

// 递归
function fib(n) {
  return n <= 1 ? n : fib(n - 1) + fib(n - 2);
}
// 循环
function fib(n) {
  let a = 1;
  let b = 1;
  for (let i = 3; i <= n; i++) {
    let c = a + b;
    a = b;
    b = c;
  }
  return b;
}
// 循环
let list = {
  value: 1,
  next: {
    value: 2,
    next: {
      value: 3,
      next: {
        value: 4,
        next: null
      }
    }
  }
};

function printList(list) {
  let tmp = list;

  while (tmp) {
    alert(tmp.value);
    tmp = tmp.next;
  }

}

printList(list);

// 递归
let list = {
  value: 1,
  next: {
    value: 2,
    next: {
      value: 3,
      next: {
        value: 4,
        next: null
      }
    }
  }
};

function printList(list) {

  alert(list.value); // 输出当前元素

  if (list.next) {
    printList(list.next); // 链表中其余部分同理
  }

}

printList(list);


// 循环

let list = {
  value: 1,
  next: {
    value: 2,
    next: {
      value: 3,
      next: {
        value: 4,
        next: null
      }
    }
  }
};

function printReverseList(list) {
  let arr = [];
  let tmp = list;

  while (tmp) {
    arr.push(tmp.value);
    tmp = tmp.next;
  }

  for (let i = arr.length - 1; i >= 0; i--) {
    alert( arr[i] );
  }
}

printReverseList(list);

// 递归

let list = {
  value: 1,
  next: {
    value: 2,
    next: {
      value: 3,
      next: {
        value: 4,
        next: null
      }
    }
  }
};

function printReverseList(list) {

  if (list.next) {
    printReverseList(list.next);
  }

  alert(list.value);
}

printReverseList(list);
```
