import { Colors } from "./CONST";
export const handelColor = function (color) {
  return Colors.includes(color) ? `var(--${color})` : color;
}