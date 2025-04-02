package edu.sia.credigo.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionEntity createTransaction(TransactionEntity transaction) {
        return transactionRepository.save(transaction);
    }

    public List<TransactionEntity> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<TransactionEntity> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public List<TransactionEntity> getTransactionsByUserId(Long userid) {
        return transactionRepository.findByUser_Userid(userid);
    }

    public TransactionEntity updateTransaction(Long id, TransactionEntity updatedTransaction) {
        Optional<TransactionEntity> existingTransaction = transactionRepository.findById(id);
        if (existingTransaction.isPresent()) {
            TransactionEntity transaction = existingTransaction.get();
            transaction.setAmount(updatedTransaction.getAmount());
            transaction.setStatus(updatedTransaction.getStatus());
            transaction.setPaymentMethod(updatedTransaction.getPaymentMethod());
            transaction.setDescription(updatedTransaction.getDescription());
            return transactionRepository.save(transaction);
        }
        return null;
    }

    public boolean deleteTransaction(Long id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
