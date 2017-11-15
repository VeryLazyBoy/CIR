package com.cir.controllers.jsonobj;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "year", "publications" })
public class YearLine {

    @JsonProperty("year")
    private Integer year;
    @JsonProperty("publications")
    private Integer publications;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public YearLine() {
    }

    /**
     * 
     * @param publications
     * @param year
     */
    public YearLine(Integer year, Integer publications) {
        super();
        this.year = year;
        this.publications = publications;
    }

    @JsonProperty("year")
    public Integer getYear() {
        return year;
    }

    @JsonProperty("year")
    public void setYear(Integer year) {
        this.year = year;
    }

    @JsonProperty("publications")
    public Integer getPublications() {
        return publications;
    }

    @JsonProperty("publications")
    public void setPublications(Integer publications) {
        this.publications = publications;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("year", year).append("publications", publications)
                .append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(publications).append(additionalProperties).append(year).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof YearLine) == false) {
            return false;
        }
        YearLine rhs = ((YearLine) other);
        return new EqualsBuilder().append(publications, rhs.publications)
                .append(additionalProperties, rhs.additionalProperties).append(year, rhs.year).isEquals();
    }

}