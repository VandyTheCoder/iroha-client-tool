package me.lucifer.model

import java.math.BigDecimal
import java.security.KeyPair

data class IrohaTransfer(
    val senderAccountId: String,
    val senderKeyPair: KeyPair,
    val receiverAccountId: String,
    val amount: BigDecimal,
    val asset: String,
    val description: String = ""
)
