package com.sap.wishlist.client.document;

public class DocumentMetaData {

    @javax.validation.constraints.Pattern(regexp = "^.+")
    @javax.validation.constraints.NotNull
    private String _createdAt;

    @javax.validation.constraints.Pattern(regexp = "^.+")
    @javax.validation.constraints.NotNull
    private String _modifiedAt;

    @javax.validation.constraints.Pattern(regexp = "^.+")
    @javax.validation.constraints.NotNull
    private String _version;

    public String getcreatedAt() {
	return _createdAt;
    }

    public void setcreatedAt(String createdAt) {
	this._createdAt = createdAt;
    }

    public String getModifiedAt() {
	return _modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
	this._modifiedAt = modifiedAt;
    }

    public String getVersion() {
	return _version;
    }

    public void setVersion(String version) {
	this._version = version;
    }
}
