<template>
  <!--  工程档案-->
  <div class="project_wrap">
    <van-nav-bar fixed placeholder safe-area-inset-top :title="title" :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <div class="top_bar">
      <van-dropdown-menu style="width: 50%">
        <van-dropdown-item v-model="scaleValue" :options="scaleoptions" @change="getProList"></van-dropdown-item>
        <van-dropdown-item v-model="yearValue" :options="yearOptions" @change="getProList"></van-dropdown-item>
      </van-dropdown-menu>
      <van-field v-model="input" placeholder="请输入关键字" style="width: 50%;" @change="getProList" />
    </div>
    <div class="list_content">
      <van-list v-model="loading" :finished="finished" @load="getProList" v-if="list.length!==0">
        <van-cell v-for="(item,index) in list" :key="index">
          <div class="item" @click="getDetail(item.id)">
            <img :src="imgSrc" v-if="item.type=='folder'" />
            <div class="item_box">
              <div class="title">
                <span>{{item.name}}</span>
              </div>
              <span class="num">{{item.fileSize}} MB</span>
              <span class="time">{{item.createTime}} 创建</span>
            </div>
          </div>
        </van-cell>
      </van-list>

      <div class="no-data" v-if="list.length==0">
        <img :src="noData">
        <div>暂无数据</div>
      </div>

    </div>
  </div>
</template>

<script>

import { appFiles } from "@/api/appView";

export default {
  name: "Index",
  props: {},
  components: {

  },
  data() {
    return {
      scaleValue: '',
      scaleoptions: [
        { text: '全部工程', value: '' },
        { text: '水库', value: '水库' },
        { text: '水闸', value: '水闸' },
        { text: '海塘', value: '海塘' },
        { text: '堤防', value: '堤防' },
      ],
      yearValue: '',
      yearOptions: [
        { text: '年度', value: '' },
        { text: '2022', value: 2022 },
        { text: '2021', value: 2021 },
        { text: '2020', value: 2020 },
      ],
      input: '',
      loading: false,
      finished: true,
      finishedText: {
        //数据加载完成后的提示文案
        type: String,
        default: "",
      },
      imgSrc: require("@/assets/images/officeInformation/file.png"),
      list: [],
      type: '',
      noData: require("@/assets/images/officeInformation/no_data.png"),

    }
  },
  watch: {

  },
  mounted() {
    this.type = this.$route.query.type
    this.$route.query.type === '工程' ? this.title = '工程档案' : this.title = '物业档案'
    this.getProList()
  },
  methods: {
    async getProList() {
      var typeNum;
      if (this.type === '工程') {
        typeNum = 1
      } else {
        typeNum = 2
      }
      let res = await appFiles.getAppAchievement({
        achievementType: typeNum,
        year: this.yearValue,
        projectType: this.scaleValue,
        keyWord: this.input
      })
      if (res.code === 0) {
        this.list = res.data
        this.loading = false;
        this.finished = true;
      }
    },

    getDetail(id) {
      this.$router.push({
        path: '/projectDetail',
        query: {
          id: id,
          type: this.type
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
.project_wrap {
  width: 100%;
  height: 100%;
  .top_bar {
    background: #fbfbfb;
    height: 44px;
    display: flex;
    flex-direction: row;
  }
  .item {
    display: flex;
    flex-direction: row;
    font-size: 12px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 200;
    color: #666666;
    line-height: 16px;
    margin-bottom: 10px;
    margin-top: 10px;
    margin-left: 21px;
    img {
      width: 45px;
      height: 39px;
    }
    .title {
      font-size: 15px;
      font-family: SourceHanSansSC-Medium, SourceHanSansSC;
      font-weight: 250;
      color: #000000;
      margin-left: 12px;
      margin-bottom: 5px;
    }
    .num {
      margin-left: 12px;
      margin-right: 14px;
    }
  }
  .list_content {
    height: calc(100% - 120px);
    overflow: auto;
    .no-data {
      margin-top: 60px;
      margin-left: 129px;
      img {
        width: 117px;
        height: 114px;
        margin-bottom: 28px;
      }
      > div {
        width: 78px;
        height: 22px;
        font-size: 12px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 200;
        color: #999999;
        line-height: 22px;
        margin-left: 32px;
      }
    }
  }
}
</style>
<style lang="scss">
.project_wrap {
  .van-dropdown-menu__bar {
    height: 44px;
    box-shadow: none;
  }
}
</style>
