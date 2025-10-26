package co.unicauca.infra.listener;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.FormatoARequest;
import co.unicauca.service.FormatoAService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FormatoEvaluadoListener {

        private final FormatoAService formatoAService;

        public FormatoEvaluadoListener(FormatoAService formatoAService) {
            this.formatoAService = formatoAService;
        }

        /**
         * Escucha evaluaciones de FormatoA del microservicio de coordinación
         */
        @RabbitListener(queues = RabbitMQConfig.FORMATOA_EVALUADO_QUEUE)
        public void recibirEvaluacionFormatoA(FormatoARequest request) {
            System.out.println("📥 Recibida evaluación para FormatoA ID: " + request.id());
            formatoAService.actualizarFormatoAEvaluado(request);
        }

}
