package com.cir.controllers.jsonobj.old;

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
@JsonPropertyOrder({ "id", "title", "authors", "level" })
public class ArticleToSend {

    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("authors")
    private List<String> authors = null;
    @JsonProperty("level")
    private Integer level;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ArticleToSend() {
    }

    /**
     * 
     * @param id
     * @param authors
     * @param title
     * @param level
     */
    public ArticleToSend(String id, String title, List<String> authors, Integer level) {
        super();
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.level = level;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("authors")
    public List<String> getAuthors() {
        return authors;
    }

    @JsonProperty("authors")
    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    @JsonProperty("level")
    public Integer getLevel() {
        return level;
    }

    @JsonProperty("level")
    public void setLevel(Integer level) {
        this.level = level;
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
        return new ToStringBuilder(this).append("id", id).append("title", title).append("authors", authors)
                .append("level", level).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(authors).append(title).append(level).append(additionalProperties)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ArticleToSend) == false) {
            return false;
        }
        ArticleToSend rhs = ((ArticleToSend) other);
        return new EqualsBuilder().append(id, rhs.id).append(authors, rhs.authors).append(title, rhs.title)
                .append(level, rhs.level).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}