package com.mycompany.roadtripplanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.roadtripplanner.controllers.ItineraryController;
import com.mycompany.roadtripplanner.dtos.budget.BudgetDTO;
import com.mycompany.roadtripplanner.dtos.comment.CommentGetDTO;
import com.mycompany.roadtripplanner.dtos.itinerary.ItineraryDTO;
import com.mycompany.roadtripplanner.dtos.itinerary.ItineraryUpdateDTO;
import com.mycompany.roadtripplanner.dtos.todolist.TodoListDTO;
import com.mycompany.roadtripplanner.dtos.user.UserGetSaveDTO;
import com.mycompany.roadtripplanner.services.ItineraryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItineraryController.class)
public class itineraryControllerTest {
    //Injection de dependance
    @Autowired
    private MockMvc mockMvc;

    //On copie le service
    @MockBean
    private ItineraryService service;

    //On test le controller findAll
    @Test
    public void testFindAllItinerary() throws Exception{
        this.mockMvc.perform(get("/itineraries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    //On test si l'itineraire n'existe pas
    @Test
    public void testFindOneItineraryWhereWrongId() throws Exception{
        //On execute la requete
        this.mockMvc.perform(get("/itineraries/dvdcsdf5fs5sf"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindOne() throws Exception{
        //On crée un itineraire
        ItineraryDTO commentDTO = this.itineraryDto();
        //On appel la methode given pour moker notre service
        //en parametre le type de requete
        BDDMockito.given(service.find("1"))
                .willReturn (Optional.of(commentDTO));
        MvcResult result = this.mockMvc.perform(get("/itineraries/1"))
                .andExpect(status().isOk())
                .andReturn();
        //On initialisz l'objet Gson pour la transformation
        Gson json = new GsonBuilder().create();
        ItineraryDTO body = json.fromJson(result.getResponse().getContentAsString(), ItineraryDTO.class);
        Assertions.assertEquals(body.getTitle(),this.itineraryDto().getTitle());

    }

    @Test
    public void testSaveItinerary() throws Exception {
        // on creer notre commentaire
        ItineraryDTO itineraryDTO= this.itineraryDto();
        //On initialise notre json pour la creation
        Gson json = new GsonBuilder().create();
        // on transforme notre obj en json
        String body = json.toJson(itineraryDTO);
        this.mockMvc.perform(post("/itineraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDeleteItinerary() throws Exception{
        //On Initialise l'objet Gson pour la transformation
        Gson json = new GsonBuilder().create();
        String body = json.toJson(this.itineraryDto());
        this.mockMvc.perform(delete("/itineraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateItinerary() throws Exception{
        //1 On recupère le commentaire
        ItineraryDTO itineraryDTO = this.itineraryDto();
        ItineraryDTO itineraryUpdateDTo = this.itineraryUPDATEDTO();
        BDDMockito.given(service.find("1"))
                .willReturn(Optional.of(itineraryDTO));
        MvcResult result = this.mockMvc.perform(get("/itineraries/1"))
                .andExpect((status().isOk()))
                .andReturn();
        Gson json = new GsonBuilder().create();
        ItineraryUpdateDTO body = json.fromJson(result.getResponse().getContentAsString(),ItineraryUpdateDTO.class);

        BDDMockito.when(service.update(any(ItineraryUpdateDTO.class)))
                .thenReturn(itineraryUpdateDTo);
        //2 On fait la modification
        String bodyToSave = json.toJson(body);
        MvcResult resultUpdated = this.mockMvc.perform(put("/itineraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyToSave))
                .andExpect(status().isOk())
                .andReturn();

        //2.5 On transforme le résultat en objet
        ItineraryDTO finalBody = json.fromJson(resultUpdated.getResponse().getContentAsString(), ItineraryDTO.class);

        //3 On verifie si il a bien été modifié
        Assertions.assertEquals(finalBody.getTitle(),this.itineraryUPDATEDTO().getTitle());
    }

    private ItineraryDTO itineraryDto(){
        return new ItineraryDTO();
    }

    private ItineraryDTO itineraryUPDATEDTO(){
        return new ItineraryDTO();
    }
}