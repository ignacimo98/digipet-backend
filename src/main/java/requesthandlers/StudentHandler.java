package requesthandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.org.apache.xpath.internal.operations.Mod;
import dataobjects.Model;
import routing.CustomResponse;
import routing.ResponseCreator;
import sun.util.resources.cldr.chr.CalendarData_chr_US;

public class StudentHandler {
    public static ResponseCreator getCaregiver(Model model, int id) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return CustomResponse.ok(mapper.writeValueAsString(model.getCaregiverFromId(id)));
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }

    public static ResponseCreator getServices(Model model, int id) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return CustomResponse.ok(mapper.writeValueAsString(model.getServicesForCaregiver(id)));
        } catch (Exception e) {
            return CustomResponse.error(404, e.getMessage());
        }
    }


}
