<script setup lang="ts">
import { ref, reactive, toRef, toRefs } from "vue";
let a = ref("heelo");

let arr = reactive([1, 2, 3]);

setTimeout(() => {
  a.value = "fdsfafaf";
}, 1000);
let bj = reactive({
  e: "1",
  c: "2",
  b: "2",
});
let c = toRef(bj, "c");
let { e, b } = toRefs(bj);
</script>


<template>
  <div>
    <h1>{{ a }}</h1>
  </div>

  <p v-for="(item,index) in arr" :key="index">
    {{ item }}
  </p>
</template>


<style scoped>
p {
  padding: 20px;
}
</style>

