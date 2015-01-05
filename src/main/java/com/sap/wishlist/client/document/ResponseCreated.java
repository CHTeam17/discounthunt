package com.sap.wishlist.client.document;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
	"link",
	"id"
})
public class ResponseCreated {
    @JsonProperty("link")
    private String link;
    @JsonProperty("id")
    private String id;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return The link
     */
    @JsonProperty("link")
    public String getLink() {
	return link;
    }

    /**
     *
     * @param link
     *            The link
     */
    @JsonProperty("link")
    public void setLink(String link) {
	this.link = link;
    }

    /**
     *
     * @return The id
     */
    @JsonProperty("id")
    public String getId() {
	return id;
    }

    /**
     *
     * @param id
     *            The id
     */
    @JsonProperty("id")
    public void setId(String id) {
	this.id = id;
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
