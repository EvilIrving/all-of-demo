
package com.pcm.ws.jws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>getSentTaskResultCall complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="getSentTaskResultCall">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="taskCallId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSentTaskResultCall", propOrder = {
    "taskCallId"
})
public class GetSentTaskResultCall {

    protected long taskCallId;

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

}
