package com.ppdm.backend.projects;

import com.ppdm.backend.projects.dto.ProjectCreateDto;
import com.ppdm.backend.projects.dto.ProjectDto;
import com.ppdm.backend.projects.dto.ProjectUpdateDto;
import com.ppdm.backend.projects.exception.ProjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) { this.projectService = projectService; }

    @PostMapping
    public ResponseEntity<ProjectDto> saveProject(@RequestBody ProjectCreateDto projectDto) {
        return ResponseEntity.ok(projectService.saveProject(projectDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody ProjectUpdateDto projectDto) {
        try{
            return ResponseEntity.ok(projectService.updateProject(id, projectDto));
        }catch (ProjectNotFoundException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

}
