package ru.senya.deal.entity.field;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.*;
import ru.senya.deal.entity.enums.ChangeType;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString()
@Table(name = "status_history")
public class StatusHistory {

    @Column(name = "status")
    private String status;

    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "change_type")
    private ChangeType changeType;

}
