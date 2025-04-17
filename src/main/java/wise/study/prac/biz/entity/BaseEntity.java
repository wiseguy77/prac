package wise.study.prac.biz.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  @Column(updatable = false, columnDefinition = "TIMESTAMP(0)")
  private LocalDateTime createdAt;
  @LastModifiedDate
  @Column(columnDefinition = "TIMESTAMP(0)")
  private LocalDateTime updatedAt;

}
