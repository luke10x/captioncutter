package dev.luke10x.captioncutter.speech2text.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranscriptionSummary {
    private final String status;
    private final String fileName;
    private final String transcription;
}
