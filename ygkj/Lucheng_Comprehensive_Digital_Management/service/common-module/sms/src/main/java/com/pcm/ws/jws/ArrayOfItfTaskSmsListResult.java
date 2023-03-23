
package com.pcm.ws.jws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfItfTaskSmsListResult complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="ArrayOfItfTaskSmsListResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="itfTaskSmsListResults" type="{http://v1.ws.api.service.yxt.gooben.com/}itfTaskSmsListResult" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfItfTaskSmsListResult", propOrder = {
    "itfTaskSmsListResults"
})
public class ArrayOfItfTaskSmsListResult {

    @XmlElement(nillable = true)
    protected List<ItfTaskSmsListResult> itfTaskSmsListResults;

    /**
     * Gets the value of the itfTaskSmsListResults property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itfTaskSmsListResults property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItfTaskSmsListResults().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ItfTaskSmsListResult }
     * 
     * 
     */
    public List<ItfTaskSmsListResult> getItfTaskSmsListResults() {
        if (itfTaskSmsListResults == null) {
            itfTaskSmsListResults = new ArrayList<ItfTaskSmsListResult>();
        }
        return this.itfTaskSmsListResults;
    }

}
