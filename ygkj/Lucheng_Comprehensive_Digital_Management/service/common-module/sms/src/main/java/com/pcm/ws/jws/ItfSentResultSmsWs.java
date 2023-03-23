
package com.pcm.ws.jws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ItfSentResultSmsWs complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="ItfSentResultSmsWs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="receiptResult" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="receiptResultCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="receiptResultName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="receiveNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resultDetails" type="{http://v1.ws.api.service.yxt.gooben.com/}ArrayOfItfTaskSmsListResult" minOccurs="0"/>
 *         &lt;element name="sendNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sentResult" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="sentResultCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sentResultName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transactionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItfSentResultSmsWs", propOrder = {
    "receiptResult",
    "receiptResultCode",
    "receiptResultName",
    "receiveNumber",
    "resultDetails",
    "sendNumber",
    "sentResult",
    "sentResultCode",
    "sentResultName",
    "transactionId"
})
public class ItfSentResultSmsWs {

    protected Integer receiptResult;
    protected String receiptResultCode;
    protected String receiptResultName;
    protected String receiveNumber;
    protected ArrayOfItfTaskSmsListResult resultDetails;
    protected String sendNumber;
    protected Integer sentResult;
    protected String sentResultCode;
    protected String sentResultName;
    protected String transactionId;

    /**
     * ��ȡreceiptResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getReceiptResult() {
        return receiptResult;
    }

    /**
     * ����receiptResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setReceiptResult(Integer value) {
        this.receiptResult = value;
    }

    /**
     * ��ȡreceiptResultCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiptResultCode() {
        return receiptResultCode;
    }

    /**
     * ����receiptResultCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiptResultCode(String value) {
        this.receiptResultCode = value;
    }

    /**
     * ��ȡreceiptResultName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiptResultName() {
        return receiptResultName;
    }

    /**
     * ����receiptResultName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiptResultName(String value) {
        this.receiptResultName = value;
    }

    /**
     * ��ȡreceiveNumber���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiveNumber() {
        return receiveNumber;
    }

    /**
     * ����receiveNumber���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiveNumber(String value) {
        this.receiveNumber = value;
    }

    /**
     * ��ȡresultDetails���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfItfTaskSmsListResult }
     *     
     */
    public ArrayOfItfTaskSmsListResult getResultDetails() {
        return resultDetails;
    }

    /**
     * ����resultDetails���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfItfTaskSmsListResult }
     *     
     */
    public void setResultDetails(ArrayOfItfTaskSmsListResult value) {
        this.resultDetails = value;
    }

    /**
     * ��ȡsendNumber���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendNumber() {
        return sendNumber;
    }

    /**
     * ����sendNumber���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendNumber(String value) {
        this.sendNumber = value;
    }

    /**
     * ��ȡsentResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSentResult() {
        return sentResult;
    }

    /**
     * ����sentResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSentResult(Integer value) {
        this.sentResult = value;
    }

    /**
     * ��ȡsentResultCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSentResultCode() {
        return sentResultCode;
    }

    /**
     * ����sentResultCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSentResultCode(String value) {
        this.sentResultCode = value;
    }

    /**
     * ��ȡsentResultName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSentResultName() {
        return sentResultName;
    }

    /**
     * ����sentResultName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSentResultName(String value) {
        this.sentResultName = value;
    }

    /**
     * ��ȡtransactionId���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * ����transactionId���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionId(String value) {
        this.transactionId = value;
    }

}
