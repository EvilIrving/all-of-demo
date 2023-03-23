<script>
export default {
  name: 'ReservoirInfo',
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
    let basicInfo = this.renderBasicInfo();
    return <div class="basic-info">{basicInfo}</div>;
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
      if (this.propList.length > 0) {
        let data = this.propList.find((item) => item.prop == 'currentLimitLevel');
        let realData = this.propList.find((item) => item.prop == 'waterLevel');
        let str = 'current-water-level';
        let realStr = 'real-time-water-level';
        if (realData.data == '-' || realData.data > data.data) {
          str = 'current-water-level water-level';
          realStr = 'real-time-water-level water-level';
        }
        let dikeRender = (
          <div class="figure-box">
            <div class="figure-box-staff">
              <div>m</div>
              <div>0</div>
            </div>
            <div class={str}>汛限水位(m): {data.data}</div>
            <div class={realStr}>实时水位(m): {realData.data}</div>
            <div class="over-flood"></div>
            <div class="green-water" />
            <div class="gray-dam">
              <img src={damImg} />
            </div>
          </div>
        );
        let basicInfoList = this.propList;
        let detailRender = (
          <div class="basic-info-item__detail">
            {basicInfoList.map((item, index) => {
              return (
                <div key={index} class="detail-item">
                  <span class="item-title">{item.name}:</span>
                  <span class="item-label">{item.data}</span>
                </div>
              );
            })}
          </div>
        );
        return (
          <div class="basic-info-item">
            {dikeRender}
            {detailRender}
          </div>
        );
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.basic-info {
  width: 100%;
  .basic-info-item {
    margin-bottom: 16px;
    display: flex;
    justify-content: space-between;
    .figure-box {
      width: 70%;
    }
    .basic-info-item__detail {
      width: 25%;
      display: flex;
      flex-direction: column;
      .detail-item {
        display: flex;
        height: 33%;
        align-items: center;
        padding: 14px 0;
        > span:nth-child(2) {
          padding-left: 10px;
        }
      }
    }
  }
  .item-title {
    color: #00baff;
  }
  .item-label {
    color: #fff;
  }
}
.figure-box {
  height: 12rem;
  padding-left: 1px;
  position: relative;
  // overflow: hidden;
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
    width: 300px;
    padding: 0.5rem;
    border-bottom: 2px dashed yellow;
    color: yellow;
    position: absolute;
    top: 18%; //需要动态修改
    left: 1rem;
    z-index: 2;
    &.water-level {
      top: 38%;
    }
  }
  .real-time-water-level {
    width: 300px;
    padding: 0.5rem;
    border-bottom: 2px dashed #00baff;
    color: #00baff;
    position: absolute;
    top: 0%; //需要动态修改
    left: 1rem;
    &.water-level {
      top: 18%;
    }
  }
  @keyframes wave {
    0% {
      background-position: 0px bottom;
    }
    100% {
      background-position: 250px bottom;
    }
  }
  .green-water {
    position: absolute;
    width: 300px;
    height: 123px;
    bottom: 0;
    left: 1rem;
    background: url(~assets/images/project-map-greenwater.png) center center;
    background-size: cover;
    animation: wave 10s linear infinite;
  }
  .gray-dam {
    height: 100%;
    position: relative;
    width: 170px;
    top: 0px;
    left: calc(250px + 1rem);
    z-index: 4;
    img {
      width: 100%;
      height: 100%;
    }
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
