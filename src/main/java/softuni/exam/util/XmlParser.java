package softuni.exam.util;

public interface XmlParser {
    String serialize(Object obj);

    void serialize(Object obj, String fileName);

    <T> T deserializeFromString(String format, Class<T> toType);

    <T> T deserializeFromFile(String format, Class<T> toType);
}
