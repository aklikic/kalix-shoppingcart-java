package com.example.shoppingcart;

import kalix.springsdk.testkit.EventSourcedTestKit;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class EntityUnitTest {
    @Test
    public void happyPath(){
        var cartId = UUID.randomUUID().toString();
        var item = new Model.Item("prodId1", 1);

        var testKit = EventSourcedTestKit.of(cartId, ShoppingCartEntity::new);

        //add item to cart
        var addRequest = testKit.call(entity -> entity.addItem(new Model.AddItemRequest(item)));
        var itemAddedEvent = addRequest.getNextEventOfType(Model.ItemAddedEvent.class);
        assertEquals(item.productId(), itemAddedEvent.item().productId());

        var cartState =(Model.Cart) addRequest.getUpdatedState();
        assertFalse(cartState.checkedOut());

        //check out
        var checkoutRequest = testKit.call(entity -> entity.checkOut());
        checkoutRequest.getNextEventOfType(Model.CheckedOutEvent.class);
        cartState =(Model.Cart) checkoutRequest.getUpdatedState();
        assertTrue(cartState.checkedOut());


    }
}
