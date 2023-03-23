
package com.pcm.ws.jws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>itfCallOption complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="itfCallOption">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="callContentType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hasCall" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="ivrCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="retryInterval" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="retryNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
@XmlType(name = "itfCallOption", propOrder = {
    "callContentType",
    "content",
    "hasCall",
    "ivrCode",
    "retryInterval",
    "retryNumber",
    "routeName"
})
public class ItfCallOption {

    protected int callContentType;
    protected String content;
    protected boolean hasCall;
    protected String ivrCode;
    protected int retryInterval;
    protected int retryNumber;
    protected String routeName;

    public int getCallContentType() {
        return callContentType;
    }

    public void setCallContentType(int value) {
        this.callContentType = value;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String value) {
        this.content = value;
    }

    public boolean isHasCall() {
        return hasCall;
    }

    public void setHasCall(boolean value) {
        this.hasCall = value;
    }

    public String getIvrCode() {
        return ivrCode;
    }

    public void setIvrCode(String value) {
        this.ivrCode = value;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    /**
     * 设置retryInterval属性的值。
     * 
     */
    public void setRetryInterval(int value) {
        this.retryInterval = value;
    }

    /**
     * 获取retryNumber属性的值。
     * 
     */
    public int getRetryNumber() {
        return retryNumber;
    }

    /**
     * 设置retryNumber属性的值。
     * 
     */
    public void setRetryNumber(int value) {
        this.retryNumber = value;
    }

    /**
     * 获取routeName属性的值。
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
     * 设置routeName属性的值。
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
