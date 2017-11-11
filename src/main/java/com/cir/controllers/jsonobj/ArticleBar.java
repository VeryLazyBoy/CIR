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
@JsonPropertyOrder({ "title", "citations" })
public class ArticleBar {

    @JsonProperty("title")
    private String title;
    @JsonProperty("citations")
    private Integer citations;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ArticleBar() {
    }

    /**
     * 
     * @param title
     * @param citations
     */
    public ArticleBar(String title, Integer citations) {
        super();
        this.title = title;
        this.citations = citations;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
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
        return new ToStringBuilder(this).append("title", title).append("citations", citations)
                .append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(title).append(citations).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ArticleBar) == false) {
            return false;
        }
        ArticleBar rhs = ((ArticleBar) other);
        return new EqualsBuilder().append(title, rhs.title).append(citations, rhs.citations)
                .append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}