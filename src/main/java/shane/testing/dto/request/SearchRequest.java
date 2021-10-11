package shane.testing.dto.request;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import shane.testing.dto.Request;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class SearchRequest implements Request {

    @NonNull
    @SerializedName("q")
    private String searchValue;

    @NonNull
    @SerializedName("appid")
    private String appId;

}
