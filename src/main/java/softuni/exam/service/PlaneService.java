package softuni.exam.service;


import softuni.exam.models.entities.Plane;

import java.io.IOException;

//ToDo - Before start App implement this Service and set areImported to return false
public interface PlaneService {

    boolean areImported();

    String readPlanesFileContent() throws IOException;
	
	String importPlanes() throws IOException;

    Plane findPlaneByRegisterNumber(String planeNumber);

}
