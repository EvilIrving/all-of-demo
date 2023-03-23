<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
let themeClass = "default";
// import "@/assets/custom-theme/index.css"; // the theme changed version element-ui css
export default {
  name: "App",
  created() {
    //console.log(99999);
    // const head = document.head || document.getElementsByTagName("head")[0];
    // const link = document.createElement("link");
    // link.rel = "stylesheet";
    // link.type = "text/css";
    // link.href = `../public/${themeClass}/index.css`;
    // head.appendChild(link);
    // require(`@/assets/${themeClass}/index.css`);
    // document.body.className = themeClass;
    window.document.documentElement.setAttribute("data-theme", themeClass);
  },
};
</script>
