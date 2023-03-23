<!--
 * @Date: 2022-06-08 14:30:47
 * @Author: 
 * @Description: 物业督办-督查检查-列表页
 * @LastEditors: dtb
 * @LastEditTime: 2022-06-23 15:20:21
 * @FilePath: \lucheng-app\src\views\appView\propertysupervision\supervise\Index.vue
-->
<template>
  <div class="relay_task">
    <van-nav-bar fixed placeholder safe-area-inset-top :title="$route.meta.title" :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <div class="top_box">
      <el-date-picker
        class="data-picker"
        v-model="year"
        prefix-icon="el-icon-caret-bottom"
        :clearable="false"
        value-format="yyyy"
        type="year"
        @change="changeYear"
        placeholder="年份"
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
        v-model="name"
        left-icon="search"
        placeholder="搜索"
        @blur="getList"
      />
    </div>
    <div class="task_list">
      <van-list v-if="list.length > 0">
        <div
          class="card-list"
          v-for="(item, index) in list"
          :key="index"
          @click="toDetail(item)"
        >
          <div class="card-title">
            <div class="card-name">{{ item.projectName }}</div>
            <div class="card-type">
              <span
                :class="{
                  'blue-color': item.handlingStatus == 2,
                  'orange-color': item.handlingStatus == 1,
                }"
              >
                {{ item.handlingStatus == 1 ? "待处理" : "已处理" }}
              </span>
              <span class="el-icon-arrow-right"></span>
            </div>
          </div>
          <div class="card-box">
            <div class="card-item">
              <div class="card-item-label">检查时间</div>
              <div class="card-item-value">{{ item.inspectionTime }}</div>
            </div>
            <div class="card-item">
              <div class="card-item-label">责任单位</div>
              <div class="card-item-value">{{ item.responsibleUnitName }}</div>
            </div>
            <div class="card-item">
              <div class="card-item-label">责任人</div>
              <div class="card-item-value">
                {{ item.responsiblePeopleName }}
              </div>
            </div>
            <div class="card-item">
              <div class="card-item-label">督办状态</div>
              <div class="card-item-value">
                {{ item.supervisionStatus | supervisionStatus }}
              </div>
            </div>
            <!-- <div class="card-item">
              <div class="card-item-label">问题类别</div>
              <div class="card-item-value">{{item.problemCategory}}</div>
            </div>
            <div class="card-item">
              <div class="card-item-label">责任单位</div>
              <div class="card-item-value">{{item.responsibleUnit}}</div>
            </div>
            <div class="card-item">
              <div class="card-item-label">具体问题</div>
              <div class="card-item-value">{{item.problems}}</div>
            </div> -->
          </div>
        </div>
      </van-list>
      <div v-else style="padding-top: 150px">
        <no-data />
      </div>
    </div>
  </div>
</template>

<script>
import { supervisedCheck } from "@/api/projectDetail";

export default {
  name: "Index",
  props: {},
  components: {},
  filters: {
    supervisionStatus: (value) => {
      switch (value) {
        case "1":
          return "未督促";
        case "2":
          return "已督促";
        default:
          return "-";
      }
    },
  },
  data() {
    return {
      year: "2022",
      alarmValue: "",
      alarmOption: [
        { text: "全部", value: "" },
        { text: "待处理", value: 1 },
        { text: "已处理", value: 2 },
      ],
      name: "",
      finished: false,
      loading: false,
      list: [],
      addIcon: require("@/assets/images/officeInformation/add_icon.png"),
    };
  },
  mounted() {
    this.addZwlog(this)
    this.getList();
  },
  methods: {
    changeYear(val) {
      this.year = val;
      this.getList();
    },
    async getList() {
      let res = await supervisedCheck.list({
        projectName: this.name,
        year: this.year,
        handlingStatus: this.alarmValue,
        pageNum: 1,
        pageSize: 9999,
        type: 2,
      });
      if (res.code == 0) {
        this.list = res.data.list.map((item) => this.filterParams(item));
        // 加载状态结束
        this.loading = false;
        this.finished = true;
      }
    },
    filterParams(obj) {
      let _newPar = {};
      for (let key in obj) {
        if (
          (obj[key] === 0 || obj[key]) &&
          obj[key].toString().replace(/(^\s*)|(\s*$)/g, "") !== ""
        ) {
          _newPar[key] = obj[key];
        } else {
          _newPar[key] = "-";
        }
      }
      // 返回新对象
      return _newPar;
    },
    handerChangeTime(value) {
      console.log(value);
    },
    toDetail(data) {
      this.$router.push("/propertySupervision/scheduleDetail?id=" + data.id);
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
