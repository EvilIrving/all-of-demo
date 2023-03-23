<!--
 * @Author: 抢险调度方案
 * @Date: 2021-07-13 18:40:45
 * @LastEditTime: 2021-07-16 15:58:29
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\dialog\EmergencySchemeDialog.vue
-->
<template>
  <div class="detail-table emergency-scheme">
    <el-dialog
      top="3vh"
      width="1200px"
      destroy-on-close
      :visible.sync="visible"
      :before-close="handleClose"
      append-to-body
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      class="primary-dialog emergency-scheme-dialog"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">抢险调度方案</p>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <span class="label-name">地址名称：</span>
        <el-input style="width: 300px" v-model="form.stName" />
        <span class="label-name" style="margin-left: 30px">坐标：</span>
        <el-input style="width: 160px" v-model="form.point" />
        <p class="module-title">选择队伍</p>
        <p class="divide" style="margin-bottom: 16px"></p>
        <el-table
          ref="multipleTable"
          :data="teamData"
          height="280px"
          stripe
          width="100%"
          v-loading="teamLoading"
          element-loading-text="拼命加载中"
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.8)"
          @row-click="itemsRowClick"
        >
          <el-table-column label width="35">
            <template slot-scope="scope">
              <el-radio :label="scope.row.id" v-model="teamId"></el-radio>
            </template>
          </el-table-column>
          <el-table-column prop="index" label="序号" width="55">
            <template slot-scope="scope">
              <span class="table-order">{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="队伍名称" />
          <el-table-column prop="existingNumber" label="人数" />
          <el-table-column prop="areaName" label="行政区划" />
          <el-table-column sortable prop="distance" label="目标距离(m)" />
        </el-table>
        <p class="divide" style="margin-top: 16px"></p>
        <p class="module-title">选择物资</p>
        <span class="label-name">物资选择：</span>
        <el-select
          style="width: 300px"
          popper-class="blue-popper"
          v-model="selectGood"
          placeholder="请选择调度的物资"
        >
          <el-option
            v-for="(item, index) in goodList"
            :key="index"
            :label="item"
            :value="item"
          ></el-option>
        </el-select>
        <span class="label-name" style="margin-left: 30px">数量：</span>
        <el-input-number
          v-model="goodNum"
          size="small"
          controls-position="right"
        />
        <span class="add-btn" @click="addGoods">添加</span>
        <el-table
          :data="goodsData"
          style="width: 100%; margin-top: 16px"
          stripe
          height="280px"
        >
          <el-table-column
            prop="materialName"
            label="物资名称"
          ></el-table-column>
          <el-table-column prop="num" label="数量"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <span
                class="table-del"
                style="cursor: pointer"
                @click="del(scope.$index)"
                >删除</span
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="dialog-footer">
        <span class="cancel-btn" @click="handleClose">取消</span>
        <span class="analysis-btn" @click="analysis">分析</span>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import { mapActions, mapGetters } from "vuex";
import { dispatchApi } from "@/api/dataScreen/rescueSupport/dispatch.js";
export default {
  name: "EmergencySchemeDialog",
  data() {
    return {
      teamId: "",
      form: {
        stName: "",
        lat: "",
        lng: "",
      },
      goodList: [],
      selectGood: "",
      goodsData: [], //添加的物资列表
      teamData: [], //测站最近的3个队伍列表
      goodNum: 0,
      teamLoading: false,
      checkedTeam: {},
    };
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    lat: String,
    lng: String,
  },

  computed: {
    ...mapGetters(["DispatchCarousel"]),
  },
  components: {},
  created() {},
  mounted() {
    this.form.point = this.lat + " , " + this.lng;
    this.form.lat = this.lat;
    this.form.lng = this.lng;
    this.getEmergencyDispatch();
    this.getGoodsList();
    this.getGeoPosition();
  },
  methods: {
    ...mapActions([
      "SetGoodsList",
      "SetTeamInfo",
      "SetWarehouseInfo",
      "SetDispatchCarousel",
    ]),
    itemsRowClick(row) {
      this.teamId = row.id;
      this.checkedTeam = row;
    },
    handleClose() {
      this.$emit("closeDialog");
    },
    async getEmergencyDispatch() {
      this.teamLoading = true;
      const opt = {
        latitude: this.lat,
        longitude: this.lng,
      };
      await dispatchApi.emergencyDispatch(opt).then((res) => {
        this.teamData = res.data.teams;
        this.teamLoading = false;
      });
    },
    //获取下拉框物资列表
    async getGoodsList() {
      await dispatchApi.goodsList().then((res) => {
        this.goodList = res.data;
      });
    },
    async getGeoPosition() {
      await axios({
        url: `https://restapi.amap.com/v3/geocode/regeo?location=${this.lng},${this.lat}&key=${this.$config.gaoDeKey}&output=json`,
        method: "get",
      }).then((res) => {
        this.form.stName = res.data.regeocode.formatted_address;
      });
    },
    //添加物资
    addGoods() {
      if (this.goodNum > 0 && this.selectGood) {
        //判断选中物资列表中是否存在该物资
        const index = this.goodsData.findIndex((item) => {
          return item.materialName == this.selectGood;
        });
        if (index > 0) {
          this.goodsData[index].num += this.goodNum;
        } else {
          let opt = {
            materialName: this.selectGood,
            num: this.goodNum,
          };
          this.goodsData.push(opt);
        }
      }
    },
    //智能分析
    async analysis() {
      if (this.teamId) {
        let opt = {
          teamId: this.teamId,
          list: this.goodsData,
        };
        await dispatchApi.getIntelligentAnalysis(opt).then((res) => {
          if (res.code == 0) {
            this.DispatchCarousel && this.SetDispatchCarousel(false);
            this.$emit("dispatchInfo", res.data, this.form);
            this.SetTeamInfo(this.checkedTeam);
            this.SetWarehouseInfo(
              res.data.warehouses.length > 0 ? res.data.warehouses[0] : []
            );
            this.SetGoodsList(this.goodsData);
            this.handleClose();
          }
        });
      } else {
        this.$message.warning("请选择队伍");
      }
    },
    del(index) {
      this.goodsData.splice(index, 1);
    },
  },
};
</script>
<style lang="scss" scoped>
.emergency-scheme-dialog.primary-dialog {
  .module-title {
    color: #1ef8f2;
    padding-left: 4px;
    line-height: 40px;
  }
  .divide {
    width: 100%;
    height: 1px;
    background: rgba(0, 193, 255, 0.2);
  }
  .label-name {
    color: #ffffff;
  }
  .add-btn {
    float: right;
    height: 34px;
    line-height: 34px;
    width: 105px;
    text-align: center;
    background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
    border-radius: 4px;
    cursor: pointer;
    font-size: 16px;
    color: #ffffff;
  }
  ::v-deep .el-radio__input.is-checked .el-radio__inner {
    border-color: #1ef8f2;
    background-color: #1ef8f2;
  }
  ::v-deep .el-dialog {
    .el-dialog__header {
      padding: 2px;
      .primary-dialog__title {
        .primary-dialog__title--label {
          width: 80%;
          height: 32px;
          color: #fff;
          font-size: 16px;
          letter-spacing: 0;
          margin-left: 15px;
          font-weight: bold;
          line-height: 35px;
          padding-left: 56px;
        }
      }
    }
    .el-dialog__body {
      padding: 2px;
      padding-top: 0;
      border-bottom: 0.0625rem solid #00c1ff;
      position: relative;
      .primary-dialog__content {
        height: 830px;
        border-top: 0;
        padding: 0 16px;
        box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);
      }
      .el-radio__label {
        visibility: hidden;
      }
    }
  }
  .dialog-footer {
    position: absolute;
    right: 18px;
    bottom: 24px;
    > span {
      display: inline-block;
      width: 67px;
      height: 35px;
      line-height: 32px;
      text-align: center;
      border-radius: 4px;
      margin-left: 16px;
      cursor: pointer;
      color: #fff;
      background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
      &.cancel-btn {
        border: 1px solid #1ef8f2;
        color: #1ef8f2;
        background: transparent;
      }
    }
  }
  ::v-deep.el-input-number {
    width: 5.25rem;
  }
  ::v-deep.el-input__inner:hover,
  .el-select:hover .el-input__inner {
    border: 1px solid #fff;
  }
  ::v-deep.el-input__inner {
    height: 2rem;
    background: rgba(24, 47, 90, 0.1);
    color: #fff;
    border: 1px solid #fff;
  }
  ::v-deep.el-select .el-input .el-select__caret {
    color: #fff;
  }
  ::v-deep input::-webkit-input-placeholder {
    color: #fff;
  }
  //   .el-table th {
  //     color: #1ef8f2;
  //   }
  //   .el-table thead tr {
  //     background-image: linear-gradient(
  //       90deg,
  //       rgba(44, 158, 191, 0.3) 3%,
  //       rgba(41, 147, 180, 0.1) 100%
  //     );
  //   }
  //   .el-table__row.el-table__row--striped {
  //     background-image: linear-gradient(
  //       90deg,
  //       rgba(255, 255, 255, 0.2) 0%,
  //       rgba(249, 249, 249, 0) 100%
  //     );
  //   }
  //   ::v-deep .el-input-number .el-input {
  //     width: 40px;
  //   }
  ::v-deep .el-input-number .el-input__inner {
    padding-left: 16px;
    padding-right: 0;
    background: none;
    text-align: left;
  }
  ::v-deep.el-input-number {
    position: relative;
    display: inline-block;
    width: 66px;
    border: 1px solid #ffffff;
    border-radius: 4px;
  }
  ::v-deep.el-input-number__decrease {
    border-left: 1px solid #fff;
    background: none;
    > i {
      color: #fff;
    }
  }
  ::v-deep.el-input-number__increase {
    height: 1.05rem;
    border-left: 1px solid #fff;
    background: none;
    > i {
      color: #fff;
    }
  }
  ::v-deep.el-input-number--small .el-input-number__decrease,
  ::v-deep.el-input-number--small .el-input-number__increase {
    width: 20px;
  }
}
</style>