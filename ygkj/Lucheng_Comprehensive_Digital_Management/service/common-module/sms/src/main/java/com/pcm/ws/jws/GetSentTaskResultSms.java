
package com.pcm.ws.jws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>getSentTaskResultSms complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="getSentTaskResultSms">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="taskSmsId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSentTaskResultSms", propOrder = {
    "taskSmsId"
})
public class GetSentTaskResultSms {

    protected long taskSmsId;

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

}
