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
@JsonPropertyOrder({ "conference", "citations" })
public class ConfTransition {

    @JsonProperty("conference")
    private String conference;
    @JsonProperty("citations")
    private Integer citations;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ConfTransition() {
    }

    /**
     * 
     * @param citations
     * @param conference
     */
    public ConfTransition(String conference, Integer citations) {
        super();
        this.conference = conference;
        this.citations = citations;
    }

    @JsonProperty("conference")
    public String getConference() {
        return conference;
    }

    @JsonProperty("conference")
    public void setConference(String conference) {
        this.conference = conference;
    }

    @JsonProperty("citations")
    public Integer getCitations() {
        return citations;
    }

    @JsonProperty("citations")
    public void setCitations(Integer citations) {
        this.citations = citations;
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
        return new ToStringBuilder(this).append("conference", conference).append("citations", citations)
                .append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(citations).append(additionalProperties).append(conference).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ConfTransition) == false) {
            return false;
        }
        ConfTransition rhs = ((ConfTransition) other);
        return new EqualsBuilder().append(citations, rhs.citations)
                .append(additionalProperties, rhs.additionalProperties).append(conference, rhs.conference).isEquals();
    }

}