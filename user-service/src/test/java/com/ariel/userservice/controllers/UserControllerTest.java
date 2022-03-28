package com.ariel.userservice.controllers;

import com.ariel.userservice.exceptions.DuplicateUniqueResourceException;
import com.ariel.userservice.exceptions.ResourceNotFoundException;
import com.ariel.userservice.models.ERole;
import com.ariel.userservice.models.Role;
import com.ariel.userservice.models.User;
import com.ariel.userservice.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService mockService;

    @Test
    void assertGetUserFromUsernameValid() throws Exception {
        when(mockService.getUserFromUsername("user")).thenReturn(new User(1L, "user", "123", new HashSet<>()));
        RequestBuilder request = MockMvcRequestBuilders.get("/users/byName/user");

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    void assertGetUserFromUsernameNotFound() throws Exception {
        when(mockService.getUserFromUsername("user")).thenThrow(ResourceNotFoundException.class);
        RequestBuilder request = MockMvcRequestBuilders.get("/users/byName/user");

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isNotFound());
    }

    @Test
    void assertInsertUserValid() throws Exception {
        User user = new User(1L, "user", "123", new HashSet<Role>(List.of(new Role(ERole.SECRETARIA))));
        when(mockService.insertUser(any(User.class))).thenReturn(user);
        String content = objectMapper.writeValueAsString(user);
        System.out.println(content);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(content);

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isCreated());
    }

    @Test
    void assertInsertUserDuplicatedResource() throws Exception {
        when(mockService.insertUser(any(User.class))).thenThrow(DuplicateUniqueResourceException.class);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new User("user", "123", new HashSet<>())));

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    void assertUpdateUserValid() throws Exception {
        when(mockService.updateUser(eq("user"), any(User.class))).thenReturn(new User());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/users/byName/user").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new User(1L, "user", "123", new HashSet<>())));

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    void assertUpdateUserNotFound() throws Exception {
        when(mockService.updateUser(eq("user"), any(User.class))).thenThrow(ResourceNotFoundException.class);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/users/byName/user").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new User(1L, "user", "123", new HashSet<>())));

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isNotFound());
    }

    @Test
    void assertUpdateUserDuplicatedResource() throws Exception {
        when(mockService.updateUser(eq("user"), any(User.class))).thenThrow(DuplicateUniqueResourceException.class);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/users/byName/user").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new User(1L, "user", "123", new HashSet<>())));

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    void assertDeleteUserValid() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/users/byName/user");

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    void assertDeleteUserNotFound() throws Exception {
        doThrow(ResourceNotFoundException.class).when(mockService).deleteUser("user");
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/users/byName/user");

        ResultActions result = mockMvc.perform(request);

        result.andExpect(status().isNotFound());
    }
}