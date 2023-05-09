package com.epam.esm.model.converter;

import java.time.LocalDateTime;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, String> {

  @Override
  public String convertToDatabaseColumn(LocalDateTime locDate) {
    return (locDate == null ? null : locDate.toString());
  }

  @Override
  public LocalDateTime convertToEntityAttribute(String dateValue) {
    return (dateValue == null ? null : LocalDateTime.parse(dateValue));
  }
}