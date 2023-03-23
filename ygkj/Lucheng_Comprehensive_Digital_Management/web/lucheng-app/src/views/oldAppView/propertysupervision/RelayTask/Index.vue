<!--
 * @Date: 2022-06-08 14:30:47
 * @Author: 
 * @Description: 物业督办-督查检查-列表页
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-11-09 14:10:27
 * @FilePath: \lucheng-older\src\views\appView\propertysupervision\RelayTask\Index.vue
-->
<template>
  <view-page>
    <div class="relay_task">
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
            @click="toDetail(item.id)"
          >
            <div class="card-title">
              <div class="card-name">{{ item.projectName }}</div>
            </div>
            <div class="card-box">
              <div class="card-item">
                <div class="card-item-label">检查时间</div>
                <div class="card-item-value">{{ item.inspectionTime }}</div>
              </div>
              <div class="card-item">
                <div class="card-item-label">责任单位</div>
                <div class="card-item-value">
                  {{ item.responsibleUnitName }}
                </div>
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
      <div class="add_btn">
        <img :src="addIcon" @click="toAddNew" />
      </div>
    </div>
  </view-page>
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
        { text: "未督促", value: 1 },
        { text: "已督促", value: 2 },
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
        supervisionStatus: this.alarmValue,
        pageNum: 1,
        pageSize: 9999,
        type: 1,
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
    toDetail(id) {
      this.$router.push("/propertySupervision/inspectionDetail?id=" + id);
    },
    toAddNew() {
      this.$router.push("/propertySupervision/addInspection");
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
      width: 150px;
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
          font-size: 22px;
        }
      }
      .card-box {
        .card-item {
          display: flex;
          justify-content: space-between;
          line-height: 26px;
          padding-bottom: 5px;
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
