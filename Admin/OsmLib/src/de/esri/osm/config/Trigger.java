//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.16 at 03:35:11 PM CEST 
//


package de.esri.osm.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="direction" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="triggerID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="radius" type="{http://www.w3.org/2001/XMLSchema}float"/&gt;
 *         &lt;element name="tags" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element ref="{}notification"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "direction",
    "triggerID",
    "radius",
    "tags",
    "notification"
})
@XmlRootElement(name = "trigger")
public class Trigger {

    @XmlElement(required = true)
    protected String direction;
    @XmlElement(required = true)
    protected String triggerID;
    protected float radius;
    @XmlElement(required = true)
    protected String tags;
    @XmlElement(required = true)
    protected Notification notification;

    /**
     * Gets the value of the direction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the value of the direction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirection(String value) {
        this.direction = value;
    }

    /**
     * Gets the value of the triggerID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTriggerID() {
        return triggerID;
    }

    /**
     * Sets the value of the triggerID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTriggerID(String value) {
        this.triggerID = value;
    }

    /**
     * Gets the value of the radius property.
     * 
     */
    public float getRadius() {
        return radius;
    }

    /**
     * Sets the value of the radius property.
     * 
     */
    public void setRadius(float value) {
        this.radius = value;
    }

    /**
     * Gets the value of the tags property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTags() {
        return tags;
    }

    /**
     * Sets the value of the tags property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTags(String value) {
        this.tags = value;
    }

    /**
     * Gets the value of the notification property.
     * 
     * @return
     *     possible object is
     *     {@link Notification }
     *     
     */
    public Notification getNotification() {
        return notification;
    }

    /**
     * Sets the value of the notification property.
     * 
     * @param value
     *     allowed object is
     *     {@link Notification }
     *     
     */
    public void setNotification(Notification value) {
        this.notification = value;
    }

}