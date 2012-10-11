package de.congrace.blog4j.forms.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import de.congrace.blog4j.forms.CategoryCommand;

public class CategoryValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        if (CategoryCommand.class.equals(clazz)){
            return true;
        }
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required", "A name is required");
    }

}
