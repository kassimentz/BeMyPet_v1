package bemypet.com.br.bemypet_v1.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kassianesmentz on 12/08/17.
 */

public class PontoGeo {
    public Double lat;
    public Double lon;

    public PontoGeo() {
    }

    public PontoGeo(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "PontoGeo{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lat", lat);
        result.put("lon", lon);

        return result;
    }
}
