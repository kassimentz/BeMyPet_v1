package bemypet.com.br.bemypet_v1.pojo;

/**
 * Created by kassianesmentz on 12/08/17.
 */

public class PontoGeo {
    private Double lat;
    private Double lon;

    public PontoGeo() {
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "PontoGeo{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
