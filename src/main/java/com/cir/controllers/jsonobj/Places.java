package com.cir.controllers.jsonobj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

// Enumeration used with JsonInclude to define which properties of Java Beans are to be included in serialization.
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "places" })
public class Places {

    @JsonProperty("places")
    private List<String> places = new ArrayList<String>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Places() {
    }

    /**
     * 
     * @param places
     */
    public Places(List<String> places) {
        super();
        this.places = places;
    }

    @JsonProperty("places")
    public List<String> getPlaces() {
        return places;
    }

    @JsonProperty("places")
    public void setPlaces(List<String> places) {
        this.places = places;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}