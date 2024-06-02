package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
@Tag(name = "Training Management System")
class TrainingController {

    private final TrainingServiceImpl trainingService;
    private final TrainingMapper trainingMapper;

    @Operation(summary = "View a list of all trainings")
    @GetMapping
    public List<TrainingDto> getAllTrainings() {
        return trainingService.findAllTrainings()
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    @Operation(summary = "View a list of trainings for a specific user")
    @GetMapping("/{userId}")
    public List<TrainingDto> getTrainingsByUser(@PathVariable Long userId) {
        return trainingService.findTrainingsByUserId(userId)
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    @Operation(summary = "View a list of completed trainings with a specific date")
    @GetMapping("/finished/{afterTime}")
    public List<TrainingDto> getCompletedTrainings(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate afterTime) {
        return trainingService.findCompletedTrainings(afterTime)
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    @Operation(summary = "View a list of trainings for a specific activity type")
    @GetMapping("/activityType")
    public List<TrainingDto> getTrainingsByActivity(@RequestParam ActivityType activityType) {
        return trainingService.findTrainingsByActivityType(activityType)
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    @Operation(summary = "Create a new training")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrainingDto addTraining(@RequestBody TrainingDto trainingDto) {
        Training training = trainingMapper.toEntity(trainingDto);
        Training createdTraining = trainingService.createTraining(training);
        return trainingMapper.toDto(createdTraining);
    }

    @Operation(summary = "Update an existing training")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TrainingDto updateTraining(@PathVariable Long id, @RequestBody TrainingDto trainingDto) {
        return trainingMapper.toDto(trainingService.updateTraining(id, trainingDto));
    }
}