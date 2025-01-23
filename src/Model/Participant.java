package Model;

/**
 * Represents a participant with various attributes such as
 * name the name of the participant
 * foodPreference the food preference of the participant
 * age the age of the participant
 * gender the gender of the participant
 * kitchen the kitchen of the participant
 */
public class Participant {
    private String id;
    private String name;
    private FoodPreference foodPreference;
    private Integer age;
    private Gender gender;
    private Kitchen kitchen;
    private boolean cancelled = false;



    public Participant(String id, String name, FoodPreference foodPreference, Integer age, Gender gender, Kitchen kitchen) {
        this.id = id;
        this.name = name;
        this.foodPreference = foodPreference;
        this.age = age;
        this.gender = gender;
        this.kitchen = kitchen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoodPreference getFoodPreference() {
        return foodPreference;
    }

    public void setFoodPreference(FoodPreference foodPreference) {
        this.foodPreference = foodPreference;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setKitchen(Kitchen kitchen) {
        this.kitchen = kitchen;
    }

    public Kitchen getKitchen() {
        return kitchen;
    }

    public int getAgeGroup() {
        if (this.age <= 17)
            return 0;
        else if (this.age <= 23)
            return 1;
        else if (this.age <= 27)
            return 2;
        else if (this.age <= 30)
            return 3;
        else if (this.age <= 35)
            return 4;
        else if (this.age <= 41)
            return 5;
        else if (this.age <= 46)
            return 6;
        else if (this.age <= 56)
            return 7;
        else return 8;
    }

    public int getFoodPreferenceValue() {
        if (this.foodPreference == FoodPreference.MEAT)
            return -1;
        else if (this.foodPreference == FoodPreference.NONE)
            return 0;
        else if (this.foodPreference == FoodPreference.VEGGIE)
            return 1;
        else return 2; // VEGAN
    }
    public boolean isCancelled() { return cancelled; }

    public void setCancelled() { this.cancelled = cancelled; }
}
