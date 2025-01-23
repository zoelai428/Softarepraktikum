package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Represents a pair of participants.
 */
public class Pair {
    private Participant participant1;
    private Participant participant2;
    private boolean registeredAsPair = false;
    private FoodPreference foodPreference;
    private boolean pariticipant2IsKitchenOwner; //false: participant1 is kitchen owner;  true: participant2 is kitchen owner
    private boolean hasCooked = false;
    private double pathLength;
    private int ageDifference;
    private int preferenceDeviation;
    private double femaleProportion;
    private Integer number;

    public Pair(Participant participant1, Participant participant2, FoodPreference foodPreference, boolean pariticipant2IsKitchenOwner) {
        this.participant1 = participant1;
        this.participant2 = participant2;
        this.foodPreference = foodPreference;
        this.pariticipant2IsKitchenOwner = pariticipant2IsKitchenOwner;
    }

    public Participant getParticipant1() {
        return participant1;
    }

    public Participant getParticipant2() {
        return participant2;
    }

    public List<Participant> getParticipants() {
        return Arrays.asList(participant1, participant2);
    }

    public void setParticipant1(Participant participant1) {
        this.participant1 = participant1;
    }

    public void setParticipant2(Participant participant2) {
        this.participant2 = participant2;
    }

    public boolean isRegisteredAsPair() {
        return registeredAsPair;
    }

    public void setRegisteredAsPair(boolean registeredAsPair) {
        this.registeredAsPair = registeredAsPair;
    }

    public FoodPreference getFoodPreference() {
        return foodPreference;
    }

    public void setFoodPreference(FoodPreference foodPreference) {
        this.foodPreference = foodPreference;
    }

    public boolean getPariticipant2IsKitchenOwner() {
        return pariticipant2IsKitchenOwner;
    }

    public void setPariticipant2IsKitchenOwner(boolean kitchenOwner) {
        this.pariticipant2IsKitchenOwner = kitchenOwner;
    }

    public Kitchen getKitchen() {
        if (!pariticipant2IsKitchenOwner)
            return participant1.getKitchen();
        else return participant2.getKitchen();
    }

    public Location getKitchenLocation() {
        if (!pariticipant2IsKitchenOwner)
            return participant1.getKitchen().getLocation();
        else return participant2.getKitchen().getLocation();
    }

    public Participant getKitchenOwner() {
        if (!pariticipant2IsKitchenOwner)
            return participant1;
        else return participant2;
    }

    public boolean hasCooked() { return hasCooked; }
    public void setHasCooked(boolean hasCooked) { this.hasCooked = hasCooked; }

    public Integer getNumber() {
        return number;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }

    public double getPathLength() { return pathLength; }
    public void setPathLength(double pathLength) { this.pathLength = pathLength; }

    public int getAgeDifference() { return ageDifference; }
    public void setAgeDifference(int ageDifference) { this.ageDifference = ageDifference; }

    public int getPreferenceDeviation() { return preferenceDeviation; }
    public void setPreferenceDeviation(int preferenceDeviation) { this.preferenceDeviation = preferenceDeviation; }

    public double getAverageAgeGroup() {
        return (double) (participant1.getAgeGroup() + participant2.getAgeGroup()) / 2;
    }

    public double getAverageFoodPreferenceValue() {
        return (double) (participant1.getFoodPreferenceValue() + participant2.getFoodPreferenceValue()) / 2;
    }

    public double getFemaleProportion() {
        return femaleProportion;
    }

    public void setFemaleProportion(double femaleProportion) {
        this.femaleProportion = femaleProportion;
    }

}
