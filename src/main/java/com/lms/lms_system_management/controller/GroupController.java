package com.lms.lms_system_management.controller;

import com.lms.lms_system_management.dto.request.NewGroupRequest;
import com.lms.lms_system_management.dto.request.UpdateGroupRequest;
import com.lms.lms_system_management.dto.response.GroupResponse;
import com.lms.lms_system_management.service.GroupService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@Validated
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupResponse createGroup(@RequestBody NewGroupRequest request) {
        return groupService.create(request);
    }

    @GetMapping("/{group_id}")
    public GroupResponse getGroupById(@PathVariable("group_id") Long id) {
        return groupService.findById(id);
    }

    @GetMapping
    public List<GroupResponse> getGroups() {
        return groupService.findAll();
    }

    @PutMapping("/{group_id}")
    public GroupResponse updateGroup(@RequestBody UpdateGroupRequest request,
                                     @PathVariable("group_id") Long id) {
        return groupService.update(request, id);
    }

    @DeleteMapping("/{group_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGroup(@PathVariable("group_id") Long id) {
        groupService.deleteById(id);
    }
}
