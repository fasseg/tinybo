package de.congrace.blog4j.forms.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import de.congrace.blog4j.forms.ArticleCommand;

public class ArticleValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ArticleCommand.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "field.required", "A title is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "teaser", "field.required", "A teaser is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "msg", "field.required", "A text is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "categoryIds", "field.required", "No category chosen!");
    }

}
