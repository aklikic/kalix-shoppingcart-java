package com.example.shoppingcart;

import kalix.javasdk.eventsourcedentity.EventSourcedEntity;
import kalix.javasdk.eventsourcedentity.EventSourcedEntityContext;
import kalix.springsdk.annotations.EntityKey;
import kalix.springsdk.annotations.EntityType;
import kalix.springsdk.annotations.EventHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;

@EntityKey("cartId")
@EntityType("shoppingcart")
@RequestMapping("/shoppingcart/{cartId}")
public class ShoppingCartEntity extends EventSourcedEntity<Model.Cart> {
    private final String cartId;

    public ShoppingCartEntity(EventSourcedEntityContext context) {
        this.cartId = context.entityId();
    }
    @Override
    public Model.Cart emptyState() {
        return Model.Cart.empty(cartId);
    }
    @PostMapping("/add-item")
    public Effect<String> addItem(@RequestBody Model.AddItemRequest request){
        if(!currentState().checkedOut()){
            if(currentState().isItemAlreadyAdded(request.item().productId())){
                return effects().reply(Model.RESPONSE_OK);
            }else {
                Model.ItemAddedEvent event = new Model.ItemAddedEvent(cartId, request.item(), Instant.now());
                return effects().emitEvent(event).thenReply(updatedCart -> Model.RESPONSE_OK);
            }
        }else{
            return effects().error("Cart already checked out!");
        }
    }
    @PostMapping("/checkout")
    public Effect<String> checkOut(){
        if(currentState().checkedOut()){
            return effects().reply(Model.RESPONSE_OK);
        }else{
            Model.CheckedOutEvent event = new Model.CheckedOutEvent(cartId,Instant.now());
            return effects().emitEvent(event).thenReply(updatedCart -> Model.RESPONSE_OK);
        }
    }

    @GetMapping
    public Effect<Model.Cart> get(){
       return effects().reply(currentState());
    }

    @EventHandler
    public Model.Cart onItemAddedEvent(Model.ItemAddedEvent event){
        return currentState().onItemAdded(event);
    }
    @EventHandler
    public Model.Cart onCheckedOutEvent(Model.CheckedOutEvent event){
        return currentState().onCheckedOut(event);
    }
}
