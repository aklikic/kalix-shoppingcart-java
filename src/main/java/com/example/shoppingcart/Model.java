package com.example.shoppingcart;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public interface Model {

    record Cart(String cartId, List<Item> items, boolean checkedOut) implements Model{
        public static Cart empty(String cartId){
            return new Cart(cartId,new ArrayList<>(),false);
        }
        public boolean isEmpty(){
            return items.isEmpty();
        }
        public boolean isItemAlreadyAdded(String productId){
            return items.stream().filter(i -> i.productId.equals(productId)).findFirst().isPresent();
        }
        public Cart onItemAdded(ItemAddedEvent event){
            items.add(event.item);
            return new Cart(cartId,items,false);
        }
        public Cart onCheckedOut(CheckedOutEvent event){
            return new Cart(cartId,items,true);
        }
    }
    record Item(String productId, Integer quantity) implements Model{}


    record ItemAddedEvent(String cartId, Item item, Instant timestamp) implements Model{}
    record CheckedOutEvent(String cartId, Instant timestamp) implements Model{}

    record AddItemRequest(Item item) implements Model{}

    public static final String RESPONSE_OK = "OK";

}
