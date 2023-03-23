<template>
  <div class="defec-box">
    <DefectTreatmentList @checkType="checkType" v-if="type == 1" />
    <DefectTreatmentInfo @checkType="checkType" v-if="type == 2" />
  </div>
</template>

<script>
import DefectTreatmentList from "./DefectTreatmentList.vue";
import DefectTreatmentInfo from "./DefectTreatmentInfo.vue";
export default {
  components: { DefectTreatmentList, DefectTreatmentInfo },
  data() {
    return {
      type: 1,
    };
  },
  methods: {
    checkType(type) {
      this.type = type;
    },
  },
};
</script>

<style lang="scss" scoped>
.defec-box {
  height: 100%;
}
</style>