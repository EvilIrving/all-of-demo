export default [
	{
		path: "/waterDisasterPrevention",
		name: "waterDisasterPrevention",
		meta: { title: "水灾害防御默认" },
		component: (resolve) => require(["@/views/waterDisasterPrevention/index.vue"], resolve),
	},
	{
		path: "/waterDisasterCustom",
		name: "waterDisasterCustom",
		meta: { title: "水灾害防御" },
		component: (resolve) => require(["@/views/water_disaster_custom/index_back.vue"], resolve),
	},
];
