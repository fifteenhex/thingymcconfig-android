package jp.thingy.thingymcconfig;

import jp.thingy.thingymcconfig.model.ConfigRequest;
import jp.thingy.thingymcconfig.model.ConfigResponse;
import jp.thingy.thingymcconfig.model.ScanResponse;
import jp.thingy.thingymcconfig.model.StatusResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ThingyMcConfigService {
    @GET("scan")
    Call<ScanResponse> scan();

    @POST("config")
    @Headers("Content-Type: application/json")
    Call<ConfigResponse> config(@Body ConfigRequest configRequest);

    @GET("status")
    Call<StatusResponse> status();
}
