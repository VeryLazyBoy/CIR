package com.cir.controllers.jsonobj;

import java.util.HashMap;
import java.util.List;
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
@JsonPropertyOrder({ "articles", "links" })
public class ArticleNetwork {

    @JsonProperty("articles")
    private List<ArticleToSend> articles = null;
    @JsonProperty("links")
    private List<Link> links = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ArticleNetwork() {
    }

    /**
     * 
     * @param articles
     * @param links
     */
    public ArticleNetwork(List<ArticleToSend> articles, List<Link> links) {
        super();
        this.articles = articles;
        this.links = links;
    }

    @JsonProperty("articles")
    public List<ArticleToSend> getArticles() {
        return articles;
    }

    @JsonProperty("articles")
    public void setArticles(List<ArticleToSend> articles) {
        this.articles = articles;
    }

    @JsonProperty("links")
    public List<Link> getLinks() {
        return links;
    }

    @JsonProperty("links")
    public void setLinks(List<Link> links) {
        this.links = links;
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
        return new ToStringBuilder(this).append("articles", articles).append("links", links)
                .append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(articles).append(additionalProperties).append(links).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ArticleNetwork) == false) {
            return false;
        }
        ArticleNetwork rhs = ((ArticleNetwork) other);
        return new EqualsBuilder().append(articles, rhs.articles).append(additionalProperties, rhs.additionalProperties)
                .append(links, rhs.links).isEquals();
    }

}