package com.ygkj.lcsyn.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BhLgLmrDisDom {

    /**
     * id
     */
    private Integer id;

    /**
     * pid
     */
    private String pid;

    /**
     * d_year
     */
    private String dYear;

    /**
     * tm
     */
    private Date tm;

    /**
     * s_order_code
     */
    private String sOrderCode;

    /**
     * s_order
     */
    private String sOrder;

    /**
     * gate_open_tm
     */
    private Date gateOpenTm;

    /**
     * gate_close_tm
     */
    private Date gateCloseTm;

    /**
     * soc_outflow
     */
    private BigDecimal socOutflow;

    /**
     * soc_g_outflow
     */
    private BigDecimal socGOutflow;

    /**
     * soc_s_outflow
     */
    private BigDecimal socSOutflow;

    /**
     * soc_geflow
     */
    private BigDecimal socGeflow;

    /**
     * soc_wsflow
     */
    private BigDecimal socWsflow;

    /**
     * soc_iflow
     */
    private BigDecimal socIflow;

    /**
     * soc_otherflow
     */
    private BigDecimal socOtherflow;

    /**
     * soes_outflow
     */
    private BigDecimal soesOutflow;

    /**
     * soes_g_outflow
     */
    private BigDecimal soesGOutflow;

    /**
     * soes_s_outflow
     */
    private BigDecimal soesSOutflow;

    /**
     * soes_geflow
     */
    private BigDecimal soesGeflow;

    /**
     * soes_wsflow
     */
    private BigDecimal soesWsflow;

    /**
     * soes_iflow
     */
    private BigDecimal soesIflow;

    /**
     * soes_otherflow
     */
    private BigDecimal soesOtherflow;

    /**
     * is_upload_success
     */
    private String isUploadSuccess;

    /**
     * upload_errormsg
     */
    private String uploadErrormsg;

    /**
     * upload_key
     */
    private String uploadKey;

    /**
     * is_upload_success_order
     */
    private String isUploadSuccessOrder;

    /**
     * is_upload_success_actual
     */
    private String isUploadSuccessActual;

    /**
     * is_upload_success_open
     */
    private String isUploadSuccessOpen;

    /**
     * is_upload_success_close
     */
    private String isUploadSuccessClose;

    /**
     * is_upload_success_real
     */
    private String isUploadSuccessReal;

}
