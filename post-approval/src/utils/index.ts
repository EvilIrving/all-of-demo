const numberReg = /^\d+$/;
/**
 * 对传入的字符串转换成适合样式的值
 * @param unit
 * @returns
 */
export function fixToPixels(unit: string | number): string | undefined {
  if (typeof unit === 'string') {
    if (!unit.includes('%') && !unit.includes('rem') && !unit.includes('px') && numberReg.test(unit)) {
      return `${unit}px`;
    } else return unit;
  } else if (typeof unit === 'number') {
    return `${unit}px`;
  }
}
