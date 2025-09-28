import java.io.Serializable;
import java.util.*;

public class CartLine implements Serializable {
    static final long serialVersionUID = 77L;
    public String cartLineName;
    public List<Station> cartLineStations;

    public CartLine(String cartLineName, List<Station> cartLineStations) {
        this.cartLineName = cartLineName;
        this.cartLineStations = cartLineStations;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CartLine)) return false;
        CartLine cartLine = (CartLine) o;
        return Objects.equals(cartLineName, cartLine.cartLineName) && Objects.equals(cartLineStations, cartLine.cartLineStations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartLineName, cartLineStations);
    }
}
