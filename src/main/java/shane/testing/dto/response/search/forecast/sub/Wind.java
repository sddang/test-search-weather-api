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
public class Wind implements Response {

    @JsonProperty("speed")
    @SerializedName("speed")
    private String speed;

    @JsonProperty("deg")
    @SerializedName("deg")
    private String deg;

}
