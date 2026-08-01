package com.szponty.recruitment_system.dictionary.controller;

import com.szponty.recruitment_system.auth.service.JwtService;
import com.szponty.recruitment_system.common.exception.NotFoundException;
import com.szponty.recruitment_system.dictionary.dto.DictionaryItemRequest;
import com.szponty.recruitment_system.dictionary.dto.DictionaryItemResponse;
import com.szponty.recruitment_system.dictionary.service.BenefitService;
import com.szponty.recruitment_system.user.repository.UserRepository;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BenefitController.class)
@AutoConfigureMockMvc(addFilters = false)
class BenefitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BenefitService benefitService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void shouldReturnListOfBenefits() throws Exception {
        mockBenefitGetAll();

        mockMvc.perform(get("/api/dictionary/benefit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Private Healthcare"))
                .andExpect(jsonPath("$[1].name").value("Multisport"))
                .andExpect(jsonPath("$[2].name").value("Training Budget"));
    }

    @Test
    void shouldReturnCreatedBenefit() throws Exception {
        DictionaryItemResponse response = new DictionaryItemResponse(UUID.randomUUID(), "Multisport", "Sports card", LocalDateTime.now());

        when(benefitService.create(any(DictionaryItemRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/dictionary/benefit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Multisport", "description": "Sports card"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Multisport"));
    }

    @Test
    void shouldReturn404WhenBenefitNotFoundOnUpdate() throws Exception {
        when(benefitService.update(any(UUID.class), any(DictionaryItemRequest.class)))
                .thenThrow(new NotFoundException("Benefit not found"));

        mockMvc.perform(put("/api/dictionary/benefit/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Multisport", "description": "Sports card"}
                                """))
                .andExpect(status().isNotFound());
    }

    private void mockBenefitGetAll() {
        DictionaryItemResponse responseItem1 = new DictionaryItemResponse(UUID.randomUUID(), "Private Healthcare", "Sports card", LocalDateTime.now());
        DictionaryItemResponse responseItem2 = new DictionaryItemResponse(UUID.randomUUID(), "Multisport", "Sports card", LocalDateTime.now());
        DictionaryItemResponse responseItem3 = new DictionaryItemResponse(UUID.randomUUID(), "Training Budget", "Sports card", LocalDateTime.now());

        when(benefitService.getAll())
                .thenReturn(List.of(responseItem1, responseItem2, responseItem3));
    }
}
