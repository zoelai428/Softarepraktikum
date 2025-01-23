package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a group of participants, which consists of three pairs.
 */
public class Group {
    private Course course;
    private FoodPreference foodPreference;
    private List<Pair> groupPairs;
    private Integer number;
    private Pair kitchenOwner;
    private double ageDifference;
    private double preferenceDeviation;
    private double genderDiversity;

    public Group(List<Pair> groupPairs, FoodPreference foodPreference, Course course, Pair kitchenOwner) {
        this.groupPairs = new ArrayList<>(3);
        this.foodPreference = foodPreference;
        this.course = course;
        this.kitchenOwner = kitchenOwner;
    }

    public List<Pair> getGroupPairs() {return groupPairs;}
    public void setGroupPairs(List<Pair> groupPairs) {this.groupPairs = groupPairs;}

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public FoodPreference getFoodPreference() {
        return foodPreference;
    }
    public void setFoodPreference(FoodPreference foodPreference) {
        this.foodPreference = foodPreference;
    }

    public Course getCourse() {return course;}
    public void setCourse(Course course) {this.course = course;}

    public Pair getKitchenOwner() { return kitchenOwner; }
    public void setKitchenOwner(Pair kitchenOwner) { this.kitchenOwner = kitchenOwner; }

    public double getAgeDifference() { return ageDifference; }
    public void setAgeDifference(double ageDifference) { this.ageDifference = ageDifference; }

    public double getPreferenceDeviation() { return preferenceDeviation; }
    public void setPreferenceDeviation(double preferenceDeviation) { this.preferenceDeviation = preferenceDeviation; }

    public double getGenderDiversity() { return genderDiversity; }
    public void setGenderDiversity (double genderDiversity) { this.genderDiversity = genderDiversity; }




}
