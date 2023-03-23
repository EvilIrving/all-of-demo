
package com.pcm.ws.jws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>itfSmsOption complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="itfSmsOption">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="extCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hasSms" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="isFlashMsg" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="isLongMsg" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="isReceipt" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="needMO" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="routeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "itfSmsOption", propOrder = {
    "content",
    "extCode",
    "hasSms",
    "isFlashMsg",
    "isLongMsg",
    "isReceipt",
    "needMO",
    "routeName"
})
public class ItfSmsOption {

    protected String content;
    protected String extCode;
    protected boolean hasSms;
    protected boolean isFlashMsg;
    protected boolean isLongMsg;
    protected boolean isReceipt;
    protected boolean needMO;
    protected String routeName;

    /**
     * ��ȡcontent���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * ����content���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
    }

    /**
     * ��ȡextCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtCode() {
        return extCode;
    }

    /**
     * ����extCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtCode(String value) {
        this.extCode = value;
    }

    /**
     * ��ȡhasSms���Ե�ֵ��
     * 
     */
    public boolean isHasSms() {
        return hasSms;
    }

    /**
     * ����hasSms���Ե�ֵ��
     * 
     */
    public void setHasSms(boolean value) {
        this.hasSms = value;
    }

    /**
     * ��ȡisFlashMsg���Ե�ֵ��
     * 
     */
    public boolean isIsFlashMsg() {
        return isFlashMsg;
    }

    /**
     * ����isFlashMsg���Ե�ֵ��
     * 
     */
    public void setIsFlashMsg(boolean value) {
        this.isFlashMsg = value;
    }

    /**
     * ��ȡisLongMsg���Ե�ֵ��
     * 
     */
    public boolean isIsLongMsg() {
        return isLongMsg;
    }

    /**
     * ����isLongMsg���Ե�ֵ��
     * 
     */
    public void setIsLongMsg(boolean value) {
        this.isLongMsg = value;
    }

    /**
     * ��ȡisReceipt���Ե�ֵ��
     * 
     */
    public boolean isIsReceipt() {
        return isReceipt;
    }

    /**
     * ����isReceipt���Ե�ֵ��
     * 
     */
    public void setIsReceipt(boolean value) {
        this.isReceipt = value;
    }

    /**
     * ��ȡneedMO���Ե�ֵ��
     * 
     */
    public boolean isNeedMO() {
        return needMO;
    }

    /**
     * ����needMO���Ե�ֵ��
     * 
     */
    public void setNeedMO(boolean value) {
        this.needMO = value;
    }

    /**
     * ��ȡrouteName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRouteName() {
        return routeName;
    }

    /**
     * ����routeName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRouteName(String value) {
        this.routeName = value;
    }

}
