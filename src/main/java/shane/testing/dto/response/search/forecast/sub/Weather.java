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
public class Weather implements Response {

    @JsonProperty("id")
    @SerializedName("id")
    private String id;

    @JsonProperty("main")
    @SerializedName("main")
    private String main;

    @JsonProperty("description")
    @SerializedName("description")
    private String description;

    @JsonProperty("icon")
    @SerializedName("icon")
    private String icon;

}
