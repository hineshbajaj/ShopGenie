package com.hinesh.shopgenie.order.web;

import com.hinesh.shopgenie.order.dto.OrderDto;
import com.hinesh.shopgenie.order.service.OrderService;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/orders")
@Tag(name = "order", description = "All the order methods")
public class OrderResource {


    @Inject
    OrderService orderService;

    @GET
    public List<OrderDto> findAll() {
        return this.orderService.findAll();
    }

    @GET
    @Path("/customer/{id}")
    public List<OrderDto> findAllByUser(@PathParam("id") Long id) {
        return this.orderService.findAllByUser(id);
    }

    @GET
    @Path("/{id}")
    public OrderDto findById(@PathParam("id") Long id) {
        return this.orderService.findById(id);
    }

    @POST
    public OrderDto create(OrderDto orderDto) {
        return this.orderService.create(orderDto);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.orderService.delete(id);
    }

    @GET
    @Path("/exists/{id}")
    public boolean existsById(@PathParam("id") Long id) {
        return this.orderService.existsById(id);
    }
}