package com.cir.models.json;

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
@JsonPropertyOrder({ "ids", "name" })
public class Author implements Comparable<Author> {

    @JsonProperty("ids")
    private List<String> ids = null;
    @JsonProperty("name")
    private String name;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
    * No args constructor for use in serialization
    * 
    */
    public Author() {
    }

    /**
    * 
    * @param ids
    * @param name
    */
    public Author(List<String> ids, String name) {
    super();
    this.ids = ids;
    this.name = name;
    }

    @JsonProperty("ids")
    public List<String> getIds() {
        return ids;
    }

    @JsonProperty("ids")
    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
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
        return new ToStringBuilder(this).append("ids", ids).append("name", name)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(additionalProperties).append(ids).append(name).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Author) == false) {
            return false;
        }
        Author rhs = ((Author) other);
        return new EqualsBuilder().append(additionalProperties, rhs.additionalProperties).append(ids, rhs.ids)
                .append(name, rhs.name).isEquals();
    }

    @Override
    public int compareTo(Author authorToCompare) {
        String anotherName = authorToCompare.getName();
        String anotherId = authorToCompare.getId();
        if (this.equals(authorToCompare)) {
            return 0;
        } else if (name.equals(anotherName)) {
            return getId().compareTo(anotherId);
        } else {
            return name.compareTo(anotherName);    
        }
    }

    public String getId() {
        if (ids.size() != 1) {
            throw new RuntimeException("!!!!");
        } else {
            return ids.get(0);
        }
    }
    // Descending
    public int compareByPublications(Author authorToCompare, Map<Author, Integer> pubMap) {
        if (pubMap.containsKey(this) && pubMap.containsKey(authorToCompare)) {
            int result = pubMap.get(this).compareTo(pubMap.get(authorToCompare));
            if (result == 0) {
                return 0 - this.compareTo(authorToCompare);
            } else {
                return result;
            }
        } else if (pubMap.containsKey(this)) {
            return 1;
        } else {
            return -1;
        }
    }
}

