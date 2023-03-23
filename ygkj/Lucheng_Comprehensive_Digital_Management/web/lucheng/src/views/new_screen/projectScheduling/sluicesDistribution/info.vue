<template>
  <div class="info-box">
    <div class="info-left">
      <div class="panel-info">
        <div class="panel-info-title">水闸分析</div>
        <div class="panel-info-check">
          <el-checkbox-group v-model="sluiceActiveArr" @change="handleSluiceCheckBox">
            <el-checkbox
              class="panel-info-check-option"
              v-for="(item, index) in sluiceList"
              :label="item"
              :key="index"
              >{{ item }}</el-checkbox
            >
          </el-checkbox-group>
        </div>
      </div>
    </div>
    <div class="info-right">
      <div class="lock-water-level" v-if="sluiceActiveArr.length === 0">
        <div class="text-color water-level-title">实时潮位水位：</div>
        <div class="water-level-list">
          <span class="text-color" v-for="item in sluiceWaterList" :key="item.stCode"
            >{{ item.stName }}：{{ item.level || '-' }}m</span
          >
        </div>
      </div>
      <div class="sluice-analyse-box" v-else>
        <div class="sluice-analyse-item">
          <div>正常</div>
          <div>
            <div>
              <span class="dark_blue_span"></span>
              <span>丰门河水闸</span>
            </div>
            <div>
              <span class="sky_blue_span"></span>
              <span>温瑞塘河水闸、七都内河水闸</span>
            </div>
          </div>
        </div>
        <div class="sluice-analyse-item" v-if="sluiceActiveArr[0] === '负荷分析'">
          <div>接近满负荷</div>
          <div>
            <div>
              <span class="yellow_span"></span>
              <span>所有水闸</span>
            </div>
          </div>
        </div>
        <div class="sluice-analyse-item">
          <div>{{sluiceActiveArr[0] === '控运分析' ? '超控运' : sluiceActiveArr[0] === '负荷分析' ? '满负荷' : '强排'}}</div>
          <div>
            <div>
              <span class="red_span"></span>
              <span>所有水闸</span>
            </div>
          </div>
        </div>
        <div class="sluice-analyse-item" v-if="sluiceActiveArr[0] !== '排水分析'">
          <div>{{ sluiceActiveArr[0] !== '控运分析' ? '无数据' : '无控运' }}</div>
          <div>
            <div>
              <span class="gray_span"></span>
              <span>所有水闸</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { SluiceDistributionMap } from '@/api/bigScreenPart';
import { mapMutations } from 'vuex';
export default {
  data() {
    return {
      sluiceList: ['控运分析', '负荷分析', '排水分析'],
      sluiceActiveArr: [],
      sluiceWaterList: []
    };
  },
  mounted() {
    this.getLatestData();
  },
  methods: {
    ...mapMutations({
      setSluiceActiveArr: 'SET_SLUICE_ACTIVE'
    }),
    handleSluiceCheckBox() {
      if (this.sluiceActiveArr.length > 0) {
        let data = this.sluiceActiveArr[this.sluiceActiveArr.length - 1];
        this.sluiceActiveArr = [data];
      }
      this.setSluiceActiveArr(this.sluiceActiveArr);
    },
    async getLatestData() {
      let res = await SluiceDistributionMap.latestData();
      if (res.code === 0) {
        this.sluiceWaterList = res.data;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.info-box {
  display: flex;
  height: 100%;
  .info-left {
    height: 100%;
    border-right: 1px solid rgba(0, 186, 255, 0.39);
    display: flex;
    justify-content: space-between;
    align-items: center;
    .panel-info {
      padding: 0 32px;
      .panel-info-title {
        color: #00baff;
        font-size: 20px;
        padding-bottom: 8px;
      }
      .panel-info-check {
        font-size: 16px;
        .panel-info-check-option {
          color: #fff;
        }
      }
    }
  }
  .info-right {
    height: 100%;
    width: 910px;
    .lock-water-level {
      padding: 12px 12px 0 32px;
      .text-color {
        font-size: 16px;
        font-weight: 400;
        background: linear-gradient(180deg, #56fefe 0%, #56fefe 30%, #0639ff 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
      }
      .water-level-title {
        padding-bottom: 8px;
      }
      .water-level-list {
        display: flex;
        flex-wrap: wrap;
        > span {
          padding: 0 30px 6px 0;
          &:last-child {
            padding-right: 0;
          }
        }
      }
    }
    .sluice-analyse-box {
      display: flex;
      align-items: center;
      height: 100%;
      .sluice-analyse-item {
        padding: 0 30px;
        border-right: 1px solid #00baff;
        &:last-child {
          border-right: none;
        }
        > div:first-child {
          font-size: 18px;
          font-weight: 400;
          color: #00baff;
          padding-bottom: 12px;
        }
        > div {
          display: flex;
          align-items: center;
          > div {
            display: flex;
            align-items: center;
            padding-right: 20px;
            color: #ffffff;
            font-size: 15px;
            &:last-child {
              padding-right: 0;
            }
            > span:first-child {
              width: 15px;
              height: 15px;
              border-radius: 50%;
              margin-right: 5px;
              &.red_span {
                background-color: #ff0000;
              }
              &.gray_span {
                background-color: #777777;
              }
              &.yellow_span {
                background-color: #fcee27;
              }
              &.sky_blue_span {
                background-color: #2cb7d0;
              }
              &.dark_blue_span {
                background-color: #799dfc;
              }
            }
          }
        }
      }
    }
  }
}
</style>