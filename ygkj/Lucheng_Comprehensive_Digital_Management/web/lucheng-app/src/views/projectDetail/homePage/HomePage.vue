<template>
  <div class="wrap">
    <section class="basis_section">
      <div class="title_box">
        <div class="title_flex">
          <img src="@/assets/decorate.png" alt="" />
          <span>基本信息</span>
        </div>
      </div>
      <div class="flex-box basic-info-box">
        <div class="flex-box">
          <div>
            <span>海塘类型：</span>
            <span>{{ projectDetail.safetyConclusion || "-" }}</span>
          </div>
          <div>
            <span>设计防潮标准(年)：</span>
            <span>{{ projectDetail.designTideStandard || "-" }}</span>
          </div>
          <div>
            <span>设计潮位(m)：</span>
            <span>{{ projectDetail.designTiddeStandard || "-" }}</span>
          </div>
          <div>
            <span>海塘长度(m)：</span>
            <span>{{ projectDetail.seawallLength || "-" }}</span>
          </div>
          <div>
            <span>海塘高度(最大值m)：</span>
            <span>{{ projectDetail.maxHeight || "-" }}</span>
          </div>
          <div>
            <span>海塘高度(最小值m)：</span>
            <span>{{ projectDetail.minHeight || "-" }}</span>
          </div>
          <div>
            <span>塘顶高程(起点m)：</span>
            <span>{{ projectDetail.startCrestAltitude || "-" }}</span>
          </div>
          <div>
            <span>海塘平均高程(m)：</span>
            <span>{{ projectDetail.averageAltitude || "-" }}</span>
          </div>
        </div>
        <div class="flex-box">
          <div>
            <span>海塘形式：</span>
            <span>{{ projectDetail.seawallType || "-" }}</span>
          </div>
          <div>
            <span>实际防潮标准(年)：</span>
            <span>{{ projectDetail.preventTideStandard || "-" }}</span>
          </div>
          <div>
            <span>水基准面：</span>
            <span>{{ projectDetail.levelDatum || "-" }}</span>
          </div>
          <div>
            <span>达到规划防洪(潮)标准的长度(m)：</span>
            <span>{{ projectDetail.reachStandardLength || "-" }}</span>
          </div>
          <div>
            <span>塘顶宽度(最大值m)：</span>
            <span>{{ projectDetail.maxWidth || "-" }}</span>
          </div>
          <div>
            <span>塘顶宽度(最小值m)：</span>
            <span>{{ projectDetail.minWidth || "-" }}</span>
          </div>
          <div>
            <span>塘顶高程(终点m)：</span>
            <span>{{ projectDetail.finalCrestAltitude || "-" }}</span>
          </div>
          <div>
            <span>海塘最低高程(m)：</span>
            <span>{{ projectDetail.lowestElevation || "-" }}</span>
          </div>
        </div>
      </div>
      <div></div>
    </section>
    <section class="dynamic_section">
      <div class="title_box">
        <div class="title_flex">
          <img src="@/assets/decorate.png" alt="" />
          <span>动态研判</span>
        </div>
      </div>
      <div class="table_box">
        <table>
          <thead>
            <tr>
              <th>预警类别</th>
              <th>预警信息</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>
                <div>
                  <img src="@/assets/meteorology.png" alt="" />
                  <span>气象</span>
                </div>
              </td>
              <td>
                <!-- <div>sdasdds</div> -->
                <div class="meteorology" v-if="weatherWarningList.length > 0">
                  <div v-for="(item, index) in weatherWarningList" :key="index">
                    <img :src="item.imgSrc" alt="" />
                    <div>
                      <p>{{ item.WARN_TYPE + item.WARN_LEVEL }}</p>
                      <p>{{ item.WARN_LEVEL }}预警</p>
                    </div>
                  </div>
                </div>
                <div class="meteorology" v-else><span>暂无数据</span></div>
              </td>
            </tr>
            <tr>
              <td>
                <div>
                  <img src="@/assets/seawall.png" alt="" /> <span>海塘</span>
                </div>
              </td>
              <td>
                <div class="seawall_alarm">
                  <div>
                    <span>海塘报警：</span>
                    <span class="no-alarm" :class="{ 'red-alarm': seawallWarn.warn != '正常' }">{{ seawallWarn.warn }}</span>
                  </div>
                  <div>
                    <span>海塘预警：</span>
                    <span class="no-alarm" :class="{ 'red-alarm': seawallWarn.earlyWarn != '正常' }">{{ seawallWarn.earlyWarn }}</span>
                  </div>
                </div>
              </td>
            </tr>
            <tr>
              <td>
                <div>
                  <img src="@/assets/security.png" alt="" />
                  <span>安全监测</span>
                </div>
              </td>
              <td>
                <div class="seawall_alarm">
                  <div>
                    <span>沉降：</span>
                    <span class="no-alarm">正常</span>
                  </div>
                  <div>
                    <span>位移：</span>
                    <span class="no-alarm">正常</span>
                  </div>
                </div>
              </td>
            </tr>
            <tr>
              <td>
                <div>
                  <img src="@/assets/typhoon.png" alt="" />
                  <span>台风影响</span>
                </div>
              </td>
              <td>
                <div v-if="typhoonData.length > 0" class="meteorology">
                  <div>
                    <div>台风名称：</div>
                    <div>
                      <span>{{ typhoonData[0].name }}</span>
                    </div>
                  </div>
                  <div>
                    <div>台风距离：</div>
                    <div>
                      <span>{{ typhoonData[0].distance }}km</span>
                    </div>
                  </div>
                </div>
                <div v-else class="meteorology">
                  <span>暂无台风</span>
                </div>
              </td>
            </tr>
            <tr>
              <td rowspan="1" align="center" style="vertical-align: center">
                <div>
                  <img src="@/assets/Callthepolice.png" alt="" />
                  <span>运行报警</span>
                </div>
              </td>
              <td>
                <div class="seawall_alarm">
                  <div>
                    <span>安全鉴定：</span>
                    <span class="no-alarm" :class="{ 'red-alarm': RuntimeWarn.safety == 1 }">{{ RuntimeWarn.safety == 1 ? "已超期" : "未超期" }}</span>
                  </div>
                  <div>
                    <span>汛前检查：</span>
                    <span class="no-alarm" :class="{ 'red-alarm': RuntimeWarn.flood == 1 }">{{ RuntimeWarn.flood == 1 ? "已超期" : "未超期" }}</span>
                  </div>
                </div>
              </td>
            </tr>
            <tr>
              <td>
                <div>
                  <img src="@/assets/Callthepolice.png" alt="" />
                  <span>运行报警</span>
                </div>
              </td>

              <td>
                <div class="seawall_alarm">
                  <div>
                    <span>年度总结：</span>
                    <span class="no-alarm" :class="{
                        'red-alarm': RuntimeWarn.yearReport == 1,
                      }">{{
                        RuntimeWarn.yearReport == 1 ? "已超期" : "未超期"
                      }}</span>
                  </div>
                  <div>
                    <span>巡查信息：</span>
                    <span class="no-alarm" :class="{ 'red-alarm': RuntimeWarn.patrol == 1 }">{{ RuntimeWarn.patrol == 1 ? "异常" : "无异常" }}</span>
                  </div>
                </div>
              </td>
            </tr>
            <tr>
              <td>
                <div>
                  <img src="@/assets/Callthepolice.png" alt="" />
                  <span>运行报警</span>
                </div>
              </td>
              <td>
                <div class="seawall_alarm">
                  <div>
                    <span>工程隐患：</span>
                    <span class="no-alarm" :class="{ 'red-alarm': RuntimeWarn.danger == 1 }">{{ RuntimeWarn.danger == 1 ? "有隐患" : "无隐患" }}</span>
                  </div>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
    <section class="person_section">
      <div class="title_box">
        <div class="title_flex">
          <img src="@/assets/decorate.png" alt="" />
          <span>责任人</span>
        </div>
      </div>
      <div class="person_list">
        <div class="person_list_item" v-for="(item, index) in personList" :key="index">
          <div class="person_left">
            <img src="../../../assets/images/person-head.png" alt="" />
            <div>
              <h3>{{ item.type }}</h3>
              <p>
                {{ item.username }}<span>{{ item.job }}</span>
              </p>
            </div>
          </div>
          <div class="person_right">
            <img src="../../../assets/phome.png" alt="" />
            <span>{{ item.phone }}</span>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { homeIndex } from "@/api/projectDetail";
export default {
  name: "HomePage",
  props: {},
  components: {},
  data() {
    return {
      personList: [],
      lat: 0,
      lng: 0,
      msgData: {},
      typhoonData: [],
      weatherWarningList: [],
      seawallWarn: {
        warn: "-",
        earlyWarn: "-",
      },
      RuntimeWarn: {
        danger: 0,
        flood: 0,
        patrol: 0,
        safety: 0,
        yearReport: 0,
      },
      seawallCode: '',
      projectDetail: {},
    };
  },
  async mounted() {
    this.seawallCode = this.$route.query.seawallCode;
    var timer = setTimeout(() => {
      this.getSeawallDetail()
      // 查询台风
      this.getSeawallTyphoonDif();
      // 责任人
      this.responsible();
      // 气象预报
      this.getWeatherWarning();
      // 海塘预警
      this.getSelectWarn();
      // 运行报警
      this.getSelectRuntimeWarn();
    }, 500)
  },
  methods: {
    async getSeawallDetail() {
      let res = await homeIndex.getProjectList({
        code: this.seawallCode,
        type: "海塘",
      });
      if (res.code == 0) {
        this.projectDetail = res.data[0];
        this.$localData("set", "projectDetail", this.projectDetail);
        this.$localData("set", "projectCode", this.seawallCode);
        this.lat = this.projectDetail.latitude;
        this.lng = this.projectDetail.longitude;
      }
    },
    // 责任人
    async responsible() {
      let res = await homeIndex.getChargePerson({
        projectCode: this.seawallCode,
      });
      if (res.code == 0) {
        res.data.forEach((element, index) => {
          if (index < 3) {
            element.phone = element.phone.substring(0, 3) + '****' + element.phone.substring(7)
          }
        });
        this.personList = res.data;
      }
    },
    async getSelectWarn() {
      let res = await homeIndex.getSeawallSelectWarn(
        this.seawallCode
      );
      if (res.code == 0) {
        if (res.data && res.data.warn) {
          this.seawallWarn.warn = this.disposeWarn(res.data.warn);
        }
        if (res.data && res.data.earlyWarn) {
          this.seawallWarn.earlyWarn = this.disposeWarn(res.data.earlyWarn);
        }
      }
    },
    async getSelectRuntimeWarn() {
      let res = await homeIndex.selectRuntimeWarn({
        projectCode: this.seawallCode,
      });
      if (res.code == 0) {
        this.RuntimeWarn = res.data[0];
      }
    },
    //根据海塘查询台风
    async getSeawallTyphoonDif() {
      let res = await homeIndex.seawallTyphoonDif({
        lat: this.lat,
        lng: this.lng,
      });
      if (res.code == 0) {
        this.typhoonData = res.data.map((item) => {
          let points = (item.points);

          item["distance"] = points[points.length - 1].distance;
          item["power"] = points[points.length - 1].power;
          return item;
        });
      }
    },
    // 气象预报
    getWeatherWarning() {
      homeIndex.weatherWarning().then((res) => {
        if (res.code == 0) {
          let arr = {
            blue: 0,
            yellow: 0,
            orange: 0,
            red: 0,
          };
          this.weatherWarningList = res.data;
          this.weatherWarningList.map((item) => {
            let type = item.WARN_TYPE + item.WARN_LEVEL;
            item.imgSrc = this.getWeatherWarningIcon(type);
            arr.blue += item.WARN_LEVEL == "蓝色" ? 1 : 0;
            arr.yellow += item.WARN_LEVEL == "黄色" ? 1 : 0;
            arr.orange += item.WARN_LEVEL == "橙色" ? 1 : 0;
            arr.red += item.WARN_LEVEL == "红色" ? 1 : 0;
          });
        }
      });
    },
    disposeWarn(type) {
      switch (type) {
        case 1:
          return "正常";
        case 2:
          return "超蓝色";
        case 3:
          return "超黄色";
        case 4:
          return "超橙色";
        default:
          return "超红色";
      }
    },
    getWeatherWarningIcon(name) {
      let imgSrc = "";
      switch (name) {
        case "暴雨橙色":
          {
            imgSrc = require("@/assets/images/warns/heavy-rain-orange.png");
          }
          break;
        case "暴雨红色":
          {
            imgSrc = require("@/assets/images/warns/heavy-rain-red.png");
          }
          break;
        case "暴雨黄色":
          {
            imgSrc = require("@/assets/images/warns/heavy-rain-yellow.png");
          }
          break;
        case "暴雨蓝色":
          {
            imgSrc = require("@/assets/images/warns/heavy-rains-blue.png");
          }
          break;
        case "大风橙色":
          {
            imgSrc = require("@/assets/images/warns/the-wind-orange.png");
          }
          break;
        case "大风黄色":
          {
            imgSrc = require("@/assets/images/warns/the-wind-yellow.png");
          }
          break;
        case "大风蓝色":
          {
            imgSrc = require("@/assets/images/warns/the-wind-blue.png");
          }
          break;
        case "干旱橙色":
          {
            imgSrc = require("@/assets/images/warns/dry-orange.png");
          }
          break;
        case "雷电橙色":
          {
            imgSrc = require("@/assets/images/warns/lightning-orange.png");
          }
          break;
        case "雷电黄色":
          {
            imgSrc = require("@/assets/images/warns/lightning-yellow.png");
          }
          break;
        case "雷雨大风橙色":
          {
            imgSrc = require("@/assets/images/warns/thunderstorm-gale-orange.png");
          }
          break;
        case "沙尘暴橙色":
          {
            imgSrc = require("@/assets/images/warns/dust-storm-orange.png");
          }
          break;
        default: {
          imgSrc = require("@/assets/images/warns/warning-remind.png");
        }
      }
      return imgSrc;
    },
  },
  beforeDestroy() {
    this.$localData("clean");
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  // background: #eee;
  // height: 800px;
  width: 100%;
  .section_title {
    font-size: 20px;
    color: #1f1f1f;
    font-weight: 500;
  }
  .second_title {
    font-size: 14px;
    color: #b3b3b3;
    font-weight: 400;
  }
  .basis_section {
    padding: 12px;
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
  .dynamic_section {
    padding: 12px;
    .table_box {
      margin-top: 12px;
      margin-bottom: 12px;
      table {
        width: 100%;
        height: 100%;
        display: table;
        // border-collapse: separate;
        box-sizing: border-box;
        border-collapse: collapse;
        border-spacing: 0px;
        margin: auto;
      }
      table,
      table tr th,
      table tr td {
        padding: 5px;
        border: 1px solid #e6e6e6;
      }
      thead {
        background: #f4f8fb;
        tr > th {
          font-size: 16px;
          font-weight: 700;
        }
      }
      tbody {
        span {
          font-size: 14px;
        }
      }
      tbody tr > td:nth-child(1) {
        > div {
          width: 100%;
          height: 100%;
          @include flexbox;
          @include flexJC(flex-start);
          @include flexAI(center);
          img {
            width: 50px;
            margin-right: 5px;
          }
        }
      }
      tbody tr > td:nth-child(2) {
        position: relative;
        padding: 0;
        > div {
          position: absolute;
          top: 0;
          padding: 5px;
          box-sizing: border-box;
        }
      }
      tbody tr > td:nth-child(2) .meteorology > div {
        width: 100%;
        height: 100%;
        @include flexbox;
        @include flexJC(center);
        @include flexAI(center);
        img {
          width: 50px;
          margin-right: 5px;
        }
        div {
          @include flexbox;
          @include flexJC(flex-start);
          @include flexAI(center);
          flex-direction: column;
        }
      }
      .meteorology {
        width: 100%;
        height: 100%;
        @include flexbox;
        @include flexJC(center);
        @include flexAI(center);
        vertical-align: center;
        > div div span {
          width: 100%;
          height: 100%;
          &.no-alarm {
            color: #00baff;
          }
          &.red-alarm {
            color: #ff0000;
          }
        }
      }
      thead th:nth-child(1) {
        width: 30%;
      }
      tbody tr > td:nth-child(2) {
        width: 70%;
        height: 100%;
      }
      .seawall_alarm {
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: space-around;
        align-items: center;
        span {
          width: 100%;
          height: 100%;
          &.no-alarm {
            color: #00baff;
          }
          &.red-alarm {
            color: #ff0000;
          }
        }
      }
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
              font-size: 20px;
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
  .nodata {
    display: block;
    padding: 12px;
  }
  .last_item_title {
    margin-top: 12px;
    font-size: 18px;
    color: #1f1f1f;
    font-weight: 500;
    line-height: 24px;
    & > span {
      
    }
  }
  .last_item_detail {
    border-bottom: 1px solid #e5e5e5;
    & > div {
      padding: 12px 0;
      p {
        font-size: 14px;
        color: rgba(31, 31, 31, 0.48);
        line-height: 28px;
        font-weight: 400;
        img {
          vertical-align: text-top;
          margin-right: 6px;
        }
        i {
          vertical-align: text-top;
          margin-right: 4px;
        }
      }
    }
  }
  .title_box {
    padding-bottom: 15px;
    border-bottom: 1px solid #e5e5e5;
  }
}
</style>
