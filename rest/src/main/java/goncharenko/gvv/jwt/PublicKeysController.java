package goncharenko.gvv.jwt;

import static java.util.Collections.emptyList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PublicKeysController {

    @RequestMapping(value = "/x509/securetoken/publickeys", method = GET)
    public List<String> rsaPublicKeys(){
        return emptyList();
    }
}
