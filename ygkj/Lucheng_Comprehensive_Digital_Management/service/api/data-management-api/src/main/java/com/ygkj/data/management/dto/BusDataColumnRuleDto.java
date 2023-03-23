package com.ygkj.data.management.dto;

import com.google.common.base.Converter;
import com.ygkj.data.management.model.BusDataTableColumn;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

@NoArgsConstructor
public class BusDataColumnRuleDto {

    private String columnId;

    private List<BusDataRuleDto> rules;

    private Boolean hasRule = Boolean.FALSE;

    public BusDataColumnRuleDto(String columnId, List<BusDataRuleDto> rules) {
        this.columnId = columnId;
        this.rules = rules;
        if (!rules.isEmpty()) {
            hasRule = Boolean.TRUE;
        }
    }

    public String getColumnId() {
        return columnId;
    }

    public List<BusDataRuleDto> getRules() {
        return rules;
    }

    public Boolean getHasRule() {
        return hasRule;
    }

    public static class ColumnEntityConverter extends Converter<BusDataColumnRuleDto, BusDataTableColumn> {
        @Override
        protected BusDataTableColumn doForward(BusDataColumnRuleDto busDataColumnRuleDto) {
            BusDataTableColumn column = new BusDataTableColumn();
            BeanUtils.copyProperties(busDataColumnRuleDto, column);
            column.setId(busDataColumnRuleDto.getColumnId());
            return column;
        }

        @Override
        protected BusDataColumnRuleDto doBackward(BusDataTableColumn busDataTableColumn) {
            throw new ClassCastException("不允许逆向转换！");
        }
    }
}
