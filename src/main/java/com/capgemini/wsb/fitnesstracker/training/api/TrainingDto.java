package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.utils.MultiFormatDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor()
@AllArgsConstructor
public class TrainingDto {
    private Long id;
    private Long userId;
    private User user;
    @JsonDeserialize(using = MultiFormatDateDeserializer.class)
    private Date startTime;
    @JsonDeserialize(using = MultiFormatDateDeserializer.class)
    private Date endTime;
    private ActivityType activityType;
    private double distance;
    private double averageSpeed;
}