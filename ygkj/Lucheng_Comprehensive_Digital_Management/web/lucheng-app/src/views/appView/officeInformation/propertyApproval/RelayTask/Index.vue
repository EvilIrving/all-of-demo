<template>
  <div class="relay_task">
     <van-nav-bar fixed placeholder safe-area-inset-top :title="$route.meta.title" :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <div class="top_box">
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
    <div class="task_list">
      <van-list
          v-model="loading"
          :finished="finished"
          @load="getList"
      >
        <div class="card-list" v-for="(item,index) in list" :key="index" @click="toDetail(item.id)">
          <div class="card-title">
            <div class="card-name">{{ item.projectName }}</div>
          </div>
          <div class="card-box">
            <div class="card-item">
              <div class="card-item-label">工程类型</div>
              <div class="card-item-value">{{ item.projectType }}</div>
            </div>
            <div class="card-item">
              <div class="card-item-label">问题类型</div>
              <div class="card-item-value">{{ item.problemType }}</div>
            </div>
            <div class="card-item">
              <div class="card-item-label">时间</div>
              <div class="card-item-value">{{ item.tm }}</div>
            </div>
            <div class="card-item">
              <div class="card-item-label">任务描述</div>
              <div class="card-item-value">{{ item.taskDescribe }}</div>
            </div>
          </div>
        </div>
      </van-list>
    </div>
    <div class="add_btn">
      <img :src="addIcon" @click="toAddNew" />
    </div>
  </div>
</template>

<script>
import { propertyMt } from "@/api/appView";

export default {
  name: "Index",
  props: {},
  components: {},
  data() {
    return {
      typeValue: "2022",
      alarmValue: '',
      alarmOption: [
        { text: "全部问题", value: '' },
        { text: "一般问题", value: '一般问题' },
        { text: "较重问题", value: '较重问题' },
        { text: "严重问题", value: '严重问题' },
      ],
      value3: "",
      finished: false,
      loading: false,
      list: [],
      addIcon:require("@/assets/images/officeInformation/add_icon.png"),
      pageNum:1,
      pageSize:10
    }
  },
  mounted() {
    this.addZwlog(this)
    this.getList();
  },
  methods: {
    handerChangeTime(value) {
      console.log(value);
    },
    async getList(){
      let res = await propertyMt.DailyTask({
        pageNum:this.pageNum,
        pageSize:this.pageSize,
        problemType:this.alarmValue,
        year:this.typeValue

      })
      if(res.code===0){
        this.list = res.data.list
        this.loading = false;
        this.finished = true;
      }
    },

    toDetail(id){
      this.$router.push('/taskDetail?id=' + id)
    },
    toAddNew() {
      this.$router.push("/newTask");
    },
  },
};
</script>

<style scoped lang="scss">
.relay_task {
  width: 100%;
  height: 100%;
  .top_box {
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
      width: 180px;
    }
  }
  .task_list {
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
          font-size: 16px;
        }
        .card-type {
          font-size: 14px;
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
          justify-content: space-between;
          align-items: center;
          line-height: 26px;
          .card-item-label {
            color: #666666;
            font-size: 13px;
            position: relative;
            padding-left: 13px;
            &::after {
              position: absolute;
              content: "";
              width: 6px;
              height: 6px;
              background-color: #999999;
              border-radius: 50%;
              left: 0;
              top: 50%;
              margin-top: -4px;
            }
          }
          .card-item-value {
            color: #333333;
            font-size: 13px;
            max-width: 75%;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
          }
        }
      }
    }
  }
  .add_btn {
    position: absolute;
    top: 583.5px;
    left: 278px;
    img {
      width: 60px;
      height: 60px;
    }
  }
}
</style>
<style lang="scss">
.data-picker {
  .el-input__inner {
    border: none;
    height: 43px;
    line-height: 43px;
    padding: 0 10px;
    font-size: 12px;
    text-align: center;
  }
  .el-input__prefix {
    top: 1px;
    color: #aaaaaa;
    font-size: 16px;
    left: 75%;
    color: #aaaaaa;
  }
}
</style>
