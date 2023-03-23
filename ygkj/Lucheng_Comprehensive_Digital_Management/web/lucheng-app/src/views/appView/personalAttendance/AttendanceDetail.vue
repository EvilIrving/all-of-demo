<template>
  <div class="project-wrap">
    <van-nav-bar
      fixed
      placeholder
      safe-area-inset-top
      :title="'人员考勤详情'"
      :left-arrow="$store.state.viewType !== 'irs'"
      @click-left="$router.go(-1)"
    />
    <div class="table_wrap">
      <table-list :theadList="theadList" :list="tableData">
        <template v-slot:attendanceRate="item"
          ><span>{{ item.data.attendanceRate }}%</span></template
        >
      </table-list>
    </div>
  </div>
</template>

<script>
import {staffAttendance} from "@/api/appView";
import TableList from "@/components/tableList";

export default {
  name: "AttendanceDetail",
  props: {},
  components: {TableList},
  data() {
    return {
      cname: "",
      theadList: [
        {
          label: "姓名",
          prop: "name",
          slot: false,
        },
        {
          label: "应到打卡天数",
          prop: "needNum",
          slot: false,
        },
        {
          label: "实到打卡天数",
          prop: "attendanceNum",
          slot: false,
        },
        {
          label: "考勤到岗率",
          prop: "attendanceRate",
          slot: true,
        },
      ],
      tableData:{},
    };
  },
  mounted() {
    this.addZwlog(this)
    this.cname = this.$route.query.code;
    this.getList()
  },
  methods: {
    async getList() {
      let res = await staffAttendance.attendanceDetail({
        corporationName:this.cname
      })
      console.log(res)
      if(res.code == 0){
        this.tableData = res.data
      }
    }

  },
};
</script>

<style scoped></style>
