package ru.senya.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import ru.senya.deal.service.KafkaService;

@RestController
@RequestMapping("/deal/document/{applicationId}")
@RequiredArgsConstructor
@Tag(name = "Отправление сообщений через Kafka")
public class DossierController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaService kafkaService;

    @PostMapping("/send")
    @Operation(summary = "Запрос на формирование документов")
    public ResponseEntity<Object> sendDocuments(@PathVariable Long applicationId) {
        kafkaTemplate.send("send-documents", kafkaService.prepareDocuments(applicationId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/sign")
    @Operation(summary = "Запрос на отправку документов")
    public ResponseEntity<Object> signDocuments(@PathVariable Long applicationId) {
        kafkaTemplate.send("send-ses", kafkaService.sendDocuments(applicationId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/code")
    @Operation(summary = "Запрос на отпраку кода")
    public ResponseEntity<Object> sendSesCode(@PathVariable Long applicationId) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
