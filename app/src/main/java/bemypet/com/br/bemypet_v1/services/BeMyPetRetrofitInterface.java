package bemypet.com.br.bemypet_v1.services;

import bemypet.com.br.bemypet_v1.pojo.map.Retorno;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kassianesmentz on 09/09/17.
 */

public interface BeMyPetRetrofitInterface {

    @GET("maps/api/directions/json")
    Call<Retorno> searchPositions(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("key") String key
    );

}

