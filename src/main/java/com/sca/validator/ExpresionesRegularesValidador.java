package com.sca.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExpresionesRegularesValidador implements ConstraintValidator<ValidarExpresionesRegulares, String> {

	private String customMessage;
	private String regex;

	@Override
	public void initialize(ValidarExpresionesRegulares validarNombreYApellido) {
		customMessage = validarNombreYApellido.customMessage();
		regex = validarNombreYApellido.expresionRegular();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// If value is null or empty, let @NotBlank/@NotNull handle presence; only validate non-empty values
		if (value == null || value.trim().isEmpty()) {
			return true;
		}
		// Realiza tus validaciones aquí
		if (!value.matches(regex)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(customMessage).addConstraintViolation();
			return false;
		}
		return true;
	}

}
