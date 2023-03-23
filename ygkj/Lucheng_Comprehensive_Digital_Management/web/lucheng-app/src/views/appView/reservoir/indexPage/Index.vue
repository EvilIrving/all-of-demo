<!--
 * @Date: 2022-06-01 14:56:45
 * @Author: 
 * @Description: 工程 基础信息
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-06-30 17:23:01
 * @FilePath: \lucheng-app\src\views\appView\reservoir\indexPage\Index.vue
-->
<template>
  <div class="wrap">
    <section class="basis_section">
      <div class="title_box">
        <div class="title_flex">
          <img src="@/assets/decorate.png" alt="" />
          <span>基础信息</span>
        </div>
        <div class="van-grid-box">
          <div
            class="van-grid-item"
            v-for="(item, index) in propList"
            :key="index"
          >
            <div>{{ item.name }}</div>
            <div>
              <span :class="[item.class ? item.class : 'blue_color']"
                >{{ item["data"] || "-" }}
              </span>
              <span>{{ item.unit }}</span>
            </div>
          </div>
        </div>

        <div class="sluice" v-if="type === '水闸'">
          <img
            class="sluice-shuizhatest"
            :src="
              require(`../../../../assets/images/shuizhatest-0${
                detail.gaorNum || 1
              }.png`)
            "
            alt=""
          />
          <div class="sluice-box">
            <div
              class="sluice-box-item"
              v-for="(item, index) in detail.gaorNum"
              :key="item"
            >
              <div class="sluice-img-box">
                <img
                  :style="`bottom: ${
                    sluiceList.length > 0 ? sluiceList[index].ratio : 0
                  }%`"
                  src="../../../../assets/images/shuizha.png"
                  alt=""
                />
              </div>
              <div class="sluice-img-text">
                <el-popover
                  placement="top"
                  trigger="click"
                  :content="`闸门开度：${
                    sluiceList.length > 0 ? sluiceList[index].value : '-'
                  }m`"
                  popper-class="popper-class"
                >
                  <div class="sluice-item-txt" slot="reference">
                    {{ index + 1 }}#闸门
                  </div>
                </el-popover>
              </div>
            </div>
          </div>
        </div>
        <div class="sluice" v-if="type === '水闸'">
          <div class="sluice-cemian">
            <div class="sluice-cemian-water sluice-cemian-01">
              <div class="water-01">
                警戒水位<span>{{
                  detail.referenceWlStationWarningLevel || "-"
                }}</span
                >m
              </div>
              <div class="water-02">
                危急水位<span>{{
                  detail.referenceWlStationCriticalLevel || "-"
                }}</span
                >m
              </div>
              <div class="water-03">
                闸上水位<span>{{ detail.upz || "-" }}</span
                >m
              </div>
            </div>
            <div class="sluice-cemian-water sluice-cemian-02">
              <div class="water-04">
                水闸高程<span>{{
                  detail.gatebottomelevation && detail.gateSize
                    ? Number(detail.gatebottomelevation) +
                      Number(detail.gateSize)
                    : "-"
                }}</span
                >m
              </div>
              <div class="water-05">
                闸下水位<span>{{ detail.dwz || "-" }}</span
                >m
              </div>
            </div>
            <div class="sluice-cemian-03">
              <p>
                <span>{{ detail.gatebottomelevation || "-" }}</span
                >m
              </p>
              <p><span>闸底高程</span></p>
            </div>
          </div>
        </div>

        <div class="waterimage_box" v-if="type === '水库'">
          <div class="mianbanba"></div>
          <div class="waterlevel">
            <div class="ruler"></div>
            <div class="kedu" v-for="(item, index) in propList" :key="index">
              <span :class="item.class"></span>
              <p>{{ item["data"] || "-" }}m</p>
            </div>
          </div>
          <div class="waterbox"></div>
        </div>
      </div>
      <div class="flex-box basic-info-box"></div>
      <div></div>
    </section>
    <section class="person_section">
      <div class="title_box">
        <div class="title_flex">
          <img src="@/assets/decorate.png" alt="" />
          <span>责任人</span>
        </div>
      </div>
      <div class="person_list" v-if="personList.length > 0">
        <div
          class="person_list_item"
          v-for="(item, index) in personList"
          :key="index"
        >
          <div class="person_left">
            <img src="../../../../assets/images/person-head.png" alt="" />
            <div>
              <h3>{{ item.type }}</h3>
              <p>
                {{ item.username }}<span>{{ item.job }}</span>
              </p>
            </div>
          </div>
          <div class="person_right">
            <img src="../../../../assets/phome.png" alt="" />
            <span>{{ item.phone }}</span>
          </div>
        </div>
      </div>
      <div class="nodata" v-else>
        <img src="../../../../assets/nodata.png" alt="" />
        <p>暂无数据</p>
      </div>
    </section>
  </div>
</template>

<script>
import { homeIndex } from "@/api/projectDetail";
const Statuslist = {
  水库: [
    {
      text: "当前水位",
      class: "green_color",
      data: "",
      prop: "waterLevel",
      unit: "m",
    },
    {
      text: "汛限水位",
      class: "blue_color",
      data: "",
      prop: "yphoonFloodLlimitWaterLevel",
      unit: "m",
    },
    {
      text: "防洪高水位",
      class: "pink_color",
      data: "",
      prop: "uppLevFlco",
      unit: "m",
    },
    {
      text: "设计洪水位",
      class: "orange_color",
      data: "",
      prop: "desFlStag",
      unit: "m",
    },
    {
      text: "校核洪水位",
      class: "red_color",
      data: "",
      prop: "checkFloodLevel",
      unit: "m",
    },
  ],
  水闸: [
    { text: "水闸类型", data: "", prop: "wagaType", unit: "" },
    {
      text: "最大过闸流量",
      data: "",
      prop: "maxFlow",
      unit: "m³/s",
      class: "green_color",
    },
    { text: "闸门形式 ", data: "", prop: "gateType", unit: "" },
    { text: "闸孔总净宽", data: "", prop: "gaorTotNetWid", unit: "m" },
    { text: "闸门宽度", data: "", prop: "gatewidth", unit: "m" },
  ],
  海塘: [
    { text: "海塘形式", data: "", prop: "seawallType" },
    { text: "海塘级别", data: "", prop: "seawallLevel" },
    { text: "防潮标准（年） ", data: "", prop: "designTideStandard" },
    { text: "海塘长度（m）", data: "", prop: "seawallLength" },
    { text: "海塘平均高程(m)", data: "", prop: "averageAltitude" },
    { text: "防护范围（km2）", data: "", prop: "protectionArea" },
    { text: "保护人口（万人）", data: "", prop: "protectionPopulation" },
  ],
  堤防: [
    { text: "堤防级别", data: "", prop: "dikeGrad" },
    { text: "堤防类型", data: "", prop: "dikeType" },
    { text: "堤防型式", data: "", prop: "dikePatt" },
    { text: "防洪标准(年)", data: "", prop: "planFlSta" },
    { text: "堤防长度(m)", data: "", prop: "dikeLen" },
    { text: "堤顶最小宽度 (m)", data: "", prop: "dikeTopWidMin" },
    { text: "堤顶最大宽度 (m)", data: "", prop: "dikeTopWidMax" },
    { text: "保护范围 （km2）", data: "", prop: "protectionArea" },
    { text: "警戒水位(m)", data: "", prop: "warnWaterLevel" },
  ],
  山塘: [
    { text: "所在乡镇", data: "", prop: "strName" },
    { text: "所在村庄", data: "", prop: "village" },
    { text: "山塘类型 ", data: "", prop: "mnpdCate" },
    { text: "总容积（万m3）", data: "", prop: "jhhsRj" },
    { text: "设计洪水位(m)", data: "", prop: "sjhsSw" },
    { text: "校核洪水位(m)", data: "", prop: "jhhsP" },
    { text: "正常蓄水位(m)", data: "", prop: "zcsxswSw" },
    { text: "影响人口（人）", data: "", prop: "yxrk" },
    { text: "灌溉面积（亩）", data: "", prop: "ggmj" },
  ],
};
export default {
  name: "Index",
  props: {},
  components: {},
  data() {
    return {
      personList: [],
      lat: 0,
      lng: 0,
      propList: [],
      detail: {},
      sluiceList: [],
    };
  },
  computed: {
    type() {
      return this.$localData("get", "engineeringInfo").type;
    },
    code() {
      return this.$localData("get", "engineeringInfo").code;
    },
  },
  mounted() {
    this.addZwlog(this)
    this.getDetail();
    this.responsible();
  },
  methods: {
    async getDetail() {
      let res = await homeIndex.getProjectList({
        code: this.code,
        withWaterLevel: true,
        type: this.type,
      });
      if (res.code == 0) {
        let resProp = res.data[0];
        this.detail = resProp;
        if (this.detail.ovs) {
          let arr = this.detail.ovs.split(",");
          this.sluiceList = arr.map((item) => {
            let gateSize = Number(this.detail.gateSize) * 100;
            let ratio =
              Number(item) > 0 ? (Number(item) / gateSize).toFixed(2) * 100 : 0;
            return {
              ratio,
              value: (item / 100).toFixed(2),
            };
          });
        }
        let list = [];
        for (let item of Statuslist[this.type]) {
          list.push({
            name: item.text,
            class: item.class,
            unit: item.unit || "",
            prop: item.prop,
            data: (resProp && resProp[item.prop]) || "-",
          });
        }
        this.propList = list.sort(this.toSort("data")).reverse();
      }
    },
    // 责任人
    async responsible() {
      let res = await homeIndex.getChargePerson({
        projectCode: this.code,
      });
      if (res.code == 0) {
        res.data.forEach((element, index) => {
          if (index < 3) {
            element.phone =
              element.phone.substring(0, 3) +
              "****" +
              element.phone.substring(7);
          }
        });
        this.personList = res.data;
      }
    },
    toSort(propertyName) {
      return (a, b) => {
        return a[propertyName] - b[propertyName]; // 升序
        // return b[propertyName] - a[propertyName]; // 降序
      };
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  width: 100%;
  .basis_section {
    padding: 10px;
    .basic-info-box {
      > div {
        flex-direction: column;
        > div {
          margin: 8px 0;
          span {
            font-size: 14px;
            &:first-child {
              color: #a09f9f;
            }
            &:last-child {
              color: #333333;
            }
          }
        }
        &:first-child {
          width: 180px;
        }
        &:last-child {
          width: calc(100% - 185px);
        }
      }
    }
    .flex-box {
      display: flex;
    }
  }
  .person_section {
    padding: 12px;
    .person_list {
      .person_list_item {
        @include flexbox();
        @include flexAI(center);
        @include flexJC(space-between);
        padding: 16px 0;
        border-bottom: 1px solid #e5e5e5;
        .person_left {
          @include flexbox();
          @include flexAI(center);
          @include flexJC(center);
          img {
            margin-right: 16px;
          }
          & > div {
            h3 {
              font-size: 14px;
              color: rgba(31, 31, 31, 0.48);
              font-weight: 400;
              margin-bottom: 12px;
            }
            p {
              font-size: 18px;
              color: #1f1f1f;
              font-weight: 500;
              span {
                font-size: 14px;
                font-weight: 400;
                margin-left: 8px;
              }
            }
          }
        }
        .person_right {
          @include flexbox();
          @include flexAI(center);
          @include flexJC(flex-end);
          img {
            width: 20px;
          }
          font-size: 14px;
          color: #1f1f1f;
          font-weight: 400;
        }
      }
    }
  }
  .title_box {
    display: block;
    width: 100%;
    .van-grid-box {
      margin-top: 16px;
      display: flex;
      flex-wrap: wrap;
      .van-grid-item {
        width: calc(20% - 2px);
        padding: 8px 0;
        text-align: center;
        border: 1px solid #e6e6e6;
        box-sizing: border-box;
        > div:first-child {
          font-size: 12px;
          color: #666666;
          font-weight: 600;
        }
        > div:last-child {
          > span {
            &:first-child {
              font-size: 14px;
              font-weight: 500;
              line-height: 20px;
              &.blue_text {
                color: #276af0;
              }
              &.green_color {
                color: #01a59c;
              }
              &.red_color {
                color: #e02020;
              }
              &.orange_color {
                color: #fa6400;
              }
              &.blue_color {
                color: #276af0;
              }
              &.pink_color {
                color: #fa00b4;
              }
            }
            &:last-child {
              font-size: 10px;
            }
          }
        }
        > div:nth-child(n + 4) {
          border-right: none;
          border-top: none;
        }
      }
    }
    .sluice {
      width: 100%;
      position: relative;
      .sluice-shuizhatest {
        width: 100%;
        margin: 10px auto;
      }
    }
  }
}
.waterimage_box {
  margin-top: 20px;
  height: 250px;
  width: 100%;
  position: relative;
  background-size: 100% 8%;
  .kedu {
    @include flexbox();
    @include flexAI(center);
    flex-wrap: wrap;
    margin-top: 10px;
    span {
      display: inline-block;
      width: 82%;
      height: 2px;
      &.green_color {
        border-bottom: 2px dashed #01a59c;
      }
      &.red_color {
        border-bottom: 2px dashed #e02020;
      }
      &.orange_color {
        border-bottom: 2px dashed #fa6400;
      }
      &.blue_color {
        border-bottom: 2px dashed #276af0;
      }
      &.pink_color {
        border-bottom: 2px dashed #fa00b4;
      }
    }
    p {
      padding-left: 5px;
      font-size: 14px;
      color: #1492ff;
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
    bottom: 32px;
    left: 0;
    width: calc(100% - 60px);
    background: #bef0fb;
    height: 120px;
    background: url("../../../../assets/waterline_default.png") no-repeat center;
    background-size: 100% 70%;
    z-index: 1;
  }
  .waterlevel {
    position: absolute;
    bottom: 36px;
    left: 0;
    height: calc(100% - 40px);
    width: 100%;
    .kd_bottom {
      position: absolute;
      bottom: 15px;
    }
  }
  .mianbanba {
    width: 84%;
    height: 250px;
    position: absolute;
    background: url("../../../../assets/mianbanba.png") no-repeat;
    background-size: 100% 90%;
    z-index: 2;
  }
}

.sluice-cemian {
  width: 100%;
  height: 136px;
  background: url("../../../../assets/images/cemian.png") no-repeat;
  background-size: 100% 90%;
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  .sluice-cemian-water {
    width: 141px;
    > div {
      width: 100%;
      border-bottom: 1px dashed;
      font-size: 10px;
      span {
        font-size: 18px;
        line-height: 24px;
        padding-left: 6px;
      }
      &.water-01 {
        color: #ffa01e;
        border-bottom-color: #ffa01e;
      }
      &.water-02 {
        color: #ff0000;
        border-bottom-color: #ff0000;
      }
      &.water-03 {
        color: #276af0;
        border-bottom-color: #276af0;
      }
      &.water-04 {
        color: #00baff;
        border-bottom-color: #00baff;
      }
      &.water-05 {
        color: #01a59c;
        border-bottom-color: #01a59c;
      }
    }
    &.sluice-cemian-01 {
      position: relative;
      top: -18px;
    }
    &.sluice-cemian-02 {
      padding-top: 23px;
      margin-left: 25px;
      > div {
        padding-left: 30px;
        box-sizing: border-box;
      }
    }
  }

  .sluice-cemian-03 {
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    padding-bottom: 33px;
    p {
      font-size: 10px;
      &:nth-child(1) {
        color: #00baff;
        text-align: center;
        span {
          font-size: 18px;
        }
      }
      &:nth-child(2) {
        transform: scale(0.9);
      }
    }
  }
}
.sluice-box {
  position: absolute;
  z-index: 3;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  display: flex;
  justify-content: center;
  .sluice-box-item {
    padding-top: 54px;
    margin: 0 1px;
    .sluice-img-box {
      width: 29px;
      height: 50px;
      overflow: hidden;
      margin: 0 auto;
      img {
        width: 29px;
        height: 50px;
        position: relative;
      }
    }
    .sluice-img-text {
      padding-top: 3px;
      .sluice-item-txt {
        width: 40px;
        height: 16px;
        background: #276af0;
        border-radius: 2px;
        color: #ffffff;
        font-size: 10px;
        line-height: 16px;
        text-align: center;
      }
    }
  }
}
</style>

<style lang="scss">
.popper-class {
  background-color: #40cd96;
  min-width: 44px;
  padding: 8px;
  font-size: 12px;
  color: #ffffff;
  border-color: #40cd96;
  .popper__arrow {
    border-top-color: #40cd96 !important;
    &::after {
      border-top-color: #40cd96 !important;
    }
  }
}
</style>
