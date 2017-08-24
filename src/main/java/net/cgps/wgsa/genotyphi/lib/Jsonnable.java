package net.cgps.wgsa.genotyphi.lib;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

/**
 * The base class for Jsonnable objects. Provides JSON_PRETTY serialisation/deserialisation ob User: cyeats Date: 10/23/13
 * Time: 1:53 PM
 */
public abstract class Jsonnable {

  @Override
  public String toString() {
    return this.toPrettyJson();
  }

  public static String toJson(final Object object) {

    return toPrettyJson(object);
  }

  public static String toPrettyJson(final Object object) {

    final ObjectMapper mapper = new ObjectMapper();

    // Create a StringWriter to write the JSON_PRETTY string to
    final StringWriter writer = new StringWriter();

    try {
      mapper.writerWithDefaultPrettyPrinter().writeValue(writer, object);
    } catch (final IOException e) {
      // I don't think this can happen since there is no IO...
      LoggerFactory.getLogger(object.getClass()).error("IOException thrown when writing JSON_PRETTY string.", e);
    }

    return writer.toString();
  }

  /**
   * Method is specific to classes that are expected to be serialised as JSON_PRETTY
   *
   * @param jsonnableObject - an object that conforms to the Jackson requirements for serialisation.
   * @return A pretty representation of the JSON_PRETTY string.
   */
  public static String toPrettyJson(final Jsonnable jsonnableObject) {

    final ObjectMapper mapper = new ObjectMapper();

    // Create a StringWriter to write the JSON_PRETTY string to
    final StringWriter writer = new StringWriter();

    try {
      mapper.writerWithDefaultPrettyPrinter().writeValue(writer, jsonnableObject);
    } catch (final IOException e) {
      // I don't think this can happen since there is no IO...
      LoggerFactory.getLogger(jsonnableObject.getClass()).error("IOException thrown when writing JSON_PRETTY string.", e);
    }

    return writer.toString();
  }

  @JsonIgnore
  public final String toJson() {

    return toJson(this);
  }

  public final String toPrettyJson() {

    return toPrettyJson(this);
  }

  public static String toJson(final Jsonnable jsonnableObject) {

    final StringWriter writer = new StringWriter();

    try {
      new ObjectMapper().writer().writeValue(writer, jsonnableObject);
    } catch (final IOException e) {
      // I don't think this can happen since there is no IO...
      LoggerFactory.getLogger(jsonnableObject.getClass()).error("IOException thrown when writing JSON_PRETTY string.", e);
    }
    return writer.toString();

  }

  public static <T extends Jsonnable> T fromJson(final File snparDb, final Class<T> messageClass) {

    try {

      return new ObjectMapper().readValue(snparDb, messageClass);

    } catch (final IOException | NullPointerException e) {
      LoggerFactory.getLogger(Jsonnable.class).error("Json mapping exception for file {} to type {}", snparDb.getPath(), messageClass);
      LoggerFactory.getLogger(Jsonnable.class).error("Message: ", e);

      throw new RuntimeException(e);
    }
  }
}
