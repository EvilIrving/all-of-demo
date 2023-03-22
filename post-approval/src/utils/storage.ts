/**
 * 封装操作localstorage本地存储的方法
 */
export const storage = {
  //存储
  // eslint-disable-next-line
  set(key: string, value: any): void {
    localStorage.setItem(key, JSON.stringify(value));
  },
  //取出数据
  get<T = string>(key: string): T | null {
    const value = localStorage.getItem(key);
    if (value && value != 'undefined' && value != 'null') {
      return JSON.parse(value) as T;
    }
    return null;
  },
  // 删除数据
  remove(key: string): void {
    localStorage.removeItem(key);
  }
};
/**
 * 封装操作sessionStorage本地存储的方法
 */
export const sessionStorage = {
  //存储
  // eslint-disable-next-line
  set(key: string, value: any): void {
    window.sessionStorage.setItem(key, JSON.stringify(value));
  },
  //取出数据
  get<T = string>(key: string): T | null {
    const value = window.sessionStorage.getItem(key);
    if (value && value != 'undefined' && value != 'null') {
      return JSON.parse(value) as T;
    }
    return null;
  },
  // 删除数据
  remove(key: string): void {
    window.sessionStorage.removeItem(key);
  }
};
