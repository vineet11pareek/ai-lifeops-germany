package com.lifeops.taskservice.service;

import com.lifeops.taskservice.dto.TaskResponse;
import com.lifeops.taskservice.entity.Task;
import com.lifeops.taskservice.entity.TaskSourceType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

   private final TaskMapper taskMapper = new TaskMapper();

   @Test
    void shouldMapTaskToResponse(){

       //Given
       Task task = new Task(
               UUID.randomUUID(),
               "google-sub-123",
               TaskSourceType.DOCUMENT_ANALYSIS,
               UUID.randomUUID(),
               "Test Document Analyzed",
               "Test Document analyzed for the mentioned content",
               "Submit the test document",
               "UNKNOWN"
       );

       //When

       TaskResponse response = taskMapper.toResponse(task);

       //Then
       assertNotNull(response);
       assertThat(response.title()).isEqualTo("Test Document Analyzed");
       assertThat(response.sourceType()).isEqualTo("DOCUMENT_ANALYSIS");
       assertThat(response.riskLevel()).isEqualTo("UNKNOWN");
       assertThat(response.recommendedAction()).isEqualTo("Submit the test document");
       assertThat(response.status()).isEqualTo("WAITING_FOR_APPROVAL");
   }
}