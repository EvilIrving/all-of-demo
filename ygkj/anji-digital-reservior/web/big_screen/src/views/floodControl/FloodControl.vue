<!--
 * @Author: dtb
 * @LastEditTime: 2021-10-11 11:29:35
 * @Description: 
 * @FilePath: \big_screen\src\views\floodControl\FloodControl.vue
-->
<template>
  <div class="wrap flex-box">
    <section class="left">
      <div class="box_item warp-box">
        <h2 class="warp-title">天气预报</h2>
        <div class="weather_pieces" >
          <div class="piece" v-for="(item,index) in weathersList" :key="index">
            <p class="piece_title">{{item.name}}</p>
            <div class="pieces_img">
              <img src="@/assets/image/weather-sun.png" alt="" /><img
                src="@/assets/image/weather-rain.png"
                alt=""
              />
            </div>
            <p class="piece_title">{{item.status}}</p>
          </div>
        </div>
        <div class="rainfall">
          <span>100mm</span>
          <span>100mm</span>
          <span>100mm</span>
        </div>
         <div class="rainfall">
          <span>预报降雨量</span>
          <span>100mm</span>
          <span>100mm</span>
         
        </div>
      </div>
      <div class="box_item warp-box" style="height:500px"><h2 class="warp-title">水情预报</h2></div>
    </section>
    <section class="right">
      <div class="box_item warp-box">
        <h2 class="warp-title">放水信息</h2>
        <div class="pieces">
          <div class="piece" v-for="(item,index) in waterRealesList" :key="index">
            <p class="piece_title">{{ item.name }}</p>
            <img
              v-if="item.id == 1"
              src="@/assets/image/water-release-circle.png"
              alt=""
            />
          </div>
        </div>
      </div>
      <div class="box_item warp-box">
        <h2 class="warp-title">下游预警</h2>
        <div class="strips">
          <div class="strip" v-for="(item,index) in warningList" :key="index">
            <span>{{item.name}}</span><span>{{item.status}}</span>
            <div></div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: "floodControl",
  props: {},
  data() {
    return {
      waterRealesList:[{name:'老虎湾',id:1},{name:'老虎湾',id:1},{name:'老虎湾',id:1},{name:'老虎湾',id:1},{name:'老虎湾',id:1},{name:'老虎湾',id:1},],
      warningList:[{name:'老虎湾',status:'已完成'},{name:'老虎湾',status:'已完成'},{name:'老虎湾',status:'已完成'},{name:'老虎湾',status:'已完成'},{name:'老虎湾',status:'已完成'},],
      weathersList:[{name:'今天',status:'多云转小雨'},{name:'今天',status:'多云转小雨'},{name:'今天',status:'多云转小雨'},]
    };
  },
  computed: {},
  created() {},
  mounted() {},
  watch: {},
  methods: {},
  components: {},
};
</script>

<style lang="scss" scoped>
.left,
.right {
  padding-top: 62px;
  width: 300px;
 
}
.flex-box {
  justify-content: space-between;
  box-sizing: border-box;
}
.left {
}
.right {
}
.box_item {
  margin-bottom: 20px;
  width: 100%;
  > h2 {
    // padding-top: 20px;
  }
  .weather_pieces,
  .pieces,
  .strips {
    width: 86.88%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-flow: row wrap;
    margin: 20px auto;
  }
  .weather_pieces .piece{
    width: 30%;
    padding: 10px 0;
    box-sizing: border-box;
    display: flex;
    justify-content: space-around;
    align-items: center;
    flex-flow: column wrap;
    p{
      font-size: 10px;
      color: #c0c4cc;
    }
    .pieces_img{
       padding: 10px 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    img{
      width: 35%;

    }
    }
  }
  .rainfall{
  width: 86.88%;
    display: flex;
    justify-content: space-around;
    align-items: center;
    padding-bottom: 20px;
    margin: 0 auto;
    span{
      width:80px;
      font-size: 14px;
      color: #ffffff;
      letter-spacing: 1px;
    }
  }
   .pieces .piece {
    width: 45%;
    height: 130px;
    padding: 10px 0;
    box-sizing: border-box;
    margin-bottom: 20px;
    // background: linear-gradient(179deg, #334269 0%, #43f7e2 100%);
    // box-shadow: 0px 0px 100px 0px #47e6f4;
    border: 1px solid #00fbff;
    display: flex;
    opacity: 0.55;
    justify-content: space-around;
    align-items: center;
    flex-flow: column wrap;
    .piece_title {
      text-align: center;
      height: 30px;
      font-size: 16px;
      font-weight: 500;
      color: #ffffff;
      line-height: 30px;
    }
    img {
      text-align: center;
      width: 60px;
      height: 60px;
    }
  }
  .strip {
    display: flex;
    justify-content: space-around;
    align-items: center;
    width: 100%;
    height: 25px;
    margin-bottom: 10px;
    // background: linear-gradient(90deg, #00fbff 0%, #224d63 100%);
    opacity: 0.55;
    border: 1px solid;
    border-image: linear-gradient(
        64deg,
        rgba(0, 251, 255, 1),
        rgba(33, 76, 98, 1)
      )
      1 1;
    span {
      font-size: 14px;
      font-weight: 500;
    }
    & > span:nth-child(1) {
      color: #00fbff;
    }
    & > span:nth-child(2) {
      color: #c0c4cc;
    }
    & > div {
      width: 11px;
      height: 11px;
      background: #39ef0f;
      border-radius: 50%;
    }
  }
}
</style>
