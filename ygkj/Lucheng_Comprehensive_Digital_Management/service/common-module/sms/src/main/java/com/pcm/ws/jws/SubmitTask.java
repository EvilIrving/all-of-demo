
package com.pcm.ws.jws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>submitTask complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="submitTask">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="details" type="{http://v1.ws.api.service.yxt.gooben.com/}itfSendDetail" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="atTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="itfOptions" type="{http://v1.ws.api.service.yxt.gooben.com/}itfOptions" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "submitTask", propOrder = {
    "details",
    "atTime",
    "itfOptions"
})
public class SubmitTask {

    protected List<ItfSendDetail> details;
    protected String atTime;
    protected ItfOptions itfOptions;

    /**
     * Gets the value of the details property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the details property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ItfSendDetail }
     * 
     * 
     */
    public List<ItfSendDetail> getDetails() {
        if (details == null) {
            details = new ArrayList<ItfSendDetail>();
        }
        return this.details;
    }

    /**
     * 获取atTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAtTime() {
        return atTime;
    }

    /**
     * 设置atTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAtTime(String value) {
        this.atTime = value;
    }

    /**
     * 获取itfOptions属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ItfOptions }
     *     
     */
    public ItfOptions getItfOptions() {
        return itfOptions;
    }

    /**
     * 设置itfOptions属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ItfOptions }
     *     
     */
    public void setItfOptions(ItfOptions value) {
        this.itfOptions = value;
    }

}
