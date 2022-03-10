package app.service;

import app.persistence.model.User;
import app.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
