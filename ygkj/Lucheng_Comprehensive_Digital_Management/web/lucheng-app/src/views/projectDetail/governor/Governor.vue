<!--
 * @Author: hanyu
 * @LastEditTime: 2021-11-18 11:10:30
 * @Description: 督
 * @FilePath: /cangnan-app/src/views/projectDetail/governor/Governor.vue
-->
<template>
  <div class="wrap">
    <section class="pg_section">
      <p class="section_title">历史督查信息</p>
      <div style="padding: 8px 0 0 0" @click="showPicker = true">
        {{ value ? value : "督查状态" }} <van-icon name="arrow-down" />
      </div>
      <van-popup v-model="showPicker" position="bottom">
        <van-picker
          show-toolbar
          :columns="columns"
          @confirm="onConfirm"
          @cancel="showPicker = false"
        />
      </van-popup>
      <div class="pg_box">
        <div
          class="pg_item"
          v-for="(item, index) in timelineArr"
          :key="item.id"
        >
          <div class="pg_left">
            <h3>{{ item.superviseTime1 }}</h3>
            <p>{{ item.superviseTime2 }}</p>
            <img src="../../../assets/images/earth.png" alt="" />
            <div v-if="index != timelineArr.length - 1"></div>
          </div>
          <div class="pg_right">
            <!-- <div class="status">{{ item.superviseState }}</div> -->
            <p class="right_title">督查信息</p>
            <img
              class="diagnosis_status"
              :src="item.superviseState == '已完结' ? done : undo"
              :alt="'督查状态' + item.superviseState"
            />
            <template>
              <div class="right_item">
                <p>督查时间：</p>
                <span>{{ item.superviseTime1 }} {{ item.superviseTime2 }}</span>
              </div>
              <div class="right_item">
                <p>问题描述：</p>
                <span>{{ item.question || "-" }}</span>
              </div>
              <div class="right_item">
                <p class="small_label">问题附件：</p>
                <template v-if="item.questionUrls.length > 0">
                  <img
                    v-for="(sitem, sindex) in item.questionUrls"
                    :key="sindex"
                    class="info-file"
                    :src="sitem"
                    alt=""
                    @click="preview(sitem)"
                  />
                </template>
                <span v-else>无</span>
              </div>
              <div class="right_item">
                <p>督查人员：</p>
                <span>{{ item.supervisePeople || "-" }}</span>
              </div>
            </template>
            <!-- <div class="head_item">
              <img src="../../../assets/images/head.png" alt="" />
            </div> -->
            <p class="right_title right_title_last">整改信息</p>
            <template v-if="item.superviseState == '已完结'">
              <div class="right_item">
                <p>整改时间：</p>
                <span>{{ item.rectificationTime || "-" }}</span>
              </div>
              <div class="right_item">
                <p>整改说明：</p>
                <span>{{ item.solveDesc || "-" }}</span>
              </div>
              <div class="right_item">
                <p class="small_label">整改附件：</p>
                <template v-if="item.solveUrls.length > 0">
                  <img
                    v-for="(sitem, sindex) in item.solveUrls"
                    :key="sindex"
                    class="info-file"
                    :src="sitem"
                    alt=""
                    @click="preview(sitem)"
                  />
                </template>
                <span v-else>无</span>
              </div>
              <div class="right_item">
                <p>整改人员：</p>
                <span>{{ item.rectificationPeople || "-" }}</span>
              </div>
            </template>
            <p v-else class="right_item yellow">
              暂无整改信息，请监督相关整改人员立即整改
            </p>
          </div>
        </div>
      </div>
    </section>
    <section class="btn_section">
      <div @click="doDc()">一键督查</div>
      <div @click="goEdit()">新增督查</div>
    </section>
    <van-popup v-model="isShow" class="popup_box">
      <h3>提示</h3>
      <p>{{ msg }}</p>
      <div @click="isShow = false">确定</div>
    </van-popup>
  </div>
</template>

<script>
import { busCodeSupervisionApi } from "@/api/projectDetail";

export default {
  name: "",
  props: {},
  components: {},
  data() {
    return {
      isShow: false,
      value: "",
      timelineArr: [],
      msg: "",
      showPicker: false,
      columns: ["全部", "未处置", "已办结"],
      undo: require(`@/assets/images/diagnosis-yellow.png`),
      done: require(`@/assets/images/diagnosis-green.png`),
    };
  },
  mounted() {
    this._loadPage();
  },
  methods: {
    async _loadPage() {
      let res = await busCodeSupervisionApi.loadPage({
        projectId: this.$localData("get", "projectCode"),
        superviseState:
          this.value == "未处置"
            ? "已下发"
            : this.value == "已办结"
            ? "已完结"
            : "",
      });
      if (res.code == 0) {
        res.data.list.map((item) => {
          item.solveUrls = item.solveUrl ? item.solveUrl.split(",") : [];
          item.questionUrls = item.question_url
            ? item.question_url.split(",")
            : [];
        });
        this.timelineArr = res.data.list;
        // this.timelineArr = res.data.list;
        if (res.data.list.length) {
          res.data.list.map((v) => {
            v.superviseTime1 = v.superviseTime
              ? v.superviseTime.split(" ")[0].slice(5)
              : "";
            v.superviseTime2 = v.superviseTime
              ? v.superviseTime.split(" ")[1]
              : "";
          });
        }
      }
    },
    goEdit() {
      this.$router.push("/projectIndex/governorForm");
    },
    async doDc() {
      let res = await busCodeSupervisionApi.doSupercision({
        id: this.$localData("get", "projectCode"),
      });
      if (res.code == 0) {
        this.msg = res.data;
      }
      this.isShow = true;
    },
    onConfirm(value) {
      this.value = value;
      this._loadPage();
      this.showPicker = false;
    },
    preview(url) {
      if (url) {
        window.open(url, "_blank");
      } else {
        this.$message.warning("暂无附件");
      }
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  width: 100%;
  height: 100%;
  .section_title {
    font-size: 20px;
    color: #1f1f1f;
    font-weight: 500;
  }
  .pg_section {
    padding: 12px 24px;
    height: calc(100% - 92px);
    overflow: auto;
    .pg_box {
      .pg_item {
        @include flexbox();
        @include flexJC(space-between);
        @include flexAI(stretch);
        // padding: 24px 0;
        margin-top: 24px;
        .pg_left {
          @include flex(1);
          h3 {
            font-size: 14px;
            color: #646464;
            letter-spacing: 1px;
            font-weight: 500;
            line-height: 20px;
          }
          p {
            font-size: 14px;
            color: #999999;
            letter-spacing: 0.78px;
            font-weight: 400;
            line-height: 20px;
            margin: 6px 0 10px 0;
          }
          img {
          }
          div {
            height: calc(100% - 70px);
            border-left: 1px #979797 dashed;
            margin-left: 7px;
            margin-top: 5px;
          }
        }
        .pg_right {
          position: relative;
          @include flex(3);
          background: #f4f7fb;
          border-radius: 12px;
          padding: 12px 16px;

          .diagnosis_status {
            position: absolute;
            right: 0;
            top: 6px;
            width: 76px;
            height: 66px;
            img {
              height: 100%;
              width: 100%;
            }
          }
          .right_title {
            font-size: 16px;
            color: #1f1f1f;
            font-weight: bold;
            margin: 8px 0;
            &_last {
              margin-top: 16px;
            }
          }
          .yellow {
            font-size: 14px;
            color: #f69500;
            line-height: 18px;
          }
          .status {
            width: 72px;
            height: 24px;
            background: #036eff;
            border-radius: 4px;
            text-align: center;
            line-height: 24px;
            color: #fff;
            font-size: 14px;
            letter-spacing: 1px;
            font-weight: 400;
          }
          .right_item {
            @include flexbox();
            margin-top: 8px;
            p {
              width: 78px;
              font-size: 14px;
              color: #646464;
              letter-spacing: 1px;
              font-weight: 400;
              line-height: 18px;
              &.small_label {
                width: unset;
              }
            }
            span {
              display: block;
              width: calc(100% - 78px);
              font-size: 14px;
              color: #646464;
              letter-spacing: 1px;
              font-weight: 400;
              line-height: 18px;
            }
            img {
            }
          }
          .head_item {
            padding-top: 8px;
          }
        }
      }
    }
  }
  .btn_section {
    // height: ;
    background: #fff;
    padding: 12px 24px;
    @include flexbox();
    @include flexJC(space-between);
    & > div {
      height: 42px;
      width: 156px;
      border-radius: 24px;
      font-size: 20px;
      letter-spacing: 0;
      text-align: center;
      font-weight: 400;
      line-height: 42px;
      &:first-child {
        border: 1px solid #036eff;
        color: #036eff;
      }
      &:last-child {
        background: #036eff;
        color: #fff;
      }
    }
  }
  .popup_box {
    background: #fff;
    width: 90%;
    border-radius: 16px;
    text-align: center;
    padding: 24px;
    box-sizing: border-box;
    h3 {
      font-size: 24px;
      color: #036eff;
      text-align: center;
      font-weight: 600;
    }
    p {
      font-size: 20px;
      color: #1f1f1f;
      font-weight: 500;
      margin: 16px 0 24px 0;
      line-height: 28px;
      text-align: left;
      text-indent: 40px;
    }
    div {
      display: inline-block;
      background: #036eff;
      border-radius: 21px;
      width: 156px;
      height: 42px;
      font-size: 20px;
      color: #ffffff;
      text-align: center;
      line-height: 42px;
    }
  }
}
</style>