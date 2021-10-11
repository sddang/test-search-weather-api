package shane.testing.dto.response.search.forecast.sub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import shane.testing.dto.Response;

@Getter
@Setter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coordinates implements Response {

    @JsonProperty("lat")
    @SerializedName("lat")
    private String lat;

    @JsonProperty("lon")
    @SerializedName("lon")
    private String lon;

}
