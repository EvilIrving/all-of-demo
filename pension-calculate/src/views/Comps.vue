<template>
  <div class="comps-wrapper">
    <breadcrumb>
      <breadcrumb-item>1</breadcrumb-item>
      <breadcrumb-item>2</breadcrumb-item>
      <breadcrumb-item>3</breadcrumb-item>
      <breadcrumb-item>4</breadcrumb-item>
    </breadcrumb>
    <divider>🍎</divider>
    <!-- <divider content-position="left" direction="vertical">中</divider> -->
    <card class="box-card" style="width:400px">
      <h2>👌</h2>Card
    </card>
    <divider>🍎</divider>
    <button type="primary">click</button>
    <divider>🍎</divider>
    <divider>🍎</divider>
    <divider>🍎</divider>
    <divider>🍎</divider>
    <divider>🍎</divider>
    <divider>🍎</divider>
  </div>
</template>

<script>
import Card from '@/components/Card.vue';
import Divider from '@/components/Divider.vue';
import Breadcrumb from '@/components/breadcrumb/Breadcrumb.vue';
import BreadcrumbItem from '@/components/breadcrumb/BreadcrumbItem.vue';
import Button from '@/components/button/Button.vue';
export default {
  name: 'Comps',
  props: {

  },
  components: {
    Card,
    Divider,
    Breadcrumb,
    BreadcrumbItem,
    Button

  },
  data() {
    return {

    };
  },
  computed: {

  },
  watch: {

  },
  created() {

  },
  mounted() {

  },
  methods: {

  },
};
</script>

<style scoped lang="scss">
.comps-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
</style>
