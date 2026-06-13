package com.lms.lms_system_management.controller;

import com.lms.lms_system_management.dto.group.NewGroupRequest;
import com.lms.lms_system_management.dto.group.UpdateGroupRequest;
import com.lms.lms_system_management.dto.group.GroupResponse;
import com.lms.lms_system_management.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;




import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@Validated
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupResponse createGroup(@Valid @RequestBody NewGroupRequest request) {
        return groupService.create(request);
    }

    @GetMapping("/{groupId}")
    public GroupResponse getGroupById(@PathVariable("groupId") Long id) {
        return groupService.findById(id);
    }

    @GetMapping
    public List<GroupResponse> getGroups() {
        return groupService.findAll();
    }

    @PutMapping("/{groupId}")
    public GroupResponse updateGroup(@Valid @RequestBody UpdateGroupRequest request,
                                     @PathVariable("groupId") Long id) {
        return groupService.update(request, id);
    }

    @DeleteMapping("/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGroup(@PathVariable("groupId") Long id) {
        groupService.deleteById(id);
    }
}
