package com.justfoodz.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StripeResponse {

@SerializedName("response")
@Expose
private String response;
@SerializedName("message")
@Expose
private String message;

public String getResponse() {
return response;
}

public void setResponse(String response) {
this.response = response;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

}