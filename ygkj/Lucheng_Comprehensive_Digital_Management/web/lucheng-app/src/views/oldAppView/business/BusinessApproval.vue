<!--
 * @Date: 2022-06-01 09:26:38
 * @Author: 
 * @Description: 业务审批
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-11-08 15:00:34
 * @FilePath: \lucheng-older\src\views\appView\business\BusinessApproval.vue
-->
<template>
  <view-page>
    <div class="project-wrap">
      <div class="select-box">
        <el-date-picker
          class="data-picker"
          v-model="typeValue"
          prefix-icon="el-icon-caret-bottom"
          :clearable="false"
          value-format="yyyy"
          type="year"
          placeholder="年份"
          @change="getList"
        >
        </el-date-picker>
        <van-dropdown-menu
          z-index="2333"
          class="van-dropdown-menu"
          active-color="#276AF0"
        >
          <van-dropdown-item
            title-class="title-class-name"
            v-model="alarmValue"
            :options="alarmOption"
            @change="getList"
          />
        </van-dropdown-menu>
        <van-field
          class="van-field"
          v-model="value3"
          left-icon="search"
          placeholder="搜索"
        />
      </div>
      <div class="map-box">
        <van-list v-model="loading" :finished="finished" @load="onLoad">
          <div
            class="card-list"
            v-for="item in form"
            :key="item"
            @click="toDetail(item)"
          >
            <div class="card-title">
              <div class="card-name">{{ item.projectName }}</div>
              <div class="card-type">
                <span class="orange-color" v-if="item.hiddenDangerStatus === 0"
                  >待处理</span
                >
                <span class="blur-color" v-if="item.hiddenDangerStatus === 1"
                  >已完成</span
                >
                <span class="el-icon-arrow-right"></span>
              </div>
            </div>
            <div class="card-box">
              <div class="card-item">
                <div class="card-item-label">工程类型</div>
                <div class="card-item-value">{{ item.projectType }}</div>
              </div>
              <div class="card-item">
                <div class="card-item-label">类型</div>
                <div class="card-item-value">{{ item.problemSource }}</div>
              </div>
              <div class="card-item">
                <div class="card-item-label">上报人</div>
                <div class="card-item-value">{{ item.creator }}</div>
              </div>
              <div class="card-item">
                <div class="card-item-label">任务描述</div>
                <div class="card-item-value">{{ item.taskDescribe }}</div>
              </div>
              <div class="card-item">
                <div class="card-item-label">具体隐患</div>
                <div class="card-item-value">{{ item.hiddenDanger }}</div>
              </div>
            </div>
          </div>
        </van-list>
      </div>
    </div>
  </view-page>
</template>

<script>
import { propertyMt } from "@/api/appView";

export default {
  name: "RainfallList",
  components: {},
  data() {
    return {
      typeValue: "",
      alarmValue: "",
      value3: "",
      alarmOption: [
        { text: "全部问题", value: "" },
        { text: "一般问题", value: "一般问题" },
        { text: "较重问题", value: "较重问题" },
        { text: "严重问题", value: "严重问题" },
      ],
      finished: false,
      loading: false,
      list: [],
      form: {},
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    handerChangeTime(value) {},
    onLoad() {
      // 异步更新数据
      // setTimeout 仅做示例，真实场景中一般为 ajax 请求
      setTimeout(() => {
        for (let i = 0; i < 10; i++) {
          this.list.push(this.list.length + 1);
        }
        // 加载状态结束
        this.loading = false;
        // 数据全部加载完成
        if (this.list.length >= 40) {
          this.finished = true;
        }
      }, 1000);
    },
    toDetail(item) {
      this.$router.push("/businessApprovalInfo?id=" + item.id);
    },
    async getList() {
      let res = await propertyMt.DailyTask({
        pageNum: 1,
        pageSize: 10,
        year: this.typeValue,
        problemType: this.alarmValue,
      });

      if (res.code === 0) {
        this.form = res.data.list;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.project-wrap {
  width: 100%;
  height: 100%;
  .select-box {
    height: 43px;
    border-bottom: 1px solid #e6e6e6;
    display: flex;
    .van-dropdown-menu {
      flex: 1;
    }
    .data-picker {
      flex: 1;
    }
    .van-field {
      width: 160px;
    }
  }
  .map-box {
    height: calc(100% - 120px);
    background-color: #f2f6f9;
    overflow: auto;
    .card-list {
      background-color: #ffffff;
      padding: 0 14px 8px 11px;
      margin-bottom: 6px;
      .card-title {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 12px 0 10px 0;
        .card-name {
          font-weight: 600;
          color: #333333;
          font-size: 22px;
          width: 65%;
        }
        .card-type {
          font-size: 18px;
          font-weight: 500;
          > span:nth-child(1) {
            padding-right: 8px;
            &.blur-color {
              color: #036eff;
            }
            &.orange-color {
              color: #fa6400;
            }
          }
          > span:nth-child(2) {
            color: #b0b0bf;
            font-weight: 600;
          }
        }
      }
      .card-box {
        .card-item {
          display: flex;
          line-height: 26px;
          padding-bottom: 4px;
          .card-item-label {
            color: #666666;
            font-size: 20px;
            position: relative;
            width: 95px;
          }
          .card-item-value {
            color: #333333;
            font-size: 20px;
            width: calc(100% - 95px);
            // overflow: hidden;
            // white-space: nowrap;
            // text-overflow: ellipsis;
            text-align: left;
          }
        }
      }
    }
  }
}
</style>

