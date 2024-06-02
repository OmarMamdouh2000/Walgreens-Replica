package idk;

import com.walgreens.payment.service.command.Command;

public class PaymentCommandMapper {
    public Command getCommand(String messageType) {
//        if ("type1".equals(requestType)) {
//            return new SpecificCommand();
//        }
        throw new IllegalArgumentException("Invalid request type");
    }
}
