package ci.orange.digital.center.ahazzran.webservice.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ci.orange.digital.center.ahazzran.webservice.dtos.CoursDetailDto;
import ci.orange.digital.center.ahazzran.webservice.entities.ContenuLangue;
import ci.orange.digital.center.ahazzran.webservice.entities.ICoursDetail;
import ci.orange.digital.center.ahazzran.webservice.repositories.ContenuLangueRepository;


@Service
public class PhraseJusteEnLangueService implements IPhraseJusteEnLangueService{

    private ContenuLangueRepository contenuLangueRepository;

    public PhraseJusteEnLangueService(final ContenuLangueRepository contenuLangueRepository) {
        this.contenuLangueRepository = contenuLangueRepository;
    }

    @Override
    public CoursDetailDto generateChoisirPhraseEnLange(ICoursDetail coursDetail, List<ContenuLangue> contenuLangues, int contenuLangueId) {
        

        ContenuLangue correctContenuLangue = contenuLangueRepository.findById(contenuLangueId).orElseThrow();

        CoursDetailDto.ListMotsLangDto correctMotDto = new CoursDetailDto.ListMotsLangDto(correctContenuLangue);
    
        List<CoursDetailDto.ListMotsLangDto> incorrectMots = generateIncorrectMots(contenuLangues, contenuLangueId, 3);

        incorrectMots.add(correctMotDto);

        Collections.shuffle(incorrectMots);

        CoursDetailDto dto = new CoursDetailDto(coursDetail,incorrectMots, correctContenuLangue);

        return dto;
    }

    private List<CoursDetailDto.ListMotsLangDto> generateIncorrectMots(List<ContenuLangue> contenuLangues, int correctContenuLangueId, int numberOfIncorrectWords) {
        List<ContenuLangue> incorrectContenuLangues = contenuLangues.stream()
            .filter(cl -> cl.getContenuLangueId() != correctContenuLangueId)
            .collect(Collectors.toList());
    
        if (incorrectContenuLangues.size() < numberOfIncorrectWords) {
            throw new RuntimeException("Pas assez de mots incorrects disponibles.");
        }
    
        Collections.shuffle(incorrectContenuLangues);
    
        return incorrectContenuLangues.stream()
            .limit(numberOfIncorrectWords)
            .map(cl -> new CoursDetailDto.ListMotsLangDto(cl))
            .collect(Collectors.toList());
    }
}
     

