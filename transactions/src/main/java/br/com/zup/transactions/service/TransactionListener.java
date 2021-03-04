package br.com.zup.transactions.service;

import br.com.zup.transactions.consumer.TransactionReceiver;
import br.com.zup.transactions.model.Card;
import br.com.zup.transactions.model.Transaction;
import br.com.zup.transactions.repository.CardRepository;
import br.com.zup.transactions.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionListener.class);

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;

    @Autowired
    public TransactionListener(TransactionRepository transactionRepository, CardRepository cardRepository) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
    }

    @KafkaListener(topics = "${transactions.kafka.topic.name}", groupId = "${transactions.kafka.consumer.group.id}")
    @Transactional
    public void receiveTransaction(TransactionReceiver transactionReceiver) {
        Card transactionCard = transactionReceiver.getCardModel();
        Card card = cardRepository.findByExternalId(transactionCard.getExternalId()).orElse(transactionCard);

        cardRepository.save(card);

        Transaction transaction = transactionReceiver.toModel(card);
        transactionRepository.save(transaction);

        LOGGER.info("Transaction {} successfully processed and saved.", transaction.getExternalId());
    }

}
