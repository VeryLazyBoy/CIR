package com.cir.models.json;

import java.util.ArrayList;
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
@JsonPropertyOrder({ "authors", "id", "inCitations", "keyPhrases", "outCitations", "paperAbstract", "pdfUrls", "s2Url",
        "title", "venue", "year" })
public class Article implements Comparable<Article> {

    @JsonProperty("authors")
    private List<Author> authors = null;
    @JsonProperty("id")
    private String id;
    @JsonProperty("inCitations")
    private List<String> inCitations = null;
    @JsonProperty("keyPhrases")
    private List<String> keyPhrases = null;
    @JsonProperty("outCitations")
    private List<String> outCitations = null;
    @JsonProperty("paperAbstract")
    private String paperAbstract;
    @JsonProperty("pdfUrls")
    private List<String> pdfUrls = null;
    @JsonProperty("s2Url")
    private String s2Url;
    @JsonProperty("title")
    private String title;
    @JsonProperty("venue")
    private String venue; // nullable
    @JsonProperty("year")
    private Integer year; // nullable
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
    * No args constructor for use in serialization
    * 
    */
    public Article() {
    }

    /**
    * 
    * @param id
    * @param authors
    * @param title
    * @param s2Url
    * @param paperAbstract
    * @param pdfUrls
    * @param outCitations
    * @param year
    * @param inCitations
    * @param venue
    * @param keyPhrases
    */
    public Article(List<Author> authors, String id, List<String> inCitations, List<String> keyPhrases, List<String> outCitations, String paperAbstract, List<String> pdfUrls, String s2Url, String title, String venue, Integer year) {
    super();
    this.authors = authors;
    this.id = id;
    this.inCitations = inCitations;
    this.keyPhrases = keyPhrases;
    this.outCitations = outCitations;
    this.paperAbstract = paperAbstract;
    this.pdfUrls = pdfUrls;
    this.s2Url = s2Url;
    this.title = title;
    this.venue = venue;
    this.year = year;
    }

    @JsonProperty("authors")
    public List<Author> getAuthors() {
        return authors;
    }

    @JsonProperty("authors")
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("inCitations")
    public List<String> getInCitations() {
        return inCitations;
    }

    @JsonProperty("inCitations")
    public void setInCitations(List<String> inCitations) {
        this.inCitations = inCitations;
    }

    @JsonProperty("keyPhrases")
    public List<String> getKeyPhrases() {
        return keyPhrases;
    }

    @JsonProperty("keyPhrases")
    public void setKeyPhrases(List<String> keyPhrases) {
        this.keyPhrases = keyPhrases;
    }

    @JsonProperty("outCitations")
    public List<String> getOutCitations() {
        return outCitations;
    }

    @JsonProperty("outCitations")
    public void setOutCitations(List<String> outCitations) {
        this.outCitations = outCitations;
    }

    @JsonProperty("paperAbstract")
    public String getPaperAbstract() {
        return paperAbstract;
    }

    @JsonProperty("paperAbstract")
    public void setPaperAbstract(String paperAbstract) {
        this.paperAbstract = paperAbstract;
    }

    @JsonProperty("pdfUrls")
    public List<String> getPdfUrls() {
        return pdfUrls;
    }

    @JsonProperty("pdfUrls")
    public void setPdfUrls(List<String> pdfUrls) {
        this.pdfUrls = pdfUrls;
    }

    @JsonProperty("s2Url")
    public String getS2Url() {
        return s2Url;
    }

    @JsonProperty("s2Url")
    public void setS2Url(String s2Url) {
        this.s2Url = s2Url;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("venue")
    public String getVenue() {
        return venue;
    }

    @JsonProperty("venue")
    public void setVenue(String venue) {
        this.venue = venue;
    }

    @JsonProperty("year")
    public Integer getYear() {
        return year == null ? -1 : year;
    }

    @JsonProperty("year")
    public void setYear(Integer year) {
        this.year = year;
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
        return new ToStringBuilder(this).append("authors", authors).append("id", id).append("inCitations", inCitations)
                .append("keyPhrases", keyPhrases).append("outCitations", outCitations)
                .append("paperAbstract", paperAbstract).append("pdfUrls", pdfUrls).append("s2Url", s2Url)
                .append("title", title).append("venue", venue).append("year", year)
                .append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(pdfUrls).append(inCitations).append(venue).append(keyPhrases).append(id)
                .append(authors).append(title).append(additionalProperties).append(s2Url).append(paperAbstract)
                .append(outCitations).append(year).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Article) == false) {
            return false;
        }
        Article rhs = ((Article) other);
        return new EqualsBuilder().append(pdfUrls, rhs.pdfUrls).append(inCitations, rhs.inCitations)
                .append(venue, rhs.venue).append(keyPhrases, rhs.keyPhrases).append(id, rhs.id)
                .append(authors, rhs.authors).append(title, rhs.title)
                .append(additionalProperties, rhs.additionalProperties).append(s2Url, rhs.s2Url)
                .append(paperAbstract, rhs.paperAbstract).append(outCitations, rhs.outCitations).append(year, rhs.year)
                .isEquals();
    }

    public boolean isWithVenue(String venue) {
        assert venue != null;
        if (this.venue == null) {
            return false;
        } else {
            return venue.equalsIgnoreCase(this.venue);    
        }
    }

    public boolean isWithTitle(String title) {
        assert title != null;
        if (this.title == null) {
            return false;
        } else {
            return title.equalsIgnoreCase(this.title);
        }
    }

    public int getCitationTimes() {
        return inCitations.size();
    }

    public List<String> getAuthorNames() {
        List<String> result = new ArrayList<>();
        for (Author a : authors) {
            result.add(a.getName());
        }
        return result;
    }

    public boolean isWithId(String id) {
        assert id != null;
        assert this.id != null;
        return this.id.equals(id);
    }

    public int compareByCitations(Article articleToCompare) {
        int citationTimes = getCitationTimes();
        int citationTimesToComapre = articleToCompare.getCitationTimes();
        int result =  citationTimes - citationTimesToComapre;
        if (result == 0) {
            return 0 - this.compareTo(articleToCompare);
        } else {
            return result;
        }
    }

    @Override
    public int compareTo(Article articleToCompare) {
        if (this.equals(articleToCompare)) {
            return 0;
        } else if (this.getTitle().equals(articleToCompare.getTitle())) {
            return this.getId().compareTo(articleToCompare.getId());
        } else {
            return this.getTitle().compareTo(articleToCompare.getTitle());
        }
    }

}
