import "./style.scss";

const TableTitle = {
	functional: true,
	name: "TableTitle",
	props: { title: { type: String, require: true }, hideLine: { type: Boolean, default: false } },
	render(h, context) {
		let { props, slots } = context;
		return (
			<div class="table-title">
				<div class="table-title__label">
					<span>{props.title}</span>
					<div class="table-title__append">{slots().append}</div>
				</div>
				{!props.hideLine && <div class="table-title__bottom" />}
			</div>
		);
	},
};

const FormItem = {
	functional: true,
	name: "FormItem",
	props: { label: String },
	render(h, context) {
		let { props, slots } = context;
		return (
			<div class="form-item">
				<div class="form-item__label">{props.label}</div>
				<div class="form-item__content">{slots().default}</div>
			</div>
		);
	},
};

const FormItemInline = {
	name: "FormItemInline",
	props: {
		label: String,
		unit: String,
		inputWidth: { type: Number, default: 64 },
		labelWidth: { type: [Number, String], default: 70 },
		customLabel: Boolean,
	},
	mounted() {
		this.calcInputWidth();
	},
	updated() {
		this.calcInputWidth();
	},
	methods: {
		calcInputWidth() {
			let { inputWidth, $slots } = this;
			const children = $slots.default.filter((child) => child.componentOptions?.Ctor?.extendOptions.name == "ElInput");
			children.forEach((child) => {
				child.elm.style = `width: ${inputWidth}px`;
			});
		},
	},
	render(h) {
		let { label, unit, labelWidth, $slots, customLabel } = this;
		const labelStyle = typeof labelWidth == "number" ? { width: `${labelWidth}px` } : { width: labelWidth };
		return (
			<div class="form-item-inline">
				{label && (
					<span class="form-item-inline__label" style={labelStyle}>
						{customLabel ? <span>{label}</span> : label}
					</span>
				)}
				<div class="form-item-inline__wrapper">
					<span class="form-item-inline__content">{$slots.default}</span>
					{unit && <span class="form-item-inline__unit">{unit}</span>}
				</div>
			</div>
		);
	},
};

export { TableTitle, FormItem, FormItemInline };
