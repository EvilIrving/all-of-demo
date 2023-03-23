<template>
  <view-page>
    <div class="project-wrap">
      <div class="select-box">
        <van-dropdown-menu
          z-index="2333"
          class="van-dropdown-menu"
          active-color="#276AF0"
        >
          <van-dropdown-item
            title-class="title-class-name"
            v-model="propertyValue"
            :options="propertyOption"
            @change="getList"
          />
        </van-dropdown-menu>
        <el-date-picker
          class="data-picker"
          v-model="typeValue"
          prefix-icon="el-icon-caret-bottom"
          :clearable="false"
          format="yyyy-MM"
          value-format="yyyy-MM"
          type="month"
          placeholder="年月"
          @change="getList"
          popper-class="month-pick"
        >
        </el-date-picker>
        <van-field
          class="van-field"
          v-model="value3"
          left-icon="search"
          placeholder="名称"
          @change="getList"
        />
      </div>
      <div class="map-box">
        <!--      <van-list v-model="loading" :finished="finished" @load="onLoad">-->
        <van-list>
          <div class="card-list" v-for="item in tableData" :key="item.id">
            <div class="card-title" @click="toDetail(item)">
              <div class="card-name">{{ item.cname }}</div>
              <i class="el-icon-arrow-right"></i>
            </div>
            <div class="card-box">
              <div class="card-item">
                <div class="card-item-label participae">参与考勤人数</div>
                <div class="card-item-value">{{ item.attendanceNum }}</div>
              </div>
              <div class="card-item">
                <div class="card-item-label practical">实际考勤人数</div>
                <div class="card-item-value">{{ item.joinAttendanceNum }}</div>
              </div>
              <!--              <div
                class="card-item-label"
                style="
                  color: #00baff;
                  font-size: 10px;
                  margin-top: 5px;
                  margin-left: 325px;
                "
                @click="toDetail(item)"
              >
                详情
              </div>-->
            </div>
          </div>
        </van-list>
      </div>
    </div>
  </view-page>
</template>

<script>
import { staffAttendance } from "@/api/appView";

export default {
  name: "RainfallList",
  components: {},
  data() {
    return {
      typeValue: "",
      propertyValue: "",
      value3: "",
      propertyOption: [
        {
          text: "全部",
          value: "",
        },
      ],
      finished: false,
      loading: false,
      tableData: [],
      pageNum: 1,
      pageSize: 10,
    };
  },
  mounted() {
    this.addZwlog(this)
    this.getCompanyList();
    this.getList();
  },
  methods: {
    handerChangeTime(value) {
      console.log(value);
    },
    onLoad() {
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
    getCompanyList() {
      staffAttendance.companyList().then((res) => {
        for (var key of res.data) {
          if (key !== null) {
            this.propertyOption.push({
              text: key,
              value: key,
            });
          }
        }
      });
      console.log(this.propertyOption);
    },
    async getList() {
      let res = await staffAttendance.compantAttendance({
        yearMonth: this.typeValue,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        name: this.value3,
        corporationName: this.propertyValue,
      });
      if (res.code == 0) {
        this.tableData = res.data;
      }
    },
    toDetail(row) {
      this.$router.push(
        "/personnelAttendance/AttendanceDetail?code=" + row.cname
      );
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
      font-size: 20px;
    }
    .van-field {
      width: 180px;
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
            font-size: 20px;
            position: relative;
            padding-left: 13px;
            &.participae {
              &::after {
                position: absolute;
                content: "";
                width: 6px;
                height: 6px;
                background-color: #44d7b6;
                border-radius: 50%;
                left: 0;
                top: 50%;
                margin-top: -4px;
              }
            }
            &.practical{
              &::after {
                position: absolute;
                content: "";
                width: 6px;
                height: 6px;
                background-color: #FA6400;
                border-radius: 50%;
                left: 0;
                top: 50%;
                margin-top: -4px;
              }
            }
          }
          .card-item-value {
            color: #333333;
            font-size: 20px;
            max-width: 75%;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
          }
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
    font-size: 20px;
    text-align: center;
  }
  .el-input__prefix {
    top: 1px;
    color: #aaaaaa;
    font-size: 20px;
    left: 75%;
    color: #aaaaaa;
  }
  .el-month-table td .cell {
    color: #fbfbfb;
  }
}
.month-pick {
  .el-month-table td .cell {
    font-size: 20px;
  }
}
</style>
