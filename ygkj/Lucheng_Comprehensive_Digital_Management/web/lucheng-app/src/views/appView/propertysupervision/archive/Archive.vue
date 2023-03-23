<!--
 * @Date: 2022-06-08 18:04:24
 * @Author: 
 * @Description: 物业督办-归档成果
 * @LastEditors: dtb
 * @LastEditTime: 2022-06-23 15:20:15
 * @FilePath: \lucheng-app\src\views\appView\propertysupervision\archive\Archive.vue
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
        />
      </van-dropdown-menu>
      <van-field
        class="van-field"
        v-model="name"
        left-icon="search"
        placeholder="搜索"
      />
    </div>
    <div class="task_list">
      <van-list v-if="list.length > 0">
        <div class="card-list" v-for="(item, index) in list" :key="index">
          <div class="card-title">
            <div class="card-name">{{ item.projectName }}</div>
          </div>
          <div class="card-box" @click="toDetail(item.id)">
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
          </div>
          <div class="evaluation_box">
            <div class="archive">已归档</div>
            <div class="evaluation">
              <img src="../../../../assets/comment.png" alt="" />
              <span class="state" @click="evaluate(item.id)">{{ item.evaluationStatus == 1 ? '评价' : '已评价'}}</span>
            </div>
          </div>
        </div>
      </van-list>
      <div v-else style="padding-top: 150px">
        <no-data  />
      </div>
    </div>
  </div>
</template>

<script>
import { supervisedCheck } from "@/api/projectDetail";
import { filterParams } from "@/utils/utils";
import noData from '@/components/noData.vue'
export default {
  name: "archive",
  props: {},
  components: {noData},
  data() {
    return {
      year: "2022",
      alarmValue: '',
      alarmOption: [
        { text: "全部", value: '' },
        { text: "未评价", value: 1 },
        { text: "已评价", value: 2 },
      ],
      name: "",
      list: [],
      addIcon: require("@/assets/images/officeInformation/add_icon.png"),
    };
  },
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
        evaluationStatus: this.alarmValue,
        pageNum: 1,
        pageSize: 9999,
        type: 4,
      });
      if (res.code == 0) {
        this.list = res.data.list.map((item) => filterParams(item));
        // 加载状态结束
      }
    },
    handerChangeTime(value) {
    },
    toDetail(id) {
      this.$router.push("/propertySupervision/evaluate?id=" + id);
    },
    evaluate(id) {
      this.$router.push("/propertySupervision/evaluateArchive?id=" + id);
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
    .evaluation_box {
      color: #036eff;
      font-size: 14px;
      background-color: #ffffff;
      margin: 6px 0 0 0;
      display: flex;
      justify-content: space-between;
      align-items: center;
      line-height: 26px;

      .evaluation {
        width: 70px;
        background: #f2f6f9;
        padding: 1px 0px;
        border-radius: 20px;
        display: flex;
        justify-content: center;
        align-items: center;
        img {
          width: 20%;
          margin-right: 6px;
        }
      }
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
