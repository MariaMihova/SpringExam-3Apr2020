package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.TownSeedDto;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TownServiceImpl implements TownService {

    private static String TOWN_FILE_PATH = "src/main/resources/files/json/towns.json";
    private final TownRepository townRepository;
    private final Gson gson;
    private final ValidationUtil validator;
    private final ModelMapper modelMapper;

    public TownServiceImpl(TownRepository townRepository, Gson gson, ValidationUtil validator, ModelMapper modelMapper) {
        this.townRepository = townRepository;
        this.gson = gson;
        this.validator = validator;
        this.modelMapper = modelMapper;
    }


    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of(TOWN_FILE_PATH));
    }

    @Override
    public String importTowns() throws IOException {
        StringBuilder sb = new StringBuilder();
       TownSeedDto[] townSeedDtos = this.gson.fromJson(this.readTownsFileContent(), TownSeedDto[].class);
        for (TownSeedDto townSeedDto : townSeedDtos) {
            try {
                if (!this.validator.isValid(townSeedDto)) {
                    sb.append("Invalid Town").append(System.lineSeparator());
                    continue;
                }
                Town town = this.modelMapper.map(townSeedDto, Town.class);
                this.townRepository.save(town);
                sb.append(String.format("Successfully imported Town %s -%d", town.getName(), town.getPopulation()))
                        .append(System.lineSeparator());
            } catch (Exception e) {
                sb.append("Invalid Town").append(System.lineSeparator());
            }

        }
//        System.out.println(sb.toString());
        return sb.toString();
    }

    @Override
    public Town findTownByName(String town) {
        return this.townRepository.findByName(town);
    }
}
