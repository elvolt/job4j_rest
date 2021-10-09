package ru.job4j.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.AuthApplication;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.service.PersonService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = AuthApplication.class)
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PersonService service;

    @Test
    public void whenFindAllThenReturnStatusOk() throws Exception {
        mockMvc.perform(get("/person/"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(service).findAll();
    }

    @Test
    public void whenFindByIdThenReturnPerson() throws Exception {
        Person person = new Person();
        person.setId(1);
        when(service.findById(1)).thenReturn(Optional.of(person));
        String jsonResult = mockMvc.perform(get("/person/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(service).findById(1);
        Assertions.assertEquals(person, mapper.readValue(jsonResult, Person.class));
    }

    @Test
    public void whenNotFoundByIdThenReturnStatusNoFound() throws Exception {
        mockMvc.perform(get("/person/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(service).findById(1);
    }

    @Test
    public void whenCreateThenReturnStatusCreatedAndPerson() throws Exception {
        Person stub = new Person();
        stub.setId(0);
        Person createdPerson = new Person();
        createdPerson.setId(1);
        when(service.save(stub)).thenReturn(createdPerson);
        String jsonResult = mockMvc.perform(post("/person/")
                .content(mapper.writeValueAsString(stub))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(service).save(stub);
        Assertions.assertEquals(createdPerson, mapper.readValue(jsonResult, Person.class));
    }

    @Test
    public void whenUpdateThanReturnStatusOk() throws Exception {
        Person person = new Person();
        person.setId(1);
        when(service.findById(1)).thenReturn(Optional.of(person));
        mockMvc.perform(put("/person/")
                .content(mapper.writeValueAsString(person))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        verify(service).save(person);
    }

    @Test
    public void whenUpdateButPersonNotFoundThanReturnStatusNotFound() throws Exception {
        Person person = new Person();
        person.setId(1);
        when(service.findById(1)).thenReturn(Optional.empty());
        mockMvc.perform(put("/person/")
                .content(mapper.writeValueAsString(person))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteThenReturnStatusOk() throws Exception {
        mockMvc.perform(delete("/person/1"))
                .andDo(print())
                .andExpect(status().isOk());
        Person person = new Person();
        person.setId(1);
        verify(service).delete(person);
    }
}