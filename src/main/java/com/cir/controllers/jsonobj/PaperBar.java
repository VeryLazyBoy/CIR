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
@JsonPropertyOrder({ "paper", "cited" })
public class PaperBar {

    @JsonProperty("paper")
    private String paper;
    @JsonProperty("cited")
    private Integer cited;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public PaperBar() {
    }

    /**
     * 
     * @param cited
     * @param paper
     */
    public PaperBar(String paper, Integer cited) {
        super();
        this.paper = paper;
        this.cited = cited;
    }

    @JsonProperty("paper")
    public String getPaper() {
        return paper;
    }

    @JsonProperty("paper")
    public void setPaper(String paper) {
        this.paper = paper;
    }

    @JsonProperty("cited")
    public Integer getCited() {
        return cited;
    }

    @JsonProperty("cited")
    public void setCited(Integer cited) {
        this.cited = cited;
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
        return new ToStringBuilder(this).append("paper", paper).append("cited", cited)
                .append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(additionalProperties).append(cited).append(paper).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PaperBar) == false) {
            return false;
        }
        PaperBar rhs = ((PaperBar) other);
        return new EqualsBuilder().append(additionalProperties, rhs.additionalProperties).append(cited, rhs.cited)
                .append(paper, rhs.paper).isEquals();
    }

}