package softuni.exam.util;

import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

@Component
public class XmlParserImpl implements XmlParser {
    private JAXBContext jbc;

    @Override
    public String serialize(Object obj) {
        try{
            jbc = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = jbc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(obj, sw);
            return sw.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void serialize(Object obj, String fileName) {
        try{
            jbc = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = jbc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            FileWriter sw = new FileWriter(fileName);
            marshaller.marshal(obj, sw);

        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserializeFromString(String format, Class<T> toType) {
        try{
            jbc = JAXBContext.newInstance(toType);
            Unmarshaller unmarshaller = jbc.createUnmarshaller();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(format.getBytes());
            return (T) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserializeFromFile(String format, Class<T> toType) {
        try{
            jbc = JAXBContext.newInstance(toType);
            Unmarshaller unmarshaller = jbc.createUnmarshaller();
            return (T) unmarshaller.unmarshal(new FileInputStream(format));
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
