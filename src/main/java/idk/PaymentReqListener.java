package idk;
import com.walgreens.payment.service.PaymentService;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentReqListener {
    private final PaymentService invoker;
//    private MessageStructure messageStructure;
    public PaymentReqListener(PaymentService invoker){
        this.invoker = invoker;
    }

//    @KafkaListener(topics = "payments", groupId = "my-group")
    public void listen(String message) {
//        Request request = parseMessage(message);
//        invoker.invokeCommand(request);
    }


}
