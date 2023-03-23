
package com.pcm.ws.jws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfItfSentResultSmsWs complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="ArrayOfItfSentResultSmsWs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ItfSentResultSmsWs" type="{http://v1.ws.api.service.yxt.gooben.com/}ItfSentResultSmsWs" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfItfSentResultSmsWs", propOrder = {
    "itfSentResultSmsWs"
})
public class ArrayOfItfSentResultSmsWs {

    @XmlElement(name = "ItfSentResultSmsWs", nillable = true)
    protected List<ItfSentResultSmsWs> itfSentResultSmsWs;

    /**
     * Gets the value of the itfSentResultSmsWs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itfSentResultSmsWs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItfSentResultSmsWs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ItfSentResultSmsWs }
     * 
     * 
     */
    public List<ItfSentResultSmsWs> getItfSentResultSmsWs() {
        if (itfSentResultSmsWs == null) {
            itfSentResultSmsWs = new ArrayList<ItfSentResultSmsWs>();
        }
        return this.itfSentResultSmsWs;
    }

}
