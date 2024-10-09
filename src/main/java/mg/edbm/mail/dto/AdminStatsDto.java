package mg.edbm.mail.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminStatsDto {
    private Double waitingUrgentPercentage = 0.;
    private Double waitingNormalPercentage = 0.;
    private Double deliveringPercentage = 0.;
    private Long mailCount = 0L;
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

    public void setMailCount(Long mailCount) {
        if(mailCount!=null)
            this.mailCount = mailCount;
    }

    public void setUrgentCount(Long urgentCount) {
        if(urgentCount!=null)
            this.urgentCount = urgentCount;
    }

    public void setWaitingCount(Long waitingCount) {
        if(waitingCount!=null)
            this.waitingCount = waitingCount;
    }

    public void setDeliveringCount(Long deliveringCount) {
        if(deliveringCount!=null)
            this.deliveringCount = deliveringCount;
    }
}
