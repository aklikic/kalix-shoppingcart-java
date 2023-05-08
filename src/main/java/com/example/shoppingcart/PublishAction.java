package com.example.shoppingcart;

import kalix.javasdk.action.Action;
import kalix.springsdk.annotations.Acl;
import kalix.springsdk.annotations.Publish;
import kalix.springsdk.annotations.Subscribe;

@Subscribe.EventSourcedEntity(value = ShoppingCartEntity.class, ignoreUnknown = true)

@Acl(allow = @Acl.Matcher(service = "*"))
public class PublishAction extends Action {
    @Publish.Topic("sc-topic")
    public Effect<Model.CheckedOutEvent> onCheckedOutEvent(Model.CheckedOutEvent event){
        return effects().reply(event);
    }
}
