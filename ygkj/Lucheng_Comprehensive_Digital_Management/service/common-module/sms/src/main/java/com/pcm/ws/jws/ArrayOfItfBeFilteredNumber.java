
package com.pcm.ws.jws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfItfBeFilteredNumber complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="ArrayOfItfBeFilteredNumber">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="itfBeFilteredNumbers" type="{http://v1.ws.api.service.yxt.gooben.com/}itfBeFilteredNumber" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfItfBeFilteredNumber", propOrder = {
    "itfBeFilteredNumbers"
})
public class ArrayOfItfBeFilteredNumber {

    @XmlElement(nillable = true)
    protected List<ItfBeFilteredNumber> itfBeFilteredNumbers;

    /**
     * Gets the value of the itfBeFilteredNumbers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itfBeFilteredNumbers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItfBeFilteredNumbers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ItfBeFilteredNumber }
     * 
     * 
     */
    public List<ItfBeFilteredNumber> getItfBeFilteredNumbers() {
        if (itfBeFilteredNumbers == null) {
            itfBeFilteredNumbers = new ArrayList<ItfBeFilteredNumber>();
        }
        return this.itfBeFilteredNumbers;
    }

}
