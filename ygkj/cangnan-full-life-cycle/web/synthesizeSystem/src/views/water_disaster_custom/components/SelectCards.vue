<template>
	<div class="select-cards">
		<el-row class="search-area" :gutter="30">
			<el-col :span="4">
				<el-input v-model="searchName" placeholder="请输入您要查找的内容" @change="searchComponent"></el-input>
			</el-col>
			<el-col :span="20" class="cards-tabs">
				<div :class="['tab-item', tabActive == -1 ? 'active' : '']" @click="doSelectClick()">
					<p>已选</p>
					<p>{{ checkList.length }}</p>
				</div>
				<div
					v-for="(item, idx) in this.statisticList"
					:key="idx"
					:class="['tab-item', tabActive == idx ? 'active' : '']"
					@click="changeStatistic(item, idx)"
				>
					<p>{{ item.key }}</p>
					<p>{{ item.value }}</p>
				</div>
			</el-col>
		</el-row>
		<div class="card-box clearfix">
			<div
				class="card-item-box fl"
				v-for="(item, idx) in cardList"
				:key="idx"
				@click="selectCardComponent(item.unitName)"
			>
				<div class="card-tag">
					<i v-if="item.drill" class="icon-drill"></i>
					<i :class="item.per == 'one' ? 'icon-one-one' : 'icon-one-third'"></i>
				</div>
				<div :class="['card-item', checkList.includes(item.unitName) ? 'active' : '']">
					<i v-if="checkList.includes(item.unitName)" class="icon-card-selected"></i>
					<img :src="item.imgSrc" />
					<div class="card-title">{{ item.unitName }}</div>
					<div class="order-num">
						{{ checkList.indexOf(item.unitName) == -1 ? "" : checkList.indexOf(item.unitName) + 1 }}
					</div>
				</div>
			</div>
		</div>
	</div>
</template>
<script>
import { mapGetters, mapActions } from "vuex";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention/";
export default {
	name: "SelectCards",
	data() {
		return {
			checkList: [],
			searchName: "",
			statisticList: [],
			tabActive: 0,
			cardList: [],
			allList: [] //接口获取的组件列表
		};
	},
	computed: {
		...mapGetters(["SelectList", "CheckedArr"])
	},
	watch: {
		CheckedArr(val) {
			this.checkList = Array.from(val).filter(item => {
				return item != "报警";
			});
		}
	},
	mounted() {
		this.checkList = Array.from(this.CheckedArr).filter(item => {
			return item != "报警";
		});
		this.unitNumStatistic();
		this.listUnit("");
	},
	methods: {
		...mapActions([]),
		//搜索组件
		searchComponent() {
			this.cardList = this.allList.filter(item => {
				return item.unitName.indexOf(this.searchName) != -1;
			});
		},
		//已选tab选中
		doSelectClick() {
			this.tabActive = -1;
			this.cardList = this.allList.filter(item => {
				return this.checkList.includes(item.unitName);
			});
		},
		//统计tab切换
		changeStatistic(item, idx) {
			this.tabActive = idx;
			this.cardList = this.allList;
			this.listUnit(item.key);
		},
		//各个大屏组件统计
		async unitNumStatistic() {
			let res = await disasterPreventionApi.unitNumStatistic();
			if (res.code == 0) {
				this.statisticList = res.data;
			}
		},
		//查询大屏组件信息
		async listUnit(type) {
			const oneRegExp = /[^数据.{2}$]/; // 高度1卡片正则表达式
			const oneList = ["山洪预警", "实时降雨", "实时台风", "台风", "规划专题", "水研究专题", "资产管理"]; // 高度1卡片数组
			const drillList = ["水库", "山洪预警", "实时降雨", "实时台风", "台风"]; // 能演练的卡片
			const opt = { bsName: type == "全部" ? "" : type };
			let res = await disasterPreventionApi.listUnit(opt);
			if (res.code == 0) {
				if (res.data.list) {
					res.data.list.map(item => {
						if (oneList.includes(item.unitName) || item.unitName.match(oneRegExp)) {
							item.per = "one";
						}
						if (drillList.includes(item.unitName)) {
							item.drill = true;
						}
					});
				}
				this.allList = res.data.list;
				this.allList.map(item => {
					item.imgSrc = require("@/assets/images/component_card_img/" + item.unitName + ".png");
				});
				this.cardList = this.allList;
			}
		},
		//选中组件卡片
		selectCardComponent(item) {
			let idx = this.checkList.indexOf(item);
			if (idx != -1) {
				this.checkList.splice(idx, 1);
			} else {
				this.checkList.push(item);
			}
		}
	}
};
</script>

<style lang="scss" scoped>
.fl {
	float: left;
}
.select-cards {
	width: 100%;
	height: 600px;
	@include flexbox;
	@include flexflow(column, nowrap);
	.search-area {
		@include flexbox;
		@include flexAC;
		.cards-tabs {
			@include flexbox;
			@include flexAC;
			.tab-item {
				padding: 16px;
				margin-right: 12px;
				cursor: pointer;
				&.active {
					border-top: 2px solid #4eebe6;
					background-image: linear-gradient(180deg, #1cfff533 0%, #0000 100%);
				}
				p {
					font-size: 16px;
					color: #ffffff;
					letter-spacing: 0;
					text-align: center;
					line-height: 24px;
				}
			}
		}
	}
	.card-box {
		@include flex(1);
		overflow-y: auto;
		padding: 16px 0;
		.card-item-box {
			width: 280px;
			height: 230px;
			position: relative;
			margin: 0 20px 20px 8px;
			.card-tag {
				position: absolute;
				right: 8px;
				top: 6px;
				z-index: 10;
			}
		}
		.card-item {
			width: 280px;
			height: 230px;
			position: relative;
			border-radius: 4px;
			opacity: 0.6;
			> i {
				position: absolute;
				right: 0;
				top: 0;
				z-index: 100;
			}
			img {
				width: 280px;
				height: 230px;
				position: absolute;
				top: 0;
				left: 0;
				border-radius: 6px;
			}
			.card-title {
				width: 100%;
				height: 64px;
				background-image: linear-gradient(180deg, rgba(0, 0, 0, 0.27) 0%, #000000 50%);
				border-radius: 0 0 4px 6px;
				color: #fff;
				z-index: 1;
				position: absolute;
				bottom: 0;
				left: 0;
				font-size: 20px;
				letter-spacing: 0;
				line-height: 32px;
				@include flexbox;
				@include flexAC;
				@include flexJC(center);
			}
			&.active {
				opacity: 1;
				box-shadow: #1ef8f2 0 0 6px 2px;
				.order-num {
					width: 50px;
					height: 64px;
					border-radius: 0 0 4px 4px;
					bottom: 0;
					position: absolute;
					left: 0;
					@include flexbox;
					@include flexAC;
					@include flexJC(center);
					font-size: 32px;
					color: #1ef8f2;
					z-index: 2;
					background-image: linear-gradient(270deg, rgba(0, 255, 246, 0) 0%, #1ef8f1d8 99%);
				}
			}

			&.high-height {
				height: 736px;
			}
		}
	}

	::v-deep .el-checkbox-group {
		width: 100%;
		@include flexbox;
	}
}
</style>
