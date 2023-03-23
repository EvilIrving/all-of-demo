<template>
	<div ref="dragCard" class="drag-card collapse-panel__border">
		<div
			class="close-box"
			title="关闭"
			@click="closeCard()"
			@mouseout="hoverShow = false"
			@mouseover="hoverShow = true"
		>
			<i class="el-icon-close" v-show="hoverShow"></i>
		</div>
		<div class="collapse-panel__content" :style="cardStyle">
			<slot></slot>
		</div>
	</div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
export default {
	name: "DragCard",
	props: {
		isHigh: {
			type: Boolean,
			default: false
		},
		cardName: {
			type: String,
			default: ""
		}
	},
	data() {
		return {
			hoverShow: false,
			indexStyle: {}
		};
	},
	mounted() {
		this.SetTempZindex();
		this.$nextTick(() => {
			this.$refs.dragCard.style.zIndex = this.TempZIndex;
		});
	},
	computed: {
		...mapGetters(["CardHeight", "HighCardHeight", "CheckedArr", "TempZIndex"]),
		cardStyle() {
			let height = this.CardHeight;
			if (this.isHigh) {
				height = this.HighCardHeight;
			}
			return {
				height
			};
		}
	},
	methods: {
		...mapActions(["SetCheckedArr", "SetTempZindex"]),
		closeCard() {
			const layerName = this.$slots.default[0]?.componentOptions?.Ctor?.extendOptions?.layerName;
			const legendName = this.$slots.default[0]?.componentOptions?.Ctor?.extendOptions?.legendName;
			const previewModule = this.$slots.default[0]?.componentOptions?.Ctor?.extendOptions?.previewModule;
			this.$EventBus.$emit("clearLayer", { layerName, legendName, previewModule });
			if (this.cardName) {
				let arr = Array.from(this.CheckedArr);
				let idx = arr.findIndex(item => {
					return item == this.cardName;
				});
				arr.splice(idx, 1);
				this.SetCheckedArr(arr);
			}
		}
	}
};
</script>

<style lang="scss" scoped>
.collapse-panel__content {
	// border: 1px solid #00c1ff;
	box-shadow: inset 0 4px 30px 0 rgba(2, 51, 72, 0.88);
	background-color: rgba(2, 51, 72, 0.88);
	> div {
		height: 100%;
	}
	// background-image: linear-gradient(
	//   180deg,
	//   rgba(53, 149, 227, 0.4) 0%,
	//   rgba(25, 59, 197, 0.4) 100%
	// );
	width: 100%;
	height: 33.3%;
	// padding: 16px;
	display: flex;
	flex-direction: column;
	overflow: scroll;
	padding: 0 16px;
	// justify-content: space-between;
	::v-deep .panel-item__content {
		display: flex;
		flex-direction: column;
		&::-webkit-scrollbar {
			display: none;
		}
	}

	&::-webkit-scrollbar {
		display: none;
	}
}
// .collapse-panel__border {
//   width: 100%;
//   background: linear-gradient(to bottom, #35b9e3, #5398f6) left top no-repeat,
//     linear-gradient(to bottom, #5398f6, #608aff) left top no-repeat,
//     linear-gradient(to bottom, #35b9e3, #5398f6) right top no-repeat,
//     linear-gradient(to bottom, #5398f6, #608aff) right top no-repeat,
//     linear-gradient(to bottom, #5398f6, #35b9e3) left bottom no-repeat,
//     linear-gradient(to bottom, #608aff, #5398f6) left bottom no-repeat,
//     linear-gradient(to bottom, #5398f6, #35b9e3) right bottom no-repeat,
//     linear-gradient(to bottom, #608aff, #5398f6) right bottom no-repeat;
//   /*设置大小*/
//   background-size: 18px 2px, 2px 18px;
//   padding: 2px;
// }
.drag-card {
	z-index: 1000;
	width: 400px;
	color: #fff;
	position: relative;
	border-radius: 6px;
	.close-box {
		position: absolute;
		width: 20px;
		height: 20px;
		right: 4px;
		top: 8px;
		cursor: pointer;
	}
}
</style>
