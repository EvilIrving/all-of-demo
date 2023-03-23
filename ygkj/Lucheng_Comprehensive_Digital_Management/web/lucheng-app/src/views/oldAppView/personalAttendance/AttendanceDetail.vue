<template>
  <view-page>
    <div class="project-wrap">
      <div class="table_wrap">
        <table-list :theadList="theadList" :list="tableData">
          <template v-slot:attendanceRate="item"
          ><span>{{ item.data.attendanceRate }}%</span></template
          >
        </table-list>
      </div>
    </div>
  </view-page>
</template>

<script>
import { staffAttendance } from "@/api/appView";
import TableList from "@/components/tableList";

export default {
  name: "AttendanceDetail",
  props: {},
  components: { TableList },
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
          label: "应到",
          prop: "needNum",
          slot: false,
        },
        {
          label: "实到",
          prop: "attendanceNum",
          slot: false,
        },
        {
          label: "到岗率",
          prop: "attendanceRate",
          slot: true,
        },
      ],
      tableData: {},
    };
  },
  mounted() {
    this.addZwlog(this)
    this.cname = this.$route.query.code;
    this.getList();
  },
  methods: {
    async getList() {
      let res = await staffAttendance.attendanceDetail({
        corporationName: this.cname,
      });
      console.log(res);
      if (res.code == 0) {
        this.tableData = res.data;
      }
    },
  },
};
</script>

<style scoped></style>
