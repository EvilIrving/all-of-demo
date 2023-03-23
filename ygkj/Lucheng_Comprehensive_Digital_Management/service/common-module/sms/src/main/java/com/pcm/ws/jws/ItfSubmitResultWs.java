
package com.pcm.ws.jws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>itfSubmitResultWs complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="itfSubmitResultWs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="taskSmsId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="taskCallId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="beFilteredNumbers" type="{http://v1.ws.api.service.yxt.gooben.com/}ArrayOfItfBeFilteredNumber" minOccurs="0"/>
 *         &lt;element name="itfAcceptedDetailSmses" type="{http://v1.ws.api.service.yxt.gooben.com/}ArrayOfItfAcceptedDetail" minOccurs="0"/>
 *         &lt;element name="itfAcceptedDetailCalls" type="{http://v1.ws.api.service.yxt.gooben.com/}ArrayOfItfAcceptedDetail" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "itfSubmitResultWs", propOrder = {
    "taskSmsId",
    "taskCallId",
    "status",
    "beFilteredNumbers",
    "itfAcceptedDetailSmses",
    "itfAcceptedDetailCalls"
})
public class ItfSubmitResultWs {

    protected long taskSmsId;
    protected long taskCallId;
    protected int status;
    protected ArrayOfItfBeFilteredNumber beFilteredNumbers;
    protected ArrayOfItfAcceptedDetail itfAcceptedDetailSmses;
    protected ArrayOfItfAcceptedDetail itfAcceptedDetailCalls;

    /**
     * ��ȡtaskSmsId���Ե�ֵ��
     * 
     */
    public long getTaskSmsId() {
        return taskSmsId;
    }

    /**
     * ����taskSmsId���Ե�ֵ��
     * 
     */
    public void setTaskSmsId(long value) {
        this.taskSmsId = value;
    }

    /**
     * ��ȡtaskCallId���Ե�ֵ��
     * 
     */
    public long getTaskCallId() {
        return taskCallId;
    }

    /**
     * ����taskCallId���Ե�ֵ��
     * 
     */
    public void setTaskCallId(long value) {
        this.taskCallId = value;
    }

    /**
     * ��ȡstatus���Ե�ֵ��
     * 
     */
    public int getStatus() {
        return status;
    }

    /**
     * ����status���Ե�ֵ��
     * 
     */
    public void setStatus(int value) {
        this.status = value;
    }

    /**
     * ��ȡbeFilteredNumbers���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfItfBeFilteredNumber }
     *     
     */
    public ArrayOfItfBeFilteredNumber getBeFilteredNumbers() {
        return beFilteredNumbers;
    }

    /**
     * ����beFilteredNumbers���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfItfBeFilteredNumber }
     *     
     */
    public void setBeFilteredNumbers(ArrayOfItfBeFilteredNumber value) {
        this.beFilteredNumbers = value;
    }

    /**
     * ��ȡitfAcceptedDetailSmses���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfItfAcceptedDetail }
     *     
     */
    public ArrayOfItfAcceptedDetail getItfAcceptedDetailSmses() {
        return itfAcceptedDetailSmses;
    }

    /**
     * ����itfAcceptedDetailSmses���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfItfAcceptedDetail }
     *     
     */
    public void setItfAcceptedDetailSmses(ArrayOfItfAcceptedDetail value) {
        this.itfAcceptedDetailSmses = value;
    }

    /**
     * ��ȡitfAcceptedDetailCalls���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfItfAcceptedDetail }
     *     
     */
    public ArrayOfItfAcceptedDetail getItfAcceptedDetailCalls() {
        return itfAcceptedDetailCalls;
    }

    /**
     * ����itfAcceptedDetailCalls���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfItfAcceptedDetail }
     *     
     */
    public void setItfAcceptedDetailCalls(ArrayOfItfAcceptedDetail value) {
        this.itfAcceptedDetailCalls = value;
    }

}
