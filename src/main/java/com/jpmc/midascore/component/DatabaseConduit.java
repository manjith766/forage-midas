package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRepository;
    private final IncentiveClient incentiveClient;

    public DatabaseConduit(UserRepository userRepository,
                           TransactionRecordRepository transactionRepository,
                           IncentiveClient incentiveClient) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.incentiveClient = incentiveClient;
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }


    public void processTransaction(Transaction tnx) {

        UserRecord sender = userRepository.findById(tnx.getSenderId());
        UserRecord recipient = userRepository.findById(tnx.getRecipientId());

        if (sender == null || recipient == null) return;
        if (sender.getBalance() < tnx.getAmount()) return;

        Incentive incentive = incentiveClient.getIncentive(tnx);
        float incentiveAmount = incentive != null ? incentive.getAmount() : 0;

        sender.setBalance(sender.getBalance() - tnx.getAmount());
        recipient.setBalance(recipient.getBalance() + tnx.getAmount() + incentiveAmount);

        userRepository.save(sender);
        userRepository.save(recipient);

        TransactionRecord record =
                new TransactionRecord(sender, recipient, tnx.getAmount(), incentiveAmount);

        transactionRepository.save(record);
    }
}
