package app.service;

import java.util.Map;

public interface IHomeService {
    Map<String, Integer> countVaccinatedByNationality();
    int[] countMaleByAgeGroup();
    int[] countFemaleByAgeGroup();
}
