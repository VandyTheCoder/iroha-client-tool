package me.lucifer.function

import jp.co.soramitsu.iroha.java.IrohaAPI
import jp.co.soramitsu.iroha.java.Transaction
import jp.co.soramitsu.iroha.java.Utils
import me.lucifer.common.Constant.CREATOR_ACCOUNT_ID
import me.lucifer.common.Constant.CREATOR_ACCOUNT_KEYPAIR
import me.lucifer.common.Constant.DOMAIN_ID
import me.lucifer.function.CheckHash.getTransactionsStatus
import me.lucifer.model.IrohaCommand
import me.lucifer.model.IrohaCreateAccount
import org.slf4j.LoggerFactory

object CreateAccount {

    private val log = LoggerFactory.getLogger(this::class.java)

    @OptIn(ExperimentalStdlibApi::class)
    fun createAccount(irohaCreateAccount: IrohaCreateAccount, irohaPeer: IrohaAPI) {
        log.info("Creating account with ID: ${irohaCreateAccount.accountName} in domain: $DOMAIN_ID")
        val accountID = "${irohaCreateAccount.accountName}@$DOMAIN_ID"
        val publicKey = Utils.parseHexPublicKey(irohaCreateAccount.publicKey)
        val createAccountCommand = Transaction
            .builder(CREATOR_ACCOUNT_ID)
            .createAccount(accountID, publicKey)
            .build().sign(CREATOR_ACCOUNT_KEYPAIR).build()
        val createAccountCommandBytes = Utils.hash(createAccountCommand)
        irohaPeer.transaction(createAccountCommand)
        log.info("CreateAccount[${createAccountCommandBytes.toHexString()}] for AccountID[$accountID] is sent to PEER[${irohaPeer.uri}] successfully!")
        getTransactionsStatus(
            listOfNotNull(
                createAccountCommandBytes?.let {
                    IrohaCommand(
                        action = "Create account",
                        objectValue = "New account id : $accountID",
                        txHashBytes = createAccountCommandBytes
                    )
                }
            ),
            irohaPeer
        )
    }
}