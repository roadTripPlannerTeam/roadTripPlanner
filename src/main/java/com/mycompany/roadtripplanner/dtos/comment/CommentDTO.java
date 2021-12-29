package com.mycompany.roadtripplanner.dtos.comment;

import com.mycompany.roadtripplanner.dtos.commentResponse.CommentResponseDTO;
import com.mycompany.roadtripplanner.dtos.commentResponse.CommentResponseGetDTO;
import com.mycompany.roadtripplanner.dtos.user.UserCommentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private String id;
    private String title;
    private String description;
    private Date date;
    private int like;
    private UserCommentDTO user;
    private List<CommentResponseGetDTO> commentsResponse;
}
