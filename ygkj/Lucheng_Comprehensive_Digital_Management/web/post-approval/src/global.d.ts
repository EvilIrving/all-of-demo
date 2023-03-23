declare namespace request {
  /**
   * 基础的接口响应类型
   * @generics <T>为接口响应中的数据类型,基本代码补全用
   */
  interface BaseResponse<T = any> {
    code: number;
    data: T;
    message?: string;
    msg?: string;
    [keys: string]: any;
  }
}
