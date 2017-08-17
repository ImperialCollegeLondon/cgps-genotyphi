//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2014.07.29 at 08:18:25 PM BST
//


package net.cgps.wgsa.genotyphi.lib.ncbi;

import javax.xml.bind.annotation.*;
import java.math.BigInteger;


/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Parameters_matrix" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Parameters_expect" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="Parameters_include" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="Parameters_sc-match" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="Parameters_sc-mismatch" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="Parameters_gap-open" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="Parameters_gap-extend" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="Parameters_filter" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Parameters_pattern" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Parameters_entrez-query" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "parametersMatrix",
    "parametersExpect",
    "parametersInclude",
    "parametersScMatch",
    "parametersScMismatch",
    "parametersGapOpen",
    "parametersGapExtend",
    "parametersFilter",
    "parametersPattern",
    "parametersEntrezQuery"
})
@XmlRootElement(name = "Parameters")
public class Parameters {

  @XmlElement(name = "Parameters_matrix")
  protected String parametersMatrix;
  @XmlElement(name = "Parameters_expect")
  protected double parametersExpect;
  @XmlElement(name = "Parameters_include")
  protected Double parametersInclude;
  @XmlElement(name = "Parameters_sc-match")
  protected BigInteger parametersScMatch;
  @XmlElement(name = "Parameters_sc-mismatch")
  protected BigInteger parametersScMismatch;
  @XmlElement(name = "Parameters_gap-open", required = true)
  protected BigInteger parametersGapOpen;
  @XmlElement(name = "Parameters_gap-extend", required = true)
  protected BigInteger parametersGapExtend;
  @XmlElement(name = "Parameters_filter")
  protected String parametersFilter;
  @XmlElement(name = "Parameters_pattern")
  protected String parametersPattern;
  @XmlElement(name = "Parameters_entrez-query")
  protected String parametersEntrezQuery;

  /**
   * Gets the value of the parametersMatrix property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getParametersMatrix() {

    return this.parametersMatrix;
  }

  /**
   * Sets the value of the parametersMatrix property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   */
  public void setParametersMatrix(String value) {

    this.parametersMatrix = value;
  }

  /**
   * Gets the value of the parametersExpect property.
   */
  public double getParametersExpect() {

    return this.parametersExpect;
  }

  /**
   * Sets the value of the parametersExpect property.
   */
  public void setParametersExpect(double value) {

    this.parametersExpect = value;
  }

  /**
   * Gets the value of the parametersInclude property.
   *
   * @return possible object is
   * {@link Double }
   */
  public Double getParametersInclude() {

    return this.parametersInclude;
  }

  /**
   * Sets the value of the parametersInclude property.
   *
   * @param value
   *     allowed object is
   *     {@link Double }
   */
  public void setParametersInclude(Double value) {

    this.parametersInclude = value;
  }

  /**
   * Gets the value of the parametersScMatch property.
   *
   * @return possible object is
   * {@link BigInteger }
   */
  public BigInteger getParametersScMatch() {

    return this.parametersScMatch;
  }

  /**
   * Sets the value of the parametersScMatch property.
   *
   * @param value
   *     allowed object is
   *     {@link BigInteger }
   */
  public void setParametersScMatch(BigInteger value) {

    this.parametersScMatch = value;
  }

  /**
   * Gets the value of the parametersScMismatch property.
   *
   * @return possible object is
   * {@link BigInteger }
   */
  public BigInteger getParametersScMismatch() {

    return this.parametersScMismatch;
  }

  /**
   * Sets the value of the parametersScMismatch property.
   *
   * @param value
   *     allowed object is
   *     {@link BigInteger }
   */
  public void setParametersScMismatch(BigInteger value) {

    this.parametersScMismatch = value;
  }

  /**
   * Gets the value of the parametersGapOpen property.
   *
   * @return possible object is
   * {@link BigInteger }
   */
  public BigInteger getParametersGapOpen() {

    return this.parametersGapOpen;
  }

  /**
   * Sets the value of the parametersGapOpen property.
   *
   * @param value
   *     allowed object is
   *     {@link BigInteger }
   */
  public void setParametersGapOpen(BigInteger value) {

    this.parametersGapOpen = value;
  }

  /**
   * Gets the value of the parametersGapExtend property.
   *
   * @return possible object is
   * {@link BigInteger }
   */
  public BigInteger getParametersGapExtend() {

    return this.parametersGapExtend;
  }

  /**
   * Sets the value of the parametersGapExtend property.
   *
   * @param value
   *     allowed object is
   *     {@link BigInteger }
   */
  public void setParametersGapExtend(BigInteger value) {

    this.parametersGapExtend = value;
  }

  /**
   * Gets the value of the parametersFilter property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getParametersFilter() {

    return this.parametersFilter;
  }

  /**
   * Sets the value of the parametersFilter property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   */
  public void setParametersFilter(String value) {

    this.parametersFilter = value;
  }

  /**
   * Gets the value of the parametersPattern property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getParametersPattern() {

    return this.parametersPattern;
  }

  /**
   * Sets the value of the parametersPattern property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   */
  public void setParametersPattern(String value) {

    this.parametersPattern = value;
  }

  /**
   * Gets the value of the parametersEntrezQuery property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getParametersEntrezQuery() {

    return this.parametersEntrezQuery;
  }

  /**
   * Sets the value of the parametersEntrezQuery property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   */
  public void setParametersEntrezQuery(String value) {

    this.parametersEntrezQuery = value;
  }

}
