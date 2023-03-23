<template>
  <div class="project-responsible-item">
    <div class="item-title">{{ data.title }}</div>
    <div class="item-contact">
      <div class="contact-avatar">
        <el-avatar shape="square" :src="squareUrl"></el-avatar>
      </div>
      <div class="contact-content">
        <div class="contact-name">
          <span>{{ data.name }}</span>
          <span>
            <el-tooltip content="信息" placement="top">
              <i class="icon-proj-message" />
            </el-tooltip>

            <el-tooltip content="通话" placement="top">
              <i class="icon-proj-viedo-call" />
            </el-tooltip>
            <el-tooltip content="钉钉" placement="top">
              <i class="icon-proj-dingtalk" />
            </el-tooltip>
          </span>
        </div>
        <div class="contact-position">{{ data.position }}</div>
        <div class="contact-phone">{{ data.phone }}</div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "ResonsibleListItem",
  props: {
    data: {
      type: Object,
    },
  },
  data() {
    return {
      squareUrl:
        "https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png",
    };
  },
};
</script>

<style lang="scss" scoped>
.project-responsible-item {
  margin-bottom: 16px;
  .item-title {
    color: #45a4cf;
    font-size: 16px;
    margin-bottom: 5px;
    margin-left: 5px;
  }
  .item-contact {
    border: 1px solid #285c9e;
    border-radius: 5px;
    display: flex;
    padding: 6px;
    .contact-avatar {
      height: 80px;
      width: 80px;
      border-radius: 5px;
      background-color: #042550;
      ::v-deep .el-avatar {
        width: 100%;
        height: 100%;
      }
    }
    .contact-content {
      margin-left: 16px;
      margin-bottom: 8px;
      flex: 2;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      .contact-name {
        color: #fff;
        font-size: 20px;
        display: flex;
        justify-content: space-between;
        i {
          cursor: pointer;
          margin: 0 5px;
          &:first-child {
            margin: 0;
          }
          &:last-child {
            margin: 0;
          }
        }
      }
      .contact-position {
        color: #fff;
      }
    }
  }
}
</style>