<script>
import PanelItem from "../../components/PanelItem.vue";
import { mapActions, mapGetters } from "vuex";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
export default {
  name: "PanelMountainTorrents",
  prop: {},
  data() {
    return {
      ylbtn: false,
      tabActive:'预报',
    };
  },
  computed: { 
    ...mapGetters(["ScreenMap", "torrentsWarning", "effectTown","previewModule","formatterCurrentTime","AffairId"]),
    currentTime(){
      let time = this.formatterCurrentTime();
      return time != "Invalid date" ? time : "请选择时间";
    }
  },
  watch:{
    currentTime: {
      immediate: true,
      handler(val) {
        this.$nextTick(() => {
          if(this.currentTime != '请选择时间'){
            this.tabChange('监测');
          }else{
            this.getTorrentsWarning();
          }
        });
      }
    },
  },
  mounted() {
    let time = new Date().format('yyyy-MM-dd hh:mm:ss');
    this.getTorrentsWarning(time);
  },
  methods: {
    ...mapActions(["getTorrentsWarning"]),
    renderPanelLeft() {
      return (
        <el-carousel-item class="torrent-total-info__page">
          <div class="page-item">
            <div class="page-item__label">
              <i class="icon-torrents-total-counties" />
              涉及县市区
            </div>
            <div class="page-item__data">
              <span class="number">12</span>
              <span class="unit">个</span>
            </div>
          </div>
          <div class="page-item">
            <div class="page-item__label">
              <i class="icon-torrents-total-town" />
              防治乡镇
            </div>
            <div class="page-item__data">
              <span class="number">184</span>
              <span class="unit">个</span>
            </div>
          </div>
          <div class="page-item">
            <div class="page-item__label">
              <i class="icon-torrents-total-village" />
              防治村落
            </div>
            <div class="page-item__data">
              <span class="number">7200</span>
              <span class="unit">个</span>
            </div>
          </div>
          <div class="page-item">
            <div class="page-item__label">
              <i class="icon-torrents-total-population" />
              影响人口
            </div>
            <div class="page-item__data">
              <span class="number">12.98</span>
              <span class="unit">万人</span>
            </div>
          </div>
        </el-carousel-item>
      );
    },
    renderPanelRight() {
      return (
        <el-carousel-item class="torrent-total-info__page">
          <div class="page-item">
            <div class="page-item__label baseline">
              <span class="title">总数</span>
              <span class="number">3027</span>
              <span class="unit">个</span>
            </div>
          </div>
          <div class="page-item">
            <div class="page-item__label">
              <i class="icon-torrents-alert" />
              危险区
            </div>
            <div class="page-item__data">
              <span class="number">2002</span>
              <span class="unit">个</span>
            </div>
          </div>
          <div class="page-item">
            <div class="page-item__label">
              <i class="icon-torrents-alert" />
              高危险区
            </div>
            <div class="page-item__data">
              <span class="number">709</span>
              <span class="unit">个</span>
            </div>
          </div>
          <div class="page-item">
            <div class="page-item__label">
              <i class="icon-torrents-alert" />
              极高危险区
            </div>
            <div class="page-item__data">
              <span class="number">316</span>
              <span class="unit">个</span>
            </div>
          </div>
        </el-carousel-item>
      );
    },
    renderTwoCard() {
      return [
        <div class="capsule-tabs">
          <div onClick={()=>{this.tabChange('预报')}} class={['capsule-tabs__item',this.tabActive == '预报' ? 'active':'']}>预报</div>
          <div onClick={()=>{this.tabChange('监测')}} class={['capsule-tabs__item',this.tabActive == '监测' ? 'active':'']}>监测</div>
        </div>,
        <div class="possibility-card yellow">
          <div class="card-title">{this.tabActive == '监测' ?'准备转移' : '有可能发生山洪'}</div>
          <div class="card-content">
            <div class="card-content-item">
              <div class="content-label">县区</div>
              <div>
                <span class="count-number">{this.torrentsWarning.grade4County || 0}</span>
                <span class="unit">个</span>
              </div>
            </div>
            <div class="card-content-item">
              <div class="content-label">乡镇</div>
              <div>
                <span class="count-number">{this.torrentsWarning.grade4street || 0}</span>
                <span class="unit">个</span>
              </div>
            </div>
            <div class="card-content-item">
              <div class="content-label">村落</div>
              <div>
                <span class="count-number">
                  {this.torrentsWarning.grade4villages || 0}
                </span>
                <span class="unit">个</span>
              </div>
            </div>
            <div class="card-content-item">
              <div class="content-label">人口</div>
              <div>
                <span class="count-number">{this.torrentsWarning.grade4Person || 0}</span>
                <span class="unit">个</span>
              </div>
            </div>
          </div>
        </div>,
        <div class="possibility-card">
          <div class="card-title">{this.tabActive == '监测' ?'立即转移' : '可能性很大'}</div>
          <div class="card-content">
            <div class="card-content-item">
              <div class="content-label">县区</div>
              <div>
                <span class="count-number">{this.torrentsWarning.grade5County || 0}</span>
                <span class="unit">个</span>
              </div>
            </div>
            <div class="card-content-item">
              <div class="content-label">乡镇</div>
              <div>
                <span class="count-number">{this.torrentsWarning.grade5street || 0}</span>
                <span class="unit">个</span>
              </div>
            </div>
            <div class="card-content-item">
              <div class="content-label">村落</div>
              <div>
                <span class="count-number">
                  {this.torrentsWarning.grade5villages || 0}
                </span>
                <span class="unit">个</span>
              </div>
            </div>
            <div class="card-content-item">
              <div class="content-label">人口</div>
              <div>
                <span class="count-number">{this.torrentsWarning.grade5Person || 0}</span>
                <span class="unit">个</span>
              </div>
            </div>
          </div>
        </div>,
      ];
    },
    // 地图落点
    drawFloodWarningPoint() {
      if (!this.ylbtn) {
        this.ScreenMap.drawPoint([], {}, "floodWarningLayer");
        this.ScreenMap.clearAreaWKTLayer();
        this.$emit("legend-change",'村落',{});
        this.$emit("legend-change",'危险区',{});
        return;
      }
      let style = {
        src: require("@/assets/images/MapPoint/torrents.png"),
        anchor: [27, 30],
        anchorXUnits: "pixels",
        scale: 0.7,
      };
      let pointList = [];
      let towns = this.effectTown || [];
      for (let town of towns) {
        let list = town.villages.map((item) => {
          return {
            name: item.adnm,
            lng: item.lng,
            lat: item.lat,
            type: "山洪预警",
            label: item.desc,
          };
        });
        pointList = pointList.concat(list);
      }
      this.ScreenMap.drawPoint(pointList, style, "floodWarningLayer");
      this.$emit("legend-change", "村落", {
        label: "村落",
        children: [
          {
            label: "一般村落",
            icon: "icon-normal-village",
          },
          {
            label: "重要村落",
            icon: "icon-important-village",
          },
        ],
      });
      this.$emit("legend-change", "危险区", {
        label: "危险区",
        children: [
          {
            label: "危险区",
            icon: "icon-danger-area",
          },{
            label: "高危险区",
            icon: "icon-moredanger-village",
          },{
            label: "极高危险区",
            icon: "icon-mostdanger-village",
          },
        ],
      });

    },
    async listPrevTfInfo(){
      if(!this.ylbtn){
        this.ScreenMap.drawClusterPoint([], {}, "normalPointLayer");
        this.ScreenMap.drawClusterPoint([], {}, "importPointLayer");
        return;
      }
      let opt = {};
      let res = await disasterPreventionApi.listPrevTfInfo(opt);
      let normalArr = [];
      let importArr = [];
      let areaPolyArr = [];
      for(let i=0;i<res.data.length;i++){
        if(res.data[i].recType == '一般村落'){
          let item = res.data[i];
          item.src = require("@/assets/images/MapPoint/normal-village.png");
          item.lng = item.lon;
          item.name = item.avi;
          item.type = '山洪';
          item.props = {
            '总户数':item.tho + '户',
            '总人口':item.tpo + '人'
          }
          normalArr.push(item);
          
        }else if(res.data[i].recType == '重要村落'){
          let item = res.data[i];
          item.lng = item.lon;
          item.type = '山洪';
          item.src = require("@/assets/images/MapPoint/important-village.png");
          item.name = item.avi;
          item.props = {
            '总户数':item.tho+ '户',
            '总人口':item.tpo+ '人'
          }
          importArr.push(item);
        }else{
          let item = res.data[i];
          item.type = '山洪';
          item.coordinates = item.areaPoints;
          areaPolyArr.push(item);
        }
      }
      const normalStyle = {
        src: require("@/assets/images/MapPoint/normal-village.png"),
        srcs:require("@/assets/images/MapPoint/normal-village-group.png"),
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
      };
      const importStyle = {
        src: require("@/assets/images/MapPoint/important-village.png"),
        srcs:require("@/assets/images/MapPoint/important-village-group.png"),
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
      }
      this.ScreenMap.drawClusterPoint(normalArr,normalStyle,'normalPointLayer');
      this.ScreenMap.drawClusterPoint(importArr,importStyle,'importPointLayer');
      this.ScreenMap.drawAreaWKT(areaPolyArr);
    },
    tabChange(type){
      if(this.previewModule && type == '预报') return;
      this.tabActive = type;
      if(type == '预报'){
        this.getTorrentsWarning();
      }else{
        let time = new Date(this.formatterCurrentTime()).format('yyyy-MM-dd hh:mm:ss');
        let opt;
        if(this.previewModule){
          opt = {
            currentFlag:false,
            time,
            drillAffairId:this.AffairId
          }
        }else{
          time = new Date().format('yyyy-MM-dd hh:mm:ss');
          opt = {
            currentFlag:true,
            time
          }
        }
        this.getTorrentsWarning(opt);
      }
    }
  },

  render() {
    let leftPart = this.renderPanelLeft();
    let rightPart = this.renderPanelRight();
    let twoCard = this.renderTwoCard();
    let yinzhangClass = [
      this.ylbtn ? "icon-yinzhang-active" : "icon-yinzhang-1",
      "icon-sty",
    ];
    return (
      <PanelItem inSimulation={this.previewModule} title='山洪预警' hasSwitch class="panel-torrent">
        <template slot="title-switch">
          <i
            title="地图落点"
            onClick={() => {
              this.ylbtn = !this.ylbtn;
              this.drawFloodWarningPoint();
              this.listPrevTfInfo();
            }}
            class={yinzhangClass}
          />
        </template>
        <el-row class="torrent-info">
          <el-col span={12} class="torrent-total-info">
            <el-carousel
              trigger="click"
              arrow="never"
              height="345px"
              interval={5000}
              autoplay={false}
            >
              {leftPart}
              {rightPart}
            </el-carousel>
          </el-col>
          <el-col span={12} class="torrent-possibility">
            {twoCard}
          </el-col>
        </el-row>
      </PanelItem>
    );
  },
};
</script>
<style lang="scss" scoped>
.panel-torrent {
  color: #fff;
  ::v-deep.el-carousel__indicators {
    .el-carousel__indicator {
      &.is-active button {
        background-color: #1ef8f2;
      }
    }
  }
  ::v-deep.el-carousel__button {
    padding: 0;
    width: 9px;
    height: 9px;
    border-radius: 50%;
  }
  .torrent-info {
    padding-top: 16px;
    .torrent-total-info {
      background-image: url(~assets/images/ScreenAsset/torrents-bg1.png);
      background-repeat: no-repeat;
      background-size: 95% 100%;
      ::v-deep.el-carousel {
        margin-right: 40px;
      }
      .torrent-total-info__page {
        padding: 16px;
        display: flex;
        flex-direction: column;
        .page-item {
          height: 25%;
          .page-item__label {
            display: flex;

            i {
              margin: 0 8px;
              margin-bottom: 8px;
            }
            .title {
              font-size: 18px;
              padding-left: 8px;
            }
            .number {
              font-size: 36px;
              color: #17cfcb;
              margin-left: 8px;
            }
            &.baseline {
              align-items: flex-end;
            }
          }
          .page-item__data {
            padding-left: 30px;
            .number {
              font-size: 28px;
              color: #1cfff5;
            }
          }
        }
      }
    }
    .torrent-possibility {
      .capsule-tabs{
        @include flexJC(flex-end);
        width: 100%;
      }
      .possibility-card {
        height: 160px;
        display: flex;
        flex-direction: column;
        align-items: center;
        .card-title {
          font-family: PingFangSC-Semibold;
          font-size: 20px;
          color: #ff7144;
          margin: 16px auto;
        }
        .card-content {
          display: flex;
          flex-wrap: wrap;
          width: 100%;
          .card-content-item {
            width: 50%;
            text-align: center;
            margin-bottom: 16px;
            .content-label {
              margin-bottom: 6px;
              color: #fff;
            }
            .count-number {
              font-size: 20px;
              color: #ff7144;
            }
          }
        }
        &.yellow {
          .card-title {
            color: #e2c417;
          }
          .card-content {
            .card-content-item {
              .count-number {
                color: #e2c417;
              }
            }
          }
        }

        &:first-child {
          background-image: url(~assets/images/ScreenAsset/torrents-bg3.png);
          background-repeat: no-repeat;
          background-size: 100%;
          margin-bottom: 10px;
        }
        &:last-child {
          background-image: url(~assets/images/ScreenAsset/torrents-bg2.png);
          background-repeat: no-repeat;
          background-size: 100%;
        }
      }
    }
  }
}
</style>
