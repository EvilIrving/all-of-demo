<template>
  <div class="info-box">
    <div class="info-left">
      <div class="panel-info">
        <div class="panel-info-title">显示信息</div>
        <div class="panel-info-check">
          <el-checkbox-group v-model="activeArr" @change="handleCheckBox">
            <el-checkbox
              class="panel-info-check-option"
              v-for="(item, index) in checkedList"
              :label="item"
              :key="index"
              >{{ item }}</el-checkbox
            >
          </el-checkbox-group>
        </div>
      </div>
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
      <div class="info-right-item" v-for="(item, index) in infoList" :key="index">
        <div class="item-title">{{ item.name }}</div>
        <div>
          <span :class="item.className">{{ item.num }}</span
          >座
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapMutations, mapGetters } from 'vuex';
export default {
  computed: {
    ...mapGetters(['infoDetail', 'sluiceInfoList'])
  },
  watch: {
    sluiceInfoList: {
      handler: function (val) {
        if (val && this.sluiceActiveArr.length > 0) {
          this.infoList = val;
        }
      },
      deep: true
    },
    infoDetail: {
      handler: function (val) {
        if (val) {
          this.infoList = [
            {
              name: '水库报警',
              className: 'warn-span',
              num: val.skbj
            },
            {
              name: '水闸报警',
              className: 'warn-span',
              num: val.szbj
            },
            {
              name: '测站报警',
              className: 'warn-span',
              num: val.czbj
            },
            {
              name: '水闸开启',
              className: 'open-span',
              num: val.szkq
            }
          ];
        }
      },
      deep: true
    }
  },
  data() {
    return {
      checkedList: ['名称', '水位', '流量'],
      activeArr: ['名称'],
      sluiceList: ['控运分析', '负荷分析', '排水分析'],
      sluiceActiveArr: [],
      checkType: false,
      legendList: [
        {
          title: '水库',
          status: [
            {
              color: '#00FFA4',
              name: '正常'
            },
            {
              color: '#F7B500',
              name: '超汛限'
            }
          ]
        },
        {
          title: '测站',
          status: [
            {
              color: '#00FFA4',
              name: '正常'
            },
            {
              color: '#F7B500',
              name: '超汛限'
            }
          ]
        },
        {
          title: '水闸',
          status: [
            {
              color: '#00C1FF',
              name: '开启'
            },
            {
              color: '#999999',
              name: '关闭'
            },
            {
              color: '#F7B500',
              name: '超警戒'
            }
          ]
        }
      ],
      sluiceLegendList: [
        {
          title: '水闸',
          status: []
        }
      ],
      infoList: []
    };
  },
  created() {
    this.setCheckInfo(this.activeArr);
    this.setLegendList(this.legendList);
    this.infoList = [
      {
        name: '水库报警',
        className: 'warn-span',
        num: 25
      },
      {
        name: '水闸报警',
        className: 'warn-span',
        num: 25
      },
      {
        name: '测站报警',
        className: 'warn-span',
        num: 25
      },
      {
        name: '水闸开启',
        className: 'open-span',
        num: 25
      }
    ];
  },
  methods: {
    ...mapMutations({
      setCheckInfo: 'SET_CHECK_INFO',
      setLegendList: 'SET_LEGEND_LIST',
      setSluiceActiveArr: 'SET_SLUICE_ACTIVE_ARR',
    }),
    handleCheckBox() {
      this.setCheckInfo(this.activeArr);
    },
    handleSluiceCheckBox() {
      if (this.sluiceActiveArr.length > 0) {
        let data = this.sluiceActiveArr[this.sluiceActiveArr.length - 1];
        this.sluiceActiveArr = [data];
        switch (data) {
          case '控运分析':
            this.sluiceLegendList[0].status = [
              {
                color: '#00FFA4',
                name: '正常'
              },
              {
                color: '#999999',
                name: '无控运'
              },
              {
                color: '#F7B500',
                name: '超控运'
              }
            ];
            break;
          case '负荷分析':
            this.sluiceLegendList[0].status = [
              {
                color: '#00FFA4',
                name: '正常'
              },
              {
                color: '#999999',
                name: '无数据'
              },
              {
                color: '#FF0200',
                name: '满负荷'
              },
              {
                color: '#F7B500',
                name: '接近满负荷'
              }
            ];
            break;
          default: {
            this.sluiceLegendList[0].status = [
              {
                color: '#00FFA4',
                name: '正常'
              },
              {
                color: '#FF0200',
                name: '强排水'
              }
            ];

            break;
          }
        }
        this.setLegendList(this.sluiceLegendList);
      } else {
        this.setLegendList(this.legendList);
        this.infoList = [
          {
            name: '水库报警',
            className: 'warn-span',
            num: this.infoDetail.skbj
          },
          {
            name: '水闸报警',
            className: 'warn-span',
            num: this.infoDetail.szbj
          },
          {
            name: '测站报警',
            className: 'warn-span',
            num: this.infoDetail.czbj
          },
          {
            name: '水闸开启',
            className: 'open-span',
            num: this.infoDetail.szkq
          }
        ];
      }
      this.setSluiceActiveArr(this.sluiceActiveArr)
    }
  }
};
</script>

<style lang="scss" scoped>
.info-box {
  display: flex;
  height: 100%;
  .info-left {
    padding: 0 31px;
    height: 100%;
    border-right: 1px solid rgba(0, 186, 255, 0.39);
    display: flex;
    justify-content: space-between;
    align-items: center;
    .panel-info {
      padding-right: 32px;
      border-right: 1px solid rgba(0, 186, 255, 0.39);
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
      &:last-child {
        padding-left: 31px;
        padding-right: 0px;
        border-right: none;
      }
    }
  }
  .info-right {
    display: flex;
    align-items: center;
    height: 100%;
    .info-right-item {
      padding: 0 39px 0 31px;
      border-right: 1px solid rgba(0, 186, 255, 0.39);
      &:last-child {
        border-right: none;
        padding-right: 0;
      }
      > div {
        font-size: 16px;
        color: #fff;
        &.item-title {
          padding-bottom: 5px;
        }
        span {
          font-size: 32px;
          padding-right: 15px;
          &.warn-span {
            color: #fcee27;
          }
          &.open-span {
            color: #23e3fb;
          }
          &.normal-span {
            color: #47f5a7;
          }
          &.error-span {
            color: #ff0200;
          }
        }
      }
    }
  }
}
</style>