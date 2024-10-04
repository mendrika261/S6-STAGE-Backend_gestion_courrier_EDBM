package mg.edbm.mail.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessengerStatsDto {
    private Long urgentCount = 0L;
    private Long waitingCount = 0L;
    private Long deliveringCount = 0L;
    private Double deliveringDistance = 0.;
    private Double deliveringTime = 0.;
    private LocalDateTime firstDeliveringDatetime = LocalDateTime.now();

    public void setDeliveringTime(Double deliveringTime) {
        if (deliveringTime!=null)
            this.deliveringTime = deliveringTime;
    }

    public void setDeliveringDistance(Double deliveringDistance) {
        if(deliveringDistance!=null)
            this.deliveringDistance = deliveringDistance;
    }

    public void setFirstDeliveringDatetime(LocalDateTime firstDeliveringDatetime) {
        if(firstDeliveringDatetime!=null)
            this.firstDeliveringDatetime = firstDeliveringDatetime;
    }
}
