package Model;

import java.util.List;

import static Model.SpinfoodEvent.findDistance;

public class Metrics {

    protected static int measurePairAgeGroupDifference(Pair pair) {
        int ageDifference = Math.abs(pair.getParticipant1().getAgeGroup() - pair.getParticipant2().getAgeGroup());
        pair.setAgeDifference(ageDifference);
        return pair.getAgeDifference();
    }

    protected static int measurePairPreferenceDeviation(Pair pair) {
        int foodPreferenceDeviation = pair.getParticipant1().getFoodPreferenceValue() - pair.getParticipant2().getFoodPreferenceValue();
        pair.setPreferenceDeviation(foodPreferenceDeviation);
        return pair.getPreferenceDeviation();
    }

    protected static double measurePairFemaleProportion(Pair pair) {
        int countFemale = 0;
        if (pair.getParticipant1().getGender() == Gender.FEMALE) countFemale++;
        if (pair.getParticipant2().getGender() == Gender.FEMALE) countFemale++;
        pair.setFemaleProportion((double) countFemale/2);
        return pair.getFemaleProportion();
    }

    protected static double measureGroupAgeGroupDifference(Group group) {
        double avgAge1 = group.getGroupPairs().get(0).getAverageAgeGroup();
        double avgAge2 = group.getGroupPairs().get(1).getAverageAgeGroup();
        double avgAge3 = group.getGroupPairs().get(2).getAverageAgeGroup();
        double difference = 0.0;
        difference += Math.abs(avgAge1 - avgAge2);
        difference += Math.abs(avgAge2 - avgAge3);
        difference += Math.abs(avgAge1 - avgAge3);
        group.setAgeDifference(difference / 3);
        return group.getAgeDifference();
    }

    protected static double measureGroupPreferenceDeviation(Group group) {
        double foodPreferenceDeviation1 = group.getGroupPairs().get(0).getAverageFoodPreferenceValue();
        double foodPreferenceDeviation2 = group.getGroupPairs().get(1).getAverageFoodPreferenceValue();
        double foodPreferenceDeviation3 = group.getGroupPairs().get(2).getAverageFoodPreferenceValue();
        double groupDeviation = 0.0;
        groupDeviation += Math.abs(foodPreferenceDeviation1 - foodPreferenceDeviation2);
        groupDeviation += Math.abs(foodPreferenceDeviation2 - foodPreferenceDeviation3);
        groupDeviation += Math.abs(foodPreferenceDeviation1 - foodPreferenceDeviation3);
        group.setPreferenceDeviation(groupDeviation / 3);
        return group.getPreferenceDeviation();
    }

    protected static double measureGenderDiversityInGroup(Group group) {
        double countFemale = 0;
        for(Pair pair : group.getGroupPairs()) {
            for(Participant p : pair.getParticipants()) {
                if(p.getGender().equals(Gender.FEMALE)) countFemale++;
            }
        }
        double genderDiversity = countFemale / 6;
        group.setGenderDiversity(genderDiversity);
        return genderDiversity;
    }

    protected static void measurePathLength(Pair pair, List<Group> eventGroups, Location afterDinnerPartyLocation) {
        double pathLength = 0.0;

        double length1 = 0.0;
        double length2 = 0.0;
        double length3 = 0.0;

        Location loc1 = null;
        Location loc2 = null;
        Location loc3 = null;

        for(Group group : eventGroups) {
            if(group.getGroupPairs().contains(pair)) {
                Course course = group.getCourse();
                switch (course) {
                    case APPETIZER:
                        loc1 = group.getKitchenOwner().getKitchenLocation();

                    case MAIN:
                        loc2 = group.getKitchenOwner().getKitchenLocation();

                    case DESSERT:
                        loc3 = group.getKitchenOwner().getKitchenLocation();

                }
            }
        }
        length1 = findDistance(loc1, loc2);
        length2 = findDistance(loc2, loc3);
        length3 = findDistance(loc3, afterDinnerPartyLocation);

        pathLength = length1 + length2 + length3;

        pair.setPathLength(pathLength);
    }
}
