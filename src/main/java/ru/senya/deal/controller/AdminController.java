package ru.senya.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.senya.deal.service.ApplicationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deal/admin")
@Tag(name = "Админские API")
public class AdminController {

    private final ApplicationService applicationService;

    @GetMapping("application")
    @Operation(summary = "Вывести все заявки")
    public ResponseEntity<Object> findAllApplications() {
        return new ResponseEntity<>(applicationService.findAll(), HttpStatus.OK);
    }

    @GetMapping("application/{applicationId}")
    @Operation(summary = "Вывести заявку по ID")
    public ResponseEntity<Object> showApplication(@PathVariable Long applicationId) {
        return new ResponseEntity<>(applicationService.findApplication(applicationId), HttpStatus.OK);
    }

}
