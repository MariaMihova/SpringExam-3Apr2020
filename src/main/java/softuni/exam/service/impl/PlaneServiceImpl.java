package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.PlaneSeedDto;
import softuni.exam.models.dtos.roots.RootPlaneDto;
import softuni.exam.models.entities.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PlaneServiceImpl implements PlaneService {

    private static final String PLANE_FILE_PATH = "src/main/resources/files/xml/planes.xml";
    private final PlaneRepository planeRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validator;

    public PlaneServiceImpl(PlaneRepository planeRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validator) {
        this.planeRepository = planeRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }


    @Override
    public boolean areImported() {
        return this.planeRepository.count() > 0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return Files.readString(Path.of(PLANE_FILE_PATH));
    }

    @Override
    public String importPlanes() throws IOException {
        StringBuilder sb = new StringBuilder();
        RootPlaneDto root = this.xmlParser.deserializeFromString(this.readPlanesFileContent(), RootPlaneDto.class);
        for (PlaneSeedDto planeSeedDto : root.getPlanes()) {
            try {
                if (!this.validator.isValid(planeSeedDto)) {
                    sb.append("Invalid Plane").append(System.lineSeparator());
                    continue;
                }
                Plane plane = this.modelMapper.map(planeSeedDto, Plane.class);
                this.planeRepository.save(plane);
                sb.append(String.format("Successfully imported Plane %s", plane.getRegisterNumber()))
                        .append(System.lineSeparator());
            } catch (Exception e) {
                sb.append("Invalid Plane").append(System.lineSeparator());
            }

        }

//        System.out.println(sb.toString());
        return sb.toString();
    }

    @Override
    public Plane findPlaneByRegisterNumber(String planeNumber) {
        return this.planeRepository.findByRegisterNumber(planeNumber);
    }
}

