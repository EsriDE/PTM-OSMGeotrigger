//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.16 at 10:53:52 AM CEST 
//


package de.esri.osm.config;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


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
 *         &lt;element name="query" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="overpass"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="arcgis"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                             &lt;element name="featureClass" type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 *                             &lt;element name="login"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;simpleContent&gt;
 *                                   &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *                                     &lt;attribute name="user" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                                     &lt;attribute name="password" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                                   &lt;/extension&gt;
 *                                 &lt;/simpleContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="matching"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="fields"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="field" maxOccurs="unbounded" minOccurs="0"&gt;
 *                                         &lt;complexType&gt;
 *                                           &lt;simpleContent&gt;
 *                                             &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *                                               &lt;attribute name="tag" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                                               &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                                             &lt;/extension&gt;
 *                                           &lt;/simpleContent&gt;
 *                                         &lt;/complexType&gt;
 *                                       &lt;/element&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
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
    "query"
})
@XmlRootElement(name = "configuration")
public class Configuration {

    protected List<Configuration.Query> query;

    /**
     * Gets the value of the query property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the query property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQuery().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Configuration.Query }
     * 
     * 
     */
    public List<Configuration.Query> getQuery() {
        if (query == null) {
            query = new ArrayList<Configuration.Query>();
        }
        return this.query;
    }


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
     *         &lt;element name="overpass"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="arcgis"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="featureClass" type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
     *                   &lt;element name="login"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;simpleContent&gt;
     *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
     *                           &lt;attribute name="user" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                           &lt;attribute name="password" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                         &lt;/extension&gt;
     *                       &lt;/simpleContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="matching"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="fields"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="field" maxOccurs="unbounded" minOccurs="0"&gt;
     *                               &lt;complexType&gt;
     *                                 &lt;simpleContent&gt;
     *                                   &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
     *                                     &lt;attribute name="tag" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                                     &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *                                   &lt;/extension&gt;
     *                                 &lt;/simpleContent&gt;
     *                               &lt;/complexType&gt;
     *                             &lt;/element&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
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
        "overpass",
        "arcgis",
        "matching"
    })
    public static class Query {

        @XmlElement(required = true)
        protected Configuration.Query.Overpass overpass;
        @XmlElement(required = true)
        protected Configuration.Query.Arcgis arcgis;
        @XmlElement(required = true)
        protected Configuration.Query.Matching matching;

        /**
         * Gets the value of the overpass property.
         * 
         * @return
         *     possible object is
         *     {@link Configuration.Query.Overpass }
         *     
         */
        public Configuration.Query.Overpass getOverpass() {
            return overpass;
        }

        /**
         * Sets the value of the overpass property.
         * 
         * @param value
         *     allowed object is
         *     {@link Configuration.Query.Overpass }
         *     
         */
        public void setOverpass(Configuration.Query.Overpass value) {
            this.overpass = value;
        }

        /**
         * Gets the value of the arcgis property.
         * 
         * @return
         *     possible object is
         *     {@link Configuration.Query.Arcgis }
         *     
         */
        public Configuration.Query.Arcgis getArcgis() {
            return arcgis;
        }

        /**
         * Sets the value of the arcgis property.
         * 
         * @param value
         *     allowed object is
         *     {@link Configuration.Query.Arcgis }
         *     
         */
        public void setArcgis(Configuration.Query.Arcgis value) {
            this.arcgis = value;
        }

        /**
         * Gets the value of the matching property.
         * 
         * @return
         *     possible object is
         *     {@link Configuration.Query.Matching }
         *     
         */
        public Configuration.Query.Matching getMatching() {
            return matching;
        }

        /**
         * Sets the value of the matching property.
         * 
         * @param value
         *     allowed object is
         *     {@link Configuration.Query.Matching }
         *     
         */
        public void setMatching(Configuration.Query.Matching value) {
            this.matching = value;
        }


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
         *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="featureClass" type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
         *         &lt;element name="login"&gt;
         *           &lt;complexType&gt;
         *             &lt;simpleContent&gt;
         *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
         *                 &lt;attribute name="user" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *                 &lt;attribute name="password" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *               &lt;/extension&gt;
         *             &lt;/simpleContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
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
            "type",
            "featureClass",
            "login"
        })
        public static class Arcgis {

            @XmlElement(required = true)
            protected String type;
            @XmlElement(required = true)
            @XmlSchemaType(name = "anyURI")
            protected String featureClass;
            @XmlElement(required = true)
            protected Configuration.Query.Arcgis.Login login;

            /**
             * Gets the value of the type property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getType() {
                return type;
            }

            /**
             * Sets the value of the type property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setType(String value) {
                this.type = value;
            }

            /**
             * Gets the value of the featureClass property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getFeatureClass() {
                return featureClass;
            }

            /**
             * Sets the value of the featureClass property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setFeatureClass(String value) {
                this.featureClass = value;
            }

            /**
             * Gets the value of the login property.
             * 
             * @return
             *     possible object is
             *     {@link Configuration.Query.Arcgis.Login }
             *     
             */
            public Configuration.Query.Arcgis.Login getLogin() {
                return login;
            }

            /**
             * Sets the value of the login property.
             * 
             * @param value
             *     allowed object is
             *     {@link Configuration.Query.Arcgis.Login }
             *     
             */
            public void setLogin(Configuration.Query.Arcgis.Login value) {
                this.login = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType&gt;
             *   &lt;simpleContent&gt;
             *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
             *       &lt;attribute name="user" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
             *       &lt;attribute name="password" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
             *     &lt;/extension&gt;
             *   &lt;/simpleContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "value"
            })
            public static class Login {

                @XmlValue
                protected String value;
                @XmlAttribute(name = "user")
                protected String user;
                @XmlAttribute(name = "password")
                protected String password;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setValue(String value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the user property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getUser() {
                    return user;
                }

                /**
                 * Sets the value of the user property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setUser(String value) {
                    this.user = value;
                }

                /**
                 * Gets the value of the password property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPassword() {
                    return password;
                }

                /**
                 * Sets the value of the password property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPassword(String value) {
                    this.password = value;
                }

            }

        }


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
         *         &lt;element name="fields"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="field" maxOccurs="unbounded" minOccurs="0"&gt;
         *                     &lt;complexType&gt;
         *                       &lt;simpleContent&gt;
         *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
         *                           &lt;attribute name="tag" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
         *                         &lt;/extension&gt;
         *                       &lt;/simpleContent&gt;
         *                     &lt;/complexType&gt;
         *                   &lt;/element&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
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
            "fields"
        })
        public static class Matching {

            @XmlElement(required = true)
            protected Configuration.Query.Matching.Fields fields;

            /**
             * Gets the value of the fields property.
             * 
             * @return
             *     possible object is
             *     {@link Configuration.Query.Matching.Fields }
             *     
             */
            public Configuration.Query.Matching.Fields getFields() {
                return fields;
            }

            /**
             * Sets the value of the fields property.
             * 
             * @param value
             *     allowed object is
             *     {@link Configuration.Query.Matching.Fields }
             *     
             */
            public void setFields(Configuration.Query.Matching.Fields value) {
                this.fields = value;
            }


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
             *         &lt;element name="field" maxOccurs="unbounded" minOccurs="0"&gt;
             *           &lt;complexType&gt;
             *             &lt;simpleContent&gt;
             *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
             *                 &lt;attribute name="tag" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
             *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
             *               &lt;/extension&gt;
             *             &lt;/simpleContent&gt;
             *           &lt;/complexType&gt;
             *         &lt;/element&gt;
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
                "field"
            })
            public static class Fields {

                protected List<Configuration.Query.Matching.Fields.Field> field;

                /**
                 * Gets the value of the field property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the field property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getField().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Configuration.Query.Matching.Fields.Field }
                 * 
                 * 
                 */
                public List<Configuration.Query.Matching.Fields.Field> getField() {
                    if (field == null) {
                        field = new ArrayList<Configuration.Query.Matching.Fields.Field>();
                    }
                    return this.field;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType&gt;
                 *   &lt;simpleContent&gt;
                 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
                 *       &lt;attribute name="tag" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
                 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
                 *     &lt;/extension&gt;
                 *   &lt;/simpleContent&gt;
                 * &lt;/complexType&gt;
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "value"
                })
                public static class Field {

                    @XmlValue
                    protected String value;
                    @XmlAttribute(name = "tag")
                    protected String tag;
                    @XmlAttribute(name = "name")
                    protected String name;

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getValue() {
                        return value;
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setValue(String value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the tag property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getTag() {
                        return tag;
                    }

                    /**
                     * Sets the value of the tag property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setTag(String value) {
                        this.tag = value;
                    }

                    /**
                     * Gets the value of the name property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getName() {
                        return name;
                    }

                    /**
                     * Sets the value of the name property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setName(String value) {
                        this.name = value;
                    }

                }

            }

        }


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
         *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
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
            "url"
        })
        public static class Overpass {

            @XmlElement(required = true)
            @XmlSchemaType(name = "anyURI")
            protected String url;

            /**
             * Gets the value of the url property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getUrl() {
                return url;
            }

            /**
             * Sets the value of the url property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setUrl(String value) {
                this.url = value;
            }

        }

    }

}
