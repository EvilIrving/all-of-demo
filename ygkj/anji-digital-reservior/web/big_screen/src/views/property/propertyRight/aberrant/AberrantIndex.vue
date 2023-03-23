<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-10-12 15:50:29
 * @Description: 
 * @FilePath: \big_screen\src\views\property\propertyRight\aberrant\AberrantIndex.vue
-->
<template>
  <div class="aberrant-box">
    <div class="flex-box aberrant-title">
      <div>水库名称</div>
      <div>异常信息</div>
      <div>更新时间</div>
    </div>
    <div class="aberrant-list">
      <ul>
        <li class="flex-box">
          <div>老虎湾</div>
          <div>xxxxxxxxxx</div>
          <div>xxxxxxxxxx</div>
        </li>
        <li class="flex-box">
          <div>大官塘</div>
          <div>xxxxxxxxxx</div>
          <div>xxxxxxxxxx</div>
        </li>
        <li class="flex-box">
          <div>杨梅坞</div>
          <div>xxxxxxxxxx</div>
          <div>xxxxxxxxxx</div>
        </li>
        <li class="flex-box">
          <div>回车</div>
          <div>xxxxxxxxxx</div>
          <div>xxxxxxxxxx</div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {};
</script>

<style lang="scss" scoped>
.aberrant-box {
  width: 86.88%;
  margin: 0 auto;
  margin-top: 15px;
  background: rgba(67, 247, 226, 0.15);
  border: 1px solid rgba(4, 217, 226, 0.19);
  .aberrant-title {
    > div {
      width: 33.333%;
      color: #ffffff;
      font-size: 14px;
      height: 30px;
      line-height: 32px;
      text-align: center;
      border-right: 1px solid rgba(4, 217, 226, 0.19);
      &:last-child {
        border-right: none;
      }
    }
  }
  .aberrant-list {
    width: 100%;
    ul {
      width: 100%;
      li {
        border-top: 1px solid rgba(4, 217, 226, 0.19);
        > div {
          width: 33.333%;
          color: #ffffff;
          font-size: 12px;
          min-height: 24px;
          line-height: 24px;
          text-align: center;
          border-right: 1px solid rgba(4, 217, 226, 0.19);
          &:last-child {
            border-right: none;
          }
        }
      }
    }
  }
}
</style>