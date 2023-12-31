package ru.netology.page;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PaymentEntityInfo {
    private String id;
    private String amount;
    private String created;
    private String status;
    private String transaction_id;
}