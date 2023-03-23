<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-05-23 14:02:20
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\SluiceDialogForSchedule\components\BasicInfo.vue
-->
<script>
export default {
  name: 'BasicInfo',
  props: {
    propList: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {};
  },
  render() {
    let featureInfo = this.renderFeatureInfo();
    return <div class="basic-info">{featureInfo}</div>;
  },
  methods: {
    renderBasicInfo() {
      const damMap = {
        面板坝: require('@/assets/images/damTypes/concrete-face-dam.png'),
        均质坝: require('@/assets/images/damTypes/homogeneity-dam.png'),
        拱坝: require('@/assets/images/damTypes/arch-dam.png'),
        斜墙坝: require('@/assets/images/damTypes/Inclined-wall-dam.png'),
        心墙坝: require('@/assets/images/damTypes/core-dam.png'),
        支墩坝: require('@/assets/images/damTypes/abanurus-dam.png'),
        重力坝: require('@/assets/images/damTypes/gravity-dam.png')
      };
      const damType = this.propList.filter((item) => item.prop == 'mainWrType')[0]?.data;

      const damImg = damType && damMap[damType] ? damMap[damType] : require('@/assets/images/project-map-dam.png');

      let dikeRender = (
        <div class="figure-box">
          <div class="figure-box-staff">
            <div>m</div>
            <div>0</div>
          </div>
          <div class="green-water" />

          <div class="gray-dam">
            <img src={damImg} />
            <div class="small-green-water"></div>
          </div>
        </div>
      );
      let includesList = ['engScal', 'areaName', 'totCap'];
      let basicInfoList = this.propList.filter((item) => {
        return includesList.includes(item.prop);
      });

      let detailRender = (
        <div class="basic-info-item__detail">
          {basicInfoList.map((item, index) => {
            return (
              <div key={index} class="detail-item">
                <span class="item-title">{item.name}</span>
                <span class="item-label">{item.data}</span>
              </div>
            );
          })}
        </div>
      );

      return (
        <InfoItem title="工程基础信息">
          {dikeRender}
          {detailRender}
        </InfoItem>
      );
    },
    renderFeatureInfo() {
      let ignoreList = ['engScal', 'areaCode', 'totCap', 'mainWrType'];
      const infoListItem = this.propList.map((item, index) => {
        if (!ignoreList.includes(item.prop))
          return (
            <el-col class="info-item" key={index} span={12}>
              <span class="item-title">{item.name}:</span>
              <span class="item-label">{item.data}</span>
            </el-col>
          );
      });
      return <el-row class="info-list">{infoListItem}</el-row>;
    }
  }
};
</script>

<style lang="scss" scoped>
.info-list {
  .info-item {
    margin-bottom: 16px;
    .item-title {
      color: #00BAFF;
    }
    .item-label {
      color: #fff;
      padding-left: 10px;
    }
  }
}
.basic-info {
  ::v-deep .basic-info-item {
    margin-bottom: 16px;
    &:last-child {
      margin-bottom: 0;
    }
    .basic-info-item__title {
      border-left: 2px solid #1e8bd6;
      padding-left: 6px;
      color: #44a0cd;
      font-size: 15px;
    }
    .basic-info-item__content {
      display: flex;
      margin-top: 16px;
      justify-content: space-between;
      padding-left: 6px;
      .figure-box {
        width: 70%;
      }
      .basic-info-item__detail {
        width: 25%;
        display: flex;
        flex-direction: column;
        .detail-item {
          display: flex;
          flex-direction: column;
          height: 33%;
          justify-content: space-between;
          padding: 14px 0;
        }
      }
    }
    .info-list {
      .el-col {
        margin: 8px 0;
      }
      .item-title {
        margin-right: 16px;
      }
    }
  }
  .item-title {
    color: #3f86df;
  }
  .item-label {
    color: #fff;
  }
}
.figure-box {
  height: 12rem;
  padding-left: 1px;
  position: relative;
  overflow: hidden;
  .figure-box-staff {
    height: 100%;
    width: 1rem;
    color: #fff;
    border-right: 1px solid #ffffff;
    position: absolute;
    top: 0;
    left: 0;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }

  .current-water-level {
    width: 50%;
    padding: 0.5rem;
    border-bottom: 2px dashed #32bbb3;
    color: #ffffff;
    position: absolute;
    top: 18%; //需要动态修改
    left: 1rem;
  }
  @keyframes wave {
    0% {
      background-position: 0px bottom;
    }
    100% {
      background-position: 252px bottom;
    }
  }
  .green-water {
    position: absolute;
    width: 182px;
    height: 140px;
    bottom: 0;
    left: 1rem;
    background: url(~assets/images/project-map-greenwater.png) left bottom;
    background-size: cover;
    animation: wave 10s linear infinite;
  }

  .gray-dam {
    height: 100%;
    // background: url(~assets/images/project-map-dam.png) left bottom;
    // background-size: 100% 100%;
    position: relative;
    width: 170px;
    top: 0px;
    left: calc(30% + 1rem);
    img {
      width: 100%;
      height: 100%;
    }
  }

  .over-flood {
    width: calc(100% - 1rem);
    padding: 0.5rem;
    border-bottom: 2px solid #ff0000;
    color: #ff0000;
    text-align: right;
    position: absolute;
    top: calc(50% - 0.5rem);
    left: 1rem;
  }

  .small-green-water {
    width: 20px;
    height: 100%;
    background: url(~assets/images/project-map-greenwater-s.png) left bottom no-repeat;
    background-size: 100% 9%;
    position: absolute;
    bottom: -2px;
    right: -20px;
  }
}
</style>
