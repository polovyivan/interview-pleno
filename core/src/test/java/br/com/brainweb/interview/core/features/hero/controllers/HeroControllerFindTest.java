package br.com.brainweb.interview.core.features.hero.controllers;

import br.com.brainweb.interview.core.features.controllers.HeroController;
import br.com.brainweb.interview.core.features.services.HeroService;
import br.com.brainweb.interview.model.dtos.response.HeroResponseDTO;
import br.com.brainweb.interview.model.dtos.response.PowerStatsResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.*;

import static br.com.brainweb.interview.core.features.TestUtils.createHeroResponseDTO;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(HeroController.class)
public class HeroControllerFindTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private HeroService heroService;

    private static final Locale BRAZIL = new Locale("pt", "BR");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", BRAZIL);

    private HeroResponseDTO heroResponseDTO = createHeroResponseDTO();
    private PowerStatsResponseDTO powerStatsResponseDTO = heroResponseDTO.getPowerStats();

    @Test
    public void shouldFetchHeroById() throws Exception {

        when(heroService.find(heroResponseDTO.getId().toString())).thenReturn(heroResponseDTO);

        String heroResponseDTOAsString = objectMapper.writeValueAsString(heroResponseDTO);

        mvc.perform(get("/heroes/" + heroResponseDTO.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(heroResponseDTOAsString))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

    }

    @Test
    public void shouldReturnNotFoundWhenHeroDoesNotExist() throws Exception {
        UUID idDoesNotExist = UUID.randomUUID();
        when(heroService.find(heroResponseDTO.getId().toString())).thenReturn(heroResponseDTO);

        mvc.perform(get("/heroes/" + idDoesNotExist))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldFetchHeroName() throws Exception {
        List<HeroResponseDTO> heroesDTO = new ArrayList();
        heroesDTO.add(heroResponseDTO);


        when(heroService.find(Optional.of(heroResponseDTO.getName()))).thenReturn(heroesDTO);


        HeroResponseDTO heroResponseDTO1 = heroesDTO.get(0);
        PowerStatsResponseDTO powerStats = heroResponseDTO1.getPowerStats();

        String heroResponseDTO1AsString = objectMapper.writeValueAsString(heroResponseDTO1);

        mvc.perform(get("/heroes/names/" + heroResponseDTO.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(heroResponseDTO1AsString))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

    }

    @Test
    public void shouldReturnAllWhenNoNamePassed() throws Exception {
        List<HeroResponseDTO> heroesDTO = new ArrayList<>();
        heroesDTO.add(heroResponseDTO);

        when(heroService.find(Optional.of(""))).thenReturn(heroesDTO);


        mvc.perform(get("/heroes/names/" + heroResponseDTO.getName()))
                .andExpect(status().isOk());

    }
}
