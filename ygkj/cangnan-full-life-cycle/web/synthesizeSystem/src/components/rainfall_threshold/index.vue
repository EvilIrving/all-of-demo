<template>
    <ul class="cloudy-wrap">
        <li v-for="(item,index) in list" :key="index">
            <i :class=item.icon></i>
            <div>
                <p>{{item.label}}</p>
                <p>{{item.value}}</p>
            </div>
        </li>
    </ul>
</template>
<script>
export default {
    name:'RainfallThreshold',
    props:{
        list: Array,
    },
    data () {
        return {
          typeSelect:'pic'
        };
    },
    components: {

    },
    mounted() {

    },
    methods: {
    }
}
</script>
<style lang="scss">
// 小云彩 
.cloudy-wrap{
    width: 100%;
    @include flexbox;
    @include flexflow(row, wrap);
    color: #fff;
    li{
        @include flexbox;
        width: 50%;
        height: 40px;
        margin: 12px 0;
        >i{
            height: 40px;
            width: 44px;
            margin-right: 14px;
        }
        >div{
            @include flexbox;
            @include flexJC(space-between);
            flex-direction: column;
        }
    }
}
</style>