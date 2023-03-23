<script>
//@ts-checked
import DailyPatrol from "./DailyPatrol.vue";
import SuperviseConfig from "./SuperviseConfig.vue";
import LawTeam from "./LawTeam.vue";
import SuperviseConfiguration from "./SuperviseConfiguration.vue";
import SuperviseIdentification from "./SuperviseIdentification.vue";
// /**
//  * @typedef {import('./index').InspectionItem} InspectionItem
//  */
export default {
  props: {
    inspection: { type: Object, default: () => ({}) },
    time: Number,
    index: Number,
  },
  components: {
    DailyPatrol,
    SuperviseConfig,
    LawTeam,
    SuperviseConfiguration,
    SuperviseIdentification,
  },
  data() {
    return {
      /** @type {{content:string,size:string,type:string,icon:string,color:string,timestamp:number}[]} */
      timeline: [],
      activeItem: "",
    };
  },
  mounted() {
    this.getTimeline();
  },
  computed: {
    showDailyPatrol() {
      return this.activeItem == "日常项目监管";
    },
    showSuperviseConfig() {
      return "复核监管项鉴定" == this.activeItem;
    },
    showLawTeam() {
      const reg = /第\d+次监管完成/;
      return this.activeItem == "移交执法大队" || reg.test(this.activeItem);
    },
    jurisdictionShow() {
      return {
        日常监管项配置: "1",
        日常项目监管: "2",
        复核监管项配置: "3",
        复核监管项鉴定: "4",
        复核巡查鉴定: "5",
        移交执法大队: "6",
      }[this.activeItem];
    },
  },
  methods: {
    /** @param {string} content */
    addTimeItem(content) {
      const item = { content, size: "large", type: "primary" };
      this.timeline.push(item);
    },
    onTimelineItemClick(content) {
      this.activeItem = content;
    },
    getTimelineClass(activity) {
      return this.activeItem == activity.content ? "active item" : "item";
    },
    getTimeline() {
      const finish = () => {
        const name = `第${this.time}次监管完成`;
        this.addTimeItem(name);
        this.activeItem = name;
      };
      // /** @type {InspectionItem} */
      const inspection = this.inspection;
      this.addTimeItem("日常项目监管");
      if (inspection.patrolResult === null) {
        this.activeItem = "日常项目监管";
      } else if (inspection.patrolResult === 0) {
        // 无异常 视为监管完成
        finish();
      } else if (inspection.patrolResult === 1) {
        // 有异常
        if (inspection.reviewResult === null && !inspection.reviewer) {
          // 未配置派发复核监管项
          this.addTimeItem("复核监管项配置");
          this.activeItem = "复核监管项配置";
        } else if (inspection.reviewer && inspection.reviewResult === null) {
          // 复核已派发 还未做处理
          this.addTimeItem("复核监管项鉴定");
          this.activeItem = "复核监管项鉴定";
        } else if (inspection.reviewResult === 1) {
          //  复核已派发 复合巡查结果异常
          this.addTimeItem("复核巡查鉴定");
          this.activeItem = "复核巡查鉴定";
        }

        if (inspection.reviewResult === 0) {
          //复核已派发 复合巡查结果正常
          finish();
          return;
        }
        if (inspection.hiddenDanger == 1 && inspection.resultStatus == 0) {
          //确认是隐患
          this.addTimeItem("移交执法大队");
          this.activeItem = "移交执法大队";
        }
        if (inspection.resultStatus == 1) {
          finish();
        }
      }
    },
  },
};
</script>
<template>
  <div class="inspection">
    <div class="inspection-timeline">
      <p>流程节点</p>
      <div class="timeline-wrap">
        <el-timeline>
          <el-timeline-item
            v-for="(item, index) in timeline"
            v-bind="item"
            :key="index"
          >
            <div
              :class="getTimelineClass(item)"
              @click="onTimelineItemClick(item.content)"
            >
              {{ item.content }}
            </div>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>
    <div class="inspection-content">
      <SuperviseConfiguration
        v-if="activeItem == '复核监管项配置'"
        :inspection="inspection"
      />
      <SuperviseIdentification
        v-if="activeItem == '复核巡查鉴定'"
        :inspection="inspection"
      />
      <DailyPatrol
        v-if="showDailyPatrol"
        :commonalityId="inspection"
        :selectTab="index + ''"
      />
      <SuperviseConfig
        v-if="showSuperviseConfig"
        :inspection="inspection"
        @detail="$emit('detail')"
      />
      <LawTeam
        v-if="showLawTeam"
        :commonalityId="inspection"
        :selectTab="index + ''"
        @detail="$emit('detail')"
      />
    </div>
  </div>
</template>

<style lang="scss" scoped>
.inspection {
  height: 100%;
  display: flex;
  overflow: hidden;
  .inspection-timeline {
    width: 16%;
    height: 100%;
    overflow-y: auto;
    background: #fff;
    border: 1px solid rgba(94, 91, 91, 0.2);
    > p {
      padding: 15px;
      font-size: 16px;
      font-weight: 600;
    }
    .timeline-wrap {
      padding-left: 25px;
      padding-top: 20px;
      ::v-deep {
        .el-timeline-item__node {
          width: 15px;
          height: 15px;
        }

        .el-timeline-item__tail {
          position: absolute;
          left: 0.25rem;
          height: 100%;
          border-left: 0.125rem solid #1492ff;
        }
        .el-timeline-item__content {
          cursor: pointer;
          position: relative;
          top: -9px;
          .item {
            height: 40px;
            line-height: 40px;
            width: 160px;
            padding-left: 10px;
            &.active {
              color: #fff;
              background: #1492ff;

              border-radius: 5px;
            }
          }
        }
      }
    }
  }
  .inspection-content {
    margin-left: 16px;
    width: 84%;
    height: 100%;
  }
}
</style>