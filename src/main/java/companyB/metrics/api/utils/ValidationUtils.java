package companyB.metrics.api.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ValidationUtils
{
    private final String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    private final String dateRegex = "[0-9]{4}[-][0-1][0-9][-][0-3][0-9][T][0-5][0-9][:][0-5][0-9][:][0-5][0-9][:.][0-9]{1,3}";


    public Boolean validateDateString(String dateString)
    {
        return validate(dateRegex,dateString);
    }
    public Boolean validateEmail(String email)
    {
        return validate(emailRegex,email);
    }

    private Boolean validate(String regex, String string)
    {
        Boolean found = true;
        if(StringUtils.isNotBlank(string))
        {
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(string);
            found = matcher.find();
        }
        return found;
    }
}
