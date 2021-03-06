package com.blackfriar.controllers;

import com.blackfriar.domain.Beer;
import com.blackfriar.BeerService;
import com.blackfriar.assemblers.BeerResourceAssembler;
import com.blackfriar.exceptions.BeerNotFoundException;
import com.blackfriar.resources.BeerResource;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;



@Controller
@RequestMapping(path="/api", produces = MediaTypes.HAL_JSON_VALUE)
public class BeerController {

    @Autowired
    private BeerService beerService;

    @Autowired
    private BeerResourceAssembler beerResourceAssembler;


    @ApiOperation("Get all the known beers")
    @ApiResponses({
        @ApiResponse(code = 404, message = "No beers could be found")
    })
    @RequestMapping(value = "beers", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> getAllBeers() {
        List<Beer> allBeers = beerService.getAllBeers();
        List<BeerResource> beerResources = beerResourceAssembler.toResources(allBeers);
        Link link = linkTo(methodOn(BeerController.class).getAllBeers()).withSelfRel();
        Resources<BeerResource> beerResources1 = new Resources<BeerResource>(beerResources, link);
        return new ResponseEntity(beerResources1, HttpStatus.OK);

    }

    @ApiOperation("Get a beer by it's unique id")
    @ApiResponses({
        @ApiResponse(code = 200, message = "The beer")
    })
    @RequestMapping(value = "beers/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<BeerResource> findById(@ApiParam("The unique identifier of the beer") @PathVariable Long id) {
        Beer beer = beerService.getById(id)
                .orElseThrow(() -> new BeerNotFoundException());
        BeerResource beerResource = beerResourceAssembler.toResource(beer);


        return new ResponseEntity<BeerResource>(beerResource, HttpStatus.OK);
    }


}