package com.hinesh.shopgenie.order.web;

import com.hinesh.shopgenie.order.dto.CartDto;
import com.hinesh.shopgenie.order.service.CartService;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;



@Path("/carts")
@Tag(name = "cart", description = "All the cart methods")
public class CartResource {
    @Inject
    CartService cartService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CartDto> findAll() {
        return this.cartService.findAll();
    }

    @GET
    @Path("/active")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CartDto> findAllActiveCarts() {
        return this.cartService.findAllActiveCarts();
    }

    @GET
    @Path("/customer/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CartDto getActiveCartForCustomer(@PathParam("id") Long customerId) {
        return this.cartService.getActiveCart(customerId);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CartDto findById(@PathParam("id") Long id) {
        return this.cartService.findById(id);
    }


    @POST
    @Path("/customer/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CartDto create(@PathParam("id") Long customerId) {
        return this.cartService.createDto(customerId);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("id") Long id) {
        this.cartService.delete(id);
    }
}
