package com.mycompany.roadtripplanner.dtos.stage;

import com.mycompany.roadtripplanner.entities.position.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StageSaveDTO {
    private String name;
    private Position position;
}
