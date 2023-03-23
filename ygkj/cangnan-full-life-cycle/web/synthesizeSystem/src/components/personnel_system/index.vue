<template>
    <ul class="person-wrap">
        <li v-for="(item,index) in list" :key="index">
            <img :src="item.icon ? item.icon : personDefault" alt="">
            <div>
                <p>
                    <strong>{{item.name}}</strong>
                    <span :class="item.leader ? 'yellow' : 'green'">{{item.leader ? '组长' : '组员'}}</span>
                </p>
                <p>电话： {{item.phone}}</p>
            </div>
        </li>
    </ul>
</template>
<script>
export default {
    name:'PersonnelSystem',
    props:{
        list: Array,
    },
    data () {
        return {
            personDefault: require("@/assets/images/person-default.png"),
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
<style lang="scss" scoped>
// 人员体系
.person-wrap{
    width: 100%;
    color: rgba(255,255,255,0.85);
    >li{
        @include flexbox;
        width: 100%;
        height: 88px;
        margin-bottom: 16px;
        img{
            height: 90px;
            margin-right: 16px;
        }
        >div{
            @include flexbox;
            @include flexJC(space-around);
            flex-direction: column;
            strong{
                font-size: 24px;
                font-weight: 400;
                margin-right: 16px;
            }
            span{
                font-size: 16px;
            }
            .yellow{
                color: #FBA240;
            }
            .green{
                color: #32BCB4;
            }
        }
        &:last-child{
            margin-bottom: 0;
        }
    }
}
</style>