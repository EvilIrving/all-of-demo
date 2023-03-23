<!--
 * @Date: 2022-01-24 17:31:45
 * @Author: 
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2022-02-28 11:16:44
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenWorkbench\projectDetail\PerformDuties.vue
-->
<template>
  <div class="perform_wrap">
    <ul class="top_wrap">
      <li :class="tabIndex === index ? 'active' : ''" @click="chooesTabList(index)" v-for="(item,index) in menuList" :key="item.id">
        {{item.name}}
      </li>
    </ul>
    <div class="bottom_wrap">
      <div class="left_bottom" v-show="showLeft">
        <div v-for="(item, p_index) in administrativeList" :key="item.id" @click.stop="chooesSecondTitle(p_index, item.subset)">
          <p class="third_title">
            <img v-if="item.star" class="point" :src="$config.imgUrl+ 'staring.png'" alt="">
            <img v-else class="point" :src="$config.imgUrl + 'point.png'" alt="">
            <span :class="item.star || item.selected ? 'active' : ''">{{item.name}}</span>
            <i></i>
            <img class="arr" :src="$config.imgUrl+ 'arr.gif'" alt="">
          </p>
          <ul>
            <li :class="ite.selected ? 'active' : ''" @click.stop="chooesAdministrativeList(p_index, index, ite.id)" v-for="(ite,index) in item.subset" :key="ite.id">
              <i v-if="ite.star" class="staring"></i>
              <span class="name">{{ite.name}}</span>
            </li>
          </ul>
        </div>
      </div>
      <div class="right_bottom">
        <h2 class="perform_title">{{jobName}}履职</h2>
        <div class="line"></div>
        <ul class="">
          <li v-for="(item,index) in dutyDetail" :key="item.id">
            <span>{{index + 1}}. {{item.duty}}</span>
          </li>
        </ul>
      </div>
    </div>

  </div>
</template>

<script>
import { responsibilityListApi } from '@/api/api_reservoir'
export default {
  props: {

  },
  data() {
    return {
      tabIndex: 0,
      menuList: [],
      administrativeIndex: 0,
      administrativeList: [],
      dutyDetail: [],
      jobName: '',
      showLeft: true
    };
  },
  computed: {

  },
  created() {

  },
  mounted() {
    this.getTop()
  },
  watch: {

  },
  methods: {
    chooesTabList(index) {
      this.tabIndex = index
      this.getLeftTree(this.menuList[index].id, this.menuList[index].name)
      if (index !== 0) {
        console.log(this.administrativeList, 'this.administrativeList');
        // this.chooesSecondTitle(0,this.menuList[0])
      }
    },
    chooesSecondTitle(p_index, subset) {
      console.log(p_index, subset, 'p_index, subset');
      this.administrativeList.map(v => {
        v.selected = false
        if (v.subset.length) {
          v.subset.map(y => {
            y.selected = false
          })
        }
      })
      if (subset.length == 0) {
        this.jobName = this.administrativeList[p_index].name
        this.getDetail(this.administrativeList[p_index].id)
        this.administrativeList[p_index].selected = true
      } else {
        this.jobName = this.administrativeList[0].subset[0].name
        this.administrativeList[0].subset[0].selected = true
        this.getDetail(this.administrativeList[0].subset[0].id)
      }
    },
    chooesAdministrativeList(p_index, index, id) {
      this.jobName = this.administrativeList[p_index].subset[index].name
      this.administrativeList.map(v => {
        if (v.subset.length) {
          v.subset.map(y => {
            y.selected = false
          })
        }
      })
      this.administrativeList[p_index].subset[index].selected = true
      this.getDetail(id)
    },
    async getTop() {
      let res = await responsibilityListApi.dutyParent()
      if (res.success) {
        if (res.data.length) {
          this.menuList = res.data
          this.getLeftTree(res.data[0].id)
        } else {
          this.menuList = []
        }
      }
    },
    async getLeftTree(id, name) {
      let res = await responsibilityListApi.dutyTree({
        id: id
      })
      if (res.success) {
        if (res.data !== null) {
          this.showLeft = true
          if (res.data.length) {
            this.administrativeList = res.data
            if (this.tabIndex !== 0) {
              if (this.administrativeList[0].subset && this.administrativeList[0].subset.length) {
                this.jobName = this.administrativeList[0].subset[0].name
                this.administrativeList[0].subset[0].selected = true
                this.getDetail(this.administrativeList[0].subset[0].id)
              } else {
                this.jobName = this.administrativeList[0].name
                this.administrativeList[0].selected = true
                this.getDetail(this.administrativeList[0].id)
              }
            } else {
              if (res.data[0].subset && res.data[0].subset.length) {
                let tempIndex = this.administrativeList[0].subset.findIndex((item, index) => {
                  if (item.star) {
                    return index
                  }
                })
                this.jobName = res.data[0].subset[tempIndex].name
                this.administrativeList[0].subset[tempIndex].selected = true
                this.getDetail(res.data[0].subset[tempIndex].id)
              } else {
                this.getDetail(res.data[0].id)
              }
            }
          } else {
            this.administrativeList = []
          }
        } else {
          this.showLeft = false
          this.jobName = name
          this.getDetail(id)
        }
      }
    },
    async getDetail(id) {
      let res = await responsibilityListApi.dutyDetail({
        dutyId: id
      })
      if (res.success) {
        if (res.data.length) {
          this.dutyDetail = res.data
        } else {
          this.dutyDetail = []
        }
      }
    }
  },
  components: {

  },
};
</script>

<style scoped lang="scss">
.perform_wrap {
  width: 96%;
  box-sizing: border-box;
  margin: 0 auto;
  color: #ffffff;
  .bottom_wrap {
    display: flex;
    width: 100%;
    height: 580px;
    .left_bottom {
      height: 100%;
      overflow: auto;
      flex: 1;
      ul {
        width: 100%;
        li {
          width: 100%;
          height: 50px;
          margin: 8px 0;
          @include flexbox();
          @include flexAI(center);
          @include flexJC(center);
          background: center/100%
            url($imageUrl + "notSelected.png") no-repeat;
          &.active {
            background: center/100%
              url($imageUrl + "selected.png") no-repeat;
          }
          cursor: pointer;
          span.name {
            font-size: 16px;
            &.active {
              color: #00daff;
              font-size: 18px;
            }
          }
          i.staring {
            width: 20px;
            height: 20px;
            margin: 0 10px;
            background: center/100%
              url($imageUrl + "staring.png") no-repeat;
          }
        }
      }
    }
    .right_bottom {
      box-sizing: border-box;
      height: 100%;
      flex: 3;
      margin-left: 1%;
      background: #03338d;
      border: 2px solid #0051b5;
      color: #ffffff;
      padding: 10px 20px;
      .perform_title {
        font-size: 28px;
        font-weight: 500;
        color: #ffffff;
        line-height: 40px;
      }
      .line {
        width: 100%;
        height: 2px;
        background: #3390e1;
        opacity: 0.2;
        margin: 10px 0;
      }
      ul {
        height: calc(100% - 70px);
        overflow: auto;
        li {
          width: 100%;
          // height: 30px;
          background: #00051548;
          margin: 10px 0px;
          span {
            line-height: 30px;
            text-align: left;
            padding-left: 10px;
          }
        }
      }
    }
  }
}
.top_wrap {
  width: 100%;
  height: 40px;
  background: center/100%
    url($imageUrl + "work_title_bg.png") no-repeat;
  box-sizing: border-box;
  padding-left: 5px;
  padding-top: 5px;
  margin-bottom: 20px;
  & > img {
    width: 31px;
    height: 20px;
    vertical-align: bottom;
  }
  & > li {
    display: inline-block;
    font-size: 26px;
    font-family: "youshe";
    color: #8491b7;
    line-height: 10px;
    position: relative;
    margin-left: 10px;
    transition: all 0.1s linear;
    cursor: pointer;
    margin-bottom: 20px;
    &.active {
      font-size: 30px;
      font-family: "youshe";
      color: #fff;
      text-shadow: 0px 0px 10px #69e3ee;
    }

    span {
      position: absolute;
      top: -16px;
      right: -16px;
      display: block;
      width: 24px;
      height: 24px;
      background: #e02020;
      font-size: 14px;
      text-align: center;
      line-height: 24px;
      border-radius: 12px;
      color: #fff;
      font-family: none;
    }
  }
}
.third_title {
  @include flexbox();
  @include flexAI(center);
  @include flexJC(center);
  font-weight: bold;
  color: #fff;
  margin-top: 20px;
  .point {
    width: 20px;
    margin-right: 5px;
  }
  span {
    font-family: "youshe";
    font-size: 24px;
    cursor: pointer;
    user-select: none;
    &.unit {
      margin-left: 2%;
      font-family: "youshe";
      color: #69e3ee;
      font-size: 20px;
    }
    &.active {
      color: #00d9fd;
    }
  }
  i {
    display: block;
    height: 1px;
    @include flex(1);
    border-bottom: 1px solid #4c70a3;
    margin: 0 10px;
  }
  .arr {
    width: 30px;
  }
}
</style>
