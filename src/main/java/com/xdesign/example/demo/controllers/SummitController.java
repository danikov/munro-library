package com.xdesign.example.demo.controllers;

import com.querydsl.core.BooleanBuilder;
import com.xdesign.example.demo.controllers.entities.QSummit;
import com.xdesign.example.demo.entities.Summit;
import com.xdesign.example.demo.repositories.SummitRepository;
import com.xdesign.example.demo.services.PredicateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RepositoryRestController
@RequestMapping("/summit")
public class SummitController {
    @Autowired
    SummitRepository summitRepository;

    @Autowired
    PagedResourcesAssembler pagedResourcesAssembler;

    @Autowired
    PredicateService predicateService;

    @RequestMapping(method = RequestMethod.GET, produces = {"application/hal+json"})
    @ResponseBody
    public PagedModel<PersistentEntityResource> summit(
            @RequestParam Optional<Double> minHeight,
            @RequestParam Optional<Double> maxHeight,
            @RequestParam MultiValueMap<String, String> parameters,
            Pageable pageable,
            PersistentEntityResourceAssembler resourceAssembler
    ) {
        BooleanBuilder predicateBuilder = new BooleanBuilder(
                predicateService.getPredicateFromParameters(parameters, Summit.class));

        if (minHeight.isPresent() && maxHeight.isPresent() && minHeight.get().compareTo(maxHeight.get()) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "min height must be less or equal to max height");
        }

        if (minHeight.isPresent() || maxHeight.isPresent()) {
            predicateBuilder.and(QSummit.summit.height.between(
                    minHeight.orElse(Double.MIN_VALUE), maxHeight.orElse(Double.MAX_VALUE)));
        }

        Page<Summit> summitPage = summitRepository.findAll(predicateBuilder, pageable);

        if (summitPage.hasContent()) {
            return pagedResourcesAssembler.toModel(summitPage, resourceAssembler);
        }

        return pagedResourcesAssembler.toEmptyModel(summitPage, Summit.class);
    }
}
