<!--
 * @Author: 抢险调度方案
 * @Date: 2021-07-13 18:40:45
 * @LastEditTime: 2022-06-14 16:14:41
 * @LastEditors: dtb
 * @Description: In User Settings Edit
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\EmergencySchemeDialog.vue
-->
<template>
  <div class="detail-table emergency-scheme">
    <el-dialog top="3vh" destroy-on-close :visible.sync="visible" :before-close="handleClose" append-to-body :modal-append-to-body="true" :close-on-click-modal="false" class="primary-dialog emergency-scheme-dialog">
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label" v-if="tabControl == 0">抢险调度方案</p>
        <p class="primary-dialog__title--label" v-if="tabControl == 1">{{ tabControlThreeTitle }}-周边分析</p>
        <p class="primary-dialog__title--label" v-if="tabControl == 2">{{ tabControlThreeTitle }}</p>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content" v-if="tabControl == 0">
        <span class="label-name">地址名称：</span>
        <el-input style="width: 300px" v-model="form.stName" />
        <span class="label-name" style="margin-left: 30px">坐标：</span>
        <el-input style="width: 160px" v-model="form.point" />
        <p class="module-title">选择队伍</p>
        <el-table ref="multipleTable" :data="teamData" height="280px" stripe class="primary-table" v-loading="teamLoading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.8)" @row-click="itemsRowClick">
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
          <el-table-column sortable prop="distance" label="目标距离(km)" />
        </el-table>
        <p class="module-title">选择物资</p>
        <span class="label-name">物资选择：</span>
        <el-select style="width: 300px" popper-class="blue-popper" v-model="selectGood" placeholder="请选择调度的物资">
          <el-option v-for="(item, index) in goodList" :key="index" :label="item" :value="item"></el-option>
        </el-select>
        <span class="label-name" style="margin-left: 30px">数量：</span>
        <el-input-number v-model="goodNum" size="small" controls-position="right" />
        <span class="add-btn" @click="addGoods">添加</span>
        <el-table :data="goodsData" style="width: 100%; margin-top: 16px" stripe height="280px">
          <el-table-column prop="materialName" label="物资名称"></el-table-column>
          <el-table-column prop="num" label="数量"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <span class="table-del" style="cursor: pointer" @click="del(scope.$index)">删除</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="primary-dialog__content" v-if="tabControl == 1">
        <SurroundingAnalysis ref="surroundCom" :lat="lat" :lng="lng" v-if="tabControl == 1" />
      </div>
      <div class="primary-dialog__content" v-if="tabControl == 2">
        <rainfall :lat="lat" :lng="lng" v-if="tabControl == 2" />
      </div>
      <div class="dialog-footer" v-if="tabControl != 2">
        <span class="cancel-btn" @click="handleClose(tabControl)">取消</span>
        <span class="analysis-btn" @click="analysis" v-if="tabControl == 0">分析</span>
        <span class="analysis-btn" v-if="tabControl == 1" @click="handleClose(tabControl)">确定</span>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios';
import { mapActions, mapGetters } from 'vuex';
import { dispatchApi } from '@/api/dataScreen/rescueSupport/dispatch.js';
import SurroundingAnalysis from './components/SurroundingAnalysis.vue';
import ScopeDialog from './components/ScopeDialog.vue';
import rainfall from './components/rainfall.vue';
export default {
  name: 'EmergencySchemeDialog',
  data() {
    return {
      teamId: '',
      form: {
        stName: '',
        lat: '',
        lng: ''
      },
      goodList: [],
      selectGood: '',
      goodsData: [], //添加的物资列表
      teamData: [], //测站最近的3个队伍列表
      goodNum: 0,
      teamLoading: false,
      checkedTeam: {},
      tabControl: 0,
      tabControlThreeTitle: '',
      flyCenter: []
    };
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    lat: String,
    lng: String
  },

  computed: {
    ...mapGetters(['DispatchCarousel', 'MapComponent']),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  components: { SurroundingAnalysis, rainfall, ScopeDialog },
  created() {},
  mounted() {
    console.log(this.lat, this.lng, 'this.lat');
    this.form.point = this.lat + ' , ' + this.lng;
    this.form.lat = this.lat;
    this.form.lng = this.lng;
    this.getGeoPosition();
    this.tabControl = localStorage.getItem('tabControl') || 0;
    if (this.tabControl == 0) {
      this.getEmergencyDispatch();
      this.getGoodsList();
    }
  },
  methods: {
    ...mapActions(['SetGoodsList', 'SetTeamInfo', 'SetWarehouseInfo', 'SetDispatchCarousel', 'SetSpotsFlag']),
    itemsRowClick(row) {
      this.teamId = row.id;
      this.checkedTeam = row;
    },
    handleClose(type) {
      if (type == 1) {
        let list = this.$refs.surroundCom.dataList;
        let selectNode = this.$refs.surroundCom.selectNode;
        if (this.map) {
          this.map.showAnalysisPoints(list, selectNode);
          this.map._drawCircleInMeter([+this.lng, +this.lat], 3000);
          this.map.flyPoint([+this.lng, +this.lat], 14);
        } else {
          this.$emit('handleCloseNoThisMap', list, selectNode, [+this.lng, +this.lat], 3000, 14)
        }
      }

      localStorage.removeItem('tabControl');
      this.SetSpotsFlag(false);
      this.$emit('closeDialog');
    },
    async getEmergencyDispatch() {
      this.teamLoading = true;
      const opt = {
        latitude: this.lat,
        longitude: this.lng
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
        method: 'get'
      }).then((res) => {
        this.flyCenter = res.data.regeocode.addressComponent.streetNumber.location.split(','); // 周边分析的经纬度
        this.form.stName = res.data.regeocode.formatted_address;
        this.tabControlThreeTitle =
          res.data.regeocode.addressComponent.city +
          res.data.regeocode.addressComponent.district +
          res.data.regeocode.addressComponent.township;
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
            num: this.goodNum
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
          list: this.goodsData
        };
        await dispatchApi.getIntelligentAnalysis(opt).then((res) => {
          if (res.code == 0) {
            this.DispatchCarousel && this.SetDispatchCarousel(false);
            this.$emit('dispatchInfo', res.data, this.form);
            this.SetTeamInfo(this.checkedTeam);
            this.SetWarehouseInfo(res.data.warehouses.length > 0 ? res.data.warehouses[0] : []);
            this.SetGoodsList(this.goodsData);
            this.handleClose();
          }
        });
      } else {
        this.$message.warning('请选择队伍');
      }
    },
    del(index) {
      this.goodsData.splice(index, 1);
    }
  }
};
</script>
<style lang="scss" scoped>
.emergency-scheme-dialog.primary-dialog {
  .module-title {
    color: #fff;
    padding-left: 28px;
    height: 40px;
    line-height: 40px;
    font-size: 18px;
    position: relative;
    margin-bottom: 16px;
    &::after {
      content: '';
      position: absolute;
      width: 100%;
      height: 4px;
      background: url(~assets/images/WaterDisastersImage/seawall-title-icon-overview-01.png) no-repeat;
      background-size: 100% 100%;
      bottom: -2px;
      left: 0;
    }
    &::before {
      content: '';
      position: absolute;
      background: url(~assets/images/WaterDisastersImage/seawall-title-icon.png) no-repeat;
      background-size: 100% 100%;
      width: 20px;
      height: 20px;
      margin-right: 6px;
      left: 0;
      top: 50%;
      margin-top: -10px;
    }
  }
  .primary-table {
    width: 100%;
    margin-bottom: 16px;
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
    border-color: #00baff;
    background-color: #00baff;
  }
  ::v-deep .el-dialog {
    background: url(~assets/images/WaterDisastersImage/dialog-bg.png) no-repeat;
    background-size: 100% 100%;
    width: 1200px;
    .el-dialog__headerbtn {
      top: 39px;
      right: 30px;
      font-size: 18px;
      .el-dialog__close {
        color: #fff;
      }
    }
    .el-dialog__header {
      padding-top: 28px;
      padding-bottom: 16px;
      .primary-dialog__title {
        background: url(~assets/images/WaterDisastersImage/dialog-title-bg.png) no-repeat;
        background-size: 100% 100%;
        height: 42px;
        padding: 0;
        .primary-dialog__title--label {
          width: 80%;
          height: 32px;
          color: #fff;
          font-size: 16px;
          letter-spacing: 0;
          margin-left: 15px;
          font-weight: bold;
          line-height: 35px;
          padding-left: 36px;
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
        // box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        // background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);
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
        border: 1px solid #00baff;
        color: #00baff;
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