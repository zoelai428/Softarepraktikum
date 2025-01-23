package Model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PairAlgorithm {

    /**
     * Finds participants who do not belong to any pair.
     * @return the list of all participants in the event who are not found in this.pairs yet.
     */
    protected static List<Participant> findParticipantsWithoutPartner(List<Participant> participants, List<Pair> pairs) {
        List<Participant> participantsWithoutPartner = new ArrayList<>();
        for (Participant participant : participants) {
            boolean found = false;
            for (Pair pair : pairs) {
                if ((pair.getParticipants().get(0).equals(participant))
                        || (pair.getParticipants().get(1).equals(participant))) {
                    found = true;
                    break;
                }
            }
            if (!found) participantsWithoutPartner.add(participant);
        }
        return participantsWithoutPartner;
    }

    /**
     * Finds participants who belong to a pair already.
     * @return the list of all participants in the event who are found in this.pairs.
     */
    protected static List<Participant> findParticipantsWithPartner(List<Participant> participants, List<Pair> pairs) {
        List<Participant> participantsWithPartner = new ArrayList<>();
        for (Participant participant : participants) {
            boolean found = false;
            for (Pair pair : pairs) {
                if ((pair.getParticipants().get(0).equals(participant))
                        || (pair.getParticipants().get(1).equals(participant))) {
                    found = true;
                    break;
                }
            }
            if (found) participantsWithPartner.add(participant);
        }
        return participantsWithPartner;
    }

    /**
     * Find all participants from a given list who registered in the event with specified KitchenExists.
     * @param participants an input list of participants
     * @return filtered input list, with only those participants who match the input KitchenExists remaining.
     */
    protected static List<Participant> findParticipantsWithSpecificKitchenExists(List<Participant> participants, KitchenExists kitchenExists) {
        List<Participant> result = new ArrayList<>();
        for (Participant participant : participants) {
            if (kitchenExists == KitchenExists.YES || kitchenExists == KitchenExists.MAYBE) {
                if (participant.getKitchen() != null && participant.getKitchen().getExists() == kitchenExists)
                    result.add(participant);
            } else {
                if (participant.getKitchen() == null)
                    result.add(participant);
            }
        }
        return result;
    }

    /**
     * Find all participants from a given list who do not have a partner yet.
     * @param inputList an input list of participants
     * @return filtered input list, with participants who already have a partner removed.
     */
    protected static List<Participant> removeMatchedParticipants(List<Participant> inputList, List<Pair> pairs) {
        List<Participant> participantsWithPartner = findParticipantsWithPartner(inputList, pairs);
        inputList.removeAll(participantsWithPartner);
        return inputList;
    }

    /**
     * Find a list of valid candidates for a given participant. Valid means the potential matching is not forbidden in the specification.
     * Forbidden matching includes
     * - matching with oneself,
     * - MEAT with VEGGIE/VEGAN,
     * - same address/house upon registration.
     * @param participant an input participant who is looking for candidates as partner
     * @param listOfParticipants an input list of participants - potential candidates for the input participant
     * @return an extracted list of participants, which contain only valid candidates for the input participant
     */
    protected static List<Participant> findValidCandidates(Participant participant, List<Participant> listOfParticipants) {
        List<Participant> result = new ArrayList<>();
        for (Participant p : listOfParticipants) {
            if (participant.equals(p)) // a participant should not be paired with himself
                continue;
            boolean valid = true;
            if (((participant.getFoodPreference().equals(FoodPreference.MEAT)) // criteria 6.1
                    && ((p.getFoodPreference().equals(FoodPreference.VEGAN)) || (p.getFoodPreference().equals(FoodPreference.VEGGIE))))
                    || ((p.getFoodPreference().equals(FoodPreference.MEAT))
                    && ((participant.getFoodPreference().equals(FoodPreference.VEGAN)) || (participant.getFoodPreference().equals(FoodPreference.VEGGIE)))))
                valid = false;
            if ((participant.getKitchen() != null) && (p.getKitchen() != null)
                    && (participant.getKitchen().equals(p.getKitchen()))) // criteria 6.5 same address (kitchen) is not allowed
                valid = false;
            if (valid)
                result.add(p);
        }
        return result;
    }


    /**
     * Find participants who have the specified food preference.
     * @param participants an input list of participants
     * @param foodPreference a given food preference
     * @return filtered input list which contains participants who have the given food preference only.
     */
    protected static List<Participant> findParticipantsWithSameFoodPreference(List<Participant> participants, FoodPreference foodPreference) {
        List<Participant> result = new ArrayList<>();
        for (Participant p : participants) {
            if (p.getFoodPreference() == foodPreference)
                result.add(p);
        }
        return result;
    }

    /**
     * Find participants who have the specified age group.
     * @param participants an input list of participants
     * @param ageGroup a given age group
     * @return filtered input list which contains participants who have the given age group only.
     */
    protected static List<Participant> findParticipantsWithSameAgeGroup(List<Participant> participants, int ageGroup) {
        List<Participant> result = new ArrayList<>();
        for (Participant p : participants) {
            if (p.getAgeGroup() == ageGroup)
                result.add(p);
        }
        return result;
    }

    /**
     * Find participants who have the specified gender.
     * @param participants an input list of participants
     * @param gender a given gender
     * @return filtered input list which contains participants who have the given gender only.
     */
    protected static List<Participant> findParticipantsWithSameGender(List<Participant> participants, Gender gender) {
        List<Participant> result = new ArrayList<>();
        for (Participant p : participants) {
            if (p.getGender() == gender)
                result.add(p);
        }
        return result;
    }

    /**
     * For a given participant, sort the candidates according to food preference.
     * @param participant an input participant
     * @param candidates the list of candidates for the participant
     * @return a sorted list of candidates with favourable food preference first.
     */
    protected static List<Participant> sortParticipantsBySameFoodPreferenceFirst(Participant participant, List<Participant> candidates) {
        FoodPreference participantPreference = participant.getFoodPreference();
        Comparator<Participant> comparator = (p1, p2) -> {
            int priority1 = getFoodPreferencePriority(participantPreference, p1.getFoodPreference());
            int priority2 = getFoodPreferencePriority(participantPreference, p2.getFoodPreference());
            return Integer.compare(priority1, priority2);
        };
        return candidates.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * Assists the method sortParticipantsBySameFoodPreferenceFirst to assign the priority of food preferences
     * @param participantPreference the food preference of the participant
     * @param candidatePreference the food preference of
     * @return the priority of the candidate (1/2/3/4) based on the participant's and the candidate's food preference.
     */
    protected static int getFoodPreferencePriority(FoodPreference participantPreference, FoodPreference candidatePreference) {
        Map<FoodPreference, Integer> priorityMap = new HashMap<>();
        switch (participantPreference) {
            case MEAT:
                priorityMap.put(FoodPreference.MEAT, 1);
                priorityMap.put(FoodPreference.NONE, 2);
                priorityMap.put(FoodPreference.VEGGIE, 3);
                priorityMap.put(FoodPreference.VEGAN, 4);
                break;
            case NONE:
                priorityMap.put(FoodPreference.NONE, 1);
                priorityMap.put(FoodPreference.MEAT, 2);
                priorityMap.put(FoodPreference.VEGGIE, 3);
                priorityMap.put(FoodPreference.VEGAN, 4);
                break;
            case VEGGIE:
                priorityMap.put(FoodPreference.VEGGIE, 1);
                priorityMap.put(FoodPreference.VEGAN, 2);
                priorityMap.put(FoodPreference.NONE, 3);
                priorityMap.put(FoodPreference.MEAT, 4);
                break;
            case VEGAN:
                priorityMap.put(FoodPreference.VEGAN, 1);
                priorityMap.put(FoodPreference.VEGGIE, 2);
                priorityMap.put(FoodPreference.NONE, 3);
                priorityMap.put(FoodPreference.MEAT, 4);
                break;
        }
        return priorityMap.getOrDefault(candidatePreference, Integer.MAX_VALUE);
    }

    /**
     * For a given participant, sort the candidates according to age group.
     * @param participant an input participant
     * @param listOfParticipants the list of candidates for the participant
     * @return a sorted list of candidates with favourable age group first.
     */
    protected static List<Participant> sortParticipantsBySameAgeGroupFirst(Participant participant, List<Participant> listOfParticipants) {
        List<Participant> result = new ArrayList<>(listOfParticipants);
        result.sort(Comparator.comparingInt(p -> Math.abs(p.getAgeGroup() - participant.getAgeGroup())));
        return result;
    }

    /**
     * For a given participant, sort the candidates according to gender.
     * @param participant an input participant
     * @param listOfParticipants the list of candidates for the participant
     * @return a sorted list of candidates with different gender first.
     */
    protected static List<Participant> sortParticipantsByDifferentGenderFirst(Participant participant, List<Participant> listOfParticipants) {
        List<Participant> sameGender = new ArrayList<>();
        List<Participant> differentGender = new ArrayList<>();
        for (Participant p : listOfParticipants) {
            if (participant.getGender().equals(p.getGender()))
                sameGender.add(p);
            else
                differentGender.add(p);
        }
        return Stream.concat(differentGender.stream(), sameGender.stream()).collect(Collectors.toList());
    }


    /**
     * Find the food preference of the pair when two participants are matched together based on criteria 6.1 in specification.
     * @param participant1 the first participant
     * @param participant2 the second participant
     * @return food preference for the pair
     */
    public static FoodPreference findPairFoodPreference(Participant participant1, Participant participant2) {
        if (participant1.getFoodPreference().equals(participant2.getFoodPreference()))
            return participant1.getFoodPreference();
        else if ((participant1.getFoodPreference().equals(FoodPreference.MEAT)) || (participant2.getFoodPreference().equals(FoodPreference.MEAT))) {
            return FoodPreference.MEAT;
        } else if ((participant1.getFoodPreference().equals(FoodPreference.NONE)) || (participant2.getFoodPreference().equals(FoodPreference.NONE)))  {
            return FoodPreference.NONE;
        } else
            return FoodPreference.VEGAN;
    }



    /**
     * An assisting method for the method createPairs, which forms pairs from participant lists 1 & 2 based on grouping and sorting.
     * @param participantList1 the first list of participants
     * @param participantList2 the second list of participants
     * @param foodPreference the priority for food preference
     * @param ageDifference the priority for age difference
     * @param genderDiversity the priority for gender diversity
     * @param restrictedToPriorityOnePreference if the matching is strictly restricted to the priority-1-preference
     */
    protected static void handlePairMatchingWithPreference(List<Participant> participantList1, List<Participant> participantList2,
                                                  int foodPreference,  int ageDifference, int genderDiversity, boolean restrictedToPriorityOnePreference, SpinfoodEvent event) {
        participantList1 = PairAlgorithm.removeMatchedParticipants(participantList1, event.getPairs());
        participantList2 = PairAlgorithm.removeMatchedParticipants(participantList2, event.getPairs());
        List<List<Participant>> groups1 = new ArrayList<>();
        List<List<Participant>> groups2 = new ArrayList<>();

        arrangeGroupsBasedOnPreference(participantList1, participantList2, foodPreference, ageDifference, genderDiversity, restrictedToPriorityOnePreference, groups1, groups2);

        for (int j = 0; j < groups1.size(); j++) {
            for (Participant participant : groups1.get(j)) {
                if (PairAlgorithm.findParticipantsWithPartner(event.getParticipants(), event.getPairs()).contains(participant)) // skip if participant belongs to a pair already
                    continue;
                Integer kitchenOwner = 1;
                List<Participant> candidates = PairAlgorithm.findValidCandidates(participant, groups2.get(j));
                candidates = PairAlgorithm.removeMatchedParticipants(candidates, event.getPairs());
                candidates = sortByBestCandidates(foodPreference, ageDifference, genderDiversity, restrictedToPriorityOnePreference, participant, candidates);

                Participant partner = null;
                if (candidates.size() > 0) partner = candidates.get(0);

                if (partner != null) {
                    if (partner.getKitchen() != null) // criteria 6.3
                        if (SpinfoodEvent.findDistance(partner.getKitchen().getLocation(), event.getAfterDinnerPartyLocation())
                                < SpinfoodEvent.findDistance(participant.getKitchen().getLocation(), event.getAfterDinnerPartyLocation()))
                            kitchenOwner = 2;
                    event.getPairs().add(new Pair(participant, partner, PairAlgorithm.findPairFoodPreference(participant, partner), kitchenOwner==2));
                }

            }
        }

    }

    private static void arrangeGroupsBasedOnPreference(List<Participant> participantList1, List<Participant> participantList2, int foodPreference, int ageDifference, int genderDiversity, boolean restrictedToPriorityOnePreference, List<List<Participant>> groups1, List<List<Participant>> groups2) {
        if (restrictedToPriorityOnePreference) { // strict matching which priority 1 must be fulfilled
            if (foodPreference == 1) // criteria 6.1 & 6.6: we classify the groups further with food preference
                for (FoodPreference preference : FoodPreference.values()) {
                    groups1.add(PairAlgorithm.findParticipantsWithSameFoodPreference(participantList1, preference));
                    groups2.add(PairAlgorithm.findParticipantsWithSameFoodPreference(participantList2, preference));
                }
            if (ageDifference == 1) // criteria 6.7
                for (int i = 0; i < 9; i++) {
                    groups1.add(PairAlgorithm.findParticipantsWithSameAgeGroup(participantList1, i));
                    groups2.add(PairAlgorithm.findParticipantsWithSameAgeGroup(participantList2, i));
                }
            if (genderDiversity == 1) {// criteria 6.8
                groups1.add(PairAlgorithm.findParticipantsWithSameGender(participantList1, Gender.FEMALE));
                groups2.add(Stream.concat(
                                PairAlgorithm.findParticipantsWithSameGender(participantList2, Gender.MALE).stream(),
                                PairAlgorithm.findParticipantsWithSameGender(participantList2, Gender.OTHER).stream())
                        .toList());
                groups1.add(Stream.concat(
                                PairAlgorithm.findParticipantsWithSameGender(participantList1, Gender.MALE).stream(),
                                PairAlgorithm.findParticipantsWithSameGender(participantList1, Gender.OTHER).stream())
                        .toList());
                groups2.add(PairAlgorithm.findParticipantsWithSameGender(participantList2, Gender.FEMALE));
            }
        } else { // relaxed matching which priority 1 can be violated
            groups1.add(participantList1);
            groups2.add(participantList2);
        }
    }

    protected static List<Participant> sortByBestCandidates(int foodPreference, int ageDifference, int genderDiversity, boolean restrictedToPriorityOnePreference, Participant participant, List<Participant> candidates) {
        if (foodPreference < ageDifference && foodPreference < genderDiversity) {
            if (ageDifference < genderDiversity) {
                candidates = PairAlgorithm.sortParticipantsByDifferentGenderFirst(participant, candidates);
                candidates = PairAlgorithm.sortParticipantsBySameAgeGroupFirst(participant, candidates);
            } else {
                candidates = PairAlgorithm.sortParticipantsBySameAgeGroupFirst(participant, candidates);
                candidates = PairAlgorithm.sortParticipantsByDifferentGenderFirst(participant, candidates);
            }
            if (!restrictedToPriorityOnePreference) // if restricted, the candidates would already contain those with same food preference only, no need to sort
                candidates = PairAlgorithm.sortParticipantsBySameFoodPreferenceFirst(participant, candidates);

        } else if (ageDifference < foodPreference && ageDifference < genderDiversity) {
            if (foodPreference < genderDiversity) {
                candidates = PairAlgorithm.sortParticipantsByDifferentGenderFirst(participant, candidates);
                candidates = PairAlgorithm.sortParticipantsBySameFoodPreferenceFirst(participant, candidates);
            } else {
                candidates = PairAlgorithm.sortParticipantsBySameFoodPreferenceFirst(participant, candidates);
                candidates = PairAlgorithm.sortParticipantsByDifferentGenderFirst(participant, candidates);
            }
            if (!restrictedToPriorityOnePreference)
                candidates = PairAlgorithm.sortParticipantsBySameAgeGroupFirst(participant, candidates);

        } else {
            if (foodPreference < ageDifference) {
                candidates = PairAlgorithm.sortParticipantsBySameAgeGroupFirst(participant, candidates);
                candidates = PairAlgorithm.sortParticipantsBySameFoodPreferenceFirst(participant, candidates);
            } else {
                candidates = PairAlgorithm.sortParticipantsBySameFoodPreferenceFirst(participant, candidates);
                candidates = PairAlgorithm.sortParticipantsBySameAgeGroupFirst(participant, candidates);
            }
            if (!restrictedToPriorityOnePreference)
                candidates = PairAlgorithm.sortParticipantsByDifferentGenderFirst(participant, candidates);
        }
        return candidates;
    }

}
