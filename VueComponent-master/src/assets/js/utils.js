import { Colors } from "./const";
export const handelColor = function (color) {
  return Colors.includes(color) ? `var(--${color})` : color;
}