package com.cir.controllers.jsonobj.old;

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
@JsonPropertyOrder({ "venue", "publications" })
public class VenueSector {

    @JsonProperty("venue")
    private String venue;
    @JsonProperty("publications")
    private Integer publications;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public VenueSector() {
    }

    /**
     * 
     * @param publications
     * @param venue
     */
    public VenueSector(String venue, Integer publications) {
        super();
        this.venue = venue;
        this.publications = publications;
    }

    @JsonProperty("venue")
    public String getVenue() {
        return venue;
    }

    @JsonProperty("venue")
    public void setVenue(String venue) {
        this.venue = venue;
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
        return new ToStringBuilder(this).append("venue", venue).append("publications", publications)
                .append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(publications).append(additionalProperties).append(venue).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VenueSector) == false) {
            return false;
        }
        VenueSector rhs = ((VenueSector) other);
        return new EqualsBuilder().append(publications, rhs.publications)
                .append(additionalProperties, rhs.additionalProperties).append(venue, rhs.venue).isEquals();
    }

}