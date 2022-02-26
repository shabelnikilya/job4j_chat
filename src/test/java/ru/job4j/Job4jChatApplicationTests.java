package ru.job4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.job4j.model.Message;
import ru.job4j.model.Person;
import ru.job4j.model.Role;
import ru.job4j.model.Room;
import ru.job4j.service.PersonService;
import ru.job4j.service.RoomService;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Job4jChatApplication.class)
@AutoConfigureMockMvc
class Job4jChatApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private PersonService personService;
    @MockBean
    private RoomService roomService;
    private final Person first = new Person(1,
            "name",
            "surname",
            "login",
            "password",
            Role.USER,
            new Date());
    private final Person second = new Person(2,
            "second name",
            "second surname",
            "second login",
            "second password",
            Role.ADMIN, new Date());
    private final Room room = new Room("sport", "111", Set.of(first, second),
            List.of(new Message("Кто сегодня играет?", first, new Date())));

    @Test
    public void testPersonControllerFindAll() throws Exception {
        Iterable<Person> exp = List.of(first, second
        );
        Mockito.when(personService.findAll()).thenReturn(exp);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/person/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].login", is("login")))
                .andExpect(jsonPath("$[1].login", is("second login")))
                .andExpect(jsonPath("$[1].role", is("ADMIN")));
    }

    @Test
    public void testPersonControllerFindByIdWhenFindPerson() throws Exception {
        Mockito.when(personService.findById(1)).thenReturn(Optional.of(first));
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/person/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.login", is("login")))
                .andExpect(jsonPath("$.role", is("USER")));
    }

    @Test
    public void testPersonControllerFindByIdWhenNotFindPerson() throws Exception {
        Mockito.when(personService.findById(1)).thenReturn(Optional.empty());
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/person/1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
    }

    @Test
    public void testPersonControllerWhenCreateOnePerson() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(first));
        Mockito.when(personService.save(first)).thenReturn(first);
        this.mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.login", is("login")))
                .andExpect(jsonPath("$.role", is("USER")));
    }

    @Test
    public void testPersonControllerWhenUpdatePerson() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(first));
        this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void testPersonControllerWhenDeletePerson() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/person/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testRoomControllerFindAll() throws Exception {
        Mockito.when(roomService.findAll()).thenReturn(List.of(room));
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/room/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("sport")))
                .andExpect(jsonPath("$[0].messages[0].text", is("Кто сегодня играет?")))
                .andExpect(jsonPath("$[0].persons", hasSize(2)));
    }

    @Test
    public void testRoomControllerFindByIdWhenFindPerson() throws Exception {
        Mockito.when(roomService.findById(1)).thenReturn(Optional.of(room));
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/room/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("sport")))
                .andExpect(jsonPath("$.password", is("111")));
    }

    @Test
    public void testRoomControllerFindByIdWhenNotFindPerson() throws Exception {
        Mockito.when(roomService.findById(1)).thenReturn(Optional.empty());
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/room/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRoomControllerWhenCreateOnePerson() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/room/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(room));
        Mockito.when(roomService.save(room)).thenReturn(room);
        this.mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("sport")))
                .andExpect(jsonPath("$.password", is("111")));
    }

    @Test
    public void testRoomControllerWhenUpdatePerson() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/room/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(room));
        this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void testRoomControllerWhenDeletePerson() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/room/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testMessageControllerFindAll() throws Exception {
        Mockito.when(roomService.findAll()).thenReturn(List.of(room));
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/room/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("sport")))
                .andExpect(jsonPath("$[0].messages[0].text", is("Кто сегодня играет?")))
                .andExpect(jsonPath("$[0].persons", hasSize(2)));
    }
}
