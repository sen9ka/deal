package ru.senya.deal.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import ru.senya.deal.services.KafkaService;

@RestController
@RequestMapping("/deal/document/{applicationId}")
@RequiredArgsConstructor
public class DossierController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaService kafkaService;

    @PostMapping("/send")
    public ResponseEntity<?> sendDocuments(@PathVariable Long applicationId) {
        kafkaTemplate.send("send-documents", kafkaService.prepareDocuments(applicationId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/sign")
    public ResponseEntity<?> sendSesCode(@PathVariable Long applicationId) {
        kafkaTemplate.send("send-ses", kafkaService.sendDocuments(applicationId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/code")
    public ResponseEntity<?> signDocuments(@PathVariable Long applicationId) {
//        kafkaTemplate.send("send-ses", kafkaService.sendSes(applicationId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
