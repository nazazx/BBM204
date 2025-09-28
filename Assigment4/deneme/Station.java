import java.io.Serializable;
import java.util.Objects;

public class Station implements Serializable {
    static final long serialVersionUID = 55L;

    public Point coordinates;
    public String description;

    public Station(Point coordinates, String description) {
        this.coordinates = coordinates;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Station)) return false;
        Station station = (Station) o;
        return Objects.equals(coordinates, station.coordinates) && Objects.equals(description, station.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates, description);
    }

    public String toString() {
        return this.description;
    }
}