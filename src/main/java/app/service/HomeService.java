package app.service;

import app.persistence.model.User;
import app.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional
public class HomeService implements IHomeService{

    @Autowired
    UserRepository userRepository;

    public Map<String, Integer> countVaccinatedByNationality() {
        Map<String, Integer> data = new LinkedHashMap<>();
        for(User user : userRepository.findAll()){
            if(user.getNumberOfDoses()==2){
                int count = data.containsKey(user.getNationality()) ? data.get(user.getNationality()) : 0;
                data.put(user.getNationality(), count+1);
            }
        }
        return data;
    }
    public int[] countMaleByAgeGroup() {
        int[] negCount = {0,0,0,0,0,0,0};
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (User user : userRepository.findAllByGender("male")){
            if(user.getNumberOfDoses()==2) {
                LocalDate newDate = LocalDate.parse(user.getDate_of_birth(), formatter);
                int userAge = Period.between(newDate, LocalDate.now()).getYears();
                if (userAge >= 18 && userAge <= 29)
                    negCount[0] -= 1;
                else if(userAge >= 30 && userAge <= 39)
                    negCount[1] -= 1;
                else if(userAge >= 40 && userAge <= 49)
                    negCount[2] -= 1;
                else if(userAge >= 50 && userAge <= 64)
                    negCount[3] -= 1;
                else if(userAge >= 65 && userAge <= 74)
                    negCount[4] -= 1;
                else if(userAge >= 75 && userAge <= 84)
                    negCount[5] -= 1;
                else
                    negCount[6] -= 1;
            }
        }
        return negCount;
    }
}
