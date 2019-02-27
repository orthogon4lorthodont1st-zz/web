package com.raf.web.errors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class Errors {
	public static ArrayList<Map<String, String>> validate(Object myModel) {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();

		Set<ConstraintViolation<Object>> violations = validator.validate(myModel);
		ArrayList<Map<String, String>> errorsArray = new ArrayList<Map<String, String>>();

		for (ConstraintViolation<Object> violation : violations) {
			Map<String, String> tempMap = new HashMap<String, String>();
			tempMap.put("message", violation.getMessage());
			errorsArray.add(tempMap);
		}

		return errorsArray;
	}
}
