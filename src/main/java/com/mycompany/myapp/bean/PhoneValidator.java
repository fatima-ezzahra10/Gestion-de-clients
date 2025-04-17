package com.mycompany.myapp.bean;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import java.util.regex.Pattern;

@FacesValidator("phoneValidator")
public class PhoneValidator implements Validator<Object> {

    private static final String PHONE_PATTERN = "^[0-9\\-\\s\\(\\)]+$"; // accepte chiffres, tirets, espaces, parenthèses
    private static final int MIN_LENGTH = 10;
    private static final int MAX_LENGTH = 15;

    private final Pattern pattern;

    public PhoneValidator() {
        pattern = Pattern.compile(PHONE_PATTERN);
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return; 
        }

        String phone = value.toString().trim();

        if (phone.length() < MIN_LENGTH || phone.length() > MAX_LENGTH || !pattern.matcher(phone).matches()) {
            throw new ValidatorException(
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Numéro de téléphone non valide.", null)
            );
        }
    }
}
