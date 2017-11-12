package com.cir.controllers.jsonobj;

import java.util.HashMap;
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
@JsonPropertyOrder({ "author", "publications" })
public class AuthorBar {

    @JsonProperty("author")
    private String author;
    @JsonProperty("publications")
    private Integer publications;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public AuthorBar() {
    }

    /**
     * 
     * @param author
     * @param publications
     */
    public AuthorBar(String author, Integer publications) {
        super();
        this.author = author;
        this.publications = publications;
    }

    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(String author) {
        this.author = author;
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
        return new ToStringBuilder(this).append("author", author).append("publications", publications)
                .append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(author).append(publications).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AuthorBar) == false) {
            return false;
        }
        AuthorBar rhs = ((AuthorBar) other);
        return new EqualsBuilder().append(author, rhs.author).append(publications, rhs.publications)
                .append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
