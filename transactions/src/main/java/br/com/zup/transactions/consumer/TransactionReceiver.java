package br.com.zup.transactions.consumer;

import br.com.zup.transactions.model.Card;
import br.com.zup.transactions.model.Transaction;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionReceiver {

    @JsonProperty(required = true)
    private UUID id;

    @JsonProperty(value = "valor", required = true)
    private BigDecimal value;

    @JsonProperty(value = "estabelecimento", required = true)
    private EstablishmentReceiver establishment;

    @JsonProperty(value = "cartao", required = true)
    private CardReceiver card;

    @JsonProperty(value = "efetivadaEm", required = true)
    private LocalDateTime activateAt;

    public TransactionReceiver() {
    }

    public Transaction toModel(Card card) {
        return new Transaction(id, value, establishment.toModel(), card, activateAt);
    }

    public Card getCardModel() {
        return card.toModel();
    }

    public UUID getId() {
        return id;
    }
}
