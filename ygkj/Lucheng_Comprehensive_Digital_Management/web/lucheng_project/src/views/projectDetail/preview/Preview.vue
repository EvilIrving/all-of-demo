<!--
 * @Author: hanyu
 * @LastEditTime: 2022-06-14 18:26:06
 * @Description: 信息总览
 * @FilePath: \lucheng_project\src\views\projectDetail\preview\Preview.vue
-->
<template>
  <div class="proview_wrap">
    <section class="wrap_top">
      <div class="top_item_box">
        <div class="item_box_title">
          <p>实时监测</p>
          <div style="float: right">
            <span>监测视图：</span>
            <el-select
              @change="changeView"
              v-model="viewType"
              placeholder="请选择监测视图"
            >
              <el-option
                v-for="item in viewTypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </div>
        </div>
      </div>
      <div class="waterimage_wrap">
        <div class="waterimage_search">
          <div
            v-if="
              seawallDetail.seawallName !== '江心屿景区防洪堤塘' &&
              seawallDetail.seawallName !== '江心屿西园防洪堤塘' &&
              seawallDetail.seawallName !== '七都标准堤西北堤' &&
              seawallDetail.seawallName !== '七都标准堤东堤南堤吟州堤'
            "
          >
            <span>预警类型：</span>
            <el-select
              @change="getData()"
              v-model="warningType"
              placeholder="请选择预警类型"
            >
              <el-option
                v-for="item in warningTypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </div>
          <div v-else>
            <span>断面：</span>
            <el-select
              @change="getWaterleverData()"
              v-model="fractureType"
              placeholder="请选择断面"
            >
              <el-option
                v-for="item in fractureList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </div>
          <el-button class="btn_save" @click="showPopup">实时监测</el-button>
        </div>

        <div class="waterimage_box" v-if="viewType == 'pmt'">
          <p class="now_level">
            当前水位:
            {{ waterleverData.waterLevel ? waterleverData.waterLevel : "-" }}m
          </p>
          <div class="waterbox"></div>
          <div
            class="waterlevel"
            v-if="
              seawallDetail.seawallName !== '江心屿景区防洪堤塘' &&
              seawallDetail.seawallName !== '江心屿西园防洪堤塘' &&
              seawallDetail.seawallName !== '七都标准堤西北堤' &&
              seawallDetail.seawallName !== '七都标准堤东堤南堤吟州堤'
            "
          >
            <div class="ruler"></div>
            <!-- <div class="kedu">
              <span style="background: #14CCFF;"></span>
              <p>风暴潮预报最高水位： 169.02m</p>
            </div> -->
            <div class="kedu">
              <span style="background: #14ccff"></span>
              <p>历史高潮位： {{ waterleverData.highestLevel || "-" }}m</p>
            </div>
            <div class="kedu">
              <span style="background: #ff1414"></span>
              <p>
                红色警戒水位：
                {{ waterleverData.redLevel ? waterleverData.redLevel : "-" }}m
              </p>
            </div>
            <div class="kedu">
              <span style="background: #ffa414"></span>
              <p>
                橙色警戒水位：
                {{
                  waterleverData.orangeLevel ? waterleverData.orangeLevel : "-"
                }}m
              </p>
            </div>
            <div class="kedu">
              <span style="background: #ffdc14"></span>
              <p>
                黄色警戒水位：
                {{
                  waterleverData.yellowLevel ? waterleverData.yellowLevel : "-"
                }}m
              </p>
            </div>
            <div class="kedu">
              <span style="background: #1492ff"></span>
              <p>
                蓝色警戒水位：
                {{ waterleverData.blueLevel ? waterleverData.blueLevel : "-" }}m
              </p>
            </div>
            <div class="kedu">
              <span style="background: #14ccff"></span>
              <p>
                天文潮预报水位：
                {{ waterleverData.twcLevel ? waterleverData.twcLevel : "-" }}m
              </p>
            </div>
            <div class="kedu kd_bottom">
              <span style="background: #14ccff"></span>
              <p>历史低潮位： {{ waterleverData.recordLow || "-" }}m</p>
            </div>
          </div>
          <div class="waterlevel" v-else>
            <div class="ruler"></div>
            <div class="kedu">
              <span style="background: #14ccff"></span>
              <p>风暴潮预测H2%： {{ waterleverData.H2 || "-" }}m</p>
            </div>
            <div class="kedu">
              <span style="background: #14ccff"></span>
              <p>
                24小时预测最大值： {{ waterleverData["24HourMax"] || "-" }}m
              </p>
            </div>
          </div>
          <div class="mianbanba"></div>
          <div
            class="kedu di-fang"
            v-if="
              seawallDetail.seawallName === '江心屿景区防洪堤塘' ||
              seawallDetail.seawallName === '江心屿西园防洪堤塘' ||
              seawallDetail.seawallName === '七都标准堤西北堤' ||
              seawallDetail.seawallName === '七都标准堤东堤南堤吟州堤'
            "
          >
            <!-- <span style="background: #14ccff"></span> -->
            <p>堤顶高度：{{ waterleverData["finalCrestAltitude"] || "-" }}m</p>
          </div>
        </div>
        <div class="stationimage_box" v-if="viewType == 'zst'">
          <div class="station_list">
            <template v-if="stationList.sz">
              <el-popover
                v-for="(item, index) in stationList.sz"
                :key="index"
                placement="top"
                width="200"
                trigger="hover"
              >
                <div class="station_pop">
                  <h3>{{ item.name }}</h3>
                  <p>
                    闸上水位：<span>{{ item.upz }}m</span>
                  </p>
                  <p>
                    闸下水位：<span>{{ item.dwz }}m</span>
                  </p>
                  <p>
                    过闸流量：<span>{{ item.mxgtq }}m³/s</span>
                  </p>
                  <p>
                    闸门开度：<span>{{ item.ovs }}</span>
                  </p>
                </div>
                <div slot="reference" class="station_item">
                  <p>{{ item.name }}</p>
                  <img src="../../../assets/images/sluice.png" alt="" />
                </div>
              </el-popover>
            </template>
            <template v-if="stationList.bz">
              <el-popover
                v-for="(item, index) in stationList.bz"
                :key="index"
                placement="top"
                width="200"
                trigger="hover"
              >
                <div class="station_pop">
                  <h3>{{ item.name }}</h3>
                  <p>
                    装机流量：<span>{{ item.ins_flow }}</span>
                  </p>
                  <p>
                    装机功率：<span>{{ item.ins_pow }}KW</span>
                  </p>
                </div>
                <div slot="reference" class="station_item">
                  <p>{{ item.name }}</p>
                  <img src="../../../assets/images/pump.png" alt="" />
                </div>
              </el-popover>
            </template>
            <template v-if="stationList.dxh">
              <el-popover
                v-for="(item, index) in stationList.dxh"
                :key="index"
                placement="top"
                width="200"
                trigger="hover"
              >
                <div class="station_pop">
                  <h3>{{ item.name }}</h3>
                </div>
                <div slot="reference" class="station_item">
                  <p>{{ item.name }}</p>
                  <img src="../../../assets/images/pump.png" alt="" />
                </div>
              </el-popover>
            </template>
            <template v-if="stationList.gh">
              <el-popover
                v-for="(item, index) in stationList.gh"
                :key="index"
                placement="top"
                width="200"
                trigger="hover"
              >
                <div class="station_pop">
                  <h3>{{ item.name }}</h3>
                </div>
                <div slot="reference" class="station_item">
                  <p>{{ item.name }}</p>
                  <img src="../../../assets/images/pump.png" alt="" />
                </div>
              </el-popover>
            </template>
          </div>
        </div>
      </div>
    </section>
    <section class="wrap_bottom">
      <div class="bottom_yunxing">
        <div class="item_box_title">
          <p>运行监测</p>
        </div>
        <div class="bottom_yunxing_wrap">
          <div class="yunxing_item_box">
            <h3>
              <img src="../../../assets/images/jcgl.png" alt="" />
              <span>基础管理</span>
            </h3>
            <div class="yunxing_item">
              <div>
                <p>
                  <img src="../../../assets/images/main_n.png" alt="" />
                  <span>站点绑定</span>
                </p>
                <span>{{ operationData.zdbd }}</span>
              </div>
              <div>
                <p>
                  <img src="../../../assets/images/main_y.png" alt="" />
                  <span>划界权限</span>
                </p>
                <span>{{ operationData.hjqx }}</span>
              </div>
              <div>
                <p>
                  <img src="../../../assets/images/main_y.png" alt="" />
                  <span>安全责任人</span>
                </p>
                <span>{{ operationData.aqzrr }}</span>
              </div>
            </div>
          </div>
          <div class="yunxing_item_box">
            <h3>
              <img src="../../../assets/images/aqgl.png" alt="" />
              <span>安全管理</span>
            </h3>
            <div class="yunxing_item">
              <div>
                <p>
                  <img src="../../../assets/images/main_y.png" alt="" />
                  <span>安全鉴定</span>
                </p>
                <span>{{ operationData.aqjd }}</span>
              </div>
              <div>
                <p>
                  <img src="../../../assets/images/main_y.png" alt="" />
                  <span>除险加固</span>
                </p>
                <span>{{ operationData.cxjg ? operationData.cxjg : "-" }}</span>
              </div>
              <div>
                <p>
                  <img src="../../../assets/images/main_y.png" alt="" />
                  <span>维修养护</span>
                </p>
                <span>{{ operationData.wxyh }}</span>
              </div>
            </div>
          </div>
          <div class="yunxing_item_box yjgl">
            <h3>
              <img src="../../../assets/images/yjgl.png" alt="" />
              <span>应急管理</span>
            </h3>
            <div class="yunxing_item">
              <div>
                <p>
                  <img src="../../../assets/images/main_n.png" alt="" />
                  <span>防汛值班</span>
                </p>
                <span>{{ operationData.fxzb }}</span>
              </div>
              <div>
                <p>
                  <img src="../../../assets/images/main_y.png" alt="" />
                  <span>除险处置</span>
                </p>
                <span>{{ operationData.xqcz }}</span>
              </div>
              <div>
                <p>
                  <img src="../../../assets/images/main_y.png" alt="" />
                  <span>防汛预案</span>
                </p>
                <span>{{ operationData.fxya }}</span>
              </div>
              <div>
                <p>
                  <img src="../../../assets/images/main_y.png" alt="" />
                  <span>防汛演练</span>
                </p>
                <span>{{ operationData.fxyl }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="bottom_person">
        <div class="item_box_title">
          <p>相关负责人</p>
        </div>
        <div>
          <div v-if="sixPeopleData1.length">
            <div
              class="person_top_item"
              v-for="(item, index) in sixPeopleData1"
              :key="index"
            >
              <p>
                <img src="../../../assets/images/personhead.png" alt="" />
                <span>{{ item.type }}</span>
              </p>
              <div>
                <p>{{ item.username }}</p>
                <span>{{ item.job }}</span>
              </div>
            </div>
            <!-- <div class="person_top_item">
              <p>
                <img src="../../../assets/images/personhead.png" alt="">
                <span>水行政主管责任人</span>
              </p>
              <div>
                <p>马林云</p>
                <span>厅长</span>
              </div>
            </div>
            <div class="person_top_item">
              <p>
                <img src="../../../assets/images/personhead.png" alt="">
                <span>主管部门责任人</span>
              </p>
              <div>
                <p>侯清国</p>
                <span>董事长</span>
              </div>
            </div> -->
          </div>

          <div class="person_bottom" v-if="sixPeopleData2.length">
            <div
              class="person_bottom_item"
              v-for="(item, index) in sixPeopleData2"
              :key="index"
            >
              <h3>{{ item.username ? item.username : "-" }}</h3>
              <p>{{ item.type ? item.type : "-" }}</p>
              <p>{{ item.phone ? item.phone : "-" }}</p>
            </div>
          </div>
        </div>
        <div
          v-if="!sixPeopleData1.length && !sixPeopleData2.length"
          class="noPeople"
        >
          <img src="../../../assets/images/no-responsible.png" alt="" />
          <p>暂无负责人</p>
        </div>
      </div>
    </section>
    <!-- 关联人员 -->
    <RealTimeMonitor
      ref="RealTimeMonitor"
      v-if="realTimeMonitorShow"
      :visible="realTimeMonitorShow"
      :info="dialogInfo"
      @close="realTimeMonitorClose"
    />
  </div>
</template>

<script>
import { perviewApi } from "@/api/projectDetail";
import RealTimeMonitor from "./RealTimeMonitor.vue";
import { stormSurgeWarningApi } from "@/api/seawallPrevent";
export default {
  name: "Preview",
  props: {},
  components: { RealTimeMonitor },
  data() {
    return {
      warningType: "bj",
      warningTypeOptions: [
        {
          label: "实时报警",
          value: "bj",
        },
        {
          label: "实时预警",
          value: "yj",
        },
      ],
      viewType: "pmt",
      viewTypeOptions: [
        {
          label: "剖面图",
          value: "pmt",
        },
        {
          label: "正视图",
          value: "zst",
        },
      ],
      stationList: [],
      waterleverData: {},
      sixPeopleData1: [],
      sixPeopleData2: [],
      operationData: [],
      topViewData: [],
      realTimeMonitorShow: false, //实时监测弹窗
      dialogInfo: {}, //给弹窗的默认参数
      fractureList: [],
      fractureType: "",
    };
  },
  computed: {
    seawallDetail() {
      return this.$store.state.seawallDetail;
    },
  },
  async mounted() {
    if (
      this.seawallDetail.seawallName === "江心屿景区防洪堤塘" ||
      this.seawallDetail.seawallName === "江心屿西园防洪堤塘" ||
      this.seawallDetail.seawallName === "七都标准堤西北堤" ||
      this.seawallDetail.seawallName === "七都标准堤东堤南堤吟州堤"
    ) {
      await this.getList();
    }
    await this.getWaterleverData();
    this.getSixPeopleData();
    this.getOperationData();
    this.getTopViewData();
  },
  methods: {
    async getList() {
      let res = await stormSurgeWarningApi.forecastSectionList({
        pageNum: 1,
        pageSize: 99999,
        prcd: this.seawallDetail.seawallCode,
      });
      if (res.code == 0) {
        this.fractureList = res.data.list;
        if (this.fractureList.length > 0) {
          this.fractureType = this.fractureList[0].id;
        }
      }
    },
    // 剖面图数据
    async getWaterleverData() {
      let obj = {
        seawallCode: this.seawallDetail.prcd,
      };
      if (
        this.seawallDetail.seawallName === "江心屿景区防洪堤塘" ||
        this.seawallDetail.seawallName === "江心屿西园防洪堤塘" ||
        this.seawallDetail.seawallName === "七都标准堤西北堤" ||
        this.seawallDetail.seawallName === "七都标准堤东堤南堤吟州堤"
      ) {
        obj["dmId"] = this.fractureType;
      }
      let res = await perviewApi.waterlevel(obj);
      if (res.code == 0) {
        this.waterleverData = res.data;
      }
    },
    // 正视图数据
    async getTopViewData() {
      let res = await perviewApi.planView({
        seawallCode: this.seawallDetail.prcd,
      });
      if (res.code == 0) {
        this.stationList = res.data;
      }
    },
    // 运行监测
    async getOperationData() {
      let res = await perviewApi.operation({
        seawallCode: this.seawallDetail.prcd,
      });
      if (res.code == 0) {
        this.operationData = res.data;
      }
    },
    async getSixPeopleData() {
      let that = this;
      let res = await perviewApi.sixPeople({
        projectCode: this.seawallDetail.prcd,
      });
      if (res.code == 0) {
        if (res.data.length) {
          that.sixPeopleData1 = this.group(res.data, 3)[0];
          that.sixPeopleData2 = this.group(res.data, 3)[1];
        }
      }
    },
    getData() {},
    changeView(val) {
      switch (val) {
        case "pmt":
          this.getWaterleverData();
          break;
        default:
          this.getTopViewData();
          this.getOperationData();
          break;
      }
      this.viewType = val;
    },
    // 实时监测弹窗 同大屏海塘接口和数据
    showPopup() {
      this.realTimeMonitorShow = true;
      this.dialogInfo = {
        projectCode: this.seawallDetail.prcd,
        stType: 2,
        projectType: 1,
        projectName: this.seawallDetail.seawallName,
        sectionId: this.fractureType,
      };
    },
    realTimeMonitorClose() {
      this.realTimeMonitorShow = false;
    },
    group(array, subGroupLength) {
      let index = 0;
      let newArray = [];
      while (index < array.length) {
        newArray.push(array.slice(index, (index += subGroupLength)));
      }
      return newArray;
    },
  },
};
</script>

<style lang="scss" scoped>
.proview_wrap {
  .wrap_top {
    @include contentBox();
    .waterimage_wrap {
      width: 100%;
      box-sizing: border-box;
      padding: 16px;
      .waterimage_search {
        position: relative;
        padding-bottom: 16px;
        @include flexbox();
        @include flexAI(center);
        @include flexJC(space-between);
      }
      .waterimage_box {
        height: 315px;
        width: 100%;
        // background-color: #F5F8FC;
        background: url("../../../assets/images/river_bottom.png") no-repeat
          bottom #f5f8fc;
        position: relative;
        .kedu {
          @include flexbox();
          @include flexAI(center);
          margin-top: 10px;
          span {
            display: inline-block;
            width: 30px;
            height: 2px;
            background: #14ccff;
          }
          p {
            padding-left: 5px;
            font-size: 14px;
            color: #1492ff;
          }
          &.di-fang {
            position: absolute;
            top: -20px;
            right: 95px;
          }
        }
        .now_level {
          width: 100%;
          text-align: center;
          color: #1492ff;
          position: relative;
          top: -30px;
        }
        .waterbox {
          position: absolute;
          bottom: 40px;
          left: 0;
          width: calc(100% - 150px);
          background: #bef0fb;
          height: 140px;
          // border-top: 2px solid #24D2B0;
          background: url("../../../assets/images/waterline_default.png")
            no-repeat center;
          background-size: 100% 100%;
        }
        .waterlevel {
          position: absolute;
          bottom: 40px;
          left: 0;
          height: calc(100% - 40px);
          .ruler {
            background: rgba(216, 216, 216, 0.425);
            width: 30px;
            height: 100%;
            position: absolute;
            left: 0;
            bottom: 0;
          }

          .kd_bottom {
            position: absolute;
            bottom: 10px;
          }
        }
        .mianbanba {
          width: 267px;
          height: 275px;
          position: absolute;
          bottom: 38px;
          right: 10px;
          background: url("../../../assets/images/mianbanba.png") no-repeat
            bottom;
        }
      }
      .stationimage_box {
        height: 315px;
        width: 100%;
        background: url("../../../assets/images/station_bg.png") no-repeat
          bottom #f5f8fc;
        background-size: auto 100%;
        position: relative;
        .station_list {
          height: 315px;
          width: 100%;
          @include flexbox();
          @include flexAI(center);
          // @include flexJC(space-between);
          padding: 25px 50px 0 50px;
          .station_item {
            margin-right: 70px;
            position: relative;
            text-align: center;
            &:last-child {
              margin-right: 100px;
            }
            p {
              width: 100%;
              text-align: center;
              position: relative;
              top: -70px;
              color: #1492ff;
              font-size: 16px;
            }
            img {
              cursor: pointer;
            }
          }
        }
      }
    }
  }
  .wrap_bottom {
    margin-top: 16px;
    @include flexbox();
    @include flexJC(space-between);
    .bottom_yunxing {
      @include flex(3);
      @include contentBox();
      margin-right: 16px;
      .bottom_yunxing_wrap {
        @include flexbox();
        @include flexJC(space-between);
        padding: 15px 20px;
        .yunxing_item_box {
          width: 32%;
          border-radius: 6px;
          border: 1px solid #eeeeee;
          h3 {
            @include flexbox();
            @include flexAI(center);
            height: 48px;
            padding: 0 24px;
            border-bottom: 1px solid #eeeeee;
            img {
              margin-right: 10px;
            }
            span {
              font-weight: 600;
              color: #1492ff;
              font-size: 20px;
            }
          }
          .yunxing_item {
            padding: 10px 0;
            & > div {
              @include flexbox();
              @include flexAI(center);
              @include flexJC(space-between);
              padding: 24px;
              & > p {
                @include flexbox();
                @include flexAI(center);
                img {
                  margin-right: 14px;
                }
                span {
                  font-size: 20px;
                  color: #333333;
                }
              }
              & > span {
                color: #79cdad;
                font-size: 18px;
              }
            }
          }
          &.yjgl {
            .yunxing_item {
              & > div {
                padding: 14px 24px;
              }
            }
          }
        }
      }
    }
    .bottom_person {
      @include flex(1.5);
      @include contentBox();
      .noPeople {
        text-align: center;
        img {
          margin-top: 50px;
          margin-bottom: 20px;
        }
        p {
          color: #999;
        }
      }
      .person_top_item {
        @include flexbox();
        @include flexJC(space-between);
        padding: 0 16px;
        & > p {
          @include flexbox();
          @include flexAI(center);
          padding: 10px 0;
          width: 50%;
          border-bottom: 1px solid #eee;
          img {
            margin-right: 24px;
          }
          span {
            font-weight: bold;
            font-size: 20px;
          }
        }
        & > div {
          width: 50%;
          padding: 10px 0;
          text-align: right;
          border-bottom: 1px solid #eee;
          p {
            margin-bottom: 5px;
            color: #333;
            font-size: 16px;
          }
          span {
            color: #333;
            font-size: 14px;
          }
        }
      }
      .person_bottom {
        @include flexbox();
        .person_bottom_item {
          width: 50%;
          // height: 80px;
          background: url("../../../assets/images/personbg.png") no-repeat
            bottom center;
          text-align: center;
          box-sizing: border-box;
          padding: 16px 0;
          h3 {
            line-height: 30px;
            padding-top: 10px;
            border-right: 1px solid #eee;
            font-weight: bold;
          }
          p {
            line-height: 25px;
            border-right: 1px solid #eee;
          }
        }
      }
    }
  }
}
.station_pop {
  h3 {
    color: #333;
    font-size: 16px;
    line-height: 25px;
  }
  p {
    line-height: 25px;
    font-size: 14px;
    span {
      font-size: 16px;
      color: #333;
    }
  }
}
</style>
