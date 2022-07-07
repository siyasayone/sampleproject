/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bezkoder.spring.security.postgresql.util.date;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

@Component
public class JsonDateTimeDeserializer extends JsonDeserializer<Date> {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	/**
	 *
	 * @param jsonparser JSON parser
	 * @param dc         deserialization context
	 * @return parsed date
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	@Override
	public Date deserialize(JsonParser jsonparser, DeserializationContext dc)
			throws IOException, JsonProcessingException {
		String date = jsonparser.getText();
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
