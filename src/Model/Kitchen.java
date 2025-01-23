package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a kitchen of a participant, includes:
 * exists - whether the participant has this kitchen as available
 * story - the floor of the kitchen, if available
 * location - the coordinates of the kitchen location, if available
 */

public class Kitchen {
    private KitchenExists exists;
    private Integer story;
    private Location location;

    public Kitchen(KitchenExists kitchenExists, Integer kitchenStory, Double kitchenLongitude, Double kitchenLatitude) {
        this.exists = kitchenExists;
        this.story = kitchenStory;
        this.location = new Location(kitchenLongitude, kitchenLatitude);
    }

    public KitchenExists getExists() {
        return exists;
    }

    public void setExists(KitchenExists exists) {
        this.exists = exists;
    }

    public Integer getStory() {
        return story;
    }

    public void setStory(Integer story) {
        this.story = story;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
