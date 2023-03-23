<!--
 * @Date: 2022-06-01 09:26:38
 * @Author: 
 * @Description:验收申请
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-11-09 10:46:54
 * @FilePath: \lucheng-older\src\views\appView\acceptance\Acceptance.vue
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
          @change="search"
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
            @change="search"
          />
        </van-dropdown-menu>
        <van-dropdown-menu
          z-index="2333"
          class="van-dropdown-menu"
          active-color="#276AF0"
        >
          <van-dropdown-item
            title-class="title-class-name"
            v-model="statusValue"
            :options="statusOption"
            @change="search"
          />
        </van-dropdown-menu>
      </div>
      <div class="select-box">
        <van-field
          class="van-field"
          v-model="dispatchName"
          left-icon="search"
          placeholder="搜索"
          @blur="search"
        />
      </div>
      <div class="map-box" v-loading="loading" ref="map_box">
        <div class="total-card" v-if="alarmValue == 1">
          申请总数：<span class="blue-color">{{ countData.totalNum }}</span>
        </div>
        <div class="total-card" v-if="alarmValue == 2">
          待审批：<span class="orange-color">{{ countData.approveNum }}</span>
        </div>
        <van-list
          v-if="list.length > 0"
          v-model="loading"
          :finished="finished"
          @load="onLoad"
        >
          <div
            class="card-list"
            v-for="item in list"
            :key="item.id"
            @click="goDetail(item)"
          >
            <div class="card-title">
              <div class="card-name">{{ item.contractName }}</div>
              <div class="card-type">
                <span
                  :class="{
                    'blue-color': item.status == 3,
                    'red-color': item.status == 2,
                    'orange-color': item.status == 1,
                  }"
                  >{{
                    item.status == 1
                      ? "审核中"
                      : item.status == 2
                      ? "已驳回"
                      : "已通过"
                  }}</span
                >
                <span class="el-icon-arrow-right"></span>
              </div>
            </div>
            <div class="card-box">
              <div class="card-item">
                <div class="card-item-label">物业管理单位</div>
                <div class="card-item-value">{{ item.propertyUnitName }}</div>
              </div>
              <div class="card-item">
                <div class="card-item-label">运行管理单位</div>
                <div class="card-item-value">{{ item.runUnitName }}</div>
              </div>
              <div class="card-item">
                <div class="card-item-label">审核人</div>
                <div class="card-item-value">
                  {{ item.runUnitReviewerName }}
                </div>
              </div>
              <div class="card-item">
                <div class="card-item-label">服务期限</div>
                <div class="card-item-value">{{ serviceTimes(item) }}</div>
              </div>
            </div>
          </div>
        </van-list>
        <div v-else style="padding-top: 150px">
          <no-data />
        </div>
      </div>

      <div class="add_btn" v-if="userInfo.userType == 3">
        <img :src="addIcon" @click="toAddNew" />
      </div>
    </div>
  </view-page>
</template>

<script>
import { contractApi } from "@/api/rainfall.js";
import { localData } from "@/plugins/storage";
export default {
  name: "RainfallList",
  components: {},
  computed: {
    userInfo() {
      return localData("get", "userInfo");
    },
  },
  data() {
    return {
      typeValue: "2022",
      alarmValue: "1",
      statusValue: "",
      dispatchName: "",
      alarmOption: [
        { text: "我的申请", value: "1" },
        { text: "我的审批", value: "2" },
      ],
      statusOption: [
        { text: "全部", value: "" },
        { text: "审批中", value: "1" },
        { text: "已通过", value: "3" },
        { text: "已驳回", value: "2" },
      ],
      finished: false,
      list: [],
      addIcon: require("@/assets/images/officeInformation/add_icon.png"),
      pageNum: 1,
      totalPages: 1,
      countData: {},
      loading: false,
    };
  },
  mounted() {
    let type = this.$route.query.type;
    if (type == 2) {
      this.alarmValue = "2";
    }
    this.getList();
    this.getCount();
  },
  methods: {
    async getCount() {
      let res = await contractApi.countApplication({
        year: this.typeValue,
        status: this.statusValue,
        propertyUnitName: this.dispatchName,
        type: this.alarmValue,
      });
      if (res.code == 0) {
        this.countData = res.data;
      }
    },
    async getList() {
      let res = await contractApi.list({
        pageNum: this.pageNum,
        pageSize: 10,
        year: this.typeValue,
        status: this.statusValue,
        propertyUnitName: this.dispatchName,
        type: this.alarmValue,
      });
      if (res.code == 0) {
        this.totalPages = res.data.totalPages;
        if (this.pageNum == 1) {
          this.list = res.data.list;
        } else {
          this.list = [...this.list, ...res.data.list];
        }
      }
    },
    serviceTimes(data) {
      if (data.serviceStartTime) {
        let startTime = data.serviceStartTime.split(" ")[0];
        let endTime = data.serviceEndTime.split(" ")[0];
        return `${startTime}至${endTime}`;
      } else {
        return "-";
      }
    },
    search() {
      this.loading = true;
      this.$refs.map_box.scrollTop = 0;
      this.pageNum = 1;
      this.getList();
    },
    toAddNew() {
      this.$router.push("/acceptanceAdditional");
    },
    goDetail(data) {
      this.$router.push("/acceptanceParticulars?id=" + data.id);
    },
    onLoad() {
      if (this.totalPages == this.pageNum) {
        this.loading = false;
        this.finished = true;
        return false;
      }
      this.pageNum += 1;
      // this.getList();
      // 异步更新数据
      // setTimeout 仅做示例，真实场景中一般为 ajax 请求
      // setTimeout(() => {
      //   for (let i = 0; i < 10; i++) {
      //     this.list.push(this.list.length + 1);
      //   }
      //   // 加载状态结束
      //   this.loading = false;
      //   // 数据全部加载完成
      //   if (this.list.length >= 40) {
      //     this.finished = true;
      //   }
      // }, 1000);
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
      width: 120px;
    }
  }
  .map-box {
    height: calc(100% - 120px);
    background-color: #f2f6f9;
    overflow: auto;
    .total-card {
      height: 44px;
      line-height: 44px;
      padding-left: 12px;
      font-size: 20px;
      color: #666666;
      background-color: #ffffff;
      border-bottom: 1px solid #e6e6e6;
    }
    .card-list {
      background-color: #ffffff;
      padding: 0 10px 8px 11px;
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
          padding-bottom: 5px;
          .card-item-label {
            color: #666666;
            font-size: 20px;
            position: relative;
            width: 150px;
          }
          .card-item-value {
            color: #333333;
            font-size: 20px;
            width: calc(100% - 85px);
            text-align: right;
          }
        }
      }
    }
  }
}
</style>

<style lang="scss">
.add_btn {
  position: absolute;
  top: 583.5px;
  left: 278px;
  img {
    width: 60px;
    height: 60px;
  }
}
</style>
