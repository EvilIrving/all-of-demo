<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-10-12 16:02:19
 * @Description: 
 * @FilePath: \big_screen\src\views\property\propertyRight\maintain\MaintainIndex.vue
-->
<template>
  <div class="maintain">
    <ul>
      <li class="flex-box">
        <div>老虎湾</div>
        <div class="carry-out">已开展</div>
        <div class="carry-out-color"></div>
      </li>
      <li class="flex-box">
        <div>灯头坞</div>
        <div>未开展</div>
        <div></div>
      </li>
      <li class="flex-box">
        <div>姚坞里</div>
        <div class="carry-out">已开展</div>
        <div class="carry-out-color"></div>
      </li>
      <li class="flex-box">
        <div>回车</div>
        <div class="carry-out">已开展</div>
        <div class="carry-out-color"></div>
      </li>
      <li class="flex-box">
        <div>杨梅坞</div>
        <div>未开展</div>
        <div></div>
      </li>
      <li class="flex-box">
        <div>大官塘</div>
        <div>未开展</div>
        <div></div>
      </li>
    </ul>
  </div>
</template>
<script>
export default {};
</script>

<style lang="scss" scoped>
.maintain {
  ul {
    width: 90%;
    margin: 0 auto;
    margin-top: 10px;
    li {
      align-items: center;
      background: url("../../../../assets/image/maintain-icon.png") no-repeat;
      height: 36px;
      line-height: 36px;
      background-size: 100% 100%;
      > div {
        font-size: 14px;
        color: #c0c4cc;
        &:first-child {
          color: #00fbff;
          padding-left: 10px;
          width: 50%;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        &:nth-child(2) {
          padding-left: 10px;
        }
        &:last-child {
          width: 11px;
          height: 11px;
          border-radius: 50%;
          background-color: rgba(217, 217, 217, 0.49);
          margin-left: 30px;
        }
        &.carry-out {
          color: #ffffff;
        }
        &.carry-out-color {
          background-color: rgba(57, 239, 15, 1);
        }
      }
    }
  }
}
</style>