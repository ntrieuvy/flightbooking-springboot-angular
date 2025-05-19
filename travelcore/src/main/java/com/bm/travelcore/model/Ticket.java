package com.bm.travelcore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TICKET")
@EntityListeners(AuditingEntityListener.class)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_seq_gen")
    @SequenceGenerator(name = "ticket_seq_gen", sequenceName = "ticket_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id", nullable = false)
    private OrderDetail orderDetail;

    @Column(name = "airline", nullable = false)
    private String airline;

    @Column(name = "ticket_number", nullable = false)
    private String ticketNumber;

    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate;

    @Column(name = "booking_code", nullable = false)
    private String bookingCode;

    @Column(name = "passenger_name", nullable = false)
    private String passengerName;

    @Column(name = "segments", columnDefinition = "CLOB")
    private String segments;

    @Column(name = "ticket_image", columnDefinition = "CLOB")
    private String ticketImage;

    @Column(name = "ticket_type")
    private String ticketType;
    
    @Column(name = "total_price", nullable = false)
    @Builder.Default
    private Double totalPrice = 0.0;

    @Column(name = "status")
    private String status;

    @Column(name = "error_message", columnDefinition = "CLOB")
    private String errorMessage;

    @Column(name = "itinerary")
    private int itinerary;

    @Column(name = "start_point")
    private String startPoint;

    @Column(name = "end_point")
    private String endPoint;

    @Column(name = "depart_date")
    private String departDate;

    @Column(name = "return_date")
    private String returnDate;

    @Column(name = "fare_class")
    private String fareClass;

    @Column(name = "fare_basis")
    private String fareBasis;

    @Column(name = "flight_type")
    private String flightType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_send_mail", nullable = false)
    @Builder.Default
    private Boolean isSendMail = false;
}