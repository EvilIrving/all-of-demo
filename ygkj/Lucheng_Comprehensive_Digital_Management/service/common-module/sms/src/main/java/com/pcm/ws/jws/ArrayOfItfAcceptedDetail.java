
package com.pcm.ws.jws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfItfAcceptedDetail complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfItfAcceptedDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="itfAcceptedDetails" type="{http://v1.ws.api.service.yxt.gooben.com/}itfAcceptedDetail" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfItfAcceptedDetail", propOrder = {
    "itfAcceptedDetails"
})
public class ArrayOfItfAcceptedDetail {

    @XmlElement(nillable = true)
    protected List<ItfAcceptedDetail> itfAcceptedDetails;

    /**
     * Gets the value of the itfAcceptedDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itfAcceptedDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItfAcceptedDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ItfAcceptedDetail }
     * 
     * 
     */
    public List<ItfAcceptedDetail> getItfAcceptedDetails() {
        if (itfAcceptedDetails == null) {
            itfAcceptedDetails = new ArrayList<ItfAcceptedDetail>();
        }
        return this.itfAcceptedDetails;
    }

}
