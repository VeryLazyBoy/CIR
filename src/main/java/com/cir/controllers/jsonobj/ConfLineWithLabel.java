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
@JsonPropertyOrder({ "confId", "confline" })
public class ConfLineWithLabel {

    @JsonProperty("confId")
    private String confId;
    @JsonProperty("confline")
    private List<ConfTransition> confline = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ConfLineWithLabel() {
    }

    /**
     * 
     * @param confId
     * @param confline
     */
    public ConfLineWithLabel(String confId, List<ConfTransition> confline) {
        super();
        this.confId = confId;
        this.confline = confline;
    }

    @JsonProperty("confId")
    public String getConfId() {
        return confId;
    }

    @JsonProperty("confId")
    public void setConfId(String confId) {
        this.confId = confId;
    }

    @JsonProperty("confline")
    public List<ConfTransition> getConfLine() {
        return confline;
    }

    @JsonProperty("confline")
    public void setConfLine(List<ConfTransition> confline) {
        this.confline = confline;
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
        return new ToStringBuilder(this).append("confId", confId).append("confline", confline)
                .append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(confId).append(additionalProperties).append(confline).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ConfLineWithLabel) == false) {
            return false;
        }
        ConfLineWithLabel rhs = ((ConfLineWithLabel) other);
        return new EqualsBuilder().append(confId, rhs.confId).append(additionalProperties, rhs.additionalProperties)
                .append(confline, rhs.confline).isEquals();
    }

}