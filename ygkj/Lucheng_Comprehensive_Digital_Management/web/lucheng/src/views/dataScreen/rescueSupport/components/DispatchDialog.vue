<template>
  <el-dialog
    :visible.sync="dialogVisible"
    width="600px"
    :before-close="handleClose"
    top="15vh"
    title="抢险调度方案"
    class="dispatch-dialog"
    :close-on-click-modal="false"
  >
    <div class="dialog-content">
      <el-row class="form-row">
        <el-col :span="4" class="form-label">地址名称</el-col>
        <el-col :span="18">
          <el-input v-model="form.stName"></el-input>
        </el-col>
      </el-row>
      <el-row class="form-row">
        <el-col :span="4" class="form-label">坐标</el-col>
        <el-col :span="18">
          <el-input v-model="form.point"></el-input>
        </el-col>
      </el-row>

      <div class="table-box">
        <p>选择队伍</p>
        <el-table
          ref="teamTable"
          :data="teamData"
          style="width: 100%"
          stripe
          height="200px"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="name" label="队伍名称"></el-table-column>
          <el-table-column
            prop="existingNumber"
            label="人员数量"
          ></el-table-column>
          <el-table-column prop="areaName" label="行政区划"></el-table-column>
          <el-table-column
            prop="distance"
            label="目标距离(km)"
          ></el-table-column>
        </el-table>
      </div>
      <div class="table-box">
        <p>选择物资</p>
        <el-row class="form-row" :gutter="16">
          <el-col :span="12">
            <el-select
              popper-class="blue-popper"
              v-model="selectGood"
              style="width: 100%"
              placeholder="请选择调度的物资"
            >
              <el-option
                v-for="(item, index) in goodList"
                :key="index"
                :label="item"
                :value="item"
              ></el-option>
            </el-select>
          </el-col>
          <el-col :span="8">
            <div class="num-area">
              <span class="left-part" @click="operateGoodsNum('add')">+</span>
              <span class="num-part">{{ goodNum }}</span>
              <span class="right-part" @click="operateGoodsNum('minus')"
                >-</span
              >
            </div>
          </el-col>
          <el-col :span="4"
            ><el-button type="primary" @click="addGoods"
              >添加</el-button
            ></el-col
          >
        </el-row>
        <el-table :data="goodsData" style="width: 100%" stripe height="200px">
          <!-- <el-table-column type="selection" width="55"></el-table-column> -->
          <el-table-column
            prop="materialName"
            label="物资名称"
          ></el-table-column>
          <el-table-column prop="num" label="数量"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <span class="table-del" @click="del(scope.row)">删除</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-row class="form-row" style="padding: 16px">
        <el-select
          popper-class="blue-popper"
          v-model="messageType"
          style="width: 100%"
          placeholder=""
        >
          <el-option label="短信" value=""></el-option>
          <el-option label="钉钉" value="1"></el-option>
        </el-select>
      </el-row>
      <el-row class="form-row" style="padding: 0 16px">
        <el-input
          type="textarea"
          :rows="2"
          placeholder="请输入消息内容"
          v-model="messageInfo"
        >
        </el-input>
      </el-row>
    </div>
    <div slot="footer">
      <el-row class="form-row">
        <div class="form-footer">
          <el-button class="form-btn btn-cancel" @click="handleClose"
            >取消</el-button
          >
          <el-button class="form-btn btn-save" @click="analysis"
            >智能分析</el-button
          >
        </div>
      </el-row>
    </div>
  </el-dialog>
</template>

<script>
import axios from "axios";
import { dispatchApi } from "@/api/dataScreen/rescueSupport/dispatch.js";
export default {
  name: "RescueSupport",
  props: {
    dialogVisible: {
      type: Boolean,
      default: false,
    },
    stationCode: {
      type: [String, Number],
      default: "SWZ_01217",
    },
    lat: String,
    lng: String,
  },
  components: {},
  data() {
    return {
      goodNum: 0,
      form: {
        stName: "",
        lat: "",
        lng: "",
      },
      selectGood: "", //下拉框选中物资
      teamData: [], //测站最近的3个队伍列表
      goodsData: [], //添加的物资列表
      goodList: [], //下拉框物资列表
      selectTeam: {}, //选中team列表
      messageType: "",
      messageInfo: "",
    };
  },
  mounted() {
    this.getEmergencyDispatch();
    this.getGoodsList();
    this.getGeoPosition();
    this.form.point = this.lat + " , " + this.lng;
    this.form.lat = this.lat;
    this.form.lng = this.lng;
  },
  methods: {
    //获取地理位置
    async getGeoPosition() {
      await axios({
        url: `https://restapi.amap.com/v3/geocode/regeo?location=${this.lng},${this.lat}&key=${this.$config.gaoDeKey}&output=json`,
        method: "get",
      }).then((res) => {
        this.form.stName = res.data.regeocode.formatted_address;
      });
    },
    handleClose() {
      this.$emit("handleClose");
    },
    //获取调度方案测站和队伍信息
    async getEmergencyDispatch() {
      const opt = {
        // stationCode: this.stationCode,
        latitude: this.lat,
        longitude: this.lng,
      };
      await dispatchApi.emergencyDispatch(opt).then((res) => {
        // this.form.stName = res.data.station.stName;
        // this.form.lat = res.data.station.stLat;
        // this.form.lng = res.data.station.stLong;
        // this.form.point =
        //   res.data.station.stLat + " , " + res.data.station.stLong;
        this.teamData = res.data.teams;
        // console.log(res);
      });
    },
    //获取下拉框物资列表
    async getGoodsList() {
      await dispatchApi.goodsList().then((res) => {
        console.log(res.data,'data');
        this.goodList = res.data;
      });
    },
    //操作物资数量
    operateGoodsNum(type) {
      if (type == "add") {
        this.goodNum++;
      } else {
        this.goodNum > 0 && this.goodNum--;
      }
    },
    handleSelectionChange(val) {
      if (val.length > 1) {
        this.$refs.teamTable.clearSelection();
        this.$refs.teamTable.toggleRowSelection(val[val.length - 1]);
      }
      this.selectTeam = val[val.length - 1];
    },
    //智能分析
    async analysis() {
      if (this.selectTeam) {
        let opt = {
          teamId: this.selectTeam.id,
          list: this.goodsData,
        };
        await dispatchApi.getIntelligentAnalysis(opt).then((res) => {
          // console.log(res);
          if (res.code == 0) {
            this.$emit("dispatchInfo", res.data, this.form);
            this.$emit("handleClose");
          }
        });
      }
    },
    //物资删除
    del(id) {},
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
  },
};
</script>

<style lang="scss">
@import "@/style/blueDialog.scss";
.dispatch-dialog {
  user-select: none;
  .el-dialog {
    background: rgba(0, 0, 0, 0)
      url(../../../../assets/images/blue-dialog-bg.png) no-repeat center center;
    background-size: 100% 100%;
    .el-dialog__header {
      background: rgba(0, 0, 0, 0);
      padding: 20px 20px 12px;
      .el-dialog__title {
        color: #4db6e4;
      }
      .el-dialog__headerbtn {
        top: 26px;
        right: 28px;
      }
    }
    .dialog-content {
      height: 646px;
      overflow: auto;
      .form-row {
        margin-bottom: 16px;
        .el-button {
          padding: 6px 18px;
          &.el-button--primary {
            background: #239def;
            border-color: #239def;
          }
        }
      }
      .form-label {
        text-align: center;
        height: 24px;
        line-height: 24px;
        color: #2c72be;
      }
      .el-input {
        .el-input__inner {
          height: 28px;
          border: 1px solid #2c72be;
          background: rgba(0, 0, 0, 0);
        }
      }
      .el-textarea .el-textarea__inner {
        background: rgba(0, 0, 0, 0);
        border: 0.0625rem solid #2c72be;
        color: #fff;
        .el-input__icon {
          line-height: normal;
        }
      }
      .num-area {
        height: 28px;
        width: 100%;
        span {
          display: inline-block;
          height: 100%;
          text-align: center;
          line-height: 26px;
          color: #2c72be;
          border: 1px solid #2c72be;
        }
        .left-part {
          width: 32px;
          cursor: pointer;
          border-radius: 4px 0 0 4px;
        }
        .right-part {
          width: 32px;
          cursor: pointer;
          border-radius: 0 4px 4px 0;
        }
        .num-part {
          width: calc(100% - 64px);
        }
      }
      .table-box {
        padding: 0 16px;
        margin-bottom: 16px;
        .el-input__icon {
          line-height: normal;
        }

        p {
          color: #2c72be;
          padding: 0 0 12px;
        }
        .el-table th {
          background: #239def;
        }
      }
    }
  }
  .form-btn {
    background: rgba(0, 0, 0, 0);
    color: #2c72be;
    border: none;
    border-radius: 0;
    &.btn-save {
      color: #fff;
      background: #239def;
    }
  }
}
.blue-popper {
  .el-select-dropdown__item.selected {
    color: #2c72be;
  }
}
</style>