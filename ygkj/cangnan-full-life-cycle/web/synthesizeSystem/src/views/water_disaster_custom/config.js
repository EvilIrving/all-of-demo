let cardConfigs = {
	数据治理: {
		component: "data-governance-card",
		cardProps: { isHigh: true }
	},
	数据责任: {
		component: "data-duty-card",
		cardProps: { isHigh: true }
	},
	数据分布: {
		component: "data-distributed-card",
		cardProps: { isHigh: true }
	},
	数据服务: {
		component: "data-service-card",
		cardProps: { isHigh: true }
	}
};

export function getCardConfig(h) {
	Object.keys(cardConfigs).forEach(cardName => {
		const configItem = cardConfigs[cardName];
		const tagName = configItem.component;
		cardConfigs[cardName] = { component: h(tagName), props: { key: tagName }, ...configItem };
	});
	return cardConfigs;
}
