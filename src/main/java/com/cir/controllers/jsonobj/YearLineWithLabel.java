package com.cir.controllers.jsonobj;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "confId", "yearline" })
public class YearLineWithLabel {

    @JsonProperty("confId")
    private String confId;
    @JsonProperty("yearline")
    private List<YearTransition> yearline = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public YearLineWithLabel() {
    }

    /**
     * 
     * @param confId
     * @param yearline
     */
    public YearLineWithLabel(String confId, List<YearTransition> yearline) {
        super();
        this.confId = confId;
        this.yearline = yearline;
    }

    @JsonProperty("confId")
    public String getConfId() {
        return confId;
    }

    @JsonProperty("confId")
    public void setConfId(String confId) {
        this.confId = confId;
    }

    @JsonProperty("yearline")
    public List<YearTransition> getYearLine() {
        return yearline;
    }

    @JsonProperty("yearline")
    public void setYearLine(List<YearTransition> yearline) {
        this.yearline = yearline;
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
        return new ToStringBuilder(this).append("confId", confId).append("yearline", yearline)
                .append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(confId).append(additionalProperties).append(yearline).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof YearLineWithLabel) == false) {
            return false;
        }
        YearLineWithLabel rhs = ((YearLineWithLabel) other);
        return new EqualsBuilder().append(confId, rhs.confId).append(additionalProperties, rhs.additionalProperties)
                .append(yearline, rhs.yearline).isEquals();
    }

}